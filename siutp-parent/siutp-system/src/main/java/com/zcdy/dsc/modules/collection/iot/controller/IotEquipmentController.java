package com.zcdy.dsc.modules.collection.iot.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipment;
import com.zcdy.dsc.modules.collection.iot.service.IotEquipmentService;
import com.zcdy.dsc.modules.collection.iot.service.IotEquipmentVariableService;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipmentVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述: 采集设备维护
 * @author： 智能无人硬核心项目组
 * 创建时间： 2020-02-26 10:52:14
 * 版本号: V1.0
 */
@Slf4j
@Api(tags = "采集设备维护")
@RestController
@RequestMapping("/equipment/iotEquipment")
public class IotEquipmentController extends BaseController<IotEquipment, IotEquipmentService> {
	@Autowired
	private IotEquipmentService iotEquipmentService;
	@Autowired
	private IotEquipmentVariableService iotEquipmentVariableService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	/**
	 * 分页列表查询
	 *
	 * @param iotEquipment
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采集设备维护-分页列表查询")
	@ApiOperation(value = "采集设备维护-分页列表查询", notes = "采集设备维护-分页列表查询")
	@GetMapping
	public Result<IPage<IotEquipmentVo>> queryPageList(IotEquipment iotEquipment,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		Page<IotEquipmentVo> page = new Page<IotEquipmentVo>(pageNo, pageSize);
		IPage<IotEquipmentVo> pageList = iotEquipmentService.queryPageList(page, iotEquipment);
		Result<IPage<IotEquipmentVo>> result = new Result<>();
		result.setCode(CommonConstant.SC_OK_200);
		result.setResult((IPage<IotEquipmentVo>) pageList);
		return result;
	}

	/**
	 * 添加或修改
	 * 
	 * @param iotEquipment
	 * @return
	 */
	@AutoLog(value = "采集设备维护-添加或修改")
	@ApiOperation(value = "采集设备维护-添加或修改", notes = "采集设备维护-添加或修改")
	@PostMapping(value = "one")
	public Result<?> add(@RequestBody IotEquipment iotEquipment) {
		String id = iotEquipment.getId();
		String iotSn = iotEquipment.getIotSn();
		String iotName = iotEquipment.getIotName();
		List<IotEquipment> list = iotEquipmentService.queryIotEquipmentByName(iotSn, iotName, id);
		if (list != null && list.size() > 0) {
			return Result.error("设备编号或设备名称重复！");
		} else {
			if (!StringUtils.isEmpty(iotEquipment.getId())) {
				iotEquipmentService.updateById(iotEquipment);
			} else {
				iotEquipmentService.save(iotEquipment);
			}
			return Result.ok("保存成功！");
		}
	}

	/**
	 * 按设备类型修改设备周期
	 * 
	 * @param
	 * @return
	 */
	@AutoLog(value = "按设备类型修改设备周期")
	@ApiOperation(value = "按设备类型修改设备周期", notes = "按设备类型修改设备周期")
	@GetMapping(value = "/updateCycleByCate")
	@ApiImplicitParams({ @ApiImplicitParam(name = "iotCategory", value = "设备类型", paramType = "query"),
			@ApiImplicitParam(name = "iotCycle", value = "周期", paramType = "query") })
	public Result<?> updateCycleByCate(String iotCategory, String iotCycle) {
		iotEquipmentService.updateCycleByCate(iotCategory, iotCycle);
		return Result.ok("修改成功！");

	}

	/**
	 * 通过id删除
	 * 
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采集设备维护-通过id删除")
	@ApiOperation(value = "采集设备维护-通过id删除", notes = "采集设备维护-通过id删除")
	@DeleteMapping(value = "{id}")
	public Result<?> delete(@PathVariable("id") String id) {
		iotEquipmentService.removeById(id);
		iotEquipmentVariableService.removeByIotId(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采集设备维护-批量删除")
	@ApiOperation(value = "采集设备维护-批量删除", notes = "采集设备维护-批量删除")
	@DeleteMapping(value = "/many")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.iotEquipmentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采集设备维护-通过id查询")
	@ApiOperation(value = "采集设备维护-通过id查询", notes = "采集设备维护-通过id查询")
	@GetMapping(value = "/{id}")
	public Result<?> queryById(@PathVariable("id") String id) {
		IotEquipment iotEquipment = iotEquipmentService.getById(id);
		return Result.ok(iotEquipment);
	}

	/**
	 * 描述: 同步功能
	 * @return
	 * @author： songguang.jiao
	 * 创建时间： 2020年3月16日 下午8:40:34
	 * 版本号: V1.0
	 */
	@ApiOperation(value = "同步功能", notes = "同步功能")
	@GetMapping("/sync")
	public Result<Object> sync() {
		try {
			iotEquipmentVariableService.saveRedisIotEquipmentVariableVoByCycle();
		} catch (Exception e) {
			log.error(e.getMessage());
			return Result.error(e.getMessage());
		}
		return Result.ok("同步成功");
	}

	/**
	 * 描述: 清除采集设备相关Redis缓存
	 * @author：  songguang.jiao
	 * 创建时间：  2020年3月21日 下午6:22:24
	 * 版本号: V1.0
	 */
	@ApiOperation(value = "清除Redis缓存", notes = "清除Redis缓存")
	@PostMapping("/clearRedis")
	public Result<Object> clearRedis(){
		Set<String> keys = this.stringRedisTemplate.keys("com:zcdy:dsc:iot:rule:*");
		stringRedisTemplate.delete(keys);
		//清除 7标段 2标段压力，流量，河东水厂瞬时流量 缓存
		stringRedisTemplate.delete("com.zcdy.dsc.modules.collection.iot.service.impl::getVariinfos");
		return Result.ok("清除成功");
	}
}