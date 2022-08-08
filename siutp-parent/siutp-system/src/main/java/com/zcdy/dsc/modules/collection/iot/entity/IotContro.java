package com.zcdy.dsc.modules.collection.iot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 描述: 反向控制
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-06
 * 版本号: V1.0
 */
@Data
@TableName("iot_contro")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "iot_contro", description = "反向控制")
public class IotContro {

    /**
     * id
     */
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private java.lang.String equipmentId;
    /**
     * 控制名称
     */
    @ApiModelProperty(value = "控制名称")
    private java.lang.String controName;
    /**
     * 变量id
     */
    @ApiModelProperty(value = "变量id")
    @NotBlank(message = "变量id不能为空")
    private java.lang.String variableId;

    /**
     * 变量值
     */
    @ApiModelProperty(value = "变量值")
    @NotBlank(message = "变量值不能为空")
    private java.lang.String variableValue;
    /**
     * 编辑时间
     */
    @ApiModelProperty(value = "编辑时间")
    private java.lang.String createTime;
}
