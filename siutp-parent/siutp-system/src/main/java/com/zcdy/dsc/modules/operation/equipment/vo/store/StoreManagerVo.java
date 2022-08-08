package com.zcdy.dsc.modules.operation.equipment.vo.store;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 设置仓库管理员
 * @author：  songguang.jiao
 * 创建时间： 2020年3月7日 下午3:32:34 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="StoreManagerVo",description="设置仓库管理员")
public class StoreManagerVo {

	//仓库id
	@ApiModelProperty(value="仓库id(支持多选)")
	private String storeIds;
	
	//用户id
	@NotEmpty(message="用户不能为空")
	@NotNull(message="用户不能为空")
	@ApiModelProperty(value="用户id(单选)")
	private String userId;
	
}
