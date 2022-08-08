package com.zcdy.dsc.modules.operation.work.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.Date;

/**
 * 描述: 班组排班列表展示
 *
 * @author： songguang.jiao
 * 创建时间： 2020年2月12日 下午12:19:45
 * 版本号: V1.0
 */
@Setter
@Getter
@ApiModel(value = "WorkMyTeamDutyVo", description = "班组排班列表展示")
public class WorkMyTeamDutyVo {

    /**
     * 排班日期
     */
    @ApiModelProperty(value = "排班日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Excel(name = "排班日期", width = 20, format = "yyyy-MM-dd")
    private Date dutyDay;

    /**
     * 班组开始时间
     */
    @ApiModelProperty(value = "班组开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    @Excel(name = "班组开始时间", width = 20, format = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 班组结束时间
     */
    @ApiModelProperty(value = "班组结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "HH:mm")
    @Excel(name = "班组结束时间", width = 20, format = "yyyy-MM-dd")
    private Date overTime;

    /**
     * 班组名称
     */
    @ApiModelProperty(value = "班组名称")
    @Excel(name = "班组名称", width = 20)
    private String teamName;

    /**
     * 班次名称
     */
    @ApiModelProperty(value = "班次名称")
    @Excel(name = "班次名称", width = 20)
    private String shiftsName;

}
