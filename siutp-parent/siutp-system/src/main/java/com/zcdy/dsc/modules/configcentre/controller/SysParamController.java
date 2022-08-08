package com.zcdy.dsc.modules.configcentre.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.constant.WorkingStatus;
import com.zcdy.dsc.modules.configcentre.constant.SystemParamConstant;
import com.zcdy.dsc.modules.configcentre.entity.SystemConfig;
import com.zcdy.dsc.modules.configcentre.service.SystemParamService;
import com.zcdy.dsc.modules.configcentre.vo.SystemConfigVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 系统参数配置管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-10 10:16:14
 * @Version: V1.0
 */
@Api(tags="系统参数配置管理")
@RestController
@RequestMapping("/system/systemConfig")
public class SysParamController extends BaseController<SystemConfig, SystemParamService> {

	@Resource
	private SystemParamService systemParamService;
	
	@Resource
	private StringRedisTemplate stringRedisTemplate;
	
	/**
	 * 分页列表查询
	 *
	 * @param systemConfig
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "系统参数配置管理-分页列表查询")
	@ApiOperation(value="系统参数配置管理-分页列表查询", notes="系统参数配置管理-分页列表查询")
	@GetMapping
	public Result<IPage<SystemConfigVo>> queryPageList(String configName,String configStatus,
			@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		
		Result<IPage<SystemConfigVo>>  result=new Result<>();
		Page<SystemConfigVo> page=new Page<>(pageNo,pageSize);
		IPage<SystemConfigVo> data= systemParamService.queryPageData(page,configName,configStatus);
		result.setResult(data);
		result.success("success");
		return result;
	}
	
	/**
	 * 添加或修改
	 * @param systemConfig
	 * @return
	 */
	@AutoLog(value = "系统参数配置管理-添加或修改")
	@ApiOperation(value="系统参数配置管理-添加或修改", notes="系统参数配置管理-添加或修改")
	@PostMapping(value = "one")
	public Result<Object> add(@RequestBody SystemConfig systemConfig) {
		if(!StringUtils.isEmpty(systemConfig.getId())){
			//key值不允许修改,修改后清除redis
			stringRedisTemplate.delete(SystemParamConstant.PREFIX+systemConfig.getConfigKey());
			systemConfig.setConfigKey(null);
			systemParamService.updateById(systemConfig);
		}else{
			systemConfig.setConfigStatus(WorkingStatus.WORKING);
			systemParamService.save(systemConfig);
		}
		return Result.ok("保存成功！");
	}
	
	/**
	 * 通过key查询
	 * @return
	 */
	@AutoLog(value = "系统参数配置管理-通过key查询")
	@ApiOperation(value="系统参数配置管理-通过key查询", notes="系统参数配置管理-通过key查询")
	@GetMapping(value = "/{configKey}")
	public Result<SystemConfig> queryById(@PathVariable("configKey") String configKey) {
		Result<SystemConfig> result=new Result<>();
		SystemConfig one = systemParamService.getConfigByKey(configKey);
		result.setResult(one);
		result.success("查询成功");
		return result;
	}

	/**
	 * 描述: 更改启停用状态
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月10日 下午2:54:35
	 * 版本: V1.0
	 */
	@ApiOperation(value="更改启停用状态",notes="更改启停用状态")
	@GetMapping("/editStatus")
	public Result<Object> editStatus(String id){
		QueryWrapper<SystemConfig> queryWrapper=new QueryWrapper<>();
		queryWrapper.lambda().select(SystemConfig::getConfigStatus).eq(SystemConfig::getId, id);
		SystemConfig one = systemParamService.getOne(queryWrapper);
		UpdateWrapper<SystemConfig> updateWrapper=new UpdateWrapper<>();
		updateWrapper.lambda().eq(SystemConfig::getId, id);
		if(WorkingStatus.WORKING.equals(one.getConfigStatus())){
			one.setConfigStatus(WorkingStatus.STOP);
		}else{
			one.setConfigStatus(WorkingStatus.WORKING);
		}
		systemParamService.update(one,updateWrapper);
		return Result.ok("更改成功");
	}
	

    /**
     * 通过key查询
     * @return
     */
	@RequiresPermissions("sys:param:sync")
    @AutoLog(value = "系统参数配置管理-同步系统参数配置")
    @ApiOperation(value="系统参数配置管理-同步系统参数配置", notes="同步系统参数配置")
    @GetMapping(value = "/sync")
    public Result<String> sync() {
        return Result.success("success","同步成功");
    }
}