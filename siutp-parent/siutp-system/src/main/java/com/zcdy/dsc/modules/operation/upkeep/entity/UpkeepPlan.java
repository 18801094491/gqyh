package com.zcdy.dsc.modules.operation.upkeep.entity;

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
 * 描述： 保养计划
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-06-04
 * 版本： V1.0
 */
@Data
@TableName("opt_upkeep_plan")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="opt_upkeep_plan", description="保养计划")
public class UpkeepPlan {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	
	/**设备id*/
    @ApiModelProperty(value = "设备id")
	private java.lang.String optId;
	
	/**上次计划日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "上次计划时间")
	private java.util.Date prevPlanDate;
	
	/**下次计划日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "下次计划时间")
	private java.util.Date nextPlanDate;
	
	/**保养次数*/
    @ApiModelProperty(value = "保养次数")
	private java.lang.Integer upkeepTimes;
	
	/**@author*/
    @ApiModelProperty(value = "@author", hidden=true)
	private java.lang.String createBy;
	
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", hidden=true)
	private java.util.Date createTime;
	
	/**修改人*/
    @ApiModelProperty(value = "修改人", hidden=true)
	private java.lang.String updateBy;
	
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间", hidden=true)
	private java.util.Date updateTime;
	
}
