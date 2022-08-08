package com.zcdy.dsc.modules.operation.work.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @description: 工单数据项子项
 * @author: 智能无人硬核心项目组
 * @date: 2020-06-24
 * @version: V1.0
 */
@Data
@TableName("work_job_datasource_item")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "work_job_datasource_item", description = "工单数据项子项")
public class WorkJobDatasourceItem {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 数据项id
     */
    @ApiModelProperty(value = "数据项id")
    private java.lang.String datasourceId;
    /**
     * 选项名称
     */
    @ApiModelProperty(value = "选项名称")
    private java.lang.String itemName;
    /**
     * 选项排序
     */
    @ApiModelProperty(value = "选项排序")
    private java.lang.String itemOrder;
    /**
     * 创建人，派单人
     */
    @JsonIgnore
    @ApiModelProperty(value = "创建人，派单人")
    private java.lang.String createBy;
    /**
     * 创建时间
     */
    @JsonIgnore
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 修改人
     */
    @JsonIgnore
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
    /**
     * 修改时间
     */
    @JsonIgnore
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
    /**
     * 删除标识0-正常,1-已删除
     */
    @JsonIgnore
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
    @TableLogic
    private java.lang.Integer delFlag;

    /**
     * 存储值
     */
    @TableField(exist = false)
    @ApiModelProperty("存储值")
    private String itemValue;
}
