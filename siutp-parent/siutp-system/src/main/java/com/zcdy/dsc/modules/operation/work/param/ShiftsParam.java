package com.zcdy.dsc.modules.operation.work.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 班次入参
 * @author：  songguang.jiao
 * 创建时间：  2020年1月10日 下午4:30:12
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="ShiftsParam",description="班次入参")
public class ShiftsParam {
	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**班次名称*/
    @ApiModelProperty(value = "班次名称")
	private java.lang.String shiftsName;
	/**班次描述*/
    @ApiModelProperty(value = "班次描述")
	private java.lang.String shiftsDescribe;
	/**上班时间*/
    @ApiModelProperty(value = "上班时间")
	private String startTime;
	/**下班时间*/
    @ApiModelProperty(value = "下班时间")
	private String overTime;
	/**下班是否需要打卡,0-否，1-是*/
    @ApiModelProperty(value = "下班是否需要打卡,0-否，1-是")
	private java.lang.String offSign;
	/**上班时长，小时*/
    @ApiModelProperty(value = "上班时长，小时")
	private java.lang.String workHours;
	/**开始休息时间*/
    @ApiModelProperty(value = "开始休息时间")
	private String restStartTime;
	/**结束休息时间*/
    @ApiModelProperty(value = "结束休息时间")
	private String restOverTime;
    /**启停用状态*/
    @ApiModelProperty(value = "启停用状态")
	private java.lang.String shiftsStatus;
	
}
