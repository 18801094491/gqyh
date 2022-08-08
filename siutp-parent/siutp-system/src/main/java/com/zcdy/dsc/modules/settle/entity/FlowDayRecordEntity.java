package com.zcdy.dsc.modules.settle.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlowDayRecordEntity {

	/**统计名称-分类字典*/
    @ApiModelProperty(value = "统计名称-分类字典")
	private java.lang.String countCode;
	/**统计值*/
    @ApiModelProperty(value = "统计值")
	private java.lang.String countValue;
	/**统计日期*/
    @ApiModelProperty(value = "统计日期")
	private String countDate;
	
}
