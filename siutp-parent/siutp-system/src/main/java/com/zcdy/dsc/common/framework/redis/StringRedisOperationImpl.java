 package com.zcdy.dsc.common.framework.redis;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
  * 字符串形式redis操作实现
 * @author Roberto
 * @date 2020/05/12
 */
@Service("stringRedisOperation")
public class StringRedisOperationImpl implements RedisOperation<String>, InitializingBean {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
    private ValueOperations<String, String> operation;
    
    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#get(java.lang.String)
     */
    @Override
    public String get(@NotEmpty String key) {
         return operation.get(key);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#set(java.lang.String, java.lang.String)
     */
    @Override
    public void set(String key, String value) {
         operation.set(key, value);
    }
    
    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        operation = this.stringRedisTemplate.opsForValue();
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#setEx(java.lang.String, java.lang.String)
     */
    @Override
    public Boolean setNx(String key, String value) {
       return this.operation.setIfAbsent(key, value);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#setEx(java.lang.String, java.lang.String, long)
     */
    @Override
    public void setEx(String key, String value, long expire) {
         this.operation.set(key, value, expire, TimeUnit.SECONDS);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#setNxEx(java.lang.String, java.lang.String, long)
     */
    @Override
    public Boolean setNxEx(String key, String value, long expire) {
         return this.operation.setIfAbsent(key, value, expire, TimeUnit.SECONDS);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#update(java.lang.String, java.lang.String)
     */
    @Override
    public void update(String key, String value) {
         this.operation.setIfPresent(key, value);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#updateEx(java.lang.String, java.lang.String, long)
     */
    @Override
    public Boolean updateEx(String key, String value, long expire) {
         return this.operation.setIfPresent(key, value, expire, TimeUnit.SECONDS);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#getAndSet(java.lang.String, java.lang.String)
     */
    @Override
    public String getAndSet(String key, String value) {
         return this.operation.getAndSet(key, value);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#hasKey(java.lang.String)
     */
    @Override
    public Boolean hasKey(String key) {
         return this.stringRedisTemplate.hasKey(key);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#getExpire(java.lang.String)
     */
    @Override
    public long getExpire(String key) {
         return this.stringRedisTemplate.getExpire(key);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#expire(java.lang.String, long)
     */
    @Override
    public void expire(String key, long time) {
        if (time > 0) {
            this.stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#del(java.lang.String[])
     */
    @Override
    public void del(String... key) {
        List<String> keys = Arrays.asList(key);
        this.stringRedisTemplate.delete(keys);
    }
}
