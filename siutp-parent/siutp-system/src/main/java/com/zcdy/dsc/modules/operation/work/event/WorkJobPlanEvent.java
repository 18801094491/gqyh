package com.zcdy.dsc.modules.operation.work.event;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDate;

/**
 * @author songguang.jiao
 * @date 2020/7/9/0009  13:27:32
 */
public class WorkJobPlanEvent extends ApplicationEvent {

    /**
     * 计划id
     */
    private String planId;

    /**
     *实际执行日期
     */
    private LocalDate executeDate;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public WorkJobPlanEvent(Object source) {
        super(source);
    }

    /**
     *
     * @param source  原对象
     * @param planId 计划id
     * @param executeDate 执行日期
     */
    public WorkJobPlanEvent(Object source,String planId,LocalDate executeDate){
        super(source);
        this.planId=planId;
        this.executeDate=executeDate;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public LocalDate getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(LocalDate executeDate) {
        this.executeDate = executeDate;
    }
}
