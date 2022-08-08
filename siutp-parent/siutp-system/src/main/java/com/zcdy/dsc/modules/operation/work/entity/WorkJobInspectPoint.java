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
 * @description: 工单巡检点
 * @author: songguang.jiao
 * @date: 2020-07-08
 */
@Data
@TableName("work_job_inspect_point")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "work_job_inspect_point", description = "工单巡检点")
public class WorkJobInspectPoint {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;

    /**
     * 工单id
     */
    @ApiModelProperty("工单id")
    private String workJobId;
    /**
     * 巡检点名称
     */
    @ApiModelProperty(value = "巡检点名称")
    private java.lang.String name;
    /**
     * 注意事项
     */
    @ApiModelProperty(value = "注意事项")
    private java.lang.String attention;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    /**
     * 数据模板
     */
    @ApiModelProperty(value = "数据模板")
    private java.lang.String tplId;
    /**
     * 类别 1-设备 2-告警 3-问题上报 4-点位
     */
    @ApiModelProperty(value = "类别 1-设备 2-告警 3-问题上报 4-点位")
    private java.lang.String category;
    /**
     * 对应数据id(告警id,设备id,问题id,点位为空)
     */
    @ApiModelProperty(value = "对应数据id(告警id,设备id,问题id,点位为空)")
    private java.lang.String dataId;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;

    /**
     * 填报状态 1-已填报 0-未填报(默认未填报)
     */
    @ApiModelProperty("填报状态 1-已填报 0-未填报(默认未填报)")
   private Short recordStatus;
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
