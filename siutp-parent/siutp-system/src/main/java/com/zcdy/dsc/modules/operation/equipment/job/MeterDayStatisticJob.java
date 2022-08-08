package com.zcdy.dsc.modules.operation.equipment.job;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zcdy.dsc.modules.operation.equipment.logic.MeterFlowLogic;

/**
 * @author： Roberto
 * 创建时间：2020年3月21日 下午5:25:35
 * 描述: <p>执行对每日统计昨天的水表数据统计</p>
 */
public class MeterDayStatisticJob implements Job{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String parameter;

	@Resource
	private MeterFlowLogic meterFlowLogic;
	
	/*
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		if(StringUtils.isEmpty(parameter)){
			if(logger.isWarnEnabled()){
				logger.warn("未设置统计模块的id，统计任务自动跳过……");
			}
			return;
		}
		
		this.meterFlowLogic.executeStatisticYestoday(parameter);
	}
	
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
