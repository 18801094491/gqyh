package com.zcdy.dsc.modules.operation.alarm.entity;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: 报警信息通知用户
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-04
 * 版本号: V1.0
 */
@Data
@TableName("business_warn_policy_users")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="business_warn_policy_users", description="报警信息通知用户")
public class BusinessWarnPolicyUsers {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**策略id*/
	@Excel(name = "策略id", width = 15)
    @ApiModelProperty(value = "策略id")
	private java.lang.String policyId;
	/**用户选择类型*/
	@Excel(name = "用户选择类型", width = 15)
    @ApiModelProperty(value = "用户选择类型(0-用户,1-角色,2-组长)")
	private java.lang.String userChooseType;
	/**类型id,（用户id,角色id，组长id，职务id）*/
	@Excel(name = "类型id,（用户id,角色id，组长id）", width = 15)
    @ApiModelProperty(value = "（用户id,角色id，组长id）")
	private java.lang.String dataId;
}
