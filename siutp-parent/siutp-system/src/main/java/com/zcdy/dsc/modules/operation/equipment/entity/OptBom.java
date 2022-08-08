package com.zcdy.dsc.modules.operation.equipment.entity;

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

import javax.validation.constraints.NotEmpty;

/**
 * @description: bom清单
 * @author: 智能无人硬核心项目组
 * @date: 2020-06-02
 * @version: V1.0
 */
@Data
@TableName("opt_bom")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "opt_bom", description = "bom清单")
public class OptBom {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 基础信息id
     */
    @NotEmpty(message = "基础信息id不能为空")
    @ApiModelProperty(value = "基础信息id")
    private java.lang.String basicId;
    /**
     * bom类型
     */
    @NotEmpty(message = "bom类型不能为空")
    @ApiModelProperty(value = "bom类型")
    private java.lang.String bomType;
    /**
     * 备件id
     */
    @NotEmpty(message = "备件id不能为空")
    @ApiModelProperty(value = "备件id")
    private java.lang.String sparepartsId;
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
