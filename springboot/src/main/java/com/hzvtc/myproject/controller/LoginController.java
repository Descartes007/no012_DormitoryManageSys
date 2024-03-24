package com.hzvtc.myproject.controller;

import com.hzvtc.myproject.entity.SystemUser;
import com.hzvtc.myproject.exception.HttpException;
import com.hzvtc.myproject.service.SystemUserService;
import com.hzvtc.myproject.config.Constant;
import com.hzvtc.myproject.utils.HttpCode;
import com.hzvtc.myproject.utils.MD5Util;
import com.hzvtc.myproject.utils.RedisUtil;
import com.hzvtc.myproject.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

import static com.hzvtc.myproject.config.Constant.HEADER_TOKEN;

/**
 * @author 熊新欣
 * @date 2020-12-08
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    private final RedisUtil redisUtil;
    private final SystemUserService systemUserService;

    public LoginController(RedisUtil redisUtil, SystemUserService systemUserService) {
        this.redisUtil = redisUtil;
        this.systemUserService = systemUserService;
    }

    @PostMapping("/login")
    public Result<String> login(SystemUser user, @RequestParam(value = "redirectUrl") String redirectUrl) {
        List<SystemUser> userList = systemUserService.listUserByLoginName(user.getLoginName());
        if (userList.size() == 0) {
            throw new HttpException(HttpCode.LOGIN_FAILED, "没有此用户");
        } else if (userList.size() > 1) {
            throw new HttpException(HttpCode.LOGIN_FAILED, "存在多个登录名，请联系管理员");
        } else {
            SystemUser systemUser = userList.get(0);
            if (systemUser.getPassword().equals(MD5Util.md5(user.getPassword()))) {
                String token = UUID.randomUUID().toString();
                redisUtil.put(token, systemUser.getId());
                return Result.<String>ok().add(redirectUrl + "#/token=" + token);
            } else {
                throw new HttpException(HttpCode.LOGIN_FAILED, "登陆失败，密码错误");
            }
        }

    }

    @GetMapping("/logout")
    public Result<?> logout(@RequestHeader(HEADER_TOKEN) String token) {
        if (redisUtil.hasToken(token)) {
            redisUtil.deleteToken(token);
        }
        return Result.ok();
    }
}
