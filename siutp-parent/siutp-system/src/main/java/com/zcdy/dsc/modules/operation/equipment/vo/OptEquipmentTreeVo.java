package com.zcdy.dsc.modules.operation.equipment.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author tangchao
 * 设备台账树
 * @date 2020/5/28
 */
@Data
@ApiModel(value="OptEquipmentTreeVo对象", description="设备台账树信息")
public class OptEquipmentTreeVo {
    //主键id
    private String id;
    /**设备编码*/
    @ApiModelProperty(value = "设备编号")
    private java.lang.String equipmentSn;
    /**equipmentName*/
    @ApiModelProperty(value = "设备名称")
    private java.lang.String equipmentName;
    //设备类别
    @ApiModelProperty(value = "设备类别")
    private java.lang.String equipmentCategory;
    //设备类型
    @ApiModelProperty(value = "设备类型")
    private java.lang.String equipmentType;
    //设备型号名称
    @ApiModelProperty(value = "设备型号")
    private String equipmentModeName;
    //设备规格名称
    @ApiModelProperty(value = "设备规格")
    private String equipmentSpecsName;
    /**所属标段，数据字典*/
    @ApiModelProperty(value = "所属标段")
    private java.lang.String equipmentSection;
    //放置位置
    @ApiModelProperty(value = "放置位置")
    private java.lang.String equipmentLocation;
    /**供应商*/
    @ApiModelProperty(value = "供应商")
    private java.lang.String equipmentSupplier;
    /**资产状态*/
    @ApiModelProperty(value = "资产状态")
    private java.lang.String equipmentStatus;
    /**投入运营时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "投入运营时间(yyyy-MM-dd)")
    private java.util.Date equipmentOperating;
    /**购置时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "购置时间(yyyy-MM-dd)")
    private java.util.Date equipmentPurchase;
    /**启用停用状态*/
    @ApiModelProperty(value = "启用停用状态")
    private java.lang.String equipmentRevstopText;
    /**启用停用状态*/
    @ApiModelProperty(value = "启用停用状态")
    private java.lang.String equipmentRevstop;
    //设备规格code
    @ApiModelProperty(value = "设备规格code")
    private String equipmentSpecs;
    //设备型号code
    @ApiModelProperty(value = "设备型号code")
    private java.lang.String equipmentMode;
    //设备类型code
    @ApiModelProperty(value = "设备类型code")
    private java.lang.String equipmentTypeCode;
    private java.lang.String labelId;
    private java.lang.String labelName;

}
