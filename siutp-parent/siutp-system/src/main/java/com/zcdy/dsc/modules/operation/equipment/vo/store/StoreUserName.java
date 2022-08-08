package com.zcdy.dsc.modules.operation.equipment.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述:下拉用户列表id-value
 * @author：  songguang.jiao
 * 创建时间：  2020年1月19日 下午4:05:22
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="StoreUserName",description="仓库管理员下拉列表")
public class StoreUserName {
	
	private String id;
	
	//用户名称
	@ApiModelProperty(value="用户名称")
	private String name;
}
