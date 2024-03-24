package com.hzvtc.myproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用于密码修改
 *
 * @author 熊新欣
 * @date 2020-12-13
 */
@Data
public class Password implements Serializable {
    private static final long serialVersionUID = -1771036907310685922L;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String oldPassword;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String current1;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String current2;
}
