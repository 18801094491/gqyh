package com.zcdy.dsc.modules.datacenter.statistic.entity;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2020年3月16日 上午12:11:57
 * 描述: <p>chart基本信息和数据信息</p>
 */
@Setter
@Getter
@ApiModel("ChartInfo")
public class ChartInfo {

	
	//y轴标题
	@ApiModelProperty(value="y轴标题")
	private String title;
	
	//y轴图标数据集合
	@ApiModelProperty(value="y轴图标数据集合")
	private List<ChartsData> series;
	
	//图表类型
	@ApiModelProperty(value="图表类型",notes="图表类型")
	private String chartType;
	
	//序列最大值,最小值
	private List<ChartsValue> charsValue;
	
	@Getter
	@Setter
	@ApiModel(value="ChartsData",description="序列数据")
	public static final class ChartsData{
		//序列名称
		@ApiModelProperty(value="序列名称")
		private String name;
		//值集合
		@ApiModelProperty(value="日期时间戳,采集值数组集合")
		private List<Object[]> data;
		
	}
	
	@Getter
	@Setter
	@ApiModel(value="ChartsValue",description="序列数据最大值,最小值")
	public static final class  ChartsValue{
		//序列名称
		@ApiModelProperty(value="序列名称")
		private String name;
		//最大值
		@ApiModelProperty(value="最大值")
		private BigDecimal max;
		//最小值
		@ApiModelProperty(value="最小值")
		private BigDecimal min;
	}
}
