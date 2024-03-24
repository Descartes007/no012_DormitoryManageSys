package com.hzvtc.myproject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 熊新欣
 * @date 2021-03-03
 */
@Data
public class Leave {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    @NotBlank
    private String reason;

    private Boolean isBack;
    @NotNull
    private Long studentId;

    private Student student;

    @NotBlank
    private String target;

    private LocalDate backDate;
}
