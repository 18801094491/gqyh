 package com.zcdy.dsc.modules.settle.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户绑定水表分页查询入参
 * @author songguang.jiao
 * @date 2020/05/19 09:41:24
 */
@Getter
@Setter
@ApiModel(value="客户绑定水表分页查询入参")
 public class EquipmentMeterPageParam extends AbstractPageParam{

    /**
     * 设备编号
     */
    @ApiModelProperty(value="设备编号")
    private  String equipmentSn;
    
    /**
     * 设备标段
     */
    @ApiModelProperty(value="设备标段")
    private String equimentSection;
    
    /**
     * 放置位置
     */
    @ApiModelProperty(value="放置位置")
    private String equipmemtLocation;
    
    /**
     * 设备名称
     */
    @ApiModelProperty(value="设备名称")
    private String equipmentName;
}
