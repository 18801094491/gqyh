package com.zcdy.dsc.modules.operation.work.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 班组下拉选列表
 * @author：  songguang.jiao
 * 创建时间： 2020年2月12日 下午2:05:19 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="TeamNameVo",description="班组下拉选列表")
public class TeamDropdown {

	/**
	 * 班组id
	 */
	@ApiModelProperty(value="班组id")
	private String id;

	/**
	 * 班组名称
	 */
	@ApiModelProperty(value="班组名称")
	private String teamName;
}
