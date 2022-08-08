package com.zcdy.dsc.modules.operation.work.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 班次下拉选
 * @author：  songguang.jiao
 * 创建时间： 2020年2月11日 下午12:40:22 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="ShiftsListVo",description="班次下拉选")
public class ShiftsDropdown {

	/**
	 * 班次id
	 */
	@ApiModelProperty(value="班次id")
	private String id;

	/**
	 * 班次名称
	 */
	@ApiModelProperty(value="班次名称")
	private String shiftsName;
}
