package com.zcdy.dsc.modules.operation.equipment.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 工具名称规格下拉选
 * @author：  songguang.jiao
 * 创建时间： 2020年3月12日 下午9:49:08 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="ToolDropdown",description="工具名称规格下拉选")
public class ToolDropdown {

	//工具主键id
	@ApiModelProperty(value="工具主键id")
	private String id;
	
	//工具规格
	@ApiModelProperty(value="工具规格")
	private String toolModel;
	
}
