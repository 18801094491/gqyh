package com.zcdy.dsc.waterwatch.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: Roberto
 * @CreateTime:2019年12月31日 下午5:00:05
 * @Description: <p>对应IOServer数据采集映射实体类</p>
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
