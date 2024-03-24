package com.hzvtc.myproject.dao;

import com.hzvtc.myproject.entity.Notice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 熊新欣
 * @date 2021-02-04
 */
@Repository
public interface NoticeMapper {
    /**
     * 根据用户id 获取通知
     * @param userId 用户id
     * @return
     */
    List<Notice> listByUserId(Long userId);


    /**
     * 获取发送的消息
     * @param userId 用户id
     * @return
     */
    List<Notice> listBySendUser(Long userId);

    /**
     * 插入消息
     * @param notice
     */
    void save(Notice notice);

    /**
     * 查询
     * @param id
     * @return
     */
    Notice query(Long id);

    /**
     * 消息详情，带有接收者列表
     * @param id
     * @return
     */
    Notice queryWithReceiver(Long id);

    /**
     * 关联插入
     * @param uId 接收用户id
     * @param nId 通知id
     */
    void saveRelevance(@Param("uid") Long uId, @Param("nid") Long nId);

    /**
     * 删除我的通知
     * @param uId 用户id
     * @param nId 通知id
     */
    void deleteMyNotice(@Param("uid") Long uId, @Param("nid") Long nId);

    /**
     * 删除通知
     * @param id 通知id
     */
    void deleteNotice(Long id);

    /**
     * 根据通知id删除关联关系
     * @param nId 通知id
     */
    void deleteByNoticeId(Long nId);

    /**
     * 设置已读
     * @param uId 用户id
     * @param nId 通知id
     */
    void updateRead(@Param("uid") Long uId, @Param("nid") Long nId);

    /**
     * 用户的未读消息数量
     * @param userId 用户id
     * @return 未读数量
     */
    int countByUserId(Long userId);
}
