package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by 柯昌年 on 2020/8/20
 */
@ApiModel(value="设备信息")
@Getter
@Setter
public class IotEquipInfoVoNew {
    /**
     * 采集设备id
     */
    @ApiModelProperty("采集设备id")
    private String gisId;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String equipmentName;

    /**
     * 列表需要展示的采集数据
     */
    @ApiModelProperty("列表需要展示的采集数据")
    private String  showData;

    /**
     * 列表需要展示明细数据
     */
    @ApiModelProperty("列表需要展示的采集数据")
    private String  conText;
}
