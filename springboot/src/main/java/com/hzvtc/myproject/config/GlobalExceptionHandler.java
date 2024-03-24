package com.hzvtc.myproject.config;

import com.hzvtc.myproject.exception.HttpException;
import com.hzvtc.myproject.utils.HttpCode;
import com.hzvtc.myproject.vo.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 熊新欣
 * @date 2021-01-07
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    /**
     * http 异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(HttpException.class)
    public Result httpExceptionHandler(HttpException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 验证失败异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Result.fail(HttpCode.FAILED, "请正确填写参数").add(e.getMessage());
    }

    /**
     * 默认异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result exceptionHandler(Exception e) {
        return Result.fail(HttpCode.FAILED, "服务器未知错误").add(e.getMessage());
    }
}
