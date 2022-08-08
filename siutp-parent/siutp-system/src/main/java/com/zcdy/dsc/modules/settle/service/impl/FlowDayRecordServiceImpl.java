package com.zcdy.dsc.modules.settle.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.settle.entity.FlowDayRecord;
import com.zcdy.dsc.modules.settle.entity.FlowDayRecordEntity;
import com.zcdy.dsc.modules.settle.mapper.FlowDayRecordMapper;
import com.zcdy.dsc.modules.settle.service.FlowDayRecordService;

/**
 * @Description: 每日用水量记录
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-30
 * @Version: V1.0
 */
@Service
public class FlowDayRecordServiceImpl extends ServiceImpl<FlowDayRecordMapper, FlowDayRecord> implements FlowDayRecordService {

	@Resource
	private FlowDayRecordMapper flowDayRecordMapper;
	
	@Override
	public List<FlowDayRecordEntity> monthList(String localDateLeft, String localDateRight) {
		return flowDayRecordMapper.monthList(localDateLeft,localDateRight);
	}

	@Override
	public List<FlowDayRecordEntity> dayList(String localDateLeft, String localDateRight) {
		return flowDayRecordMapper.dayList(localDateLeft,localDateRight);
	}

}
