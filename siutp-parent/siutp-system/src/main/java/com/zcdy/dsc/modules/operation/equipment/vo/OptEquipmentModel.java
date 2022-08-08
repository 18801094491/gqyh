package com.zcdy.dsc.modules.operation.equipment.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zcdy.dsc.modules.operation.equipment.param.OptEquipmentAttrAddParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 设备资产信息
 *
 * @author songguang.jiao
 * 2019年12月18日 下午7:57:34
 * <p>
 * descriptions:
 */
@Data
@ApiModel(value = "OptEquipmentModel对象", description = "设备资产信息")
public class OptEquipmentModel {

    //主键id
    private String id;

    /**
     * 设备编码
     */
    @Excel(name = "设备编号", width = 15, orderNum = "60")
    @ApiModelProperty(value = "设备编号")
    private java.lang.String equipmentSn;
    /**
     * equipmentName
     */
    @Excel(name = "设备名称", width = 15, orderNum = "70")
    @ApiModelProperty(value = "设备名称")
    private java.lang.String equipmentName;
    /**
     * 设备类别
     */
    @ApiModelProperty(value = "设备类别")
    @Excel(name = "设备类别", width = 15, orderNum = "80")
    private java.lang.String equipmentCategory;
    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    @Excel(name = "设备类型", width = 15, orderNum = "10")
    private java.lang.String equipmentType;
    /**
     * 设备型号名称
     */
    @ApiModelProperty(value = "设备型号")
    @Excel(name = "设备型号", width = 32, orderNum = "40")
    private String equipmentModeName;
    /**
     * 设备规格名称
     */
    @ApiModelProperty(value = "设备规格")
    @Excel(name = "设备规格", width = 15, orderNum = "50")
    private String equipmentSpecsName;
    /**
     * 所属标段，数据字典
     */
    @ApiModelProperty(value = "所属标段")
    @Excel(name = "所属标段", width = 15, orderNum = "1")
    private java.lang.String equipmentSection;
    /**
     * 放置位置
     */
    @ApiModelProperty(value = "放置位置")
    @Excel(name = "放置位置", width = 18, orderNum = "20")
    private java.lang.String equipmentLocation;
    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商")
    @Excel(name = "供应商", width = 15, orderNum = "30")
    private java.lang.String equipmentSupplier;
    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商id")
    private java.lang.String equipmentSupplierId;
    /**
     * 资产状态
     */
    @ApiModelProperty(value = "资产状态")
    @Excel(name = "资产状态", width = 15, orderNum = "90")
    private java.lang.String equipmentStatus;
    /**
     * 投入运营时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "投入运营时间", width = 15, format = "yyyy-MM-dd", orderNum = "100")
    @ApiModelProperty(value = "投入运营时间(yyyy-MM-dd)")
    private java.util.Date equipmentOperating;
    /**
     * 购置时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "购置时间(yyyy-MM-dd)")
    @Excel(name = "购置时间", width = 15, format = "yyyy-MM-dd", orderNum = "110")
    private java.util.Date equipmentPurchase;
    /**
     * 启用停用状态
     */
    @ApiModelProperty(value = "启用停用状态")
    @Excel(name = "启用停用状态", width = 15, orderNum = "120")
    private java.lang.String equipmentRevstopText;

    /**
     * 启用停用状态
     */
    @ApiModelProperty(value = "启用停用状态")
    private java.lang.String equipmentRevstop;
    /**
     * 维保次数
     */
    @ApiModelProperty(value = "维保次数")
    private String upkeepCount;
    /**
     * 最近保养时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "最近保养时间")
    private java.util.Date upkeepTimeBy;
    /**
     * 最近维修时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty(value = "最近维修时间")
    private java.util.Date upkeepTimeWx;
    /**
     * 资产图片
     */
    @ApiModelProperty(value = "资产图片")
    private String equipmentImg;
    /**
     * 设备规格code
     */
    @ApiModelProperty(value = "设备规格code")
    private String equipmentSpecs;
    /**
     * 设备型号code
     */
    @ApiModelProperty(value = "设备型号code")
    private java.lang.String equipmentMode;
    /**
     * 设备类型code
     */
    @ApiModelProperty(value = "设备类型code")
    private java.lang.String equipmentTypeCode;
    /**
     * 绑定状态值 0-已绑定  1-未绑定
     */
    @ApiModelProperty(value = " 0-已绑定  1-未绑定")
    private String bindStatus;
    /**
     * 设备附属属性列表
     */
    @ApiModelProperty(value = "设备附属属性列表")
    private List<OptEquipmentAttrAddParam> attrAddParams;
    /**
     * 设备标签ID
     */
    @ApiModelProperty(value = "设备标签ID")
    private String labelId;
    /**
     * 设备标签名称
     */
    @ApiModelProperty(value = "设备标签")
    private String labelName;
    /**
     * 计划时间
     */
    @ApiModelProperty(value = "计划时间")
    private String planDate;

    /**
     * 倒计时(天)
     */
    @ApiModelProperty(value = "倒计时(天)")
    private String countDays;

    /**
     * 是否派工
     */
    @ApiModelProperty("是否派工")
    private String dispatchStatus;

    /**
     * 责任人id
     */
    @ApiModelProperty("责任人id")
    private String personResponsible;

    /**
     * 责任人
     */
    @ApiModelProperty("责任人")
    private String personResponsibleName;

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

    /**
     * 设备纬度
     */
    @ApiModelProperty("设备纬度")
    private String latitude;

    /**
     * 设备经度
     */
    @ApiModelProperty("设备经度")
    private String longitude;

    /**
     * 投入运营时间
     */
    @ApiModelProperty(value = "投入运营时间，“空值”查询判断条件，非显示字段，只在生成sql时作为判断条件使用")
    private String equipmentOperatingEmptyStr;
    /**
     * 购置时间
     */
    @ApiModelProperty(value = "购置时间，“空值”查询判断条件，非显示字段，只在生成sql时作为判断条件使用")
    private String equipmentPurchaseEmptyStr;
}
