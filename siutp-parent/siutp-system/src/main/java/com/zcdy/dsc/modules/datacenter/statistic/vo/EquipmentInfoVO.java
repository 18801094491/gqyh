package com.zcdy.dsc.modules.datacenter.statistic.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author： Roberto
 * 创建时间：2020年3月15日 上午10:19:30
 * 描述: <p></p>
 */
@ApiModel("com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentInfoVO")
@Setter
@Getter
@ToString
public class EquipmentInfoVO {

	@ApiModelProperty(value="设备类型名称", notes="设备类型名称")
	private String equipmentName;
	
	@ApiModelProperty(value="设备id", notes="设备id")
	private String equipmentId;
}
