package com.zcdy.dsc.modules.collection.iot.event.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.zcdy.dsc.modules.collection.iot.event.EquipmentStatusEvent;

/**
 * 设备状态事件监听器
 * @author Roberto
 * @date 2020/07/17
 */
@Component
public class DefaultEquipmentStatusEventListener implements ApplicationListener<EquipmentStatusEvent> {

    @Override
    public void onApplicationEvent(EquipmentStatusEvent event) {
         
        
    }
}
