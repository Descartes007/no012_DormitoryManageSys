package com.hzvtc.myproject.dao;

import com.hzvtc.myproject.entity.SystemRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author 熊新欣
 * @date 2020-12-09
 */
@Repository
public interface SystemRoleMapper {
    /**
     * 根据用户id获取角色列表
     *
     * @param id
     * @return
     */
    List<SystemRole> listByUserId(Long id);

    /**
     * 根据条件获取角色列表
     *
     * @param role
     * @return
     */
    List<SystemRole> listAll(SystemRole role);


    /**
     * 根据 role id删除用户角色关联表
     *
     * @param roleId 用户id
     */
    void deleteUserRoleByRoleId(Long roleId);

    /**
     * 根据id获取角色详情
     *
     * @param id id
     * @return 角色
     */
    Optional<SystemRole> query(Long id);

    /**
     * 删除角色
     *
     * @param roleId 角色id
     */
    void delete(Long roleId);

    /**
     * 根据role id 删除功能角色关联表
     *
     * @param roleId 角色id
     */
    void deleteRoleFunctionByRoleId(Long roleId);

    /**
     * 添加用户角色
     *
     * @param userId 用户id
     * @param roleId 角色id
     */
    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 添加角色功能
     *
     * @param roleId     角色id
     * @param functionId 功能id
     */
    void insertRoleFunction(@Param("roleId") Long roleId, Long functionId);

    /**
     * 插入角色
     *
     * @param role 角色
     */
    void save(SystemRole role);

    /**
     * 更新
     *
     * @param role 角色
     */
    void update(SystemRole role);
}
