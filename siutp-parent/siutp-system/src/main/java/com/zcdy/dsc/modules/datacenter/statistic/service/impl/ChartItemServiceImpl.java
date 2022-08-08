package com.zcdy.dsc.modules.datacenter.statistic.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.datacenter.statistic.entity.ChartItem;
import com.zcdy.dsc.modules.datacenter.statistic.entity.ChartSerial;
import com.zcdy.dsc.modules.datacenter.statistic.mapper.ChartItemMapper;
import com.zcdy.dsc.modules.datacenter.statistic.service.ChartItemService;

/**
 * 描述: 统计要素
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-13
 * 版本号: V1.0
 */
@Service
public class ChartItemServiceImpl extends ServiceImpl<ChartItemMapper, ChartItem> implements ChartItemService {

	@Resource
	private ChartItemMapper chartItemMapper;
	
	/*
	 * @see com.zcdy.dsc.modules.datacenter.statistic.service.ChartItemService#querySerialData()
	 */
	@Override
	public List<ChartSerial> querySerialData() {
		return chartItemMapper.selectSerialData();
	}

	/*
	 * @see com.zcdy.dsc.modules.datacenter.statistic.service.ChartItemService#querySerialDataByChartId(java.lang.String)
	 */
	@Cacheable(cacheNames="com:zcdy:dsc:modules:statistic:service:impl:ChartItemServiceImpl", key = "#id", unless="#result==null")
	@Override
	public List<ChartSerial> querySerialDataByChartId(String id) {
		return this.chartItemMapper.selectSerialDataByChartId(id);
	}
}
