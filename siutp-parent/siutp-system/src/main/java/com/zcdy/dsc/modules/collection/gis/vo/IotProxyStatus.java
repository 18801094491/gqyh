package com.zcdy.dsc.modules.collection.gis.vo;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 采集代理状态
 * @author：  songguang.jiao
 * 创建时间： 2020年3月17日 上午9:39:20 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="IotProxyStatus",description="采集代理状态")
public class IotProxyStatus {

	@ApiModelProperty(value="状态数据")
	private JSONObject jsonObject;
	@ApiModelProperty(value="采集代理名称")
	private String name;
	
}
