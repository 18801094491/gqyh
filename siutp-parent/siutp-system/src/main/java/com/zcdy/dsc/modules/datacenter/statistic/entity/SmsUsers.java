package com.zcdy.dsc.modules.datacenter.statistic.entity;

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
 * 描述: 短信发送用户
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-02
 * 版本号: V1.0
 */
@Data
@TableName("sms_users")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sms_users", description="短信发送用户")
public class SmsUsers {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	
	/**userId*/
	@Excel(name = "userId", width = 15)
    @ApiModelProperty(value = "userId")
	private java.lang.String userId;
	
	/**模块id*/
	@Excel(name = "模块id", width = 15)
    @ApiModelProperty(value = "模块id")
	private java.lang.String moduleId;
	
}
