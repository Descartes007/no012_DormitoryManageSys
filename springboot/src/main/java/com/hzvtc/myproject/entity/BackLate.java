package com.hzvtc.myproject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 熊新欣
 * @date 2021-02-16
 */
@Data
public class BackLate {
    private Long id;

    @NotNull
    private Long studentId;

    private Student student;


    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime backDate;
}
