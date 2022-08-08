package com.zcdy.dsc.modules.operation.equipment.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * 资产设备添加请求参数
 * @author songguang.jiao
 * 2019年12月18日 下午7:33:03
 *
 * descriptions:
 *
 */
@Data
@ApiModel(value="GisEquipmentAddParam对象", description="设备资产添加请求参数")
public class OptEquipmentAddParam {
    /**唯一标识*/
    @ApiModelProperty(value = "唯一标识", required = true)
    private java.lang.String id;
	/**
	 * 设备编码
	 */
    @ApiModelProperty(value = "设备编码")
	private java.lang.String equipmentSn;
	/**
	 * 设备名称
	 */
    @ApiModelProperty(value = "设备名称")
	private java.lang.String equipmentName;
	/**
	 * 设备类型
	 */
    @ApiModelProperty(value = "设备类型")
	private java.lang.String equipmentType;
	/**
	 * 设备类别
	 */
    @ApiModelProperty(value = "设备类别")
	private java.lang.String equipmentCategory;
	/**
	 * 设备型号
	 */
    @ApiModelProperty(value = "设备型号")
	private java.lang.String equipmentMode;
	/**
	 * 所属标段
	 */
    @ApiModelProperty(value = "所属标段")
	private java.lang.String equipmentSection;
	/**
	 * 所在位置
	 */
    @ApiModelProperty(value = "所在位置")
	private java.lang.String equipmentLocation;
	/**
	 * 供应商名称
	 */
    @ApiModelProperty(value = "供应商名称")
	private java.lang.String equipmentSupplier;
	/**
	 * 运行状态
	 */
    @ApiModelProperty(value = "运行状态，参考数据字典")
	private java.lang.String equipmentStatus;
	/**
	 * 启用停用状态
	 */
	@ApiModelProperty(value = "启用停用状态，参考数据字典")
	private java.lang.String equipmentRevstop;
	/**
	 * 投入运营时间
	 */
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "投入运营时间")
	private java.util.Date equipmentOperating;
	/**购置时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "购置时间")
	private java.util.Date equipmentPurchase;
	/**
	 * 设备附属属性列表
	 */
	@ApiModelProperty(value = "设备附属属性列表")
    private List<OptEquipmentAttrAddParam> attrAddParams;
	/**
	 * 设备规格
	 */
	@ApiModelProperty(value = "设备规格")
  	private String equipmentSpecs;
	/**
	 * 设备规格
	 */
	@ApiModelProperty(value = "设备图片地址")
	private String equipmentImg;
	/**
	 * 基础信息id
	 */
    @ApiModelProperty(value = "基础信息id")
    private String baseId;

	/**
	 * 责任人
	 */
	@ApiModelProperty("责任人")
    private String personResponsible;

	/**
	 * 海康监控设备唯一编码
	 */
	@ApiModelProperty("海康监控设备唯一编码")
	private String hkMonitorCode;

	/**
	 * 海康监控平台在配置文件中的唯一标识
	 */
	@ApiModelProperty("海康监控平台在配置文件中的唯一标识")
	private String hkMonitorKey;
}
