package com.zcdy.dsc.common.framework.kafka.config;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zcdy.dsc.common.framework.kafka.entity.KafkaMessageObjectPoolFactory;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntityPool;

/**
 * valueEntity对象池配置
 * @author Roberto
 * @date 2020/07/17
 */
@Configuration
public class ValueEntityConfig {

    public GenericObjectPool<ValueEntity> valueEntityObjectPool() {
        
        KafkaMessageObjectPoolFactory factory = new KafkaMessageObjectPoolFactory();
        GenericObjectPoolConfig<ValueEntity> poolConfig = new GenericObjectPoolConfig<>();
        // 最大空闲数
        poolConfig.setMaxIdle(5);
        // 最小空闲数, 池中只有一个空闲对象的时候，池会在创建一个对象，并借出一个对象，从而保证池中最小空闲数为1
        poolConfig.setMinIdle(1);
        // 最大池对象总数
        poolConfig.setMaxTotal(30);
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        poolConfig.setMinEvictableIdleTimeMillis(1800000);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        poolConfig.setTimeBetweenEvictionRunsMillis(1800000 * 2L);
        // 在获取对象的时候检查有效性, 默认false
        poolConfig.setTestOnBorrow(true);
        // 在归还对象的时候检查有效性, 默认false
        poolConfig.setTestOnReturn(false);
        // 在空闲时检查有效性, 默认false
        poolConfig.setTestWhileIdle(false);
        // 最大等待时间， 默认的值为-1，表示无限等待。
        poolConfig.setMaxWaitMillis(5000);
        // 是否启用后进先出, 默认true
        poolConfig.setLifo(true);
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        poolConfig.setBlockWhenExhausted(true);
        // 每次逐出检查时 逐出的最大数目 默认3
        poolConfig.setNumTestsPerEvictionRun(3);
        return new GenericObjectPool<ValueEntity>(factory, poolConfig);
    }
    
    @Bean
    public ValueEntityPool valueEntityPool() {
        return new ValueEntityPool(valueEntityObjectPool());
    }
}
