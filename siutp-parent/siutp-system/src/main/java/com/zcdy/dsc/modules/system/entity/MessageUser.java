package com.zcdy.dsc.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述： 保养计划
 * @author : songguang.jiao
 * 创建时间：   2020-06-04
 * 版本： V1.0
 */
@Data
@TableName("system_message_user")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="system_message_user", description="保养计划")
public class MessageUser {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	
	/**消息模板id*/
    @ApiModelProperty(value = "消息模板id")
	private java.lang.String smsTemplate;
	
	/**userId*/
    @ApiModelProperty(value = "userId")
	private java.lang.String userId;
	
	/**userId*/
	@ApiModelProperty(value = "功能编码")
	private java.lang.String functionCode;
	
}
