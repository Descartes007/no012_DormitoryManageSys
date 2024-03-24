package com.hzvtc.myproject.controller;

import com.github.pagehelper.PageInfo;
import com.hzvtc.myproject.annotation.Log;
import com.hzvtc.myproject.annotation.RequirePermission;
import com.hzvtc.myproject.config.Constant;
import com.hzvtc.myproject.dto.ListQuery;
import com.hzvtc.myproject.entity.BackLate;
import com.hzvtc.myproject.entity.SystemUser;
import com.hzvtc.myproject.service.BackLateService;
import com.hzvtc.myproject.utils.RedisUtil;
import com.hzvtc.myproject.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 熊新欣
 * @date 2021-02-17
 */
@RestController
@RequestMapping("/backlate")
public class BackLateController {
    @Autowired
    private BackLateService backLateService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/saveOrUpdate")
    @RequirePermission(permissions = {"back:save","back:update"})
    @Log
    public Result<?> saveOrUpdate(@RequestBody @Validated BackLate backLate) {
        backLateService.saveOrUpdate(backLate);
        return Result.ok("操作成功");
    }

    @GetMapping("delete/{id}")
    @RequirePermission(permissions = {"back:delete"})
    @Log
    public Result<?> delete(@PathVariable Long id) {
        backLateService.delete(id);
        return Result.ok("删除成功");
    }

    @PostMapping("/list")
    @RequirePermission(permissions = {"back:list"})
    public Result<PageInfo<BackLate>> list(@RequestBody ListQuery<BackLate> listQuery,
                       @RequestHeader(Constant.HEADER_TOKEN) String token) {
        SystemUser systemUser = redisUtil.exchange(token).get();
        PageInfo<BackLate> pageInfo = backLateService.list(listQuery, systemUser.getBuildingId());
        return Result.<PageInfo<BackLate>>ok().add(pageInfo);
    }

    @GetMapping("/query/{id}")
    @RequirePermission(permissions = {"back:query"})
    public Result<BackLate> query(@PathVariable Long id) {
        BackLate backLate = backLateService.query(id);
        return Result.<BackLate>ok().add(backLate);
    }
}
