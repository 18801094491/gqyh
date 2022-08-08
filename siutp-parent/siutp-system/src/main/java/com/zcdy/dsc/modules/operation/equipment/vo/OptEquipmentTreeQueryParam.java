package com.zcdy.dsc.modules.operation.equipment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tangchao
 * 设备台账树查询条件
 * @date 2020/5/28
 */
@Data
@ApiModel(value="com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentTreeQueryParam", description="设备台账树查询参数")
public class OptEquipmentTreeQueryParam {

    @ApiModelProperty(value="标签Id", notes="标签Id")
    private String labelId;
    @ApiModelProperty(value="放置位置", notes="放置位置")
    private String optLocation;
    @ApiModelProperty(value="设备状态", notes="设备状态")
    private String equipmentRevstop;
    @ApiModelProperty(value="设备编号", notes="设备编号")
    private String equipmentSn;
    @ApiModelProperty(value="供应商", notes="供应商")
    private String equipmentSupplier;


    private String labelParentIds;

}
