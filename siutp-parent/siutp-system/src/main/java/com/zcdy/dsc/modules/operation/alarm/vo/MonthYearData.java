package com.zcdy.dsc.modules.operation.alarm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 月份曲线图(年份曲线图)
 * @author：  songguang.jiao
 * 创建时间： 2020年2月28日 下午5:49:24 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="MonthYearData",description="月份曲线图/年份曲线图")
public class MonthYearData {
	
	//月份或者年份
	@ApiModelProperty(value="月份")
	private String time;
	
	//总数量
	@ApiModelProperty(value="总数量")
	private String sumNum;

}
