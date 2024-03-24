package com.hzvtc.myproject.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 熊新欣
 * @date 2021-01-02
 */
@Data
@Accessors(chain = true)
public class Message {
    private int type;

    private String title;

    private String from;

    private String to;

    private String messageBody;
}
