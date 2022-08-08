package com.zcdy.dsc.modules.datacenter.statistic.vo;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 用水量产销差统计柱形图
 * @author：  songguang.jiao
 * 创建时间：  2020年3月24日 下午5:33:18
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="MeterFlowBarChart",description="用水量产销差统计柱形图")
public class MeterFlowBarChart {

	//x轴时间
	@ApiModelProperty(value="x轴时间")
	private List<String> time;
	
	//用水量数据
	@ApiModelProperty(value="用水量数据")
	private List<Map<String, Object>> series;
	
}
