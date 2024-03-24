package com.hzvtc.myproject.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 熊新欣
 * @date 2021-01-08
 */
@Data
public class TestDO {

    private Integer id;
    @Size(min = 3, max = 6)
    private String name;
    @NotNull
    private String a;
}
