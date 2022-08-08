package com.zcdy.dsc.modules.operation.equipment.entity;

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
 * 描述: 知识库管理
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
@Data
@TableName("opt_knowlege")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "opt_knowlege对象", description = "知识库管理")
public class Knowlege {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 知识内容
     */
    @ApiModelProperty(value = "知识内容")
    private java.lang.String knowlegeName;
    /**
     * 类型-维护分类字典
     */
    @ApiModelProperty(value = "类型-维护分类字典")
    private java.lang.String type;
    /**
     * 设备类型名称
     */
    @ApiModelProperty(value = "设备类型名称")
    private java.lang.String equipmentType;
    /**
     * 设备型号
     */
    @ApiModelProperty(value = "设备型号")
    private java.lang.String equipmentModel;
    /**
     * 设备规格
     */
    @ApiModelProperty(value = "设备规格")
    private java.lang.String equipmentSpecs;
    /**
     * 知识条目
     */
    @ApiModelProperty(value = "知识条目")
    private java.lang.Integer itemCount;
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
    /**
     * 所属资源
     */
    @ApiModelProperty(value = "所属资源")
    private String resource;
    /**
     * 供应商Id
     */
    @ApiModelProperty(value = "供应商Id")
    private String supplierId;
}
