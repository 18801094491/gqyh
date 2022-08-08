 package com.zcdy.dsc.modules.operation.equipment.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页基础信息查询
 * @author songguang.jiao
 * @date 2020/05/28 15:47:15
 */
@Getter
@Setter
@ApiModel(value="OptBaseInfoVo")
 public class OptBaseInfoVo {
    /**id*/
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**基础信息名称*/
    @ApiModelProperty(value = "基础信息名称")
    private java.lang.String baseName;
    /**设备类型*/
    @ApiModelProperty(value = "设备类型code")
    private java.lang.String equipmentType;
    /**设备类型*/
    @ApiModelProperty(value = "设备类型")
    private java.lang.String equipmentTypeName;
    /**设备规格*/
    @ApiModelProperty(value = "设备规格code")
    private java.lang.String equipmentModel;
    /**设备规格*/
    @ApiModelProperty(value = "设备规格")
    private java.lang.String equipmentModelName;
    /**设备型号*/
    @ApiModelProperty(value = "设备型号code")
    private java.lang.String equipmentSpecs;
    /**设备型号*/
    @ApiModelProperty(value = "设备型号")
    private java.lang.String equipmentSpecsName;
    /**供应商*/
    @ApiModelProperty(value = "供应商code")
    private java.lang.String equipmentSupplier;
    /**供应商*/
    @ApiModelProperty(value = "供应商")
    private java.lang.String equipmentSupplierName;
}
