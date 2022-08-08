package com.zcdy.dsc.modules.operation.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author : 智能无人硬核心项目组
 * @version V1.0
 * @description: 问题上报管理
 * @date 2020-06-05
 */
@Data
@TableName("work_problem_report")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "work_problem_report", description = "问题上报管理")
public class WorkProblemReport {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 问题编号
     */
    @ApiModelProperty(value = "问题编号")
    private java.lang.String problemSn;
    /**
     * 问题名称
     */
    @Excel(name = "问题名称", width = 15)
    @ApiModelProperty(value = "问题名称")
    private java.lang.String problemName;
    /**
     * 问题类型
     */
    @ApiModelProperty(value = "问题类型")
    private java.lang.String problemType;
    /**
     * 问题设备信息
     */
    @ApiModelProperty(value = "问题设备信息")
    private java.lang.String equipmentId;
    /**
     * 问题状态
     */
    @ApiModelProperty(value = "问题状态")
    private java.lang.String problemStatus;

    /**
     * 问题描述
     */
    @ApiModelProperty(value = "问题描述")
    private String problemDescription;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
    /**
     * 删除标识0-正常,1-已删除
     */
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
    private java.lang.Integer delFlag;
}
