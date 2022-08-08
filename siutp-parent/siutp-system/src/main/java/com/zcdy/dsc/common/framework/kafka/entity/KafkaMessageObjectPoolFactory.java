package com.zcdy.dsc.common.framework.kafka.entity;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * ValueEntity对像池工程
 * @author Roberto
 * @date 2020/07/17
 */
public class KafkaMessageObjectPoolFactory extends BasePooledObjectFactory<ValueEntity> {

    @Override
    public ValueEntity create() throws Exception {
        return ValueEntity.getEntity();
    }

    @Override
    public PooledObject<ValueEntity> wrap(ValueEntity obj) {
        return new DefaultPooledObject<ValueEntity>(obj);
    }
}
