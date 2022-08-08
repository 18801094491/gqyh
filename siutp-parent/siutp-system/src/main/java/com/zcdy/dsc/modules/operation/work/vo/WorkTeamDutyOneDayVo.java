package com.zcdy.dsc.modules.operation.work.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 当日排班详情
 *
 * @author： songguang.jiao
 * 创建时间： 2020年2月12日 下午8:07:17
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "ShiftsTeamDutyVo", description = "当日排班详情")
public class WorkTeamDutyOneDayVo {

    /**
     * id
     */
    @ApiModelProperty(value = "排班id")
    private String id;

    /**
     * 班组名称
     */
    @ApiModelProperty(value = "班组名称")
    private String teamName;

    /**
     * 班次名称
     */
    @ApiModelProperty(value = "班次名称")
    private String shiftsName;

}
