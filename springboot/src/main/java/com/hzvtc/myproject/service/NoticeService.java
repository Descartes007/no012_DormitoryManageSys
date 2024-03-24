package com.hzvtc.myproject.service;

import com.hzvtc.myproject.dao.NoticeMapper;
import com.hzvtc.myproject.dto.Message;
import com.hzvtc.myproject.entity.Notice;
import com.hzvtc.myproject.entity.SystemUser;
import com.hzvtc.myproject.exception.HttpException;
import com.hzvtc.myproject.socket.WebSocket;
import com.hzvtc.myproject.utils.HttpCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 熊新欣
 * @date 2021-02-04
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private SystemUserService systemUserService;

    public List<Notice> listByUserId(Long uId) {
        return noticeMapper.listByUserId(uId);
    }

    public List<Notice> listBySendUser(Long uId) {
        return noticeMapper.listBySendUser(uId);
    }

    public void save(Notice notice) {
        noticeMapper.save(notice);
    }

    public Notice query(Long id) {
        return noticeMapper.query(id);
    }

    public Notice queryWithReceiver(Long id) {
        return noticeMapper.queryWithReceiver(id);
    }

    public void updateRead(Long uid, Long nid) {
        noticeMapper.updateRead(uid, nid);
    }

    public void saveRelevance(Long uid, Long nid) {
        noticeMapper.saveRelevance(uid, nid);
        Notice notice = noticeMapper.query(nid);
        WebSocket.sendMessage(uid,
                new Message()
                        .setMessageBody(notice.getMsg())
                        .setType(1)
                        .setFrom(notice.getUser().getRealName()).setTitle("新通知"), systemUserService);
    }

    public void saveRelevance(List<Long> uid, Long nid) {
        uid.forEach(l -> saveRelevance(l, nid));
    }

    public void deleteMyNotice(Long uid, Long nid) {
        noticeMapper.deleteMyNotice(uid, nid);
    }

    public void deleteNotice(Long id) {
        noticeMapper.deleteByNoticeId(id);
        noticeMapper.deleteNotice(id);
    }

    public int countByUserId(Long userId) {
        return noticeMapper.countByUserId(userId);
    }
}
