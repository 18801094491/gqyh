package com.zcdy.dsc.modules.collection.iot.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.util.HttpUtil;
import com.zcdy.dsc.modules.collection.iot.entity.IotProxy;
import com.zcdy.dsc.modules.collection.iot.entity.IotProxyEquipment;
import com.zcdy.dsc.modules.collection.iot.service.IotProxyEquipmentService;
import com.zcdy.dsc.modules.collection.iot.service.IotProxyService;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipVo;
import com.zcdy.dsc.modules.collection.iot.vo.IotProxyParamVo;
import com.zcdy.dsc.modules.collection.iot.vo.IotProxyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 描述: 采集代理管理
 * @author： 智能无人硬核心项目组
 * 创建时间： 2020-03-13 12:24:01
 * 版本号: V1.0
 */
@Api(tags = "采集代理管理")
@RestController
@RequestMapping("/iot/iotProxy")
public class IotProxyController extends BaseController<IotProxy, IotProxyService> {
	@Autowired
	private IotProxyService iotProxyService;

	@Autowired
	private IotProxyEquipmentService iotProxyEquipmentService;


	/**
	 * 查询采集代理列表
	 * 
	 * @return
	 */
	@AutoLog(value = "查询采集代理列表")
	@ApiOperation(value = "查询采集代理列表", notes = "查询采集代理列表")
	@GetMapping
	public Result<List<IotProxyVo>> queryPageList() {
		Result<List<IotProxyVo>> result = new Result<List<IotProxyVo>>();
		List<IotProxyVo> pageList = iotProxyService.queryData();
		result.setResult(pageList);
		result.success("success");
		return result;
	}

	/**
	 * 添加或修改
	 * 
	 * @param iotProxy
	 * @return
	 */
	@AutoLog(value = "采集代理管理-添加或修改")
	@ApiOperation(value = "采集代理管理-添加或修改", notes = "采集代理管理-添加或修改")
	@PostMapping(value = "one")
	public Result<?> add(@RequestBody @Valid IotProxyParamVo iotProxyParamVo) {
		IotProxy iotProxy = new IotProxy();
		BeanUtils.copyProperties(iotProxyParamVo, iotProxy);
		if (!StringUtils.isEmpty(iotProxy.getId())) {
			iotProxyService.updateById(iotProxy);
		} else {
			iotProxyService.save(iotProxy);
		}
		return Result.ok("保存成功！");
	}

	/**
	 * 描述: 更改代理状态
	 * @return
	 * @author： songguang.jiao
	 * 创建时间： 2020年3月13日 下午1:40:31
	 * 版本号: V1.0
	 */
	@ApiOperation(value = "更改代理状态", notes = "更改代理状态")
	@GetMapping("/editStatus")
	public Result<Object> editProxyStatus(String id) {
		iotProxyService.editProxyStatus(id);
		return Result.ok();
	}

	/**
	 * 描述: 连接测试心跳地址
	 * @param ipAddress
	 *            心跳地址
	 * @return
	 * @author： songguang.jiao
	 * 创建时间： 2020年3月13日 下午1:51:58
	 * 版本号: V1.0
	 */
	@ApiOperation(value = "连接测试心跳地址", notes = "连接测试心跳地址")
	@GetMapping("/linkTest")
	public Result<Object> linkTest(String ipAddress) {
		JSONObject json = new JSONObject();
		try {
			String string = HttpUtil.doGet(ipAddress);
			json = JSONObject.parseObject(string);
		} catch (Exception e) {
			e.getMessage();
			return Result.error(e.getMessage());
		}
		return Result.ok(json);
	}

	/**
	 * 描述: 查询所有采集设备列表
	 * @return
	 * @author： songguang.jiao
	 * 创建时间： 2020年3月13日 下午2:28:28
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNo", value = "页码", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "每页显示条数", paramType = "query"),
			@ApiImplicitParam(name = "iotSn", value = "设备编号", paramType = "query"),
			@ApiImplicitParam(name = "iotName", value = "设备名称", paramType = "query"),
			@ApiImplicitParam(name = "iotCategory", value = "设备类型", paramType = "query") })
	@ApiOperation(value = "分页查询所有采集设备列表", notes = "分页查询所有采集设备列表")
	@GetMapping("/allEquip")
	public Result<IPage<IotEquipVo>> queryAllEquip(String iotSn, String iotName, String iotCategory,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		Result<IPage<IotEquipVo>> result = new Result<IPage<IotEquipVo>>();
		Page<IotEquipVo> page = new Page<>(pageNo, pageSize);
		IPage<IotEquipVo> data = iotProxyService.queryAllEquip(page, iotSn, iotName, iotCategory);
		result.setResult(data);
		result.success("success");
		return result;
	}

	/**
	 * 描述:单个代理已绑定的采集设备列表
	 * @return
	 * @author： songguang.jiao
	 * 创建时间： 2020年3月13日 下午2:28:28
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNo", value = "页码", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "每页显示条数", paramType = "query"),
			@ApiImplicitParam(name = "proxyId", value = "设备Id", paramType = "query"),
			@ApiImplicitParam(name = "iotSn", value = "设备编号", paramType = "query"),
			@ApiImplicitParam(name = "iotName", value = "设备名称", paramType = "query"),
			@ApiImplicitParam(name = "iotCategory", value = "设备类型", paramType = "query") })
	@ApiOperation(value = "单个代理已绑定的采集设备列表", notes = "单个代理已绑定的采集设备列表")
	@GetMapping("/getBindEquip")
	public Result<IPage<IotEquipVo>> getBindEquip(String proxyId, String iotSn, String iotName, String iotCategory,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		Result<IPage<IotEquipVo>> result = new Result<IPage<IotEquipVo>>();
		if (StringUtils.isEmpty(proxyId)) {
			return result.error500("采集代理id不能为空");
		}
		Page<IotEquipVo> page = new Page<>(pageNo, pageSize);
		IPage<IotEquipVo> data = iotProxyService.getBindEquip(page, proxyId, iotSn, iotName, iotCategory);
		result.setResult(data);
		result.success("success");
		return result;
	}

	/**
	 * 描述:单个绑定采集设备
	 * @return
	 * @author： songguang.jiao
	 * 创建时间： 2020年3月13日 下午4:27:19
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({ @ApiImplicitParam(name = "equipId", value = "采集设备id", paramType = "query"),
			@ApiImplicitParam(name = "proxyId", value = "采集代理id", paramType = "query") })
	@ApiOperation(value = "单个绑定采集设备", notes = "单个绑定采集设备")
	@GetMapping("/bindOne")
	public Result<Object> bindOne(String equipId, String proxyId) {
		if (StringUtils.isEmpty(proxyId) || StringUtils.isEmpty(equipId)) {
			return Result.error("设备id跟采集代理id都不能为空");
		}
		IotProxyEquipment iotProxyEquipment = new IotProxyEquipment();
		iotProxyEquipment.setEquipId(equipId);
		iotProxyEquipment.setProxyId(proxyId);
		iotProxyEquipmentService.save(iotProxyEquipment);
		return Result.ok("绑定成功");
	}

	/**
	 * 描述:按照类型绑定采集设备
	 * @return
	 * @author： songguang.jiao
	 * 创建时间： 2020年3月13日 下午4:27:19
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({ @ApiImplicitParam(name = "category", value = "采集设备类型", paramType = "query"),
			@ApiImplicitParam(name = "proxyId", value = "采集代理id", paramType = "query") })
	@ApiOperation(value = "按照类型绑定采集设备", notes = "按照类型绑定采集设备")
	@GetMapping("/bindCate")
	public Result<Object> bindCate(String category, String proxyId) {
		if (StringUtils.isEmpty(proxyId) || StringUtils.isEmpty(category)) {
			return Result.error("设备类型跟采集代理id都不能为空");
		}
		iotProxyService.bindCate(category,proxyId);
		return Result.ok("绑定成功");
	}

	/**
	 * 描述:绑定全部采集设备
	 * @return
	 * @author： songguang.jiao
	 * 创建时间： 2020年3月13日 下午4:27:19
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({ @ApiImplicitParam(name = "proxyId", value = "采集代理id", paramType = "query") })
	@ApiOperation(value = "绑定全部采集设备", notes = "绑定全部采集设备")
	@GetMapping("/bindAll")
	public Result<Object> bindAll(String proxyId) {
		if (StringUtils.isEmpty(proxyId)) {
			return Result.error("设备类型跟采集代理id不能为空");
		}
		iotProxyService.bindAll(proxyId);
		return Result.ok("绑定成功");
	}

	/**
	 * 描述:解绑单个采集设备
	 * @return
	 * @author： songguang.jiao
	 * 创建时间： 2020年3月13日 下午4:27:19
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({ @ApiImplicitParam(name = "equipId", value = "采集设备id", paramType = "query"),
			@ApiImplicitParam(name = "proxyId", value = "采集代理id", paramType = "query") })
	@ApiOperation(value = "解绑单个采集设备", notes = "解绑单个采集设备")
	@GetMapping("/unbindOne")
	public Result<Object> unbindOne(String equipId, String proxyId) {
		if (StringUtils.isEmpty(proxyId) || StringUtils.isEmpty(equipId)) {
			return Result.error("设备id跟采集代理id都不能为空");
		}
		QueryWrapper<IotProxyEquipment> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(IotProxyEquipment::getEquipId, equipId).eq(IotProxyEquipment::getProxyId, proxyId);
		iotProxyEquipmentService.remove(queryWrapper);
		return Result.ok("解绑成功");
	}
}