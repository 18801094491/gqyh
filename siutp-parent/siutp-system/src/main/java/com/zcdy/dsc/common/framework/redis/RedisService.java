package com.zcdy.dsc.common.framework.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author： Roberto 创建时间：2020年2月25日 上午9:51:24 描述:
 * <p>
 * redis 工具类
 * </p>
 */
@Component("redisService")
public class RedisService implements RedisOperation<Object>, InitializingBean {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> operation;

    /**
     * 指定缓存失效时间
     * 
     * @param key
     *            键
     * @param time
     *            时间(秒)
     * @return
     */
    @Override
    public void expire(String key, long time) {
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 根据key 获取过期时间
     * 
     * @param key
     *            键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    @Override
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * 
     * @param key
     *            键
     * @return true 存在 false不存在
     */
    @Override
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     * 
     * @param key
     *            可以传一个值 或多个
     */
    @Override
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 普通缓存获取
     * 
     * @param key
     *            键
     * @return 值
     */
    @Override
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @return true成功 false失败
     */
    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 普通缓存放入并设置时间
     * 
     * @param key
     *            键
     * @param value
     *            值
     * @param time
     *            时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    @Override
    public void setEx(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#setNx(java.lang.String, java.lang.Object)
     */
    @Override
    public Boolean setNx(String key, Object value) {
        return this.operation.setIfAbsent(key, value);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#setNxEx(java.lang.String, java.lang.Object, long)
     */
    @Override
    public Boolean setNxEx(String key, Object value, long expire) {
        return this.operation.setIfAbsent(key, value, expire, TimeUnit.SECONDS);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#update(java.lang.String, java.lang.Object)
     */
    @Override
    public void update(String key, Object value) {
        this.operation.setIfPresent(key, value);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#updateEx(java.lang.String, java.lang.Object, long)
     */
    @Override
    public Boolean updateEx(String key, Object value, long expire) {
        return this.operation.setIfPresent(key, value, expire, TimeUnit.SECONDS);
    }

    /* (non-Javadoc)
     * @see com.zcdy.dsc.common.framework.redis.RedisOperation#getAndSet(java.lang.String, java.lang.Object)
     */
    @Override
    public Object getAndSet(String key, Object value) {
        return this.operation.getAndSet(key, value);
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.operation = this.redisTemplate.opsForValue();
    }
}
