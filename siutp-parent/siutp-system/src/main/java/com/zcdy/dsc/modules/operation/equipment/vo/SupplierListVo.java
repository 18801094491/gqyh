package com.zcdy.dsc.modules.operation.equipment.vo;

import lombok.Setter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * 描述: 供应商下拉选列表
 * @author：  songguang.jiao
 * 创建时间： 2020年3月10日 下午10:58:45 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="SupplierListVo",description="供应商下拉选列表")
public class SupplierListVo {

	/** id*/
    @ApiModelProperty(value = " id")
	private java.lang.String  id;
	/**供应商名称*/
    @ApiModelProperty(value = "供应商名称")
	private java.lang.String supplierName;
	
}
