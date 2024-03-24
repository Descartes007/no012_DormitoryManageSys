package com.hzvtc.myproject.dao;

import com.hzvtc.myproject.entity.Faculty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author 熊新欣
 * @date 2021-01-19
 */
@Repository
public interface FacultyMapper {
    /**
     * 获取所有学院
     * @param parentId 父id
     * @return
     */
    List<Faculty> list(Long parentId);

    /**
     * 获取所有学院
     * @return
     */
    List<Faculty> listAll();

    /**
     * 插入
     * @param faculty
     */
    void insert(Faculty faculty);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);

    /**
     * 根据父节点id 获取子节点id
     * @param id 父节点id
     * @return 0
     */
    List<Long> listByParentId(Long id);

    /**
     * 查询单个
     * @param id
     * @return
     */
    Optional<Faculty> query(Long id);

    /**
     * 更新
     * @param faculty
     */
    void update(Faculty faculty);
}
