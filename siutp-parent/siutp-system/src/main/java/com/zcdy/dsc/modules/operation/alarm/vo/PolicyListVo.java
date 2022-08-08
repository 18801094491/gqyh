package com.zcdy.dsc.modules.operation.alarm.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 通知策略列表信息
 * @author：  songguang.jiao
 * 创建时间： 2020年3月5日 下午2:15:38 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="PolicyUsersVo",description="通知策略列表信息")
public class PolicyListVo {

	//id
	@ApiModelProperty(value="id")
	private String id;
	//策略名称
	@ApiModelProperty(value="策略名称")
	private String name;
	//告警等级
	@ApiModelProperty(value="告警等级")
	private String warnLevel;
	//启停用状态
	@ApiModelProperty(value="启停用状态")
	private String workStatus;
	//启停用状态
	@ApiModelProperty(value="启停用状态Code")
	private String workStatusCode;
	//用户信息列表
	@ApiModelProperty(value="用户信息列表")
	private List<PolicyUser> list;
	
	@Getter
	@Setter
	public static final class PolicyUser{
		
		//用户分类code
		@ApiModelProperty(value="用户分类code")
		private String userChooseTypeCode;
		
		//用户分类
		@ApiModelProperty(value="用户分类")
		private String userChooseType;
		
		//名称
		@ApiModelProperty(value="名称")
		private String dataName;
		
	}
	
}
