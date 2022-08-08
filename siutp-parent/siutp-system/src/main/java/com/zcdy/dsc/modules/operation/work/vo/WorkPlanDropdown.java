package com.zcdy.dsc.modules.operation.work.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 巡检计划下拉选列表
 * @author：  songguang.jiao
 * 创建时间： 2020年2月12日 下午2:05:19 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="WorkPlanDropdown",description="巡检计划下拉选列表")
public class WorkPlanDropdown {

	/**
	 * id
	 */
	@ApiModelProperty(value="id")
	private String id;

	/**
	 * 计划名称
	 */
	@ApiModelProperty(value="计划名称")
	private String planName;
}
