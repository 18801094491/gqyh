package com.zcdy.dsc.modules.operation.alarm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Roberto
 * @date 2020/05/18
 */
@Setter
@Getter
@ToString
@ApiModel(value = "WorkingDataVO", description = "仪表盘数据")
public class WorkingDataVO {

    @ApiModelProperty(value = "数据标题")
    private String title;

    @ApiModelProperty(value = "最大值")
    private String maxValue;

    @ApiModelProperty(value = "最小值")
    private String minValue;

    @ApiModelProperty(value = "安全上值")
    private String leftValue;

    @ApiModelProperty(value = "安全下值")
    private String rightValue;

    @ApiModelProperty(value = "实时值")
    private String realValue;
    
}
