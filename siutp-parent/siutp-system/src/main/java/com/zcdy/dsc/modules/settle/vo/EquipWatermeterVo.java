package com.zcdy.dsc.modules.settle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 客户页面水表列表相关信息
 * @author：  songguang.jiao
 * 创建时间：  2020年1月6日 下午2:40:59
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="EquipWatermeterVo",description="客户页面水表列表相关信息")
public class EquipWatermeterVo {

	//id
	@ApiModelProperty(value="id")
	private String id;
	
	//设备编号
	@ApiModelProperty(value="设备编号")
	private java.lang.String equipmentSn;
	
	//设备名称
	@ApiModelProperty(value="设备名称")
	private java.lang.String equipmentName;
	
	//设备信息
    @ApiModelProperty(value="设备信息")
    private java.lang.String equipmentInfo;
	
	//绑定状态0-未绑定,1-已绑定
	@ApiModelProperty(value="绑定状态0-未绑定,1-已绑定")
	private String bindStatus;
}
