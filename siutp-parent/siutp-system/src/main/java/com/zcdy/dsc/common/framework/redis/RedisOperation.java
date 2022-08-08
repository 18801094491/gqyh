 package com.zcdy.dsc.common.framework.redis;

 /**
  * redis操作接口
 * @author Roberto
 * @date 2020/05/12
 */
public interface RedisOperation<T> {

    /**
     * 根据key获取value
     * @param key
     * @return value
     */
    T get(String key);
    
    /**
     * 设置值
     * @param key
     * @param value
     */
    void set(String key, T value);
    
    /**
     * 只有不存在的时候设置
     * @param key
     * @param value
     * @return 是否设置成功
     */
    Boolean setNx(String key, T value);
    
    /**
     * 设置键值对有效期
     * @param key
     * @param value
     * @param expire 秒
     */
    void setEx(String key, T value, long expire);
    
    /**
     * 设置键值对有效期，只有不存在的时候设置
     * @param key
     * @param value
     * @param expire 秒
     * @return 是否设置成功
     */
    Boolean setNxEx(String key, T value, long expire);
    
    /**
     * 设置键值对有效期,只有key存在的时候设置
     * @param key
     * @param value
     * @param expire 秒
     */
    void update(String key, T value);
    
    /**
     * 设置键值对有效期，只有key存在的时候设置
     * @param key
     * @param value
     * @param expire 秒
     * @return 是否设置成功
     */
    Boolean updateEx(String key, T value, long expire);
    
    /**
     * 获取key的旧值并设置新的值
     * @param key
     * @param value
     * @return
     */
    T getAndSet(String key, T value);
    
    /**
     * 判断key是否存在
     * @param key
     * @return true 存在 ,false不存在
     */
    Boolean hasKey(String key);
    
    /**
     * 根据key 获取过期时间
     * @param key
     * @return 时间(秒) 返回0代表为永久有效
     */
    long getExpire(String key);
    
    /**
     * 指定缓存失效时间
     * @param key
     * @param time
     * @return 是否设置成功
     */
    void expire(String key, long time);
    
    /**
     * 删除缓存
     * @param  key 可以传一个值 或多个
     */
    public void del(String... key);
}
