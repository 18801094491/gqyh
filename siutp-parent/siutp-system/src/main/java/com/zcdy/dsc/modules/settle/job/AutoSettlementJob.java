package com.zcdy.dsc.modules.settle.job;

import com.zcdy.dsc.modules.settle.service.SettlementStatisticsService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * 自动周期结算
 * 1. 查找当前有有效合同的客户
 * 2. 每个客户生成一张结算单
 * @author tangchao
 * @date 2020/5/12
 */
public class AutoSettlementJob implements Job {

    private final static Logger LOGGER = LoggerFactory.getLogger(AutoSettlementJob.class);

    @Resource
    private SettlementStatisticsService settlementStatisticsService;

    @Override
    public void execute(JobExecutionContext context) {
        this.settlementStatisticsService.execute(context);
    }


}
