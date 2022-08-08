package com.zcdy.dsc.modules.operation.work.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 描述: 班组排班列表展示
 * @author： songguang.jiao
 * 创建时间： 2020年2月12日 下午12:19:45
 * 版本号: V1.0
 */
@Setter
@Getter
@ApiModel(value = "WorkTeamDutyVo", description = "班组排班列表展示")
public class WorkTeamDutyVo {

	/**
	 * 排班日期
	 */
	@ApiModelProperty(value = "排班日期")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date dutyDay;

	/**
	 * 每日排班集合
	 */
	List<ShiftsTeamInfo> list;

	@Getter
	@Setter
	public static class ShiftsTeamInfo {


		/**
		 * 班次名称
		 */
		@ApiModelProperty(value = "班次名称")
		private String shiftsName;

		/**
		 * 班组名称
		 */
		@ApiModelProperty(value = "班组名称")
		private String teamName;
	}

}
