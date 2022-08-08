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
public class IotEquipconfigEntity {

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
	
	//是否校验质量戳
	private String checkQuality;
	
	//是否告警
	private String needAlarm;
	
	//告警等级
	private String qualityAlarmLevel;
	
	//告警内容模板
	private String qualityAlarmMessage;
}
