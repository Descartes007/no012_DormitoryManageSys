package com.hzvtc.myproject.controller;

import com.hzvtc.myproject.dto.Password;
import com.hzvtc.myproject.entity.Image;
import com.hzvtc.myproject.entity.SystemFunction;
import com.hzvtc.myproject.entity.SystemRole;
import com.hzvtc.myproject.entity.SystemUser;
import com.hzvtc.myproject.exception.HttpException;
import com.hzvtc.myproject.service.*;
import com.hzvtc.myproject.config.Constant;
import com.hzvtc.myproject.utils.HttpCode;
import com.hzvtc.myproject.utils.MD5Util;
import com.hzvtc.myproject.utils.RedisUtil;
import com.hzvtc.myproject.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static com.hzvtc.myproject.config.Constant.HEADER_TOKEN;

/**
 * @author 熊新欣
 * @date 2020-12-10
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    private final RedisUtil redisUtil;
    private final SystemUserService systemUserService;
    private final SystemFunctionService systemFunctionService;
    private final SystemRoleService systemRoleService;
    private final NoticeService noticeService;
    private final DepartApplicationService departApplicationService;
    private final ImageService imageService;

    public PermissionController(RedisUtil redisUtil,
                                SystemUserService systemUserService,
                                SystemFunctionService systemFunctionService,
                                SystemRoleService systemRoleService,
                                NoticeService noticeService,
                                DepartApplicationService departApplicationService,
                                ImageService imageService) {
        this.redisUtil = redisUtil;
        this.systemUserService = systemUserService;
        this.systemFunctionService = systemFunctionService;
        this.systemRoleService = systemRoleService;
        this.noticeService = noticeService;
        this.departApplicationService = departApplicationService;
        this.imageService = imageService;
    }

    /**
     * 获取登录用户权限，头像，名称，菜单信息
     *
     * @param token token
     * @return
     */
    @GetMapping("/info")
    public Result<SystemUser> info(@RequestHeader(HEADER_TOKEN) String token) {
        SystemUser user = redisUtil.exchange(token).orElseThrow(() -> new HttpException(HttpCode.FAILED, "没有该用户"));
        List<SystemRole> systemRoles = systemRoleService.listByUserId(user.getId());
        Set<String> permissions;
        if (systemRoles.size() == 0) {
            permissions = new HashSet<>();
        } else {
            permissions = systemFunctionService.getPermission(systemRoles);
        }
        user.setPermissions(permissions);
        List<SystemFunction> functionList = systemFunctionService.listFunctionByParentIdAndIds(null, user.getUserRoleId());
        user.setFunctions(functionList);
        return Result.<SystemUser>ok().add(user);
    }

    /**
     * 获取登录用户基本信息
     *
     * @param token 用于获取请求头中的token
     * @return
     */
    @GetMapping("/userinfo")
    public Result<SystemUser> userInfo(@RequestHeader(HEADER_TOKEN) String token) {
        Long id = redisUtil.get(token);
        SystemUser user = systemUserService.info(id).get();
        return Result.<SystemUser>ok().add(user);
    }
    /**
     * 上传图片
     *
     * @param file  图片
     * @param token token
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file, @RequestHeader(HEADER_TOKEN) String token) {
        String fileName = UUID.randomUUID().toString() + ".png";
        if (file != null) {
            try {
                String md5 = MD5Util.getFileMD5(file.getInputStream());
                if (md5 == null) {
                    throw new HttpException(HttpCode.FAILED, "上传失败");
                }
                List<Image> list = imageService.selectByMd5(md5);
                if (list.size() == 0) {
                    file.transferTo(new File(Constant.UPLOAD_PATH, fileName));
                    Long userId = redisUtil.get(token);
                    Image image = new Image()
                            .setUploadUser(userId)
                            .setOriginalName(file.getOriginalFilename())
                            .setSaveName(fileName)
                            .setMd5(md5);
                    imageService.save(image);
                } else {
                    fileName = list.get(0).getSaveName();
                }

            } catch (IOException e) {
                throw new HttpException(HttpCode.FAILED, "上传失败");
            }
        } else {
            throw new HttpException(HttpCode.FAILED, "上传失败");
        }
        return Result.<String>ok().add(fileName);
    }

    @GetMapping("/countUnread")
    public Result<Integer> count(@RequestHeader(HEADER_TOKEN) String token) {
        Long userId = redisUtil.get(token);
        int countNotice = noticeService.countByUserId(userId);
        int countDepartApplication = departApplicationService.countFlowAgreeIsNull(userId);
        return Result.<Integer>ok().add(countNotice + countDepartApplication);
    }
}
