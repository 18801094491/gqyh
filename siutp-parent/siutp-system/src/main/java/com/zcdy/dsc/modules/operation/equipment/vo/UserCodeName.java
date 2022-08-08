package com.zcdy.dsc.modules.operation.equipment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 下拉用户列表code-value
 * @author：  songguang.jiao
 * 创建时间： 2020年2月10日 上午11:30:41 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="StoreUserName",description="仓库管理员下拉列表")
public class UserCodeName {

	private String code;
	
	//用户名称
	@ApiModelProperty(value="用户名称")
	private String name;
}
