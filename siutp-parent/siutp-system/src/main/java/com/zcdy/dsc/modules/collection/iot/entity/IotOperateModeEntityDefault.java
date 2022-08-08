package com.zcdy.dsc.modules.collection.iot.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 默认监控设备信息
 * @author songguang.jiao
 * @date 2020/6/1116:36
 */
@Getter
@Setter
@ApiModel("IotOperateModeDefault")
public class IotOperateModeEntityDefault {

    /**
     * 监测位置id
     */
    @ApiModelProperty("监测位置id")
    private String operateLocationId;

    /**
     * 监测位置
     */
    @ApiModelProperty("监测位置")
    private String operateLocationName;

    /**
     * 设备资产id
     */
    @ApiModelProperty("设备资产id")
    private String equipmentId;

    /**
     * 设备资产位置
     */
    @ApiModelProperty("设备资产位置")
    private String equimentLocation;

    /**
     * 资产图片路径
     */
    private String equipImg;

    /**
     * gisId
     */
    private String gisId;
}
