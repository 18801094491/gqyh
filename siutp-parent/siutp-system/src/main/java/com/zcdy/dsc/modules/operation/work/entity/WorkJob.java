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
 * @description: 工单
 * @author: songguang.jiao
 * @date: 2020-07-08
 */
@Data
@TableName("work_job")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "work_job", description = "工单")
public class WorkJob {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;

    /**
     * 计划id
     */
    @ApiModelProperty("计划id")
    private String planId;

    /**
     * 工单状态
     */
    private String workStatus;
    /**
     * 工单名称(计划名称+巡检日期)
     */
    @ApiModelProperty(value = "工单名称(计划名称+巡检日期)")
    private java.lang.String workName;
    /**
     * 工单编号
     */
    @ApiModelProperty(value = "工单编号")
    private java.lang.String workSn;
    /**
     * 数据模板
     */
    @ApiModelProperty(value = "数据模板")
    private java.lang.String tplId;
    /**
     * 路线名称
     */
    @ApiModelProperty(value = "路线名称")
    private java.lang.String routeName;
    /**
     * 路线注意事项
     */
    @ApiModelProperty(value = "路线注意事项")
    private java.lang.String routeAttention;
    /**
     * 路线备注
     */
    @ApiModelProperty(value = "路线备注")
    private java.lang.String routeRemark;
    /**
     * 地图画线
     */
    @ApiModelProperty(value = "地图画线")
    private java.lang.String routeMapLines;


    /**
     * 实际巡检日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "实际巡检日期")
    private java.util.Date inspectTime;
    /**
     * 工单完成时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "工单完成时间")
    private java.util.Date finishedTime;
    /**
     * 工单附件
     */
    @ApiModelProperty(value = "工单附件")
    private java.lang.String workFile;
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
    @TableLogic
    private java.lang.Integer delFlag;
}
