package com.zcdy.dsc.modules.settle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 年度统计查询条件
 * @author tangchao
 * @date 2020/5/25
 */
@Data
@ApiModel("年度统计查询")
public class AnnualStatisticalQueryParmVo {
    @ApiModelProperty("客户类型")
    private String customerType;
    @ApiModelProperty("客户名称")
    private String likeCustomerName;
    @ApiModelProperty("客户ids")
    private String[] customerIds;
    @ApiModelProperty("开始年份")
    private String beginYear;
    @ApiModelProperty("结束年份")
    private String endYear;
}
