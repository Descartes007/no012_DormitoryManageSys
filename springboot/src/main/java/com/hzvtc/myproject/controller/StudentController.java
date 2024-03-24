package com.hzvtc.myproject.controller;

import com.github.pagehelper.PageInfo;
import com.hzvtc.myproject.annotation.Log;
import com.hzvtc.myproject.annotation.RequirePermission;
import com.hzvtc.myproject.config.Constant;
import com.hzvtc.myproject.dto.ListQuery;
import com.hzvtc.myproject.dto.Message;
import com.hzvtc.myproject.entity.DepartApplication;
import com.hzvtc.myproject.entity.Student;
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
 * @date 2021-01-22
 */
@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private DepartApplicationService departApplicationService;

    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/list")
    @RequirePermission(permissions = {"student:list"})
    public Result<PageInfo<Student>> list(@RequestBody ListQuery<Student> listQuery, @RequestHeader(Constant.HEADER_TOKEN) String token) {
        SystemUser systemUser = redisUtil.exchange(token).get();
        PageInfo<Student> pageInfo = studentService.list(listQuery, systemUser.getBuildingId());
        return Result.<PageInfo<Student>>ok().add(pageInfo);
    }

    @GetMapping("/list")
    @RequirePermission(permissions = {"student:list"})
    public Result<List<Student>> list(@RequestParam("name") String name, @RequestHeader(Constant.HEADER_TOKEN) String token) {
        SystemUser user = redisUtil.exchange(token).get();
        List<Student> result = studentService.list(name ,user.getBuildingId());
        return Result.<List<Student>>ok().add(result);
    }

    @GetMapping("/query")
    @RequirePermission(permissions = {"student:query"})
    public Result<Student> query(@RequestParam("id") Long id) {
        Student student = studentService.query(id).orElseThrow(() -> new HttpException(HttpCode.FAILED, "该数据不存在"));
        return Result.<Student>ok().add(student);
    }

    @PostMapping("saveOrUpdate")
    @RequirePermission(permissions = {"student:save", "student:update"})
    @Log
    public Result<?> saveOrUpdate(@RequestBody Student student) {
        studentService.saveOrUpdate(student);
        return Result.ok("操作成功");
    }

    @PostMapping("/delete")
    @RequirePermission(permissions = {"student:delete"})
    @Log
    public Result<?> delete(@RequestBody DepartApplication departApplication, @RequestHeader(Constant.HEADER_TOKEN) String token) {
        SystemUser user = redisUtil.exchange(token).get();
        departApplication.setApplyUserId(user.getId());
        departApplicationService.saveApplication(departApplication);
        if (user.getLeaderId() == null) {
            studentService.delete(departApplication.getStudentId());
            return Result.ok("退宿成功");
        } else {
            departApplicationService.saveApplication(user.getLeaderId(), departApplication.getId());
            WebSocket.sendMessage(user.getLeaderId(),
                    new Message().setTitle("新的退宿申请").setType(2).setMessageBody(departApplication.getReason()),
                    systemUserService);
            return Result.ok("退宿申请已提交，等待上一级审核");
        }

    }
}
