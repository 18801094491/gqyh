package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 水表和流量计日累计统计Vo
 * @author：  songguang.jiao
 * 创建时间：  2020年3月26日 上午9:43:01
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="MeterAndFlowCountVo",description="水表和流量计日累计统计Vo")
public class MeterAndFlowCountVo {


	@ApiModelProperty(value="流量统计")
	private String flowCount;
	
	@ApiModelProperty(value="流量统计格式化")
	private String flowCountFormat;
	
	@ApiModelProperty(value="流量名称")
	private String flowName;

	@ApiModelProperty(value = "流量描述code")
	private String flowCode;
	
}
