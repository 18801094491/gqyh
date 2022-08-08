package com.zcdy.dsc.modules.centre.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 树和设备查询参数列表
 * @Author: 在信汇通
 * @Date:   2021-03-11
 * @Version: V1.0
 */
@Data
@ApiModel(value="树和设备查询参数列表", description="树和设备查询参数列表")
public class EquTerrParam
{
    @ApiModelProperty(value="标段", notes="所属标段")
    private String optSection;

    @ApiModelProperty(value="设备类型", notes="设备类型")
    private String equipmentType;

    @ApiModelProperty(value="放置位置", notes="放置位置")
    private String optLocation;

    @ApiModelProperty(value="设备状态", notes="设备状态")
    private String equipmentRevstop;

    @ApiModelProperty(value="设备类别", notes="设备类别")
    private String equipmentCategory;

    @ApiModelProperty(value="供应商", notes="供应商")
    private String equipmentSupplier;

    @ApiModelProperty(value="设备编码", notes="设备编码")
    private String equipmentSn;

    @ApiModelProperty(value="设备名称", notes="设备名称")
    private String optName;
    @ApiModelProperty(value="所属中心", notes="所属中心")
    private String centre;
    @ApiModelProperty(value="对象类型", notes="对象类型")
    private String objType;
    @ApiModelProperty(value="父节点id", notes="父节点id")
    private String parentId;
}
