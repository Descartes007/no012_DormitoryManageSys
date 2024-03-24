package com.hzvtc.myproject.controller;

import com.github.pagehelper.PageInfo;
import com.hzvtc.myproject.annotation.Log;
import com.hzvtc.myproject.annotation.RequirePermission;
import com.hzvtc.myproject.config.Constant;
import com.hzvtc.myproject.dto.ListQuery;
import com.hzvtc.myproject.entity.Leave;
import com.hzvtc.myproject.entity.SystemUser;
import com.hzvtc.myproject.service.LeaveService;
import com.hzvtc.myproject.utils.RedisUtil;
import com.hzvtc.myproject.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 熊新欣
 * @date 2021-03-10
 */
@RestController
@RequestMapping("/leave")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/save")
    @RequirePermission(permissions = {"leave:save"})
    @Log
    public Result<?> save(@RequestBody @Validated Leave leave) {
        leaveService.save(leave);
        return Result.ok("添加成功");
    }

    @PostMapping("/list")
    @RequirePermission(permissions = {"leave:list"})
    public Result<PageInfo<Leave>> list(@RequestBody ListQuery<Leave> listQuery,
                       @RequestHeader(Constant.HEADER_TOKEN) String token) {
        SystemUser user = redisUtil.exchange(token).get();
        PageInfo<Leave> pageInfo = leaveService.list(listQuery, user.getBuildingId());
        return Result.<PageInfo<Leave>>ok().add(pageInfo);
    }


    @GetMapping("/update/{id}")
    @RequirePermission(permissions = {"leave:update"})
    @Log
    public Result<?> update(@PathVariable Long id) {
        leaveService.update(id);
        return Result.ok("更新成功");
    }


    @GetMapping("/delete/{id}")
    @RequirePermission(permissions = {"leave:delete"})
    @Log
    public Result<?> delete(@PathVariable Long id) {
        leaveService.delete(id);
        return Result.ok("删除成功");
    }


    @GetMapping("/query/{id}")
    @RequirePermission(permissions = {"leave:query"})
    public Result<Leave> query(@PathVariable Long id) {
        Leave leave = leaveService.query(id);
        return Result.<Leave>ok().add(leave);
    }

}
