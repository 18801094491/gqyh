package com.zcdy.dsc.modules.operation.equipment.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 2 * @author： 王海东
 * 3 * 创建时间： 2020/2/21 14:08
 * 4
 */
@Getter
@Setter
public class MeterFlowVo {
    @ApiModelProperty(value = "当前日期")
    private String nowDate;
    @ApiModelProperty(value = "用户水表编号")
    private String equipmentSn;
    @ApiModelProperty(value = "标段")
    private String sectionName;
    @ApiModelProperty(value = "用户名称")
    private String customerName;
    @ApiModelProperty(value = "当前月累计用水量")
    private String monthNetFlowDay;
    @ApiModelProperty(value = "上月累计用水量")
    private String oldMonthNetFlowDay;
    @ApiModelProperty(value = "当年累计用水量")
    private String yearNetFlowDay;
    @ApiModelProperty(value = "总累计用水量")
    private String netFlowDay;
    @ApiModelProperty(value = "所属项目")
    private String projectName;
}
