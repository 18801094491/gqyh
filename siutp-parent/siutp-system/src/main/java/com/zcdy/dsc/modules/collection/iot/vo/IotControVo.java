package com.zcdy.dsc.modules.collection.iot.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 2 * @author： 王海东
 * 3 * 创建时间： 2020/3/6 11:40
 * 4
 */

@Getter
@Setter
@ApiModel(value="IotControVo",description="反向控制")
public class IotControVo {

    /**id*/
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**设备id*/
    @ApiModelProperty(value = "设备id")
    private java.lang.String equipmentId;

    /**设备名称*/
    @ApiModelProperty(value = "设备名称")
    private java.lang.String equipmentName;
    /**控制名称*/
    @ApiModelProperty(value = "控制名称")
    private java.lang.String controName;
    /**变量id*/
    @ApiModelProperty(value = "变量id")
    private java.lang.String variableId;
    /**变量名称*/
    @ApiModelProperty(value = "变量名称")
    private java.lang.String variableName;

    /**变量数据类型code*/
    @ApiModelProperty(value = "变量数据类型code")
    private java.lang.String variableDataType;

    /**变量值*/
    @ApiModelProperty(value = "变量值")
    private java.lang.String variableValue;
    /**编辑时间*/
    @ApiModelProperty(value = "编辑时间")
    private java.lang.String createTime;
}
