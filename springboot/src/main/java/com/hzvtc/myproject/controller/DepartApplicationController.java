package com.hzvtc.myproject.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzvtc.myproject.config.Constant;
import com.hzvtc.myproject.dto.ListQuery;
import com.hzvtc.myproject.dto.Message;
import com.hzvtc.myproject.entity.DepartApplication;
import com.hzvtc.myproject.entity.DepartApplicationUser;
import com.hzvtc.myproject.entity.SystemUser;
import com.hzvtc.myproject.exception.HttpException;
import com.hzvtc.myproject.service.DepartApplicationService;
import com.hzvtc.myproject.service.StudentService;
import com.hzvtc.myproject.service.SystemUserService;
import com.hzvtc.myproject.socket.WebSocket;
import com.hzvtc.myproject.utils.HttpCode;
import com.hzvtc.myproject.utils.RedisUtil;
import com.hzvtc.myproject.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 熊新欣
 * @date 2021-02-09
 */
@RestController
@RequestMapping("/depart")
public class DepartApplicationController {
    @Autowired
    private DepartApplicationService departApplicationService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SystemUserService systemUserService;

    @PostMapping("/listMy")
    public Result<PageInfo<DepartApplication>> listMyApplication(@RequestBody ListQuery<DepartApplication> listQuery,
                                    @RequestHeader(Constant.HEADER_TOKEN) String token) {
        Long id = redisUtil.get(token);
        PageHelper.startPage(listQuery.getPage(), listQuery.getRows());
        List<DepartApplication> list = departApplicationService.listMyApplication(id);
        PageInfo<DepartApplication> pageInfo = new PageInfo<>(list);
        return Result.<PageInfo<DepartApplication>>ok().add(pageInfo);
    }

    @PostMapping("/listMyFlow")
    public Result<PageInfo<DepartApplicationUser>> listMyFlow(@RequestBody ListQuery<DepartApplication> listQuery,
                             @RequestHeader(Constant.HEADER_TOKEN) String token) {
        Long id = redisUtil.get(token);
        PageHelper.startPage(listQuery.getPage(), listQuery.getRows());
        List<DepartApplicationUser> list = departApplicationService.listMyFlow(id);
        PageInfo<DepartApplicationUser> pageInfo = new PageInfo<>(list);
        return Result.<PageInfo<DepartApplicationUser>>ok().add(pageInfo);
    }

    @GetMapping("/listFlow/{id}")
    public Result<List<DepartApplicationUser>> listApplicationFlow(@PathVariable("id") Long id) {
        List<DepartApplicationUser> list = departApplicationService.listApplicationFlow(id);
        return Result.<List<DepartApplicationUser>>ok().add(list);
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody DepartApplicationUser departApplicationUser,
                         @RequestHeader(Constant.HEADER_TOKEN) String token) {
        String msg = "成功，";
        SystemUser user = redisUtil.exchange(token).get();
        DepartApplicationUser applicationUser =
                departApplicationService
                        .getApplicationUser(departApplicationUser.getId());
        if (!user.getId().equals(applicationUser.getOperateUserId())) {
            throw new HttpException(HttpCode.FAILED, "无法审核");
        }
        if (applicationUser.getIsAgree() != null) {
            throw new HttpException(HttpCode.FAILED, "已审核过，无法修改");
        }
        departApplicationService.update(departApplicationUser);
        DepartApplication application =
                departApplicationService
                        .getByDepartApplicationUserId(departApplicationUser.getId());

        if (departApplicationUser.getIsAgree()) {
            if (user.getLeaderId() == null) {
                studentService.delete(application.getStudentId());
                msg += "该学生已成功退宿";
            } else {
                departApplicationService.saveApplication(user.getLeaderId(), application.getId());
                WebSocket.sendMessage(user.getLeaderId(),
                        new Message().setTitle("新的退宿申请").setType(2).setMessageBody(application.getReason()),
                        systemUserService);
                msg += "等待上一级审核";
            }
        } else {
            msg += "审核未通过";
        }

        return Result.ok(msg);
    }

    @GetMapping("delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        departApplicationService.deleteApplication(id);
        return Result.ok("撤销成功");
    }

    @GetMapping("/query/{id}")
    public Result<DepartApplication> query(@PathVariable Long id) {
        DepartApplication data = departApplicationService.getApplication(id);
        return Result.<DepartApplication>ok().add(data);
    }

}
