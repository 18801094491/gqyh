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

import javax.validation.constraints.NotEmpty;

/**
 * @Description: 备品备件基本信息
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-06-02
 * @Version: V1.0
 */
@Data
@TableName("opt_spareparts")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "opt_spareparts", description = "备品备件基本信息")
public class OptSpareparts {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 分类名称
     */
    @NotEmpty(message = "分类名称不能为空")
    @ApiModelProperty(value = "分类名称")
    private java.lang.String categoryName;
    /**
     * 备件名称
     */
    @NotEmpty(message = "备件名称不能为空")
    @ApiModelProperty(value = "备件名称")
    private java.lang.String sparepartsName;
    /**
     * 备件规格
     */
    @NotEmpty(message = "备件规格不能为空")
    @ApiModelProperty(value = "备件规格")
    private java.lang.String sparepartsModel;
    /**
     * 备件型号
     */
    @NotEmpty(message = "备件型号不能为空")
    @ApiModelProperty(value = "备件型号")
    private java.lang.String sparepartsSpces;
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
