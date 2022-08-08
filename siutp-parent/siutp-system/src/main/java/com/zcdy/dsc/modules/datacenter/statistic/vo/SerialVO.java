package com.zcdy.dsc.modules.datacenter.statistic.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author： Roberto
 * 创建时间：2020年3月14日 下午1:34:46
 * 描述: <p>序列vo</p>
 */
@ApiModel("SerialVO")
@Setter
@Getter
@ToString
public class SerialVO {

	/**变量名称*/
    @ApiModelProperty(value = "变量名称")
	private String variableName;
	
    @ApiModelProperty(value = "序列名称")
	private String serialName;
}
