package com.zcdy.dsc.common.framework.kafka.entity;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * ValueEntity 对象池
 * @author Roberto
 * @date 2020/07/17
 */
@Slf4j
public class ValueEntityPool {

    private GenericObjectPool<ValueEntity> objectPool;

    private final long borrowMaxWaitMillis = 30;
    
    public ValueEntityPool() {
        super();
        KafkaMessageObjectPoolFactory factory = new KafkaMessageObjectPoolFactory();
        this.objectPool = new GenericObjectPool<>(factory);
    }

    /**
     * @param objectPool
     */
    public ValueEntityPool(GenericObjectPool<ValueEntity> objectPool) {
        super();
        this.objectPool = objectPool;
    }

    public ValueEntityPool(GenericObjectPoolConfig<ValueEntity> config) {
        super();
        KafkaMessageObjectPoolFactory factory = new KafkaMessageObjectPoolFactory();
        this.objectPool = new GenericObjectPool<ValueEntity>(factory, config);
    }

    /**
     * @param objectPool
     * @param kafkaMessageObjectPoolFactory
     */
    public ValueEntityPool(GenericObjectPoolConfig<ValueEntity> config,
        KafkaMessageObjectPoolFactory kafkaMessageObjectPoolFactory) {
        super();
        KafkaMessageObjectPoolFactory factory = new KafkaMessageObjectPoolFactory();
        this.objectPool = new GenericObjectPool<ValueEntity>(factory, config);
    }

    /**
     * 从对象池中获取对象
     * @return ValueEntity
     */
    public ValueEntity get() {
        ValueEntity entity = null;
        try {
            entity = this.objectPool.borrowObject(borrowMaxWaitMillis);
        } catch (Exception e) {
            log.error("获取对象池对象失败……");
            log.error(e.getMessage());
            entity = ValueEntity.getEntity();
        }
        return entity;
    }
    
    /**
     * 使用完对象要归还
     * @param entity
     */
    public void release(ValueEntity entity) {
        this.objectPool.returnObject(entity);
    }
}
