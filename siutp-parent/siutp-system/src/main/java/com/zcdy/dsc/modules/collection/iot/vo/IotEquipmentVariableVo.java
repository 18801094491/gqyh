package com.zcdy.dsc.modules.collection.iot.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 2 * @author： 王海东
 * 3 * 创建时间： 2020/2/26 13:19
 * 4
 */
@Getter
@Setter
public class IotEquipmentVariableVo {
    @ApiModelProperty(value = "变量id")
    private String id;
    @ApiModelProperty(value = "变量名称")
    private String variableName;
    @ApiModelProperty(value = "变量示意")
    private String variableTitle;
    @ApiModelProperty(value = "变量数据类型")
    private String variableDataType;
    @ApiModelProperty(value = "设备id")
    private String iotId;
    @ApiModelProperty(value = "设备名称")
    private String iotName;
    @ApiModelProperty(value = "采集周期")
    private String iotCycle;
    @ApiModelProperty(value = "绑定状态1为绑定，0为未绑定")
    private String status;
}
