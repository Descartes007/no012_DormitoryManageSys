package com.hzvtc.myproject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author 熊新欣
 * @date 2021-01-19
 */
@Data
@Accessors(chain = true)
public class Faculty {
    private Long id;

    @NotBlank
    private String name;

    private Long parentId;

    private Integer orderNum;

    private List<Faculty> children;

    private Integer studentNum;

    private List<Student> students;
}
