package com.hzvtc.myproject.exception;

import lombok.Data;
import lombok.Getter;

/**
 * @author 熊新欣
 * @date 2021-01-07
 */
@Getter
public class HttpException extends RuntimeException{
    private static final long serialVersionUID = 6771315879176421659L;
    private final Integer code;
    public HttpException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
