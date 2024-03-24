package com.hzvtc.myproject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author 熊新欣
 * @date 2021-02-09
 */
@Data
public class DepartApplication {
    private Long id;

    private Long applyUserId;

    private SystemUser applyUser;

    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime time;

    private Long studentId;

    private Student student;

    private Boolean isFinished;

    private Boolean isPass;

    private List<DepartApplicationUser> operateList;
}
