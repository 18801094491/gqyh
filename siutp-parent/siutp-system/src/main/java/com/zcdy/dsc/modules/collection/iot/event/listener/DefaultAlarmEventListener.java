package com.zcdy.dsc.modules.collection.iot.event.listener;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.zcdy.dsc.modules.collection.iot.event.AlarmEvent;
import com.zcdy.dsc.modules.operation.alarm.service.BusinessService;

/**
 * 告警事件，事件监听器
 * @author： Roberto
 * @date 2020年3月8日 上午11:34:21
 */
@Component
public class DefaultAlarmEventListener implements ApplicationListener<AlarmEvent> {

	@Resource
	private BusinessService businessService;
	
	/*
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(AlarmEvent event) {
		String eventId = event.getEvent().getId();
		//根据策略发布通知
		this.businessService.sendWarnMsg(eventId);
	}

}
