package com.zcdy.dsc.modules.settle.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRuleItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 描述: 水表计价规则Vo"
 * @author:  songguang.jiao
 * 创建时间:  2020年4月23日 上午9:15:29
 * 版本: V1.0
 */
@Getter
@Setter
@ApiModel(value="ContractWaterRuleItemVo",description="水表计价规则Vo")
public class ContractWaterRuleItemVo {

	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "规则id")
	private java.lang.String id;
	/**合同id*/
    @ApiModelProperty(value = "合同id")
    @NotBlank(message="合同id不能为空")
    @NotNull(message="合同id不能为空")
	private java.lang.String contractId;
	/**规则名称*/
    @NotBlank(message="规则名称不能为空")
    @NotNull(message="规则名称不能为空")
    @ApiModelProperty(value = "规则名称")
	private java.lang.String ruleName;
	/**基准水价*/
    @ApiModelProperty(value = "基准水价")
	private java.lang.String benchPrice;
	/**规则类型  1-阶梯计价 2-手工计价*/
    @ApiModelProperty(value = "规则类型  1-阶梯计价 2-手工计价")
    @NotBlank(message="规则类型不能为空")
    @NotNull(message="规则类型不能为空")
	private java.lang.String ruleType;
	/**阶梯增幅*/
    @ApiModelProperty(value = "阶梯增幅")
	private java.lang.String setUp;
	/**阶梯时间*/
    @ApiModelProperty(value = "阶梯时间(间隔月份-整数)")
	private java.lang.String setTime;
	/**价格列表*/
    @ApiModelProperty(value = "价格列表")
	private List<ContractWaterRuleItem> items;
	
	
}
