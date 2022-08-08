package com.zcdy.dsc.modules.configcentre.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
 * 描述: 系统界面配置信息
 * @author： 智能无人硬核心项目组
 * 创建时间： 2020-04-02 11:38:16
 * 版本号: V1.0
 */
@Api(tags = "系统界面配置信息接口组", value = "系统界面配置信息接口组")
@RestController
@RequestMapping("/sys/web/config")
public class SystemWebConfigController extends BaseController<SystemWebConfig, SystemWebConfigService> {

	@Value("${com.zcdy.dsc.web.config}")
	private String systemWebConfigId;

	@Resource
	private SystemWebConfigService systemWebConfigService;

	/**
	 * 添加或修改
	 * 
	 * @param systemWebConfig
	 * @return
	 */
	@AutoLog(value = "添加或修改")
	@ApiOperation(value = "添加或修改", notes = "添加或修改")
	@PostMapping(value = "one")
	public Result<?> add(@RequestBody SystemWebConfig systemWebConfig) {
		if (!StringUtils.isEmpty(systemWebConfig.getId())) {
			systemWebConfigService.updateById(systemWebConfig);
		} else {
			systemWebConfigService.save(systemWebConfig);
		}
		return Result.ok("保存成功！");
	}

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@AutoLog(value = "获取系统的界面配置信息")
	@ApiOperation(value = "获取系统的界面配置信息", notes = "获取系统的界面配置信息")
	@GetMapping(value = "/getCurrentConfig")
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