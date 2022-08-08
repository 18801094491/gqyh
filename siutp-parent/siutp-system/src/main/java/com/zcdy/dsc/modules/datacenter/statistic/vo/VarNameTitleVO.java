package com.zcdy.dsc.modules.datacenter.statistic.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author： Roberto
 * 创建时间：2020年3月15日 上午11:11:06
 * 描述: <p></p>
 */
@ApiModel("com.zcdy.dsc.modules.datacenter.statistic.vo.VarNameTitleVO")
@Setter
@Getter
@ToString
public class VarNameTitleVO {

	@ApiModelProperty(value="变量名称", notes="变量名称")
	private String variableName;
	
	@ApiModelProperty(value="变量标题", notes="变量标题")
	private String variableTitle;
}
