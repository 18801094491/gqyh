package com.zcdy.dsc.modules.message.service.impl;

import com.zcdy.dsc.modules.message.entity.SmsEvent;
import com.zcdy.dsc.modules.message.mapper.SmsEventMapper;
import com.zcdy.dsc.modules.message.param.SmsEventPageParam;
import com.zcdy.dsc.modules.message.service.SmsEventService;
import com.zcdy.dsc.modules.message.vo.SmsEventVo;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 事件管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-14
 * @Version: V1.0
 */
@Service
public class SmsEventServiceImpl extends ServiceImpl<SmsEventMapper, SmsEvent> implements SmsEventService {

	@Resource
	private SmsEventMapper eventMapper;
	
	@Override
	public IPage<SmsEventVo> queryPageData(Page<SmsEventVo> page, SmsEventPageParam param) {
		return eventMapper.selectPageData(page, param);
	}

	@Override
	public List<SmsEventVo> selectExprotData(SmsEventPageParam param) {
		return eventMapper.selectExprotData(param);
	}

}
