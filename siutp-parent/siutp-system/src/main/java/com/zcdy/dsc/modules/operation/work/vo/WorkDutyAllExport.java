package com.zcdy.dsc.modules.operation.work.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * 描述: 导出当月全部排班信息
 * @author：  songguang.jiao
 * 创建时间： 2020年2月13日 下午1:41:12 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="WorkDutyAllExport",description="导出当月全部排班信息")
public class WorkDutyAllExport {

	/**
	 * 排班日期
	 */
	@ApiModelProperty(value = "排班日期")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@Excel(name = "排班日期", width = 20, format = "yyyy-MM-dd")
	private Date dutyDay;

	/**
	 * 班次名称
	 */
	@Excel(name = "班次名称", width = 15)
	@ApiModelProperty(value = "班次名称")
	private String shiftsName;

	/**
	 * 班组名称
	 */
	@Excel(name = "班组名称", width = 15)
	@ApiModelProperty(value = "班组名称")
	private String teamName;
	
}
