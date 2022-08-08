package com.zcdy.dsc.modules.datacenter.statistic.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2020年3月15日 下午11:00:07
 * 描述: <p>统计图数据项目</p>
 */
@Getter
@Setter
@ApiModel("ChartData")
public class ChartData {

	@ApiModelProperty(name="时间", notes="时间")
	private String time;

	@ApiModelProperty(name="序列名称", notes="序列名称")
	private String seriallName;

	@ApiModelProperty(name="采集量", notes="采集量")
	private String value;

	@ApiModelProperty(name="资产编号", notes="资产编号")
	private String equipmentSn;

	@ApiModelProperty(name="资产标段", notes="资产标段")
	private String equipmentSection;

	@ApiModelProperty(name="资产类型", notes="资产类型")
	private String equipmentType;
	
	@ApiModelProperty(name="资产位置", notes="资产位置")
	private String equipmentLocation;
}
