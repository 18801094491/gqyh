package com.zcdy.dsc.modules.settle.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 客户绑定水表信息
 * @author：  songguang.jiao
 * 创建时间：  2020年1月8日 上午11:11:26
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="客户绑定水表信息",description="客户绑定水表信息")
public class SettleCustomerEquipVo {

	//客户id
    @ApiModelProperty(value = "客户id")
    @NotNull(message="客户id不能为空")
	@NotBlank(message = "模型ID不能为空")
	private java.lang.String customerId;
	//设备id
    @ApiModelProperty(value = "设备id")
    @NotNull(message="设备id不能为空")
	@NotBlank(message = "设备id不能为空")
	private java.lang.String equipmentId;
	//设备编号
    @ApiModelProperty(value = "equipmentSn")
	private java.lang.String equipmentSn;
	
}
