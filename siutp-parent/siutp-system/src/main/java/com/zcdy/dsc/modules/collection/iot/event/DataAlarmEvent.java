package com.zcdy.dsc.modules.collection.iot.event;

import org.springframework.context.ApplicationEvent;

import com.zcdy.dsc.modules.operation.alarm.entity.BusinessWarn;

/**
 * @author： Roberto
 * 创建时间：2020年3月8日 上午11:29:13
 * 描述: <p>定义采集设备数据告警事件</p>
 */
public class DataAlarmEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	private BusinessWarn event;
	
	public DataAlarmEvent(Object source) {
		super(source);
	}

	public DataAlarmEvent(Object source, BusinessWarn event) {
        super(source);
        this.event = event;
    }

	public BusinessWarn getEvent() {
		return event;
	}

	public void setEvent(BusinessWarn event) {
		this.event = event;
	}
}
