package com.zcdy.dsc.modules.operation.work.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @description: 巡检路线
 * @author: songguang.jiao
 * @date: 2020-07-01
 */
@Data
@TableName("work_inspection_route")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "work_inspection_route", description = "巡检路线")
public class WorkInspectionRoute {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 路线名称
     */
    @ApiModelProperty(value = "路线名称")
    @NotBlank(message = "路线名称不能为空")
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
     * 地图画线
     */
    @ApiModelProperty(value = "地图画线")
    private java.lang.String mapLines;

    /**
     * 巡检点id
     */
    @ApiModelProperty("巡检点id(多个巡检点用逗号分割)")
    @TableField(exist = false)
    private String pointIds;
    /**
     * 创建人
     */
    @JsonIgnore
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonIgnore
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
    @ApiModelProperty(value = "修改时间")
    @JsonIgnore
    private java.util.Date updateTime;
    /**
     * 删除标识0-正常,1-已删除
     */
    @JsonIgnore
    @TableLogic
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
    private java.lang.Integer delFlag;
}
