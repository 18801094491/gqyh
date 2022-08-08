package com.zcdy.dsc.modules.operation.equipment.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 供应商列表实体Vo
 * @author songguang.jiao
 * 2019年12月17日 下午7:30:04
 * descriptions:
 *
 */
@Getter
@Setter
@ApiModel(value="SupplierVo", description="供应商列表实体")
public class SupplierVo {

	/** id*/
    @ApiModelProperty(value = " id")
	private java.lang.String  id;
	/**供应商编码*/
    @ApiModelProperty(value = "供应商编码")
	private java.lang.String supplierSn;
	/**供应商名称*/
    @ApiModelProperty(value = "供应商名称")
	private java.lang.String supplierName;
	/**供应商营业执照编号*/
    @ApiModelProperty(value = "供应商营业执照编号")
	private java.lang.String supplierCert;
	/**供应商类别，参考数据字典*/
    @ApiModelProperty(value = "供应商类别，参考数据字典")
	private java.lang.String supplierCategory;
	/**供应设备*/
    @ApiModelProperty(value = "供应设备")
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
    /**创建时间*/
   	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
   	private java.util.Date createTime;
	
}
