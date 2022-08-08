package com.zcdy.dsc.modules.settle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 区域配置
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-04-20
 * @Version: V1.0
 */
@Data
@TableName("settle_district")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "settle_district", description = "区域配置")
public class District {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 区域名称
     */
    @Excel(name = "区域名称", width = 15)
    @ApiModelProperty(value = "区域名称")
    private String districtName;
    /**
     * 级别
     */
    @Excel(name = "级别", width = 15)
    @ApiModelProperty(value = "级别")
    private String level;
    /**
     * 父节点id
     */
    @Excel(name = "父节点id", width = 15)
    @ApiModelProperty(value = "父节点id")
    private String parentId;
    /**
     * 父节点ids
     */
    @Excel(name = "父节点ids", width = 15)
    @ApiModelProperty(value = "父节点ids")
    private String parentIds;
    /**
     * 是否有子节点
     */
    @Excel(name = "是否有子节点", width = 15)
    @ApiModelProperty(value = "是否有子节点")
    private String hasChild;
    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新时间
     */
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date updateTime;
    /**
     * delFlag
     */
    @Excel(name = "delFlag", width = 15)
    @ApiModelProperty(value = "delFlag")
    private Integer delFlag;
}
