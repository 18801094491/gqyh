package com.zcdy.dsc.modules.settle.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 描述: 单个水表抄表详情分页入参
 * @author : songguang.jiao
 * 创建时间:  2020年4月20日 下午2:08:08
 * 版本: V1.0
 */
@Getter
@Setter
@ApiModel(value="MeterMonthOnePageParam")
public class MeterMonthOnePageParam extends AbstractPageParam{
	
	//开始时间
	@ApiModelProperty(value="开始时间(yyyy-MM-dd)")
	private String startTime;
	
	//截至时间
	@ApiModelProperty(value="截至时间(yyyy-MM-dd)")
	private String endTime;
	
	//设备id
	@NotBlank(message="设备id不能为空")
	@NotNull(message="设备id不能为空")
	@ApiModelProperty(value="设备id")
	private String equipmentId;
}
