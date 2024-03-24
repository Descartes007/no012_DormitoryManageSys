package com.hzvtc.myproject.utils;

import com.hzvtc.myproject.entity.SystemUser;
import com.hzvtc.myproject.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.jws.Oneway;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author 熊新欣
 * @date 2020-12-08
 */
@Component
public class RedisUtil {
    private final RedisTemplate<Object, Object> redisTemplate;

    private final SystemUserService systemUserService;

    private final Integer EXPIRE_TIME = 60 * 60;

    public RedisUtil(RedisTemplate<Object, Object> redisTemplate, SystemUserService systemUserService) {
        this.redisTemplate = redisTemplate;
        this.systemUserService = systemUserService;
    }

    /**
     * 设置token过期时间
     *
     * @param key token
     */
    public void setExpireTime(String key) {
        redisTemplate.expire(key, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 删除token
     *
     * @param token 。
     */
    public void deleteToken(String token) {
        redisTemplate.delete(token);
    }

    /**
     * 判断是否存在token
     *
     * @param token 。
     * @return 。
     */
    public Boolean hasToken(String token) {
        if (token == null) {
            return false;
        }
        return redisTemplate.hasKey(token);
    }

    /**
     * 存token
     *
     * @param token  键
     * @param object 值
     */
    public void put(String token, Object object) {
        redisTemplate.opsForValue().set(token, object);
        setExpireTime(token);
    }

    /**
     * 根据token取数据
     *
     * @param token token
     * @return 值
     */
    public Long get(String token) {
        if (token == null) {
            return null;
        }
        return (Long) redisTemplate.opsForValue().get(token);
    }

    public Optional<SystemUser> exchange(String token) {
        if (token == null) {
            return Optional.empty();
        }
        Long id = (Long) redisTemplate.opsForValue().get(token);
        return systemUserService.get(id);
    }

}
