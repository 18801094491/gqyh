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
 * 描述: <p>执行对每日的流量计数据统计</p>
 */
public class FlowCountorrDayStatisticJob implements Job{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	//定时任务需要使用  7892dbd36ccb11ea9fded05099cd3eff 作为参数
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
		
		this.meterFlowLogic.executeStatisticToday(parameter);
		
		//河东水厂执行每日查询
		this.meterFlowLogic.executeHdFlow("d61d3cba7e2711ea9fded05099cd3eff");
	}
	
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
