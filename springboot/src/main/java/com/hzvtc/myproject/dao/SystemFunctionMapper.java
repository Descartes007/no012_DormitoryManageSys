package com.hzvtc.myproject.dao;

import com.hzvtc.myproject.entity.SystemFunction;
import com.hzvtc.myproject.entity.SystemRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author 熊新欣
 * @date 2020-12-09
 */
@Repository
public interface SystemFunctionMapper {
    /**
     * 根据父节点和角色id获取模块列表
     *
     * @param parentId 。
     * @param roleList 。
     * @return 子模块
     */
    List<SystemFunction> listFunctionByParentIdAndIds(@Param("pid") Long parentId, @Param("roleList") List<Long> roleList);

    /**
     * 根据角色id获取权限列表
     *
     * @param roleList 0
     * @return 0
     */
    Set<String> getPermission(List<SystemRole> roleList);

    /**
     * 获取菜单树
     *
     * @param parentId 父节点
     * @return 菜单
     */
    List<SystemFunction> getTree(Long parentId);

    /**
     * 根据角色id 获取菜单id列表
     *
     * @param roleId 角色id
     * @return 。
     */
    List<Long> selectMenuListByRoleId(Long roleId);

    /**
     * 删除
     *
     * @param id id
     */
    void delete(Long id);

    /**
     * 根据functionid 删除角色菜单管理表
     *
     * @param functionId
     */
    void deleteRoleFunctionByFunctionId(Long functionId);

    /**
     * 保存
     *
     * @param function
     */
    void save(SystemFunction function);

    /**
     * 更新
     *
     * @param function
     */
    void update(SystemFunction function);

    /**
     * 查询
     *
     * @param id id
     * @return 结果
     */
    Optional<SystemFunction> query(Long id);

    /**
     * 根据 parent id 获取列表
     *
     * @param parentId
     * @return
     */
    List<SystemFunction> listByParentId(Long parentId);
}
