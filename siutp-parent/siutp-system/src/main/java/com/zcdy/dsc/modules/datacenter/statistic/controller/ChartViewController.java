package com.zcdy.dsc.modules.datacenter.statistic.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.constant.CycletypeConstant;
import com.zcdy.dsc.modules.constant.QualityStampConstant;
import com.zcdy.dsc.modules.datacenter.statistic.entity.Chart;
import com.zcdy.dsc.modules.datacenter.statistic.entity.ChartInfo;
import com.zcdy.dsc.modules.datacenter.statistic.entity.ChartInfo.ChartsData;
import com.zcdy.dsc.modules.datacenter.statistic.entity.ChartInfo.ChartsValue;
import com.zcdy.dsc.modules.datacenter.statistic.entity.ChartSerial;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointData;
import com.zcdy.dsc.modules.datacenter.statistic.param.TimeWidthParam;
import com.zcdy.dsc.modules.datacenter.statistic.service.ChartItemService;
import com.zcdy.dsc.modules.datacenter.statistic.service.ChartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.influxdb.dto.Query;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author： Roberto
 * 创建时间：2020年3月14日 下午4:56:24
 * 描述: <p>展示统计图</p>
 */
@Api(tags="基础图表接口集")
@RestController
@RequestMapping("/statistic/chart/view")
public class ChartViewController {

	@Resource
	private ChartService chartService;
	
	@Resource
	private ChartItemService chartItemService;
	
	@Resource
	private InfluxService influxService;
	
	private Pattern pattern = Pattern.compile("[0-5][0-9]:[0-5][0-9]");
	
	@ApiOperation(value="获取所有的图表信息", notes="降序排列")
	@GetMapping("/charts")
	public Result<List<Chart>> getAllChartInfo(){
		QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().select(Chart::getId, Chart::getDisplayOrder).eq(Chart::getStatisticStatus, StatusConstant.RUN).orderByDesc(Chart::getDisplayOrder);
		List<Chart> charts = this.chartService.list(queryWrapper);
		return Result.success(charts, "success");
	} 
	
	@ApiOperation(value="图表数据接口", notes="图表数据接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", dataType="string", required=true)
	})
	@PostMapping("/{id}")
	public Result<ChartInfo> show(@PathVariable String id, @RequestBody TimeWidthParam param){
		Chart chart = this.chartService.getById(id);
		//处理查询时间区间
		long leftTime = 0L, rightTime = 0L;
		if(null == param.getLeft() || null == param.getRight()){
			LocalDateTime localDateTime = LocalDateTime.now();
			
			//是否使用自定义时间区间
			if(!StringUtils.isEmpty(chart.getCycleTime()) && "1".equals(chart.getCycleTime())){
				String startStr = chart.getStartTime();
				String endStr = chart.getEndTime();
				if(!pattern.matcher(startStr).matches()){
					startStr = "08:00";
				}
				if(!pattern.matcher(endStr).matches()){
					endStr = "20:00";
				}
				LocalDate localDate = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
				LocalTime localStartTime = LocalTime.parse(startStr, formatter);
				LocalDateTime leftLocal = LocalDateTime.of(localDate, localStartTime);
				
				LocalTime localEndTime = LocalTime.parse(endStr, formatter);
				LocalDateTime rightLocal = LocalDateTime.of(localDate, localEndTime);
				if(leftLocal.compareTo(rightLocal)>0){
					localStartTime = localStartTime.plusHours(-24);
				}
				leftTime = leftLocal.toInstant(ZoneOffset.of("+8")).toEpochMilli()*1000000L;
				rightTime = rightLocal.toInstant(ZoneOffset.of("+8")).toEpochMilli()*1000000L;
			}else{
				switch (chart.getStatisticCycleType()) {
				case CycletypeConstant.minutes:
					localDateTime = localDateTime.plus(-1*chart.getStatisticCycle(), ChronoUnit.MINUTES);
					break;
				case CycletypeConstant.hour:
					localDateTime=localDateTime.plus(-1*chart.getStatisticCycle(), ChronoUnit.HOURS);
					break;
				case CycletypeConstant.day:
					localDateTime=localDateTime.plus(-1*chart.getStatisticCycle(), ChronoUnit.DAYS);
					break;
				case CycletypeConstant.month:
					localDateTime=localDateTime.plus(-1*chart.getStatisticCycle(), ChronoUnit.MONTHS);
					break;
				case CycletypeConstant.year:
					localDateTime=localDateTime.plus(-1*chart.getStatisticCycle(), ChronoUnit.YEARS);
					break;
				default:
					localDateTime=localDateTime.plus(-1*chart.getStatisticCycle(), ChronoUnit.MINUTES);
					break;
				}
				leftTime = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli()*1000000L;
				rightTime = System.currentTimeMillis()*1000000L;
			}
		}else{
			leftTime = param.getLeft().getTime()*1000000L;
			rightTime = param.getRight().getTime()*1000000L;
		}
		
		List<ChartSerial> datas = this.chartItemService.querySerialDataByChartId(id);
		List<String> varNames = new ArrayList<>(datas.size());
		Map<String, ChartSerial> chartSerialMap = new HashMap<>(datas.size());
		//展示字段
		for(ChartSerial serial : datas){
			String varname = serial.getVariableName();
			if(!StringUtils.isEmpty(varname)){
				varNames.add(varname);
				chartSerialMap.put(varname, serial);
			}
		}
		//查询变量信息
		int size = varNames.size();
		if(size==0){
			return Result.fail("变量信息配置错误,请重新配置");
		}
		StringBuilder sqlBuilder = new StringBuilder();
		String measurement = createSQLByTime(leftTime, rightTime, sqlBuilder, varNames, size);
		Query query = new Query(sqlBuilder.toString());
		//flushDb查詢
		List<PointData> pointDatas = this.influxService.query(query, PointData.class, measurement);
		//生成  折线图
		ChartInfo chartInfo = new ChartInfo();
		chartInfo.setChartType(chart.getChartType());
		chartInfo.setTitle(chart.getStatisticName());
		
		//生成y轴所有数据集
		List<ChartsData> chartsData=new ArrayList<>(datas.size());
		
		//生成y轴所有数据最大最小值
		List<ChartsValue> chartsValueList=new ArrayList<>(datas.size());
		//根据图表管理中的变量数量,生成对应数量的序列
		for (ChartSerial serial : datas) {
			//设置每个序列的名称最大值，最小值
			ChartsValue value=new ChartsValue();
//			value.setName(serial.getEquipmentSection()+"["+serial.getEquipmentLocation()+"]");
			value.setName(serial.getEquipmentName());
			BigDecimal min=null;
			BigDecimal max=null;
			//设置序列的名称跟所有的采集时间错跟值的集合
			ChartsData data=new ChartsData();
//			data.setName(serial.getEquipmentSection()+"["+serial.getEquipmentLocation()"]");
			data.setName(serial.getEquipmentName());
			List<Object[]> timeStampData=new ArrayList<Object[]>();
			for (PointData varData : pointDatas) {
				//一个序列下,同一个变量,有采集值的情况下就放到集合中
				if(serial.getVariableName().equals(varData.getVarName())){
					if(null==varData.getVarValue()){
						continue;
					}
					long timestamp = varData.getTime().toEpochMilli();
					BigDecimal originValue = new BigDecimal(varData.getVarValue());
					originValue = originValue.setScale(3, RoundingMode.HALF_UP);
					varData.setVarValue(originValue.toString());
					Object[] timeData=new Object[]{timestamp, originValue};
					timeStampData.add(timeData);
					//判断最大值跟最小值
					if(min==null){
						min=new BigDecimal(varData.getVarValue());
					}
					if(max==null){
						max=new BigDecimal(varData.getVarValue());
					}
					if(new BigDecimal(varData.getVarValue()).compareTo(min)<0){
						min=new BigDecimal(varData.getVarValue());
					}
					if(new BigDecimal(varData.getVarValue()).compareTo(max)>0){
						max=new BigDecimal(varData.getVarValue());
					}
					
				}
			}
			if(null==max){
				max = BigDecimal.ZERO;
			}
			if(null==min){
				min = BigDecimal.ZERO;
			}
			value.setMax(max.setScale(3,RoundingMode.HALF_UP));
			value.setMin(min.setScale(3,RoundingMode.HALF_UP));
			chartsValueList.add(value);
			
			data.setData(timeStampData);
			chartsData.add(data);
		}
		chartInfo.setCharsValue(chartsValueList);
		chartInfo.setSeries(chartsData);
		return Result.success(chartInfo, "success");
	}

	private String createSQLByTime(long leftTime, long rightTime, StringBuilder sqlBuilder,List<String> varNames, int size){
		Instant start = Instant.ofEpochMilli(leftTime/1000000L);
		Instant end = Instant.ofEpochMilli(rightTime/1000000L);
		ZoneId zoneid = ZoneId.systemDefault();
		LocalDateTime left = LocalDateTime.ofInstant(start, zoneid), right = LocalDateTime.ofInstant(end, zoneid);
		String measurement = "iot_point_data";
		if(ChronoUnit.HOURS.between(left, right)<=1){
			//用纳秒为单位
			sqlBuilder.append("select var_name, var_value from iot_point_data where ");
			int index=0;
			sqlBuilder.append("(");
			for(;index<size;index++){
				String varName = varNames.get(index);
				if(index>0){
					sqlBuilder.append(" or ");
				}
				sqlBuilder.append("var_name=");
				sqlBuilder.append("'");
				sqlBuilder.append(varName);
				sqlBuilder.append("'");
			}
			sqlBuilder.append(")");
			sqlBuilder.append(" and time>="+leftTime+" and time<="+rightTime);
			sqlBuilder.append(" and quality_stamp="+QualityStampConstant.QUALITY_GOOD);
			sqlBuilder.append(" order by time asc");
			measurement = "iot_point_data";
		}else{ 
			if(ChronoUnit.MONTHS.between(left, right)>=12){
				//用月
				sqlBuilder.append("select mean(avg_value) as var_value from iot_point_data_day where ");
				int index=0;
				sqlBuilder.append("(");
				for(;index<size;index++){
					String varName = varNames.get(index);
					if(index>0){
						sqlBuilder.append(" or ");
					}
					sqlBuilder.append("var_name=");
					sqlBuilder.append("'");
					sqlBuilder.append(varName);
					sqlBuilder.append("'");
				}
				sqlBuilder.append(")");
				sqlBuilder.append(" and time>="+leftTime+" and time<="+rightTime);
				sqlBuilder.append(" group by time(30d), var_name");
				sqlBuilder.append(" order by time asc");
				measurement = "iot_point_data_day";
			} else if(ChronoUnit.DAYS.between(left, right)>30){
				//用日
				sqlBuilder.append("select var_name, avg_value as var_value from iot_point_data_hour where ");
				int index=0;
				sqlBuilder.append("(");
				for(;index<size;index++){
					String varName = varNames.get(index);
					if(index>0){
						sqlBuilder.append(" or ");
					}
					sqlBuilder.append("var_name=");
					sqlBuilder.append("'");
					sqlBuilder.append(varName);
					sqlBuilder.append("'");
				}
				sqlBuilder.append(")");
				sqlBuilder.append(" and time>="+leftTime+" and time<="+rightTime);
				sqlBuilder.append(" group by var_name");
				sqlBuilder.append(" order by time asc");
				measurement = "iot_point_data_hour";
			} else if(ChronoUnit.HOURS.between(left, right)>24){
				//用半小时
				sqlBuilder.append("select avg_value as var_value from iot_point_data_half_hour where ");
				int index=0;
				sqlBuilder.append("(");
				for(;index<size;index++){
					String varName = varNames.get(index);
					if(index>0){
						sqlBuilder.append(" or ");
					}
					sqlBuilder.append("var_name=");
					sqlBuilder.append("'");
					sqlBuilder.append(varName);
					sqlBuilder.append("'");
				}
				sqlBuilder.append(")");
				sqlBuilder.append(" and time>="+leftTime+" and time<="+rightTime);
				sqlBuilder.append(" group by var_name");
				sqlBuilder.append(" order by time asc");
				measurement = "iot_point_data_half_hour";
			} else if(ChronoUnit.HOURS.between(left, right)>2){
				//用15m
				sqlBuilder.append("select avg_value as var_value from iot_point_data_15m where ");
				int index=0;
				sqlBuilder.append("(");
				for(;index<size;index++){
					String varName = varNames.get(index);
					if(index>0){
						sqlBuilder.append(" or ");
					}
					sqlBuilder.append("var_name=");
					sqlBuilder.append("'");
					sqlBuilder.append(varName);
					sqlBuilder.append("'");
				}
				sqlBuilder.append(")");
				sqlBuilder.append(" and time>="+leftTime+" and time<="+rightTime);
				sqlBuilder.append(" group by var_name");
				sqlBuilder.append(" order by time asc");
				measurement = "iot_point_data_15m";
			}else {
				sqlBuilder.append("select mean(var_value) as var_value from iot_point_data where ");
				int index=0;
				sqlBuilder.append("(");
				for(;index<size;index++){
					String varName = varNames.get(index);
					if(index>0){
						sqlBuilder.append(" or ");
					}
					sqlBuilder.append("var_name=");
					sqlBuilder.append("'");
					sqlBuilder.append(varName);
					sqlBuilder.append("'");
				}
				sqlBuilder.append(")");
				sqlBuilder.append(" and time>="+leftTime+" and time<="+rightTime);
				sqlBuilder.append(" and quality_stamp="+QualityStampConstant.QUALITY_GOOD);
				sqlBuilder.append(" group by time(5m), var_name");
				sqlBuilder.append(" order by time asc");
				measurement = "iot_point_data";
			}
		}
		
		return measurement;
	}	
}
