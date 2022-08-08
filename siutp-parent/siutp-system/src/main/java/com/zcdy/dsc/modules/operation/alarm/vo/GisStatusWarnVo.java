package com.zcdy.dsc.modules.operation.alarm.vo;

import com.zcdy.dsc.modules.operation.equipment.vo.EquipmentDetailModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 地图界面查询所有图标
 * @author：  songguang.jiao
 * 创建时间： 2020年3月9日 上午10:05:48 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="GisStatusWarnVo",description="地图界面查询所有图标")
public class GisStatusWarnVo {

	@ApiModelProperty(value="gis设备信息")
	private EquipmentDetailModel equipmentDetailModel;

	@ApiModelProperty(value="设备状态")
	private String status;
	@ApiModelProperty(value="告警信息")
	private BusinessWarnVo businessWarnVo;

}
