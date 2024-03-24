package com.hzvtc.myproject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class SystemRole implements Serializable {
    private static final long serialVersionUID = -5639318500991429824L;
    private Long id;
    @NotBlank
    private String name;

    private LocalDateTime createTime;

    private List<Long> functionIds;

    private List<SystemFunction> functions;
}
