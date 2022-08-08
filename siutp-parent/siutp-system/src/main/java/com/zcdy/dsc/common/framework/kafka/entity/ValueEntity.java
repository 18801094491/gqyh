package com.zcdy.dsc.common.framework.kafka.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2019年12月24日 下午12:34:55
 * 描述: <p>IOServer采集数据和模型信息 </p>
 */
@Setter
@Getter
public class ValueEntity implements Cloneable{
	
	public static final ValueEntity TEMPLATE = new ValueEntity();

	public static final ValueEntity getEntity() {
	    return TEMPLATE.clone();
	}
	
	//采集的数据
	private String value;
	
	//时间戳
	private String timestamp;
	//变量id
	private String variableId;
	//变量名称
	private String variableName;
	
	//质量戳
	private String qualityStamp;
	
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
