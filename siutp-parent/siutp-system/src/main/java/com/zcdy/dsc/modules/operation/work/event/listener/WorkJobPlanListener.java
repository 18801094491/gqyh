package com.zcdy.dsc.modules.operation.work.event.listener;

import com.zcdy.dsc.modules.operation.work.event.WorkJobPlanEvent;
import com.zcdy.dsc.modules.operation.work.service.WorkJobService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 生成工单监听器
 * @author songguang.jiao
 * @date 2020/7/9/0009  13:32:04
 */
@Component
public class WorkJobPlanListener implements ApplicationListener<WorkJobPlanEvent> {

    @Resource
    private WorkJobService workJobService;

    @Override
    public void onApplicationEvent(WorkJobPlanEvent event) {
        workJobService.addWorkJob(event.getPlanId(), event.getExecuteDate().toString());
    }
}
