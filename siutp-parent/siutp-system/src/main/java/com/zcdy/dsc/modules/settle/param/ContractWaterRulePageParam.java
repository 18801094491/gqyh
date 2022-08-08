package com.zcdy.dsc.modules.settle.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.zcdy.dsc.common.api.param.AbstractPageParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/***
 * 描述: 分页查询水价规则
 * @author: songguang.jiao
 * 创建时间:  2020年4月23日 上午10:57:12
 * 版本: V1.0
 */
@Getter
@Setter
@ApiModel(value="ContractWaterRulePageParam")
public class ContractWaterRulePageParam extends AbstractPageParam{

	@ApiModelProperty(value="规则名称")
	private String ruleName;
	
	@NotBlank(message="合同id不能为空")
	@NotNull(message="合同id不能为空")
	@ApiModelProperty(value="合同id")
	private String contractId;
	
}
