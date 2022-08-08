package com.zcdy.dsc.modules.operation.alarm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 告警信息详情视图对象
 *
 * @author Roberto
 * @date 2020/05/14
 */
@Setter
@Getter
@ToString
@ApiModel(value = "AlarmResultDetailVO", description = "告警信息详情")
public class AlarmResultDetailVO extends AlarmResultVO {

    // 设备编号
    @ApiModelProperty("设备编号")
    private String optSn;

    // 设备名称
    @ApiModelProperty("设备名称")
    private String optName;

    // 型号
    @ApiModelProperty("型号")
    private String optType;

    // 所属标段
    @ApiModelProperty("所属标段")
    private String optSection;

    // 设备所在位置
    @ApiModelProperty("设备所在位置")
    private String optPosition;

    // 供应商
    @ApiModelProperty("供应商")
    private String optSupplier;

    // 设备的显示图片
    @ApiModelProperty("图片")
    private String optPicture;

    // iotid
    @ApiModelProperty("iot标识")
    private String iotId;
}
