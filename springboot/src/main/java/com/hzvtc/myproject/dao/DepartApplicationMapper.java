package com.hzvtc.myproject.dao;

import com.hzvtc.myproject.entity.DepartApplication;
import com.hzvtc.myproject.entity.DepartApplicationUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 熊新欣
 * @date 2021-02-09
 */
@Repository
public interface DepartApplicationMapper {
    /**
     * 插入退宿申请
     *
     * @param departApplication
     */
    void saveApplication(DepartApplication departApplication);

    /**
     * 插入审核用户和退宿申请管理
     *
     * @param operateUserId 审核人id
     * @param applicationId 申请id
     */
    void saveApplicationUser(@Param("userId") Long operateUserId,
                             @Param("applicationId") Long applicationId);

    /**
     * 获取我的申请列表
     * @param userId 用户id
     * @return 列表
     */
    List<DepartApplication> listMyApplication(Long userId);


    /**
     * 获取我的审核列表
     * @param userId 用户id
     * @return 列表
     */
    List<DepartApplicationUser> listMyFlow(Long userId);

    /**
     * 获取申请流转列表
     * @param id 申请id
     * @return
     */
    List<DepartApplicationUser> listApplicationFlow(Long id);

    /**
     * 获取申请详情
     * @param id id
     * @return 详情
     */
    DepartApplication getApplication(Long id);

    /**
     * 获取审核记录
     * @param id 审核记录id
     * @return
     */
    DepartApplicationUser getApplicationUser(Long id);

    /**
     * 更新
     * @param departApplicationUser 参数
     */
    void update(DepartApplicationUser departApplicationUser);

    /**
     * 删除申请
     * @param id
     */
    void deleteApplication(Long id);

    /**
     * 删除审核流转
     * @param applicationId 申请id
     */
    void deleteFlow(Long applicationId);

    /**
     * 获取流转中已审核的数量
     * @param id 申请id
     * @return 数量
     */
    int countFlow(Long id);

    /**
     * 获取流转中未审核的数量
     * @param id
     * @return
     */
    int countFlowAgreeIsNotNull(Long id);

    /**
     * 查询用户未审核的数量
     * @param userId 用户id
     * @return 数量
     */
    int countFlowAgreeIsNull(Long userId);

    /**
     * 获取最后一个审核记录
     * @param id 应用id
     * @return
     */
    DepartApplicationUser getLastFlow(Long id);

    /**
     * 获取申请
     * @param applicationUserId 审核id
     * @return 申请
     */
    DepartApplication getByDepartApplicationUserId(Long applicationUserId);
}
