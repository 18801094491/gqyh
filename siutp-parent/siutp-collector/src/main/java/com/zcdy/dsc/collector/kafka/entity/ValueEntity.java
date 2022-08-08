package com.zcdy.dsc.collector.kafka.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: Roberto
 * @CreateTime:2019年12月24日 下午12:34:55
 * @Description: <p>IOServer采集数据和模型信息 </p>
 */
@Setter
@Getter
public class ValueEntity implements Cloneable{
	
	public static final ValueEntity template = new ValueEntity();

	//采集的数据
	private String value;
	
	//时间戳
	private String timestamp;
	//IOServerid 
	private String ioserverId;
	//分组id
	private String groupId;
	//设备id
	private String equipmentId;
	//变量id
	private String variableId;
	//变量名称
	private String variableName;
	
	@Override
	public ValueEntity clone() {
		ValueEntity bean = null;
		try {
			bean =  (ValueEntity) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return bean;
	}
}
