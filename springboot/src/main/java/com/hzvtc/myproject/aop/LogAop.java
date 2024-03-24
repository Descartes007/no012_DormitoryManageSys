package com.hzvtc.myproject.aop;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzvtc.myproject.annotation.Log;
import com.hzvtc.myproject.entity.SystemLog;
import com.hzvtc.myproject.exception.HttpException;
import com.hzvtc.myproject.service.SystemLogService;
import com.hzvtc.myproject.config.Constant;
import com.hzvtc.myproject.utils.RedisUtil;
import com.hzvtc.myproject.vo.Result;
import lombok.extern.log4j.Log4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 熊新欣
 * @date 2020-12-29
 */
@Aspect
@Component
@Log4j
public class LogAop {

    private final RedisUtil redisUtil;
    private final SystemLogService systemLogService;

    public LogAop(RedisUtil redisUtil, SystemLogService systemLogService) {
        this.redisUtil = redisUtil;
        this.systemLogService = systemLogService;
    }

    @Pointcut("execution(* com.hzvtc.myproject.controller.*.*(..))")
    public void pointcut1() {
    }

    /**
     * 记录系统日志
     * @param jp 。
     */
    @Around(value = "pointcut1()")
    public Object after(ProceedingJoinPoint jp) throws Throwable {
        MethodSignature signature = (MethodSignature)jp.getSignature();
        Method targetMethod = signature.getMethod();
        //方法上有Log 注解
        if (targetMethod.isAnnotationPresent(Log.class)) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert attributes != null;
            HttpServletRequest request = attributes.getRequest();

            //获取类名
            String clas = jp.getTarget().getClass().getSimpleName();

            //获取操作用户id
            String token = request.getHeader(Constant.HEADER_TOKEN);
            Long userId = redisUtil.get(token);

            //获取url
            String url = request.getRequestURI();

            //获取方法名
            String method = targetMethod.getName();

            //获取参数，并转成json
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            Object[] args = jp.getArgs();
            Map<String, Object> param = new HashMap<>(2);
            for (Object arg : args) {
                String className = arg.getClass().getSimpleName();
                className = className.substring(0, 1).toLowerCase() + className.substring(1);
                param.put(className, arg);
            }

            //获取ip
            String ip = request.getRemoteAddr();

            //获取方法注解上的value
            Log l = targetMethod.getAnnotation(Log.class);
            log.info(l.value());

            SystemLog systemLog = new SystemLog()
                    .setClas(clas)
                    .setUserId(userId)
                    .setIp(ip)
                    .setUserId(userId)
                    .setMethod(method)
                    .setUrl(url)
                    .setDescription(l.value())
                    .setParam(objectMapper.writeValueAsString(param));
            Object result;
            //获取结果
            try {
                result = jp.proceed(args);
                systemLog.setResult(objectMapper.writeValueAsString(result));
            } catch (HttpException e) {
                Result res = new Result().setMsg(e.getMessage()).setStatusCode(e.getCode());
                systemLog.setResult(objectMapper.writeValueAsString(res));
                throw e;
            } catch (Throwable t) {
                systemLog.setResult(objectMapper.writeValueAsString(t));
                throw t;
            } finally {
                systemLogService.save(systemLog);
            }
            return result;
        }
        return jp.proceed(jp.getArgs());
    }
}
