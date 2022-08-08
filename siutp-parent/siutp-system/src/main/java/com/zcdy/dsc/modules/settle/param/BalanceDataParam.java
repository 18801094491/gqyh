package com.zcdy.dsc.modules.settle.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: Roberto
 * 创建时间:2020年4月26日 下午2:38:10
 * 描述: <p>结算信息结果集请求参数</p>
 */
@Setter
@Getter
@ApiModel("com.zcdy.dsc.modules.settle.param.BalanceDataParam")
public class BalanceDataParam extends AbstractPageParam {

	@ApiModelProperty(value="客户名称", notes="input，支持模糊查询")
	private String customerName;
	
	@ApiModelProperty(value="合同编号", notes="input，精确查询")
	private String contractSn;
	
	@ApiModelProperty(value="水表id", notes="下拉选项")
	private String optId;
}
