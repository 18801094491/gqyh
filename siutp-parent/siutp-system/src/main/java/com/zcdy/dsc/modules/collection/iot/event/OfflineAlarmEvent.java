 package com.zcdy.dsc.modules.collection.iot.event;

import org.springframework.context.ApplicationEvent;

import com.zcdy.dsc.modules.operation.alarm.entity.BusinessWarn;

/**
 * 设备离线事件
 * @author Roberto
 * @date 2020/05/11
 */
public class OfflineAlarmEvent extends ApplicationEvent {

    private BusinessWarn event;
    
    /**
     *
     */
    private static final long serialVersionUID = -4601826304806904383L;

    /**
     * @param source
     */
    public OfflineAlarmEvent(Object source) {
        super(source);
    }
    
    /**
     * @param source
     * @param event
     */
    public OfflineAlarmEvent(Object source, BusinessWarn event) {
        super(source);
        this.event = event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(BusinessWarn event) {
        this.event = event;
    }

    /**
     * @return the event
     */
    public BusinessWarn getEvent() {
        return event;
    }
}
