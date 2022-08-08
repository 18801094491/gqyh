package com.zcdy.dsc.modules.operation.work.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @description: 巡检点
 * @author: songguang.jiao
 * @date: 2020-07-01
 */
@Data
@TableName("work_inspection_point")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "work_inspection_point", description = "巡检点")
public class WorkInspectionPoint {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 巡检点名称
     */
    @ApiModelProperty(value = "巡检点名称")
    @NotBlank(message = "巡检点名称不能为空")
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
    @NotBlank(message = "数据模板不能为空")
    private java.lang.String tplId;
    /**
     * 类别
     */
    @ApiModelProperty(value = "类别")
    @NotBlank(message = "数据类别不能为空")
    private java.lang.String category;
    /**
     * 对应数据id(告警id,设备id,问题id,点位为空)
     */
    @ApiModelProperty(value = "对应数据id(告警id,设备id,问题id,点位为空)")
    private java.lang.String dataId;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private java.lang.String equipmentId;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    @NotBlank(message = "经度不能为空")
    private java.lang.String longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    @NotBlank(message = "纬度不能为空")
    private java.lang.String latitude;
    /**
     * 创建人
     */
    @JsonIgnore
    @ApiModelProperty(value = "创建人")
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
    @TableLogic
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
    private java.lang.Integer delFlag;
}
