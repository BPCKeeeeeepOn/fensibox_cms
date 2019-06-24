package com.fensibox.cms.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    final String KEY = "fansbox_cms_";
    
    /**
     * 添加缓存
     * 过期时间默认1小时
     *
     * @param key
     * @param value
     */
    public void putCache(String key, String value) {
        stringRedisTemplate.opsForValue().set(KEY + key, value, 1, TimeUnit.HOURS);
        return;
    }
    
    /**
     * 添加缓存
     *
     * @param key
     * @param value
     * @param timeout 超时时间，单位小时，如果为0，则无限制
     */
    public void putCache(String key, String value, long timeout) {
        if (timeout == 0) {
            stringRedisTemplate.opsForValue().set(KEY + key, value);
        } else {
            stringRedisTemplate.opsForValue().set(KEY + key, value, timeout, TimeUnit.HOURS);
        }
    }
    
    /**
     * 添加缓存
     *
     * @param key
     * @param value
     * @param timeout 超时时间，单位小时，如果为0，则无限制
     */
    public void putCache(String key, String value, long timeout, TimeUnit timeUnit) {
        if (timeout <= 0) {
            throw new IllegalArgumentException("time out cannot less than zero");
        }
        
        stringRedisTemplate.opsForValue().set(KEY + key, value, timeout, timeUnit);
    }
    
    
    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public String getCache(String key) throws RedisConnectionFailureException {
        return stringRedisTemplate.opsForValue().get(KEY + key);
    }
    
    /**
     * 删除缓存
     *
     * @param key
     */
    public void delCache(String key) {
        stringRedisTemplate.delete(KEY + key);
    }
}
