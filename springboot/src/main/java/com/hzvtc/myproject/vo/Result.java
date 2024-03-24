package com.hzvtc.myproject.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 熊新欣
 * @date 2020-12-08
 */
@Data
@Accessors(chain = true)
public class Result<T> {
    private Integer statusCode;
    private String msg;

    private T data;

    public static <T> Result<T> ok(String msg) {
        Result<T> result = new Result<>();
        result.statusCode = 200;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> ok() {
        Result<T> result = new Result<>();
        result.statusCode = 200;
        result.msg = "请求成功";
        return result;
    }

    public static <T> Result<T> fail(Integer statusCode, String msg) {
        Result<T> result = new Result<>();
        result.statusCode = statusCode;
        result.msg = msg;
        return result;
    }

    public Result<T> add(T data) {
        this.data = data;
        return this;
    }
}
