package com.zcdy.dsc.modules.collection.iot.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2020年3月6日 下午3:35:26
 * 描述: <p>采集设备信息和采集设备信息的规则信息映射实体类</p>
 */
@Getter
@Setter
public class IotEquipRuleEntity {

	//采集设备id
	private String equipmentId;
	
	//设备编号
	private String equipmentSn;
	
	//设备名称
	private String equipmentName;
	
	//设备类型
	private String equipmentType;
	
	//设备位置
	private String equipmentLocation;
	
	//标段
	private String equipmentSection;
	
	//并或关系
	private String andOr;
	
	//规则id
	private String ruleId;
	
	//规则是否告警
	private String ruleAlarm;
	
	//告警级别
	private String alarmLevel;
	
	//告警名称
	private String alarmName;
	
	//告警阈值
	private String messageValue;
	
	//设备状态的定义
	private String statusType;
	
	//规则是否启用
	private String alarmStatus;
}
