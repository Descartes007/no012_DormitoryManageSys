package com.hzvtc.myproject.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author 熊新欣
 * @date 2021-01-19
 */
@Data
@Accessors(chain = true)
public class Student {
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Long roomId;

    private Long facultyId;

    @NotBlank
    private String number;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;

    private String phone;

    private String photo;

    private Boolean isLeave;

    private Room room;

    private Faculty faculty;

    private LocalDate startDate;

    private LocalDate endDate;
}
