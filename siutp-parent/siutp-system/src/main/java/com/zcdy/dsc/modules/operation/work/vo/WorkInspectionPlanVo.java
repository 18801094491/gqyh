package com.zcdy.dsc.modules.operation.work.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 计划返回值
 *
 * @author songguang.jiao
 * @date 2020/7/7 15:12:52
 */
@Getter
@Setter
@ApiModel("WorkInspectionPlanVo")
public class WorkInspectionPlanVo {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 计划名称
     */
    @ApiModelProperty(value = "计划名称")
    private java.lang.String planName;
    /**
     * 路线id
     */
    @ApiModelProperty(value = "路线id")
    private java.lang.String routeId;
    /**
     * 路线id
     */
    @ApiModelProperty(value = "路线")
    private java.lang.String routeName;
    /**
     * 数据模板
     */
    @ApiModelProperty(value = "数据模板")
    private java.lang.String tplId;
    /**
     * 数据模板
     */
    @ApiModelProperty(value = "数据模板名称")
    private java.lang.String tplName;
    /**
     * 开始日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期")
    private java.util.Date startDate;
    /**
     * 截至日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "截至日期")
    private java.util.Date endDate;
    /**
     * 周期1-每天 ,2-每周  3-每月 4-单次
     */
    @ApiModelProperty(value = "周期 1-每天 ,2-每周(1-7)  3-每月(1-31) 4-单次")
    private java.lang.String period;
    /**
     * 周期-执行日期
     */
    @ApiModelProperty(value = "周期-执行日期")
    private java.lang.String periodExecuteDate;
    /**
     * 启停用状态
     */
    @ApiModelProperty(value = "启停用状态  1-启用 0-停用")
    private java.lang.String planStatus;
    /**
     * 启停用状态  1-启用 0-停用
     */
    @ApiModelProperty(value = "启停用状态  1-启用 0-停用")
    private java.lang.String planStatusName;

    /**
     * 巡检员id
     */
    @ApiModelProperty("巡检员id")
    private String usersId;

    /**
     * 用户名称
     */
    @ApiModelProperty("巡检员名称")
    private String usersName;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createByName;
}
