package com.zcdy.dsc.modules.datacenter.statistic.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 查询所有客户水表往月抄表单价VO
 * 2020-4-26 09:58:00
 * @author tangchao
 */
@Getter
@Setter
public class MeterFlowMonthPriceVo {

    /**
     * 客户编码
     */
    @ApiModelProperty(value = "客户编码")
    private String customerSn;
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    /**
     * 水表编码
     */
    @ApiModelProperty(value = "水表编码")
    private String equipmentSn;
    /**
     * 合同编码
     */
    @ApiModelProperty(value = "合同编码")
    private String contractSn;
    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    /**
     * 单价
     */
    @ApiModelProperty(value = "单价")
    private String waterPrice;

    /**
     * 日期
     */
    @ApiModelProperty(value = "日期")
    private String staticsDate;

}
