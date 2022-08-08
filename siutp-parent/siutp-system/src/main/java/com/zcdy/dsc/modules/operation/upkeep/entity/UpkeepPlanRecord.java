package com.zcdy.dsc.modules.operation.upkeep.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 描述： 保养计划
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-06-04
 * 版本： V1.0
 */
@Data
@TableName("opt_upkeep_plan_record")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "opt_upkeep_plan_record", description = "保养计划")
public class UpkeepPlanRecord {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;

    /**
     * 计划id
     */
    @ApiModelProperty(value = "计划id")
    private java.lang.String planId;

    /**
     * 计划日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "计划日期")
    private java.util.Date planDate;

    /**
     * 实际执行时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "实际执行时间")
    private java.util.Date executeTime;

    /**
     * 当前计划是否有效,1-是,0-否  派工后置为无效
     */
    @ApiModelProperty(value = "当前计划是否有效,1-是,0-否")
    private Short current;

    /**
     * 是否当前派工
     */
    @ApiModelProperty(value = "是否当前派工")
    private Short dispatchStatus;
}
