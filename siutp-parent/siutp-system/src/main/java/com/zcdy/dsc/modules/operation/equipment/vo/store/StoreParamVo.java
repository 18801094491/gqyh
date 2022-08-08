package com.zcdy.dsc.modules.operation.equipment.vo.store;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 仓库新增入参
 * @author： songguang.jiao
 * 创建时间： 2020年1月9日 上午11:02:27
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "StoreParamVo", description = "仓库新增入参")
public class StoreParamVo {

		//id
		@ApiModelProperty(value = "id")
		private String id;

		//仓库名称
		@ApiModelProperty(value = "仓库名称")
		private String storeName;
		
		//仓库地址
		@ApiModelProperty(value = "仓库地址")
		private String storePosition;
		
		/**启停用状态 1-启用，0-停用*/
	    @ApiModelProperty(value = "启停用状态 1-启用，0-停用")
		private java.lang.String storeStatus;
	    
}
