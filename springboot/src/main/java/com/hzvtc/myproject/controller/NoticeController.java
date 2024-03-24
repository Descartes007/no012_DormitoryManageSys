package com.hzvtc.myproject.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzvtc.myproject.annotation.Log;
import com.hzvtc.myproject.annotation.RequirePermission;
import com.hzvtc.myproject.config.Constant;
import com.hzvtc.myproject.dto.ListQuery;
import com.hzvtc.myproject.entity.Notice;
import com.hzvtc.myproject.service.BuildingService;
import com.hzvtc.myproject.service.NoticeService;
import com.hzvtc.myproject.service.SystemUserService;
import com.hzvtc.myproject.utils.RedisUtil;
import com.hzvtc.myproject.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 熊新欣
 * @date 2021-02-04
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private RedisUtil redisUtil;

    @RequirePermission(permissions = {"home:send:msg"})
    @PostMapping("/sendToUser/{uid}")
    @Log("发送消息给单个用户")
    public Result<?> sendToUser(@RequestBody Notice notice,
                             @PathVariable("uid") Long uid,
                             @RequestHeader(Constant.HEADER_TOKEN) String token) {
        notice.setUserId(redisUtil.get(token));
        noticeService.save(notice);
        noticeService.saveRelevance(uid, notice.getId());
        return Result.ok("发送成功");
    }

    @RequirePermission(permissions = {"home:send:msg"})
    @PostMapping("/sendToBuilding/{bid}")
    @Log("发送消息给多个用户")
    public Result<?> sendToBuilding(@RequestBody Notice notice,
                             @PathVariable("bid") Long bid,
                             @RequestHeader(Constant.HEADER_TOKEN) String token) {
        notice.setUserId(redisUtil.get(token));
        noticeService.save(notice);
        List<Long> bids = buildingService.getIdsByParentId(bid);
        List<Long> uids = systemUserService.listByBuildingIds(bids);
        noticeService.saveRelevance(uids, notice.getId());
        return Result.ok("发送成功");
    }

    @PostMapping("/list")
    public Result<PageInfo<Notice>> list(@RequestHeader(Constant.HEADER_TOKEN) String token, @RequestBody ListQuery<?> listQuery) {
        PageHelper.startPage(listQuery.getPage(), listQuery.getRows());
        List<Notice> list = noticeService.listByUserId(redisUtil.get(token));
        PageInfo<Notice> pageInfo = new PageInfo<>(list);
        return Result.<PageInfo<Notice>>ok().add(pageInfo);
    }

    @PostMapping("/listSend")
    public Result<PageInfo<Notice>> listSend(@RequestHeader(Constant.HEADER_TOKEN) String token, @RequestBody ListQuery<?> listQuery) {
        PageHelper.startPage(listQuery.getPage(), listQuery.getRows());
        List<Notice> list = noticeService.listBySendUser(redisUtil.get(token));
        PageInfo<Notice> pageInfo = new PageInfo<>(list);
        return Result.<PageInfo<Notice>>ok().add(pageInfo);
    }

    @GetMapping("/deleteReceive/{msgId}")
    public Result<?> delete(@PathVariable("msgId") Long msgId, @RequestHeader(Constant.HEADER_TOKEN) String token) {
        noticeService.deleteMyNotice(redisUtil.get(token), msgId);
        return Result.ok("删除成功");
    }

    @GetMapping("/deleteSend/{msgId}")
    @Log("删除发送的消息")
    public Result<?> delete(@PathVariable("msgId") Long msgId) {
        noticeService.deleteNotice(msgId);
        return Result.ok("删除成功");
    }

    @GetMapping("/query/{id}")
    public Result<?> query(@PathVariable("id") Long id, @RequestHeader(Constant.HEADER_TOKEN) String token) {
        noticeService.updateRead(redisUtil.get(token), id);
        Notice notice = noticeService.query(id);
        return Result.ok().add(notice);
    }

    @GetMapping("/querydetail/{id}")
    public Result<Notice> query(@PathVariable("id") Long id) {
        Notice notice = noticeService.queryWithReceiver(id);
        return Result.<Notice>ok().add(notice);
    }

}
