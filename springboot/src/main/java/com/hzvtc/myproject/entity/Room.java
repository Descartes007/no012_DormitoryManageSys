package com.hzvtc.myproject.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 熊新欣
 * @date 2021-01-19
 */
@Data
@Accessors(chain = true)
public class Room {
    private Long id;

    @NotBlank
    private String number;

    @NotNull
    private Long buildingId;

    @NotNull
    private Integer maxCapacity;

    private Integer currentNum;

    private Building building;

    private Boolean isFull;
}
