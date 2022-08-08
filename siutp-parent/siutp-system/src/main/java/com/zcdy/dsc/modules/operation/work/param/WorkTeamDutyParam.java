package com.zcdy.dsc.modules.operation.work.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 描述: 班组排版入参
 * @author：  songguang.jiao
 * 创建时间： 2020年2月12日 上午11:11:09 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="WorkTeamDutyParam",description=" 班组排版入参")
public class WorkTeamDutyParam {

	/**
	 * 排班起止日期,格式(yyyy-MM-dd,yyyy-MM-dd)
	 */
	@NotNull(message="排班日期不能为空")
	@NotEmpty(message="排班日期不能为空")
	@ApiModelProperty(value="排班起止日期,格式(yyyy-MM-dd,yyyy-MM-dd)")
	private	String dateString;

	/**
	 * 班次id
	 */
	@NotNull(message="班次不能为空")
	@NotEmpty(message="班次不能为空")
	@ApiModelProperty(value="班次id")
	private String shfitsId;

	/**
	 * 班组id
	 */
	@NotNull(message="班组不能为空")
	@NotEmpty(message="班组不能为空")
	@ApiModelProperty(value="班组id")
	private String teamId;
	
}
