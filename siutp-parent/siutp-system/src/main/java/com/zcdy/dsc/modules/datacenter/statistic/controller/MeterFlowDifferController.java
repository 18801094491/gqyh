package com.zcdy.dsc.modules.datacenter.statistic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.modules.datacenter.statistic.service.ChartService;
import com.zcdy.dsc.modules.datacenter.statistic.vo.MeterFlowBarChart;
import com.zcdy.dsc.modules.datacenter.statistic.vo.MeterFlowHistoryVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 描述: 用水量产销差统计(按照年月日统计)
 * @author：  songguang.jiao
 * 创建时间：  2020年3月23日 下午5:15:43
 * 版本号: V1.0
 */
@Api(tags="用水量产销差统计")
@RestController
@RequestMapping("/meter/differ")
public class MeterFlowDifferController {

	@Autowired
	private ChartService chartService;
	
	/**
	 * 描述: 历史用水量产销差统计  
	 * @author：  songguang.jiao
	 * 创建时间：  2020年3月23日 下午5:18:30
	 * 版本号: V1.0
	 */
	@PostMapping("/data")
	@ApiOperation(value="历史用水量产销差统计",notes="历史用水量产销差统计")
	public Result<MeterFlowBarChart> queryData(@RequestBody MeterFlowHistoryVo meterFlowHistoryVo){
		Result<MeterFlowBarChart> result=new Result<MeterFlowBarChart>();
		MeterFlowBarChart barChart=chartService.queryData(meterFlowHistoryVo);
		result.setResult(barChart);
		result.success("success");
		return result;
	}
	
}
