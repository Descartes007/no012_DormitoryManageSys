package com.hzvtc.myproject.dao;

import com.hzvtc.myproject.entity.SystemUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author 熊新欣
 * @date 2020-12-09
 */
@Repository
public interface SystemUserMapper {
    /**
     * 登录验证
     *
     * @param loginName 登录信息
     * @return 登录的用户
     */
    List<SystemUser> listUserByLoginName(String loginName);

    /**
     * 获取用户基本信息
     *
     * @param id id
     * @return 用户
     */
    Optional<SystemUser> getInfo(Long id);

    /**
     * 修改
     *
     * @param systemUser
     */
    void update(SystemUser systemUser);

    /**
     * 插入
     *
     * @param systemUser
     */
    void save(SystemUser systemUser);

    /**
     * 验证密码是否正确
     *
     * @param id 用户id
     * @return 用户密码
     */
    String validatePassword(Long id);

    /**
     * 验证登录名称是否存在
     * @param loginName 登录名称
     * @param userId 修改用户 用户id
     * @return 符合条件的用户
     */
    List<SystemUser> validateLoginName(@Param("loginName") String loginName,@Param("userId") Long userId);

    /**
     * 修改密码
     *
     * @param user
     */
    void changePassword(SystemUser user);

    /**
     * 删除
     *
     * @param id id
     */
    void delete(Long id);

    /**
     * 根据id列表获取用户
     *
     * @param ids 所有符合条件的用户id
     * @return
     */
    List<SystemUser> listByIds(List<Long> ids);

    /**
     * 获取用户详细信息
     *
     * @param id 。
     * @return .
     */
    Optional<SystemUser> get(Long id);

    /**
     * 获取条件查询所有符合条件的用户id，用于分页查询
     *
     * @param user
     * @return
     */
    List<Long> listIdFitCondition(SystemUser user);

    /**
     * 根据楼层id 获取用户id
     * @param buildingIds 楼层id列表
     * @return 用户id列表
     */
    List<Long> listByBuildingIds(List<Long> buildingIds);

    /**
     * 删除用户的所有角色
     *
     * @param userId 用户id
     */
    void deleteUserRoleByUserId(Long userId);

    /**
     * 更换头像
     *
     * @param fileName 文件名
     * @param id       用户id
     */
    void changeIcon(@Param("fileName") String fileName, @Param("id") Long id);

    /**
     * 查询所有用户
     *
     * @return
     */
    List<SystemUser> listAll();


    /**
     * 根据领导id获取用户列表
     * @param leaderId
     * @return
     */
    List<SystemUser> listByLeaderId(Long leaderId);
}
