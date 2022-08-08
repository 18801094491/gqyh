package com.zcdy.dsc.modules.operation.alarm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 角色下拉列表
 * @author：  songguang.jiao
 * 创建时间： 2020年3月5日 下午12:47:16 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="RoleNameVo",description="角色下拉列表")
public class RoleNameVo {

	//id
	@ApiModelProperty(value="id")
	private String id;
	
	//角色名称
	@ApiModelProperty(value="角色名称")
	private String roleName;
}
