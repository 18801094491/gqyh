package com.zcdy.dsc.modules.collection.iot.event;

import org.springframework.context.ApplicationEvent;

import com.zcdy.dsc.modules.operation.alarm.entity.BusinessWarn;

/**
 * @author: Roberto
 * 创建时间:2020年4月9日 下午7:18:50
 * 描述: <p>告警事件自动关闭事件(系统事件event)</p>
 */
public class AlarmAutoCloseEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;

	private BusinessWarn event;
	
	public AlarmAutoCloseEvent(Object source) {
		super(source);
	}

	public AlarmAutoCloseEvent(Object source, BusinessWarn event){
		super(source);
		this.event = event;
	}

	public BusinessWarn getEvent() {
		return event;
	}
}
