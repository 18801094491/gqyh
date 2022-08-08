package com.zcdy.dsc.modules.datacenter.statistic.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.constant.TimeTypeConstant;
import com.zcdy.dsc.modules.datacenter.statistic.entity.Chart;
import com.zcdy.dsc.modules.datacenter.statistic.entity.MeterFlowCount;
import com.zcdy.dsc.modules.datacenter.statistic.mapper.ChartItemMapper;
import com.zcdy.dsc.modules.datacenter.statistic.mapper.ChartMapper;
import com.zcdy.dsc.modules.datacenter.statistic.param.ChartParam;
import com.zcdy.dsc.modules.datacenter.statistic.service.ChartService;
import com.zcdy.dsc.modules.datacenter.statistic.vo.*;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptEquipmentMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述: 统计事项
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-13
 * 版本号: V1.0
 */
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart> implements ChartService {

	@Resource
	private ChartMapper chartMapper;
	
	@Resource
	private ChartItemMapper chartItemMapper;

	@Resource
	private OptEquipmentMapper optEquipmentMapper;
	
	/*
	 * @see com.zcdy.dsc.modules.datacenter.statistic.service.ChartService#pageQuery(com.baomidou.mybatisplus.extension.plugins.pagination.Page, com.zcdy.dsc.modules.datacenter.statistic.param.ChartParam)
	 */
	@Override
	public IPage<ChartVo> pageQuery(Page<ChartVo> page, ChartParam param) {
		return this.chartMapper.selectChartList(page, param);
	}

	/*
	 * @see com.zcdy.dsc.modules.datacenter.statistic.service.ChartService#queryEquipment()
	 */
	@Override
	public List<EquipmentInfoVO> queryEquipment(String name) {
		return this.optEquipmentMapper.selectSimpleData(name);
	}

	/*
	 * @see com.zcdy.dsc.modules.datacenter.statistic.service.ChartService#queryVarByOptId(java.lang.String)
	 */
	@Override
	public List<VarNameTitleVO> queryVarByOptId(String id) {
		return this.chartMapper.selectVarInfoByOptId(id);
	}

	@Override
	public MeterFlowBarChart  queryData(MeterFlowHistoryVo meterFlowVo) {
		//如果查询方式为空的话,默认按照每天来查询
				if(StringUtils.isEmpty(meterFlowVo.getQueryType())){
					if(TimeTypeConstant.YEAR.endsWith(meterFlowVo.getPeriodTime())){
						return formatData(chartMapper.queryByYear(meterFlowVo.getStartTime(),meterFlowVo.getEndTime()));
					}else if(TimeTypeConstant.MONTH.endsWith(meterFlowVo.getPeriodTime())){
						return formatData(chartMapper.queryByMonth(meterFlowVo.getStartTime(),meterFlowVo.getEndTime()));
					}else{
						return formatData(chartMapper.queryByDay(meterFlowVo.getStartTime(),meterFlowVo.getEndTime()));
					}
				}else{
					//查询方式不为空就计算出对应起止时间,查询数据(默认按照近7天查询展示数据)
					DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate today = LocalDate.now();
					String startTime = today.minusDays(7).format(dateformat);
					String endTime = today.format(dateformat);
					if(TimeTypeConstant.YEAR.endsWith(meterFlowVo.getQueryType())){
						//计算出当年开始日期,按月查询
						startTime=today.with(TemporalAdjusters.firstDayOfYear()).format(dateformat);
						return formatData(chartMapper.queryByMonth(startTime, endTime));
					}else if(TimeTypeConstant.MONTH.endsWith(meterFlowVo.getQueryType())){
						//计算当月开始日期,按日查询
						startTime=today.with(TemporalAdjusters.firstDayOfMonth()).format(dateformat);
						return formatData(chartMapper.queryByDay(startTime, endTime));
					}else{
						return formatData(chartMapper.queryByDay(startTime, endTime));
					}
				}
			}
	
	//处理格式为前端柱形图格式数据 TODO
	private MeterFlowBarChart formatData(List<MeterFlowCount> data){
		MeterFlowBarChart barChart=new MeterFlowBarChart();
		List<String> timeList=new ArrayList<String>();
		List<String> nameList=new ArrayList<String>();
		//y轴数据集合
		List<Map<String, Object>> series=new ArrayList<>();
		if (data!=null) {
			//先生成x轴时间轴,x轴3类柱状图
			Map<String, String> timeMap=new HashMap<>(10);
			Map<String, String> nameMap=new HashMap<>(10);
			for (MeterFlowCount meter : data) {
				timeMap.put(meter.getCountDate(), meter.getCountDate());
				nameMap.put(meter.getEquipType(), meter.getEquipType());
			}
			timeList=timeMap.values().stream().collect(Collectors.toList());
			nameList = nameMap.values().stream().collect(Collectors.toList());
			
			//y轴数据
			for (String string : timeList) {
				//生成总供水量,实际用水量,产销差数据
				Map<String, Object>  provide=new HashMap<>(timeList.size());
				
				Map<String, Map<String,Object>>  all=new HashMap<>(timeList.size());
				for(String name: nameList){
					Map<String,Object>  map= all.get(name);
					//判断有没有数据集
					if(map==null){
						Map<String,Object>  mapChild=new HashMap<>(timeList.size());
						mapChild.put("name", name);
						mapChild.put(name, new ArrayList<>());
					}
					
					//查询child的数据
					List<BigDecimal> list = (List<BigDecimal>) map.get(name);
					//匹配时间跟类型都相等时放入对应数据,没有数据放0
					for(MeterFlowCount meter : data){
						if(string.endsWith(meter.getCountDate()) && name.endsWith(meter.getEquipType())){
							list.add(meter.getCountValue());
						}else{
							list.add(BigDecimal.ZERO);
						}
					}
					provide.put("data", list);
				}
				series.add(provide);
			}
			
		}
		
		barChart.setSeries(series);
		barChart.setTime(timeList);
		return barChart;
	}
	
}
