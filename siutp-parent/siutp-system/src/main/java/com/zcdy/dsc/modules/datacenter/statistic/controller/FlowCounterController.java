package com.zcdy.dsc.modules.datacenter.statistic.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.modules.operation.equipment.logic.MeterFlowLogic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author： Roberto
 * 创建时间：2020年3月23日 下午1:50:14
 * 描述: <p>入口流量计统计</p>
 */
@Api(value="流量计统计接口集", tags="流量计统计接口集")
@RestController
@RequestMapping("statistic/fc")
public class FlowCounterController {

	@Resource
	private MeterFlowLogic meterFlowLogic;
	
	/**
	 * @author：Roberto
	 * @create:2020年3月21日 下午5:34:24
	 * 描述:<p>执行水表日累计量统计，同MeterDayStatisticJob</p>
	 * @see com.zcdy.dsc.modules.operation.equipment.job.FlowCountorrDayStatisticJob
	 */
	@AutoLog(value = "执行流量计日累计量统计")
	@ApiOperation(value = "执行流量计日累计量统计", notes = "执行流量计日累计量统计，默认使用功能id：7892dbd36ccb11ea9fded05099cd3eff")
	@GetMapping(value = "day")
	public Result<String> add(String moduleId) {
		this.meterFlowLogic.executeStatisticToday(moduleId);
		//河东水厂执行每日查询
        this.meterFlowLogic.executeHdFlow("d61d3cba7e2711ea9fded05099cd3eff");
		return Result.success("ok", "success");
	}

}
