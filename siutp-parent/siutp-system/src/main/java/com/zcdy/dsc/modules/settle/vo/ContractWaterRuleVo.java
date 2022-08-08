package com.zcdy.dsc.modules.settle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 计价规则查询列表
 * @author: songguang.jiao
 * 创建时间:  2020年4月23日 上午10:40:21
 * 版本: V1.0
 */
@Getter
@Setter
@ApiModel(value="ContractWaterRuleVo",description="计价规则查询列表")
public class ContractWaterRuleVo {

	/**id*/
    @ApiModelProperty(value = "规则id")
	private java.lang.String id;
	/**合同名称*/
    @ApiModelProperty(value = "合同名称")
	private java.lang.String contractName;
	/**规则名称*/
    @ApiModelProperty(value = "规则名称")
	private java.lang.String ruleName;
    /**规则类型  1-阶梯计价 2-手工计价*/
    @ApiModelProperty(value = "规则类型  1-阶梯计价 2-手工计价")
	private java.lang.String ruleTypeName;
}
