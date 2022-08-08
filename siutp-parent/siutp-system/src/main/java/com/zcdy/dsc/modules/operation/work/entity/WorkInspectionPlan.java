package com.zcdy.dsc.modules.operation.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @description: 巡检计划
 * @author: songguang.jiao
 * @date: 2020-07-06
 */
@Data
@TableName("work_inspection_plan")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "work_inspection_plan", description = "巡检计划")
public class WorkInspectionPlan {

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
     * 数据模板
     */
    @ApiModelProperty(value = "数据模板")
    private java.lang.String tplId;
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
    @ApiModelProperty(value = "周期1-每天 ,2-每周(1-7)  3-每月(1-31) 4-单次")
    private java.lang.String period;
    /**
     * 周期-执行日期
     */
    @ApiModelProperty(value = "周期-执行日期")
    private java.lang.String periodExecuteDate;
    /**
     * 启停用状态  1-启用 0-停用
     */
    @ApiModelProperty(value = "启停用状态  1-启用 0-停用")
    private java.lang.String planStatus;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;

    /**
     * 删除标识0-正常,1-已删除
     */
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
    @TableLogic
    private java.lang.Integer delFlag;
}
