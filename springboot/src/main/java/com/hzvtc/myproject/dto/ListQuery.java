package com.hzvtc.myproject.dto;

import lombok.Data;

/**
 * 用于分页
 *
 * @author 熊新欣
 * @date 2020-12-13
 */
@Data
public class ListQuery<T> {
    /**
     * 查询条件
     */
    private T entity;

    /**
     * 页
     */
    private Integer page = 1;

    /**
     * 每页行数
     */
    private Integer rows = 10;
}
