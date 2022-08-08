package com.zcdy.dsc.modules.collection.iot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.collection.iot.entity.EquipmentConfig;
import com.zcdy.dsc.modules.collection.iot.entity.EquipmentTypeConfig;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipment;
import com.zcdy.dsc.modules.collection.iot.service.EquipmentConfigService;
import com.zcdy.dsc.modules.collection.iot.service.IotEquipmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 采集设备配置表
 * @author： Roberto
 * 创建时间： 2020-03-07 18:02:33
 * 版本号: V1.0
 */
@Api(tags = "采集设备配置")
@RestController
@RequestMapping("/iot/equipment/config")
public class EquipmentConfigController extends BaseController<EquipmentConfig, EquipmentConfigService> {

	@Resource
	private EquipmentConfigService equipmentConfigService;

	@Resource
	private IotEquipmentService iotEquipmentService;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 单项配置
	 * 
	 * @param equipmentConfig
	 * @return
	 */
	@AutoLog(value = "采集设备配置表-单项配置")
	@ApiOperation(value = "采集设备配置表-单项配置", notes = "采集设备配置表-单项配置")
	@PostMapping(value = "one")
	public Result<Object> config(@RequestBody EquipmentConfig equipmentConfig) {
		QueryWrapper<EquipmentConfig> queryWarpper = new QueryWrapper<>();
		queryWarpper.lambda().eq(EquipmentConfig::getEquipmentId, equipmentConfig.getEquipmentId());
		EquipmentConfig one = equipmentConfigService.getOne(queryWarpper);
		if (null != one) {
			BeanUtils.copyProperties(equipmentConfig, one, "id", "equipmentType");
			equipmentConfigService.updateById(one);
		} else {
			IotEquipment equipment = this.iotEquipmentService.getById(equipmentConfig.getEquipmentId());
			equipmentConfig.setEquipmentType(equipment.getIotCategory());
			equipmentConfigService.save(equipmentConfig);
		}
		//清除采集质量戳缓存
		stringRedisTemplate.delete("com:zcdy:dsc:iot:rule:config::getIotQualitys");
		return Result.ok("保存成功！");
	}

	/**
	 * 根据类型配置
	 * 
	 * @param equipmentConfig
	 * @return
	 */
	@AutoLog(value = "采集设备配置表-根据类型配置")
	@ApiOperation(value = "采集设备配置表-根据类型配置", notes = "采集设备配置表-根据类型配置")
	@PostMapping(value = "cat")
	public Result<Object> configByType(@RequestBody EquipmentTypeConfig equipmentConfig) {
		QueryWrapper<EquipmentConfig> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(EquipmentConfig::getEquipmentType, equipmentConfig.getTypeCode());
		this.equipmentConfigService.remove(queryWrapper);

		List<String> ids = this.iotEquipmentService.listIdsByType(equipmentConfig.getTypeCode());
		List<EquipmentConfig> configs = new ArrayList<EquipmentConfig>(20);
		for (String id : ids) {
			EquipmentConfig one = new EquipmentConfig();
			BeanUtils.copyProperties(equipmentConfig, one);
			one.setEquipmentId(id);
			one.setEquipmentType(equipmentConfig.getTypeCode());
			configs.add(one);

			if (configs.size() >= 20) {
				this.equipmentConfigService.saveBatch(configs);
				configs.clear();
			}
		}
		if (configs.size() > 0) {
			this.equipmentConfigService.saveBatch(configs);
		}
		return Result.ok("保存成功！");
	}

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采集设备配置表-通过设备id查询")
	@ApiOperation(value = "采集设备配置表-通过设备id查询", notes = "采集设备配置表-通过设备id查询")
	@GetMapping(value = "/{id}")
	public Result<EquipmentConfig> queryById(@PathVariable("id") String equipmentId) {
		QueryWrapper<EquipmentConfig> queryWarpper = new QueryWrapper<>();
		queryWarpper.lambda().eq(EquipmentConfig::getEquipmentId, equipmentId);
		EquipmentConfig equipmentConfig = equipmentConfigService.getOne(queryWarpper);
		Result<EquipmentConfig> result = new Result<>();
		result.success("成功");
		result.setResult(equipmentConfig);
		return result;
	}
}