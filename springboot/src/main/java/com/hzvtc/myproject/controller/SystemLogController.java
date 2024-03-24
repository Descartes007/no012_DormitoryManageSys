package com.hzvtc.myproject.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzvtc.myproject.annotation.RequirePermission;
import com.hzvtc.myproject.dto.ListQuery;
import com.hzvtc.myproject.entity.SystemLog;
import com.hzvtc.myproject.entity.SystemUser;
import com.hzvtc.myproject.service.SystemLogService;
import com.hzvtc.myproject.service.SystemUserService;
import com.hzvtc.myproject.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 熊新欣
 * @date 2020-12-29
 */
@RestController
@RequestMapping("/system/log")
public class SystemLogController {

    @Autowired
    private SystemLogService systemLogService;


    @PostMapping("/list")
    @RequirePermission(permissions = {"system:log:list"})
    public Result<PageInfo<SystemLog>> list(@RequestBody ListQuery<SystemLog> listQuery) {
        PageHelper.startPage(listQuery.getPage(), listQuery.getRows());
        List<SystemLog> systemLogList = systemLogService.list(listQuery.getEntity());
        PageInfo<SystemLog> pageInfo = new PageInfo<>(systemLogList);
        return Result.<PageInfo<SystemLog>>ok().add(pageInfo);
    }

    @GetMapping("/query")
    @RequirePermission(permissions = {"system:log:query"})
    public Result<SystemLog> query(@RequestParam("id") Long id) {
        SystemLog systemLog = systemLogService.query(id);
        return Result.<SystemLog>ok().add(systemLog);
    }
}
