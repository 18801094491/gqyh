package com.zcdy.dsc.modules.configcentre.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.configcentre.entity.SystemConfig;
import com.zcdy.dsc.modules.configcentre.mapper.SystemConfigMapper;
import com.zcdy.dsc.modules.configcentre.service.SystemParamService;
import com.zcdy.dsc.modules.configcentre.vo.SystemConfigVo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description: 系统参数配置管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-10
 * @Version: V1.0
 */
@Service
public class SystemParamServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemParamService {
	
	@Resource
	private SystemConfigMapper systemConfigMapper;
	
	/*
	 * @see com.zcdy.dsc.modules.system.service.SystemConfigService#getConfigByKey(java.lang.String)
	 */
	@Cacheable(cacheNames="system:config", key="#configKey", unless="#result==null")
	@Override
	public SystemConfig getConfigByKey(String configKey) {
		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(SystemConfig::getConfigKey, configKey)
		.eq(SystemConfig::getConfigStatus, StatusConstant.RUN);
		return this.getOne(queryWrapper, true);
	}

	@Override
	public IPage<SystemConfigVo> queryPageData(Page<SystemConfigVo> page,String configName, String configStatus) {
		return systemConfigMapper.queryPageData(page,configName,configStatus);
	}

}
