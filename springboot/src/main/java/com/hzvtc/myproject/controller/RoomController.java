package com.hzvtc.myproject.controller;

import com.github.pagehelper.PageInfo;
import com.hzvtc.myproject.annotation.Log;
import com.hzvtc.myproject.annotation.RequirePermission;
import com.hzvtc.myproject.config.Constant;
import com.hzvtc.myproject.dto.ListQuery;
import com.hzvtc.myproject.entity.Room;
import com.hzvtc.myproject.entity.SystemUser;
import com.hzvtc.myproject.exception.HttpException;
import com.hzvtc.myproject.service.RoomService;
import com.hzvtc.myproject.service.StudentService;
import com.hzvtc.myproject.utils.HttpCode;
import com.hzvtc.myproject.utils.RedisUtil;
import com.hzvtc.myproject.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 熊新欣
 * @date 2021-01-22
 */
@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private RedisUtil redisUtil;

    @RequirePermission(permissions = {"room:list"})
    @PostMapping("/list")
    public Result<PageInfo<Room>> list(@RequestBody ListQuery<Room> listQuery, @RequestHeader(Constant.HEADER_TOKEN) String token) {
        SystemUser systemUser = redisUtil.exchange(token).orElseThrow(() -> new HttpException(HttpCode.FAILED, "登录用户不存在"));
        PageInfo<Room> pageInfo = roomService.list(listQuery, systemUser.getBuildingId());
        return Result.<PageInfo<Room>>ok().add(pageInfo);
    }

    @RequirePermission(permissions = {"room:list"})
    @GetMapping("/listAll")
    public Result<List<Room>> list(@RequestHeader(Constant.HEADER_TOKEN) String token) {
        SystemUser systemUser = redisUtil.exchange(token).orElseThrow(() -> new HttpException(HttpCode.FAILED, "登录用户不存在"));
        List<Room> list = roomService.list(systemUser.getBuildingId());
        return Result.<List<Room>>ok().add(list);
    }

    @GetMapping("/query/{id}")
    @RequirePermission(permissions = {"room:query"})
    public Result<Room> query(@PathVariable("id") Long id) {
        Room room = roomService.query(id).orElseThrow(() -> new HttpException(HttpCode.FAILED, "没有该宿舍信息"));
        return Result.<Room>ok().add(room);
    }

    @PostMapping("/saveOrUpdate")
    @RequirePermission(permissions = {"room:save", "room:update"})
    @Log
    public Result<?> saveOrUpdate(@RequestBody @Validated Room room) {
        roomService.saveOrUpdate(room);
        return Result.ok();
    }

    @GetMapping("/delete/{id}")
    @RequirePermission(permissions = {"room:delete"})
    @Log
    public Result<?> delete(@PathVariable("id") Long id) {
        int count = studentService.countByRoomId(id);
        if (count > 0) {
            throw new HttpException(HttpCode.FAILED, "该宿舍还有学生， 无法删除");
        }
        roomService.delete(id);
        return Result.ok("删除成功");
    }
}
