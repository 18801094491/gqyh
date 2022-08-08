package com.zcdy.dsc.modules.operation.alarm.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 告警通知策略所有用户id,将字符串拼接为数组
 * @author：  songguang.jiao
 * 创建时间： 2020年3月6日 下午7:35:12 
 * 版本号: V1.0
 */
@Getter
@Setter
public class WarnUsersId {
	
	//用户id
	private String usersId;
	
	//角色下所有用户id
	private String roleUsersId;
	
	//班组下所有用户id
	private String teamUsersId;
}
