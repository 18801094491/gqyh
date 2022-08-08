package com.zcdy.dsc.modules.settle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author tangchao
 * @date 2020/5/22
 */
@Data
@ApiModel("合同信息下拉")
public class ManualContractInfoVo {
    @ApiModelProperty(value = "合同ID")
    private String contractId;
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    @ApiModelProperty(value = "合同ID")
    private String customerId;
    @ApiModelProperty(value = "价格")
    private BigDecimal price;
    @ApiModelProperty(value = "价格ID")
    private String ruleItemId;
}
