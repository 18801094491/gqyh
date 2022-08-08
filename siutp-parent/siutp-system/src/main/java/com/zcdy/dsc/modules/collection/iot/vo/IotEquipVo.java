package com.zcdy.dsc.modules.collection.iot.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 采集代理跟采集设备绑定
 * @author：  songguang.jiao
 * 创建时间： 2020年3月13日 下午2:39:22 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="IotEquipVo",description="采集代理跟采集设备绑定")
public class IotEquipVo {

	//采集代理id
	@ApiModelProperty(value="采集代理id")
	private String id;
	
	//采集设备名称
	@ApiModelProperty(value="采集设备名称")
	private String iotName;
	
	//采集设备编号
	@ApiModelProperty(value="采集设备编号")
	private String iotSn;
	
	//绑定状态(0-未绑定,1-已绑定)
	@ApiModelProperty(value="绑定状态(0-未绑定,1-已绑定)")
	private String bindStatus;
}
