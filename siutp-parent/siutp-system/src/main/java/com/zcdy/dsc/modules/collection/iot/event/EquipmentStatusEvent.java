package com.zcdy.dsc.modules.collection.iot.event;

import java.math.BigDecimal;

import org.springframework.context.ApplicationEvent;

import com.zcdy.dsc.modules.collection.iot.constant.EquipmentEnum;
import com.zcdy.dsc.modules.collection.iot.constant.EventTypeEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Roberto
 * @date 2020/07/17
 */
@Setter
@Getter
@ToString
public class EquipmentStatusEvent extends ApplicationEvent {

    private static final long serialVersionUID = 5887239310657377340L;

    /**
     * 设备类型
     */
    private EquipmentEnum equipment;
    
    /**
     * 采集值
     */
    private BigDecimal value;
    
    /**
     * 事件类型
     */
    private EventTypeEnum eventType;
    
    /**
     * @param source
     */
    public EquipmentStatusEvent(Object source) {
        super(source);
    }

    /**
     * @param source
     * @param equipment
     */
    public EquipmentStatusEvent(Object source, EquipmentEnum equipment) {
        super(source);
        this.equipment = equipment;
    }
}
