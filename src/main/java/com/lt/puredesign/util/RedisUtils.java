package com.lt.puredesign.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @description: Redis工具类
 * @author: Lt
 * @date: 2022/3/16 20:03
 */
@Component
public class RedisUtils {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static StringRedisTemplate staticRedisTemplate;

    @PostConstruct
    public void setRedisTemplate() {
        staticRedisTemplate = redisTemplate;
    }

    /**
     * 设置缓存
     */
    public static void setCache(String key, String value) {
        staticRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 删除缓存
     */
    public static void flushRedis(String key) {
        staticRedisTemplate.delete(key);
    }

    /**
     * 获取缓存
     */
    public static String get(String key) {
        return staticRedisTemplate.opsForValue().get(key);
    }
}
