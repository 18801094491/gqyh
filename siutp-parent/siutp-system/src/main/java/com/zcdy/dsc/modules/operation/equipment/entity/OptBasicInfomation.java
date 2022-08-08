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

import javax.validation.constraints.NotBlank;

/**
 * @Description: 设备台账基本信息
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-05-28
 * @Version: V1.0
 */
@Data
@TableName("opt_basic_infomation")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="opt_basic_infomation", description="设备台账基本信息")
public class OptBasicInfomation {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**基础信息名称*/
	@NotBlank(message="基础信息名称不能为空")
    @ApiModelProperty(value = "基础信息名称")
	private java.lang.String baseName;
	/**设备类型*/
	@NotBlank(message="设备类型不能为空")
    @ApiModelProperty(value = "设备类型")
	private java.lang.String equipmentType;
	/**设备规格*/
	@NotBlank(message="设备型号不能为空")
    @ApiModelProperty(value = "设备型号")
	private java.lang.String equipmentModel;
	/**设备型号*/
	@NotBlank(message="设备规格不能为空")
    @ApiModelProperty(value = "设备规格")
	private java.lang.String equipmentSpecs;
	/**供应商*/
	@NotBlank(message="供应商不能为空")
    @ApiModelProperty(value = "供应商")
	private java.lang.String equipmentSupplier;
	/**创建人*/

    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
	private java.lang.String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**删除标识0-正常,1-已删除*/
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
    @TableLogic
	private java.lang.Integer delFlag;
}
