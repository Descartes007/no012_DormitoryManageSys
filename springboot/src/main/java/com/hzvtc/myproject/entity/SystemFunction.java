package com.hzvtc.myproject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author 熊新欣
 * @date 2020-12-08
 */
@Data
@Accessors(chain = true)
public class SystemFunction implements Serializable {
    private static final long serialVersionUID = 3805867595133356807L;
    private Long id;
    @NotBlank
    private String name;

    private Long parentId;

    private Integer orderNum;

    private String path;

    private String component;
    @NotBlank
    private String menuType;

    private String permission;

    private String icon;

    private LocalDateTime createTime;

    private List<SystemFunction> children;
}
