package com.zcdy.dsc.modules.settle.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * 年度统计
 * @author tangchao
 * @date 2020/5/25
 */
@ApiModel("年度统计")
public class AnnualStatistical{
    @ApiModelProperty("客户id")
    private String customerId;
    @ApiModelProperty("客户名称")
    private String customerName;
    @ApiModelProperty("年份")
    private String year;
    @ApiModelProperty(value = "月份用水,花费", notes = "逗号隔开, 左边为用水, 右边为花费")
    private List<String> month = new ArrayList<>(12);
    @ApiModelProperty("合计")
    private String tatal;
}
