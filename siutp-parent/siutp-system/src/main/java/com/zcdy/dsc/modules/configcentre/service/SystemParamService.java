package com.zcdy.dsc.modules.configcentre.service;

import com.zcdy.dsc.modules.configcentre.entity.SystemConfig;
import com.zcdy.dsc.modules.configcentre.vo.SystemConfigVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 系统参数配置管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-10
 * @Version: V1.0
 */
public interface SystemParamService extends IService<SystemConfig> {

	/**
	 * 根据配置key获取配置信息
	 * @param configKey 配置key
	 * @return
	 */
	SystemConfig getConfigByKey(String configKey);

	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param configName 配置名称
	 * @param configStatus 配置状态
	 * @return
	 */
	IPage<SystemConfigVo> queryPageData(Page<SystemConfigVo> page,String configName, String configStatus);

}
