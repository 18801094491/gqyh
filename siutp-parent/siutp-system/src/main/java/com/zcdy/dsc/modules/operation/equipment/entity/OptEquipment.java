package com.zcdy.dsc.modules.operation.equipment.entity;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述:
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-24
 * 版本号: V1.0
 */
@Data
@TableName("opt_equipment")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="opt_equipment对象", description="设备资产")
public class OptEquipment {
    
	/**唯一标识*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "唯一标识")
	private java.lang.String id;
	/**设备编码*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
	private java.lang.String equipmentSn;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
	private java.lang.String equipmentName;
	/**设备类型*/
	@Excel(name = "设备类型", width = 15)
    @ApiModelProperty(value = "设备类型")
	private java.lang.String equipmentType;
	/**设备类别*/
	@Excel(name = "设备类别", width = 15)
    @ApiModelProperty(value = "设备类别")
	private java.lang.String equipmentCategory;
	/**型号*/
	@Excel(name = "型号", width = 15)
    @ApiModelProperty(value = "型号")
	private java.lang.String equipmentMode;
	/**所属标段*/
	@Excel(name = "所属标段", width = 15)
    @ApiModelProperty(value = "所属标段")
	private java.lang.String equipmentSection;
	/**所在位置*/
	@Excel(name = "所在位置", width = 15)
    @ApiModelProperty(value = "所在位置")
	private java.lang.String equipmentLocation;
	/**供应商标识*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
	private java.lang.String equipmentSupplier;
	/**运行状态*/
	@Excel(name = "资产状态", width = 15)
    @ApiModelProperty(value = "资产状态")
	private java.lang.String equipmentStatus;
	/**启用停用状态*/
	@Excel(name = "启用停用状态", width = 15)
	@ApiModelProperty(value = "启用停用状态")
	private java.lang.String equipmentRevstop;
	/**投入运营时间*/
	@Excel(name = "投入运营时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "投入运营时间")
	private java.util.Date equipmentOperating;
	/**购置时间*/
	@Excel(name = "购置时间", width = 20, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "购置时间")
	private java.util.Date equipmentPurchase;
	/**资产的展示图片地址，一张*/
	@Excel(name = "资产的展示图片地址，一张", width = 15)
    @ApiModelProperty(value = "资产的展示图片地址，一张")
	private java.lang.String equipmentImg;
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
	/**规格*/
	@Excel(name = "规格", width = 15)
    @ApiModelProperty(value = "规格")
	private String equipmentSpecs;
	@ApiModelProperty(value="基础信息id")
	private String basicId;
	/**海康监控设备唯一编码*/
	@ApiModelProperty(value = "海康监控设备唯一编码")
	private String hkMonitorCode;
	/**海康监控平台在配置文件中的唯一标识*/
	@ApiModelProperty(value = "海康监控平台在配置文件中的唯一标识")
	private String hkMonitorKey;
}
