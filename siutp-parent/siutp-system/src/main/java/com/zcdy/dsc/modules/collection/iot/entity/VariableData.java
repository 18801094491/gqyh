package com.zcdy.dsc.modules.collection.iot.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2019年12月31日 下午5:00:05
 * 描述: <p></p>
 */
@Setter
@Getter
public class VariableData implements Cloneable{

	public final static VariableData VARIABLE_DATA = new VariableData();
	
	private String varId;
	
	private String varName;
	
	private long timestamp;
	
	private long created;
	
	private String varValue;
	
	private int qualityStamp;
	
	@Override
	public VariableData clone() {
		VariableData bean = null;
		try {
			bean =  (VariableData) super.clone();
		} catch (CloneNotSupportedException e) {
			bean = new VariableData();
			e.printStackTrace();
		}
		return bean;
	}
}
