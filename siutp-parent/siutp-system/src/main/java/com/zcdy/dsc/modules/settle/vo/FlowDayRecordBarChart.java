package com.zcdy.dsc.modules.settle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 描述: 每日用水量统计-柱状图
 *  @author: songguang.jiao
 *  创建时间: 2020年5月6日 下午4:16:29 
 */
@Getter
@Setter
@ApiModel(value = "FlowDayRecordBarChart")
public class FlowDayRecordBarChart {

	//主标题
	@ApiModelProperty(value="主标题")
	private String mainTitle;

	//y轴标题
	@ApiModelProperty(value="y轴标题")
	private String yTitle;
	
	//x轴
	@ApiModelProperty(value="x轴数据")
	private List<String> xCategories;
	
	//y轴
	@ApiModelProperty(value="y轴数据")
	private List<FlowBarChartItem> series;
	
	@Getter
	@Setter
	@ApiModel(value="FlowBarChartItem")
	public static final class FlowBarChartItem{
		@ApiModelProperty(value="数据名称")
		private String name;
		
		//data
		@ApiModelProperty(value="数据值")
		private List<BigDecimal> data;
	}
}
