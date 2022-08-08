package com.zcdy.dsc.modules.operation.work.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zcdy.dsc.constant.WorkingStatus;
import com.zcdy.dsc.modules.operation.work.constant.InspectionPeriodConstant;
import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionPlan;
import com.zcdy.dsc.modules.operation.work.event.WorkJobPlanEvent;
import com.zcdy.dsc.modules.operation.work.service.WorkInspectionPlanService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * 每日定时对工单计划生成工单列表
 *
 * @author songguang.jiao
 * @date 2020/7/9/0009  9:59:16
 */
public class WorkPlanJob implements Job {

    @Resource
    private WorkInspectionPlanService workInspectionPlanService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<WorkInspectionPlan> workInspectionPlans = workInspectionPlanService.list(Wrappers.<WorkInspectionPlan>lambdaQuery().eq(WorkInspectionPlan::getPlanStatus, WorkingStatus.WORKING));

        LocalDate now = LocalDate.now();
        workInspectionPlans.forEach(item -> {
            switch (item.getPeriod()) {
                //单日的每天生成
                case InspectionPeriodConstant.DAY:
                    this.createWorkJobEvent(item.getId(), now.plusDays(1));
                    break;
                case InspectionPeriodConstant.WEEK:
                    //如果本周一加上周期等于今天就生成下一周期的
                    if (now.with(DayOfWeek.of(Integer.parseInt(item.getPeriodExecuteDate()))).compareTo(now) == 0) {
                        this.createWorkJobEvent(item.getId(), now.plusWeeks(1));
                    }
                    break;
                case InspectionPeriodConstant.MONTH:
                    //如果执行日期是今天就执行下一周期
                    if (now.with(TemporalAdjusters.firstDayOfMonth()).plusDays(Integer.parseInt(item.getPeriodExecuteDate()) - 1).compareTo(now) == 0) {
                        this.createWorkJobEvent(item.getId(), now.plusMonths(1));
                    }
                default:
                    break;
            }
        });


    }

    /**
     * 生成事件
     *
     * @param planId    计划id
     * @param localDate 执行日期
     */
    private void createWorkJobEvent(String planId, LocalDate localDate) {
        WorkJobPlanEvent workJobPlanEvent = new WorkJobPlanEvent(this, planId, localDate);
        applicationEventPublisher.publishEvent(workJobPlanEvent);

    }
}
