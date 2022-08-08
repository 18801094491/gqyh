package com.zcdy.dsc.modules.datacenter.statistic.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.settle.service.ISettleCustomerService;
import com.zcdy.dsc.modules.settle.vo.TreeVo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.datacenter.statistic.vo.MeterFlowMonthPriceVo;
import com.zcdy.dsc.modules.operation.equipment.entity.MeterFlow;
import com.zcdy.dsc.modules.operation.equipment.logic.MeterFlowLogic;
import com.zcdy.dsc.modules.operation.equipment.service.MeterFlowService;
import com.zcdy.dsc.modules.operation.equipment.vo.MeterFlowVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 描述: 水表日累计量信
 * @author： 智能无人硬核心项目组
 * 创建时间： 2020-02-19 14:44:25
 * 版本号: V1.0
 */
@Api(tags = "水表日累计量信息")
@RestController
@RequestMapping("/equipment/meterFlow")
public class MeterFlowController extends BaseController<MeterFlow, MeterFlowService> {

	@Resource
	private MeterFlowService meterFlowService;

	@Resource
	private MeterFlowLogic meterFlowLogic;
	@Resource
	private ISettleCustomerService iSettleCustomerService;


	/**
	 * 每日累计用水量查询
	 *
	 * @return
	 */
	@AutoLog(value = "每日累计用水量查询")
	@ApiOperation(value = "每日累计用水量查询", notes = "每日累计用水量查询")
	@GetMapping(value = "queryNetFlowDay")
	@ApiImplicitParams({ @ApiImplicitParam(name = "daycount", value = "查看天数，必须为整数数字", paramType = "query"), })
	public Result<List<MeterFlow>> queryNetFlowDay(String daycount) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String staticsDateEnd = sdf.format(new Date());
		Result<List<MeterFlow>> result = new Result<>();
		List<MeterFlow> meterFlowList = meterFlowService.querynetFlowDay(daycount, staticsDateEnd);
		result.setCode(CommonConstant.SC_OK_200);
		result.setResult(meterFlowList);
		return result;
	}

	/**
	 * 每日累计用水量查询
	 *
	 * @return
	 */
	@AutoLog(value = "客户用水累计统计")
	@ApiOperation(value = "客户用水累计统计", notes = "客户用水累计统计")
	@GetMapping(value = "queryAllNetFlowDay")
	public Result<List<MeterFlowVo>> queryAllNetFlowDay() {

		List<MeterFlowVo> meterFlowVoList = meterFlowService.queryAllNetFlowDay();
		Result<List<MeterFlowVo>> result = new Result<>();
		result.setCode(CommonConstant.SC_OK_200);
		result.setResult(meterFlowVoList);
		return result;
	}

	/**
	 * @author：Roberto
	 * @create:2020年3月21日 下午5:34:24
	 * 描述:<p>执行水表日累计量统计，同MeterDayStatisticJob</p>
	 * @see com.zcdy.dsc.modules.operation.equipment.job.MeterDayStatisticJob
	 */
	@AutoLog(value = "执行水表日累计量统计")
	@ApiOperation(value = "执行水表日累计量统计", notes = "执行水表日累计量统计，默认使用功能id：2e1b78e76a8811ea9fded05099cd3eff")
	@GetMapping(value = "statistic")
	public Result<?> add(String moduleId) {
		this.meterFlowLogic.executeStatisticToday(moduleId);
		return Result.ok("success");
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param meterFlow
	 */
	@RequestMapping(value = "/export")
	public ModelAndView exportXls(HttpServletRequest request, MeterFlow meterFlow) {
		return super.exportXls(request, meterFlow, MeterFlow.class, "水表日累计量信");
	}

	/**
	 * 查询客户水表往月抄表单价 禅道#292
	 * 2020-4-26 09:58:00
	 * @author tangchao
	 * @param pageNo 页码
	 * @param pageSize 页大小
	 * @param customerId 客户id, 允许为空, 为空查询全部
	 * @return 水表往月抄表单价
	 */
	@AutoLog(value = "往月抄表单价")
	@ApiOperation(value = "往月抄表单价", notes = "往月抄表单价")
	@GetMapping(value = "/queryAllWaterMonth")
	public Result<?> queryAllWaterMonth(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
										@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
										String customerId){
		if("0".equals(customerId)){
			customerId = "";
		}
		Result<IPage<MeterFlowMonthPriceVo>> result = new Result<>();
		IPage<MeterFlowMonthPriceVo> lists =  this.meterFlowService.queryAllWaterMonth(new Page<MeterFlowMonthPriceVo>(pageNo, pageSize), customerId);
		result.setResult(lists);
		result.success("查询成功.");
		return result;
	}

	/**
	 * 查询客户水表历史所有时间抄表水量 禅道#292
	 * 2020-4-26 09:58:00
	 * @author tangchao
	 * @param customerId 客户id, 允许为空, 为空查询全部客户用水量(总用水量)
	 * @param startDate 查询开始时间 如果为空 默认30天之内
	 * @param endDate 查询结束时间 如果为空 默认30天之内
	 * @return 水表往月抄表单价
	 */
	@AutoLog(value = "用户每日用水量")
	@ApiOperation(value = "用户每日用水量", notes = "用户每日用水量")
	@GetMapping(value = "/queryAllDateWaterFlow")
	public Result<?> queryAllDateWaterFlow(String customerId, String startDate, String endDate){
		if("0".equals(customerId)){
			customerId = "";
		}
		Result<List<Number[]>> result = new Result<>();
		List<Map<String, Number>> lists =  this.meterFlowService.queryAllDateWaterFlow(customerId, startDate, endDate);
		List<Number[]> results = new ArrayList<>();
		for(Map<String, Number> m : lists){
			Number[] item = {m.get("staticsDate"), m.get("netFlowDay")};
			results.add(item);
		}
		result.setResult(results);
		result.success("查询成功.");
		return result;
	}

	/**
	 * 查询所有客户树形 禅道#292
	 * 2020-4-26 09:58:00
	 * @author tangchao
	 * @return 所有客户
	 */
	@AutoLog(value = "查询所有客户树")
	@ApiOperation(value = "查询所有客户树", notes = "查询所有客户树")
	@GetMapping(value = "/queryTreeList")
	public Result<?> queryTreeList(String keyword){
		Result<List<TreeVo>> result = new Result<>();
		List<TreeVo> list = this.iSettleCustomerService.queryCustomerTreeList(keyword);
		result.setResult(list);
		result.success("查询成功.");
		return result;
	}

}