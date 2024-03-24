package com.hzvtc.myproject.controller;

import com.github.pagehelper.PageInfo;
import com.hzvtc.myproject.annotation.Log;
import com.hzvtc.myproject.annotation.RequirePermission;
import com.hzvtc.myproject.config.Constant;
import com.hzvtc.myproject.dto.ListQuery;
import com.hzvtc.myproject.entity.Repair;
import com.hzvtc.myproject.entity.SystemUser;
import com.hzvtc.myproject.service.RepairService;
import com.hzvtc.myproject.utils.RedisUtil;
import com.hzvtc.myproject.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author XiongXinxin
 * @date 2021-03-13
 */
@RestController
@RequestMapping("/repair")
public class RepairController {
    @Autowired
    private RepairService repairService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/saveOrUpdate")
    @RequirePermission(permissions = {"repair:update", "repair:save"})
    @Log
    public Result<?> save(@RequestBody @Validated Repair repair) {
        if (repair.getId() == null) {
            repairService.save(repair);
        } else {
            repairService.update(repair);
        }
        return Result.ok("添加成功");
    }

    @GetMapping("/updateStatus/{id}")
    @RequirePermission(permissions = {"repair:update"})
    @Log
    public Result<?> updateStatus(@PathVariable Long id) {
        repairService.updateStatus(id);
        return Result.ok("修改成功");
    }

    @PostMapping("/list")
    @RequirePermission(permissions = {"repair:list"})
    public Result<PageInfo<Repair>> list(@RequestBody ListQuery<Repair> listQuery, @RequestHeader(Constant.HEADER_TOKEN) String token) {
        SystemUser user = redisUtil.exchange(token).get();
        PageInfo<Repair> pageInfo = repairService.list(listQuery, user.getBuildingId());
        return Result.<PageInfo<Repair>>ok().add(pageInfo);
    }

    @GetMapping("/query/{id}")
    @RequirePermission(permissions = {"repair:query"})
    public Result<Repair> query(@PathVariable Long id) {
        Repair repair = repairService.query(id);
        return Result.<Repair>ok().add(repair);
    }

    @GetMapping("/delete/{id}")
    @RequirePermission(permissions = {"repair:delete"})
    @Log
    public Result<?> delete(@PathVariable Long id) {
        repairService.delete(id);
        return Result.ok("删除成功");
    }
}
