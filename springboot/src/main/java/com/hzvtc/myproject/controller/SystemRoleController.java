package com.hzvtc.myproject.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzvtc.myproject.annotation.Log;
import com.hzvtc.myproject.annotation.RequirePermission;
import com.hzvtc.myproject.dto.ListQuery;
import com.hzvtc.myproject.entity.SystemRole;
import com.hzvtc.myproject.exception.HttpException;
import com.hzvtc.myproject.service.SystemFunctionService;
import com.hzvtc.myproject.service.SystemRoleService;
import com.hzvtc.myproject.utils.HttpCode;
import com.hzvtc.myproject.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 熊新欣
 * @date 2020-12-13
 */
@RestController
@RequestMapping("/system/role")
public class SystemRoleController {
    @Autowired
    private SystemRoleService systemRoleService;

    @Autowired
    private SystemFunctionService systemFunctionService;

    @GetMapping("/listInSelect")
    @RequirePermission(permissions = {"system:role:list"})
    public Result<List<SystemRole>> listInSelect() {
        return Result.<List<SystemRole>>ok().add(systemRoleService.listAll(new SystemRole()));
    }

    @PostMapping("/saveOrUpdate")
    @RequirePermission(permissions = {"system:role:save", "system:role:update"})
    @Log("添加修改角色")
    public Result<?> saveOrUpdate(@RequestBody @Validated SystemRole role) {
        systemRoleService.saveOrUpdate(role);
        return Result.ok("操作成功");
    }

    @PostMapping("/list")
    @RequirePermission(permissions = {"system:role:list"})
    public Result<PageInfo<SystemRole>> list(@RequestBody ListQuery<SystemRole> query) {
        PageHelper.startPage(query.getPage(), query.getRows());
        List<SystemRole> list = systemRoleService.listAll(query.getEntity());
        PageInfo<SystemRole> pageInfo = new PageInfo<>(list);
        return Result.<PageInfo<SystemRole>>ok().add(pageInfo);
    }

    @GetMapping("/delete")
    @RequirePermission(permissions = {"system:role:delete"})
    @Log("删除角色")
    public Result<?> delete(@RequestParam("id") Long id) {
        systemRoleService.delete(id);
        return Result.ok("操作成功");
    }

    @GetMapping("/query")
    @RequirePermission(permissions = {"system:role:query"})
    public Result<SystemRole> query(@RequestParam("id") Long id) {
        SystemRole role = systemRoleService.query(id).orElseThrow(() -> new HttpException(HttpCode.FAILED, "角色不存在"));
        role.setFunctionIds(systemFunctionService.selectMenuListByRoleId(id));
        return Result.<SystemRole>ok().add(role);
    }
}
