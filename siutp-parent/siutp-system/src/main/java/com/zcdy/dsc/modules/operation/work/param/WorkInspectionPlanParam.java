package com.zcdy.dsc.modules.operation.work.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @description: 巡检计划入参
 * @author: songguang.jiao
 * @date: 2020-07-06
 */
@ApiModel("WorkInspectionPlanParam")
@Getter
@Setter
public class WorkInspectionPlanParam{

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 计划名称
     */
    @NotBlank(message = "计划名称不能为空")
    @ApiModelProperty(value = "计划名称")
    private String planName;
    /**
     * 路线id
     */
    @NotBlank(message = "路线不能为空")
    @ApiModelProperty(value = "路线id")
    private String routeId;
    /**
     * 数据模板
     */
    @ApiModelProperty(value = "数据模板")
    private String tplId;
    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期")
    private java.util.Date startDate;
    /**
     * 截至日期
     */
    @NotNull(message = "截至日期不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "截至日期")
    private java.util.Date endDate;
    /**
     * 周期1-每天 ,2-每周  3-每月 4-单次
     */
    @ApiModelProperty(value = "周期1-每天 ,2-每周(1-7)  3-每月(1-31) 4-单次")
    @NotBlank(message = "周期不能为空")
    private String period;
    /**
     * 周期-执行选项
     */
    @ApiModelProperty(value = "周期-执行选项(1-7 || 1-31号)")
    private String periodExecuteDate;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @NotBlank(message = "用户不能为空")
    private String usersId;

    /**
     * 参数值
     */
    @ApiModelProperty("参数值(key,value),数据项id为key,同时传入值")
    private Map<String, String> mapData;
}
