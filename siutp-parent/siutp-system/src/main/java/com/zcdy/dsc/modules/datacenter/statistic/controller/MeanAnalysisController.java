package com.zcdy.dsc.modules.datacenter.statistic.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.influxdb.dto.Query;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.modules.collection.iot.service.IIotVariableInfoService;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointGroupData;
import com.zcdy.dsc.modules.datacenter.statistic.service.ModuleVarService;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentVariableVO;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author： Roberto
 * 创建时间：2020年3月19日 下午12:28:33
 * 描述: <p>指标数据表格展示接口</p>
 */
@Api(value = "指标表格数据展示接口集", tags = "指标数据展示接口集")
@RestController
@RequestMapping("iot/vardata/analysis")
public class MeanAnalysisController {

	@Resource
	private IIotVariableInfoService iotVariableInfoService;

	// 统计模块业务逻辑
	@Resource
	private ModuleVarService moduleVarService;

	@Resource
	private InfluxService influxService;

	@ApiOperation(value = "统计项目接口", notes = "统计项目接口,目前id是<ce3992ca69c911ea9fded05099cd3eff>")
	@GetMapping("/item/{moduleId}")
	public Result<List<SysCateDropdown>> getAnalyseItem(
			@ApiParam(name = "moduleId", value = "统计项目的id") @PathVariable String moduleId) {
		List<SysCateDropdown> datas = this.moduleVarService.queryModuleItem(moduleId);
		return Result.success(datas, "success");
	}

	@ApiOperation(value="根据统计项目出数据", notes="根据统计项目出数据,目前id是<ce3992ca69c911ea9fded05099cd3eff>")
	@GetMapping("/{moduleId}")
	public Result<List<PointGroupData>> getAnalyseData(@ApiParam(name="moduleId", value="统计项目的id") @PathVariable String moduleId,
			@ApiParam(name="typeCode", value="统计变量类型编码") String typeCode,Long startTime,Long endTime){
		
		//如果时间为空的话,默认查询当前1小时
		if(startTime==null || endTime==null ){
			endTime=System.currentTimeMillis()*1000000L;
		   LocalDateTime localDateTime = LocalDateTime.now();
		   localDateTime = localDateTime.plusHours(-1);
	        startTime = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli()*1000000L;
		}else{
			startTime=startTime.longValue()*1000000L;
			endTime=endTime.longValue()*1000000L;
		}
		
		//根据统计项目id和统计变量查询变量信息和对应资产信息
		List<EquipmentVariableVO> datas = this.moduleVarService.queryEquipAndVarByModuleId(moduleId, typeCode);
		StringBuilder sqlBuilder = new StringBuilder();
		String measurement = createSQLByTime(startTime, endTime, sqlBuilder);
		//sqlBuilder.append("select max(var_value) as maxValue,min(var_value) as minValue, MEAN(var_value) as avgValue from iot_point_data where ");
		
		int index = 0,size = datas.size();
		Map<String, EquipmentVariableVO> config = new HashMap<>();
		if(size==0){
			Result<List<PointGroupData>> result = new Result<>();
			result.error("变量信息配置错误,请重新配置");
			return result;
		}
		sqlBuilder.append("(");
		for(;index < size; index++){
			if(index>0){
				sqlBuilder.append(" or ");
			}
			EquipmentVariableVO item = datas.get(index);
			sqlBuilder.append("var_name='");
			sqlBuilder.append(item.getVariableName());
			sqlBuilder.append("'");
			config.put(item.getVariableName(), item);
		}
		sqlBuilder.append(")");
		sqlBuilder.append(" and time>=")
					.append(startTime)
					.append(" and time<=")
					.append(endTime);
		//sqlBuilder.append(" and quality_stamp="+QualityStampConstant.QUALITY_GOOD);
		sqlBuilder.append(" group by var_name fill(0)");
		
		Map<String, Object> varOrderConfig = new HashMap<>();
		List<Map<String, Object>> varOrders = this.moduleVarService.getVarOrder(moduleId);
		varOrders.forEach(item->{
			varOrderConfig.put((String) item.get("var_name"), item.get("display_order"));
		});
		Query query = new Query(sqlBuilder.toString());
		List<PointGroupData> groupDatas = this.influxService.query(query, PointGroupData.class, measurement);
		for(PointGroupData data : groupDatas){
			EquipmentVariableVO vo = config.get(data.getVarName());
			data.setEquipmentLocation(vo.getEquipmentLocation());
			data.setEquipmentSection(vo.getSection());
			data.setEquipmentType(vo.getEquipmentType());
			data.setVariableTitle(vo.getVariableTitle()+"["+vo.getVariableUnit()+"]");
			int scale = vo.getScale();
			if(scale>=0){
				BigDecimal bgAvg = new BigDecimal(data.getAvgValue());
				BigDecimal bgMin = new BigDecimal(data.getMinValue());
				BigDecimal bgMax = new BigDecimal(data.getMaxValue());
				data.setAvgValue(bgAvg.setScale(scale,RoundingMode.HALF_UP).toString());
				data.setMinValue(bgMin.setScale(scale,RoundingMode.HALF_UP).toString());
				data.setMaxValue(bgMax.setScale(scale,RoundingMode.HALF_UP).toString());
			}
			Object ord = varOrderConfig.get(data.getVarName());
			if(null==ord){
				ord = 0;
			}
			data.setDisplayOrder((int) ord);
		}
		Collections.sort(groupDatas);
		return Result.success(groupDatas, "success");
	}
	
	/**
	 * 描述: 导出
	 * @author：  songguang.jiao
	 * 创建时间：  2020年3月25日 下午5:27:17
	 * 版本号: V1.0
	 */
	@ApiOperation(value = "导出", notes = "导出")
	@GetMapping("/exprot/{moduleId}")
	public ModelAndView exprot(@ApiParam(name = "moduleId", value = "统计项目的id") @PathVariable String moduleId,
			@ApiParam(name = "typeCode", value = "统计变量类型编码") String typeCode, Long startTime, Long endTime) {
		// 如果时间为空的话,默认查询当前1小时
		if (startTime == null || endTime == null) {
			endTime = System.currentTimeMillis() * 1000000L;
			LocalDateTime localDateTime = LocalDateTime.now();
			localDateTime = localDateTime.plusHours(-1);
			startTime = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() * 1000000L;
		} else {
			startTime = startTime.longValue() * 1000000L;
			endTime = endTime.longValue() * 1000000L;
		}

		// 根据统计项目id和统计变量查询变量信息和对应资产信息
		List<EquipmentVariableVO> datas = this.moduleVarService.queryEquipAndVarByModuleId(moduleId, typeCode);
		StringBuilder sqlBuilder = new StringBuilder();
		String measurement = createSQLByTime(startTime, endTime, sqlBuilder);
		
		//sqlBuilder.append("select max(var_value) as maxValue,min(var_value) as minValue, MEAN(var_value) as avgValue from iot_point_data where ");
		
		int index = 0, size = datas.size();
		Map<String, EquipmentVariableVO> config = new HashMap<>();
		if (size == 0) {
			Result.error("变量信息配置错误,请重新配置");
		}
		sqlBuilder.append("(");
		for (; index < size; index++) {
			if (index > 0) {
				sqlBuilder.append(" or ");
			}
			EquipmentVariableVO item = datas.get(index);
			sqlBuilder.append("var_name='");
			sqlBuilder.append(item.getVariableName());
			sqlBuilder.append("'");
			config.put(item.getVariableName(), item);
		}
		sqlBuilder.append(")");
		sqlBuilder.append(" and time>=").append(startTime).append(" and time<=").append(endTime);
		//sqlBuilder.append(" and quality_stamp=" + QualityStampConstant.QUALITY_GOOD);
		sqlBuilder.append(" group by var_name fill(none)");
		Query query = new Query(sqlBuilder.toString());
		List<PointGroupData> groupDatas = this.influxService.query(query, PointGroupData.class, measurement);
		
		Map<String, Object> varOrderConfig = new HashMap<>();
		List<Map<String, Object>> varOrders = this.moduleVarService.getVarOrder(moduleId);
		varOrders.forEach(item->{
			varOrderConfig.put((String) item.get("var_name"), item.get("display_order"));
		});
		
		for(PointGroupData data : groupDatas){
			EquipmentVariableVO vo = config.get(data.getVarName());
			data.setEquipmentLocation(vo.getEquipmentLocation());
			data.setEquipmentSection(vo.getSection());
			data.setEquipmentType(vo.getEquipmentType());
			data.setVariableTitle(vo.getVariableTitle()+"["+vo.getVariableUnit()+"]");
			int scale = vo.getScale();
			if(scale>=0){
				BigDecimal bgAvg = new BigDecimal(data.getAvgValue());
				BigDecimal bgMin = new BigDecimal(data.getMinValue());
				BigDecimal bgMax = new BigDecimal(data.getMaxValue());
				data.setAvgValue(bgAvg.setScale(scale,RoundingMode.HALF_UP).toString());
				data.setMinValue(bgMin.setScale(scale,RoundingMode.HALF_UP).toString());
				data.setMaxValue(bgMax.setScale(scale,RoundingMode.HALF_UP).toString());
			}
			data.setDisplayOrder((int) varOrderConfig.get(data.getVarName()));
		}
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime=sdf.format(new Date());
		String secondTitle="导出人:" + sysUser.getRealname()+"  导出时间："+nowTime;
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "统计值分析");
		mv.addObject(NormalExcelConstants.CLASS, PointGroupData.class);
		mv.addObject(NormalExcelConstants.PARAMS,
				new ExportParams("统计值分析" + "报表", secondTitle, "统计值分析"));
		mv.addObject(NormalExcelConstants.DATA_LIST, groupDatas);
		Collections.sort(groupDatas);
		return mv;
	}
	
	private String createSQLByTime(long leftTime, long rightTime, StringBuilder sqlBuilder){
		Instant start = Instant.ofEpochMilli(leftTime/1000000L);
		Instant end = Instant.ofEpochMilli(rightTime/1000000L);
		ZoneId zoneid = ZoneId.systemDefault();
		LocalDateTime left = LocalDateTime.ofInstant(start, zoneid), right = LocalDateTime.ofInstant(end, zoneid);
		String measurement = "iot_point_data";
		if(ChronoUnit.HOURS.between(left, right)<=1){
			//用纳秒为单位
			sqlBuilder.append("select max(var_value) as maxValue,min(var_value) as minValue, MEAN(var_value) as avgValue from iot_point_data where ");
			measurement = "iot_point_data";
		}else{ 
			if(ChronoUnit.YEARS.between(left, right)>=1){
				sqlBuilder.append("select mean(avg_value) as avgValue,max(max_value) as maxValue,min(min_value) as minValue from iot_point_data_month where ");
				measurement = "iot_point_data_month";
			}else if(ChronoUnit.MONTHS.between(left, right)>=6){
				//用月
				sqlBuilder.append("select mean(avg_value) as avgValue,max(max_value) as maxValue,min(min_value) as minValue from iot_point_data_day where ");
				measurement = "iot_point_data_day";
			} else if(ChronoUnit.DAYS.between(left, right)>30){
				//用日
				sqlBuilder.append("select mean(avg_value) as avgValue,max(max_value) as maxValue,min(min_value) as minValue from iot_point_data_hour where ");
				measurement = "iot_point_data_hour";
			} else if(ChronoUnit.HOURS.between(left, right)>24){
				//用半小时
				sqlBuilder.append("select mean(avg_value) as avgValue,max(max_value) as maxValue,min(min_value) as minValue from iot_point_data_half_hour where ");
				measurement = "iot_point_data_half_hour";
			} else if(ChronoUnit.HOURS.between(left, right)>2){
				//用15m
				sqlBuilder.append("select mean(avg_value) as avgValue,max(max_value) as maxValue,min(min_value) as minValue from iot_point_data_15m where ");
				measurement = "iot_point_data_15m";
			}else {
				sqlBuilder.append("select mean(avg_value) as avgValue,max(max_value) as maxValue,min(min_value) as minValue from iot_point_data where ");
				measurement = "iot_point_data";
			}
		}
		return measurement;
	}	
}
