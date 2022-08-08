package com.zcdy.dsc.modules.collection.iot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipmentBind;
import com.zcdy.dsc.modules.collection.iot.service.IotEquipmentBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 描述: 采集设备绑定资产表
 * @author： Roberto
 * 创建时间： 2020-03-07 21:03:51
 * 版本号: V1.0
 */
@Api(tags = "采集设备绑定资产表")
@RestController
@RequestMapping("/iot/iotEquipmentBind")
public class IotEquipmentBindController extends BaseController<IotEquipmentBind, IotEquipmentBindService> {

	@Resource
	private IotEquipmentBindService iotEquipmentBindService;

	/**
	 * 描述: 采集设备绑定资产信息  资产跟采集设备1对1绑定数据
	 *  @see 设备资产下拉列表 /equipment/optEquipment/getGisVoList
	 * @author： songguang.jiao
	 * 创建时间： 2020年3月8日 下午4:16:19
	 * 版本号: V1.0
	 */
	@ApiOperation(value = "采集设备绑定资产信息", notes = "采集设备绑定资产信息")
	@PostMapping("/bindData")
	public Result<Object> bindData(@RequestBody @Valid IotEquipmentBind iotEquipmentBind) {
		QueryWrapper<IotEquipmentBind> queryWarpper = new QueryWrapper<>();
		queryWarpper.lambda().eq(IotEquipmentBind::getOptId, iotEquipmentBind.getOptId());
		IotEquipmentBind one = iotEquipmentBindService.getOne(queryWarpper);
		if (one != null) {
			return Result.error("该资产已经被绑定");
		}
		QueryWrapper<IotEquipmentBind> warpper = new QueryWrapper<>();
		warpper.lambda().eq(IotEquipmentBind::getIotId, iotEquipmentBind.getIotId());
		IotEquipmentBind one2 = iotEquipmentBindService.getOne(warpper);
		if(one2!=null){
			return Result.error("该采集设备已经绑定数据");
		}
		iotEquipmentBindService.save(iotEquipmentBind);
		return Result.ok("绑定成功");
	}

	/**
	 * 描述: 采集设备解绑资产
	 * @author： songguang.jiao
	 * 创建时间： 2020年3月8日 下午4:16:19
	 * 版本号: V1.0
	 */
	@ApiOperation(value = "采集设备解绑资产", notes = "采集设备解绑资产")
	@GetMapping("/unbindData")
	public Result<Object> unbindData(String iotId) {
		if (StringUtils.isEmpty(iotId)) {
			return Result.error("采集设备id不能为空");
		}
		QueryWrapper<IotEquipmentBind> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(IotEquipmentBind::getIotId,iotId);
		iotEquipmentBindService.remove(queryWrapper);
		return Result.ok("解绑成功");
	}
	
	
	
}