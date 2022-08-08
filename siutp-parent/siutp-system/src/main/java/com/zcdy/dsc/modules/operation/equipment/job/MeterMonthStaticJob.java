package com.zcdy.dsc.modules.operation.equipment.job;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.zcdy.dsc.modules.operation.equipment.logic.MeterFlowLogic;
import com.zcdy.dsc.modules.settle.service.SettleBatchService;

/*
 * 每日执行扫描任务，查询到期水价规则，然后按照规则执行抄表
 */

/**
 * 描述: 每日扫描合同水表到期执行抄表操作   (每日扫描合同水表，到期执行抄表 当月月末的正向累计量)  
 * @author: songguang.jiao
 * 创建时间:  2020年4月20日 上午11:12:34
 * 版本: V1.0
 */
public class MeterMonthStaticJob implements Job{

	@Resource
	private MeterFlowLogic meterFlowLogic;
	
	@SuppressWarnings("unused")
    private String parameter;

	@Resource
	private SettleBatchService settleBatchService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		//---update-begin-----autor:tangchao------date:2020-5-8-------for:添加生成结算批次表--------
		this.settleBatchService.executeAll(today);
		//---update-end-----autor:tangchao------date:2020-5-8-------for:添加生成结算批次表----------
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
