package com.zcdy.dsc.collector.ioserver.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: Roberto
 * @CreateTime:2019年12月31日 下午5:00:05
 * @Description: <p></p>
 */
@Setter
@Getter
public class VariableData implements Cloneable{

	public final static VariableData template = new VariableData();
	
	private Integer varId;
	
	private String varName;
	
	private long timestamp;
	
	private long created;
	
	private String varValue;
	
	//质量戳
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
