package com.zcdy.dsc.modules.operation.equipment.entity;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: 供应商管理
 * @author： bthy
 * 创建时间：   2019-12-16
 * 版本号: V1.0
 */
@Data
@TableName("opt_supplier")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="opt_supplier对象", description="供应商管理")
public class OptSupplier {
    
	/** id*/
	@Excel(name = " id", width = 15)
    @ApiModelProperty(value = " id")
	private java.lang.String  id;
	/**供应商编码*/
	@Excel(name = "供应商编码", width = 15)
    @ApiModelProperty(value = "供应商编码")
	private java.lang.String supplierSn;
	/**供应商名称*/
	@Excel(name = "供应商名称", width = 15)
    @ApiModelProperty(value = "供应商名称")
	private java.lang.String supplierName;
	/**供应商营业执照编号*/
	@Excel(name = "供应商营业执照编号", width = 15)
    @ApiModelProperty(value = "供应商营业执照编号")
	private java.lang.String supplierCert;
	/**供应商类别，参考数据字典*/
	@Excel(name = "供应商类别，参考数据字典", width = 15)
    @ApiModelProperty(value = "供应商类别，参考数据字典")
	private java.lang.String supplierCategory;
	/**供应设备*/
	@Excel(name = "供应设备", width = 15)
    @ApiModelProperty(value = "供应设备")
	private java.lang.String supplierGoods;
	/**供应商联系人*/
	@Excel(name = "供应商联系人", width = 15)
    @ApiModelProperty(value = "供应商联系人")
	private java.lang.String contacts;
	/**供应商联系人电话*/
	@Excel(name = "供应商联系人电话", width = 15)
    @ApiModelProperty(value = "供应商联系人电话")
	private java.lang.String contactsPhone;
	/**状态，0-停用，1-启用*/
	@Excel(name = "状态，0-停用，1-启用", width = 15)
    @ApiModelProperty(value = "状态，0-停用，1-启用")
	private java.lang.String supplierStatus;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private java.lang.String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**删除标识0-正常,1-已删除*/
	@Excel(name = "删除标识0-正常,1-已删除", width = 15)
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
	private java.lang.Integer delFlag;
}
