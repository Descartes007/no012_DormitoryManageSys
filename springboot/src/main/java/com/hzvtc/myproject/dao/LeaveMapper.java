package com.hzvtc.myproject.dao;

import com.hzvtc.myproject.entity.Leave;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 熊新欣
 * @date 2021-03-10
 */
@Repository
public interface LeaveMapper {
    /**
     * 保存记录
     * @param leave
     */
    void save(Leave leave);

    /**
     * 更新
     * @param id
     */
    void update(Long id);

    /**
     * 获取列表
     * @param leave 查询条件
     * @param bId
     * @return
     */
    List<Leave> list(Leave leave, List<Long> bId);

    /**
     * 获取单个
     * @param id
     * @return
     */
    Leave query(Long id);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);
}
