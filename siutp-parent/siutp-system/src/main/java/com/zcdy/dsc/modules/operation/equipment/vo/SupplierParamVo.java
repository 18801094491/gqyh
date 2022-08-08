package com.zcdy.dsc.modules.operation.equipment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 供应商新增修改请求实体
 * @author songguang.jiao
 * 2019年12月18日 下午7:01:31
 * descriptions:
 */
@Getter
@Setter
@ApiModel(value="SupplierParamVo",description="供应商新增修改请求实体")
public class SupplierParamVo {

	/** id*/
    @ApiModelProperty(value = " id")
	private java.lang.String  id;
	/**供应商编码*/
    @ApiModelProperty(value = "供应商编码")
	private java.lang.String supplierSn;
	/**供应商名称*/
    @ApiModelProperty(value = "供应商名称")
	private java.lang.String supplierName;
	/**供应商证书编码*/
    @ApiModelProperty(value = "供应商营业执照编号")
	private java.lang.String supplierCert;
	/**供应商类别，参考数据字典*/
    @ApiModelProperty(value = "供应商类别，参考数据字典")
	private java.lang.String supplierCategory;
	/**供应商主供货物*/
    @ApiModelProperty(value = "供应商主供货物")
	private java.lang.String supplierGoods;
	/**供应商联系人*/
    @ApiModelProperty(value = "供应商联系人")
	private java.lang.String contacts;
	/**供应商联系人电话*/
    @ApiModelProperty(value = "供应商联系人电话")
	private java.lang.String contactsPhone;
	/**状态，0-停用，1-启用*/
    @ApiModelProperty(value = "状态，0-停用，1-启用")
	private java.lang.String supplierStatus;

	
}
