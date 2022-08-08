package com.zcdy.dsc.modules.system.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.configcentre.entity.SystemWebConfig;
import com.zcdy.dsc.modules.configcentre.service.SystemWebConfigService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * 描述: 系统界面基础信息
 * @author：  songguang.jiao
 * 创建时间：  2020年4月8日 上午11:40:28
 * 版本号: V1.0
 */
@Api(tags = "系统界面基础信息", value = "系统界面基础信息")
@RestController
@RequestMapping("/anon/baseConfig")
public class SysConfigController extends BaseController<SystemWebConfig, SystemWebConfigService>{

	@Value("${com.zcdy.dsc.web.config}")
	private String systemWebConfigId;

	@Resource
	private SystemWebConfigService systemWebConfigService;
	
	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@AutoLog(value = "获取系统的界面配置信息")
	@ApiOperation(value = "获取系统的界面配置信息", notes = "获取系统的界面配置信息")
	@GetMapping(value = "/")
	public Result<Object> getCurrentConfig() {
		SystemWebConfig	systemWebConfig = systemWebConfigService.getById(systemWebConfigId);
		if (!StringUtils.isEmpty(systemWebConfig.getSimpleLogo())) {
			systemWebConfig.setSimpleLogo(baseStoragePath + systemWebConfig.getSimpleLogo());
		}
		if (!StringUtils.isEmpty(systemWebConfig.getLoginBg())) {
			systemWebConfig.setLoginBg(baseStoragePath + systemWebConfig.getLoginBg());
		}
		if (!StringUtils.isEmpty(systemWebConfig.getVerticalLogo())) {
			systemWebConfig.setVerticalLogo(baseStoragePath + systemWebConfig.getVerticalLogo());
		}
		if (!StringUtils.isEmpty(systemWebConfig.getHorizontalLogo())) {
			systemWebConfig.setHorizontalLogo(baseStoragePath + systemWebConfig.getHorizontalLogo());
		}
		return Result.success(systemWebConfig, "success");
	}
}
