package com.zcdy.dsc.modules.operation.alarm.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.alarm.entity.IotEquipmentRuleWarn;
import com.zcdy.dsc.modules.operation.alarm.service.IotEquipmentRuleWarnService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * 描述: 告警信息配置
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-02 16:14:02
 * 版本号: V1.0
 */

@Api(tags="告警信息配置")
@RestController
@RequestMapping("/alarm/iotEquipmentRuleWarn")
public class IotEquipmentRuleWarnController extends BaseController<IotEquipmentRuleWarn, IotEquipmentRuleWarnService> {
	@Autowired
	private IotEquipmentRuleWarnService iotEquipmentRuleWarnService;
	
	/**
	 * 分页列表查询
	 *
	 * @return
	 */
	@AutoLog(value = "告警信息配置-分页列表查询")
	@ApiOperation(value="告警信息配置-分页列表查询", notes="告警信息配置-分页列表查询")
	@GetMapping
	@RequiresPermissions("business:config")
	public Result<?> queryPageList() {
		Page<IotEquipmentRuleWarn> page = new Page<IotEquipmentRuleWarn>(1, 1);
		IPage<IotEquipmentRuleWarn> pageList = iotEquipmentRuleWarnService.queryPageList(page);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加或修改
	 * @param iotEquipmentRuleWarn
	 * @return
	 */
	@AutoLog(value = "告警信息配置-添加或修改")
	@ApiOperation(value="告警信息配置-添加或修改", notes="告警信息配置-添加或修改")
	@PostMapping(value = "one")
	@RequiresPermissions("business:config")
	public Result<?> add(@RequestBody IotEquipmentRuleWarn iotEquipmentRuleWarn) {
		String codes=iotEquipmentRuleWarn.getWarnLevelCode();
		String type=iotEquipmentRuleWarn.getType();
		iotEquipmentRuleWarnService.deleteIotEquipmentRuleWarn();
		//添加告警等级数据
		if(codes!=null&&!"".equals(codes)){
			String[] warnLevelCodes =codes.split(",");
			for(String warnLevelCode:warnLevelCodes){
				IotEquipmentRuleWarn iotRuleWarn=new IotEquipmentRuleWarn();
				iotRuleWarn.setWarnLevelCode(warnLevelCode);
				iotRuleWarn.setType("0");
				iotEquipmentRuleWarnService.save(iotRuleWarn);
			}
		}
		//添加设备类别数据，由于第一次做的时候选择告警信息添加多条，只有一个字段，导致现在只能接着添加多条，用跟后面的type进行判断是告警级别还是设备类别
		if(type!=null&&!"".equals(type)){
			String[] warnLevelCodes =type.split(",");
			for(String warnLevelCode:warnLevelCodes){
				IotEquipmentRuleWarn iotRuleWarn=new IotEquipmentRuleWarn();
				iotRuleWarn.setWarnLevelCode(warnLevelCode);
				iotRuleWarn.setType("1");
				iotEquipmentRuleWarnService.save(iotRuleWarn);
			}
		}

		return Result.ok("保存成功！");
	}
	


}