package com.zcdy.dsc.common.framework.influxdb.entity;

import lombok.Getter;
import lombok.Setter;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.io.Serializable;

/**
 * @author： 在信汇通
 * 创建时间：2021年4月2日
 * 描述: <p>时序数据-工单组员定位数据</p>
 */
@Getter
@Setter
@Measurement(name="worklist_location")
public class WorkListLocationInflux implements Serializable, Comparable<WorkListLocationInflux>, Cloneable{
	
    private static final long serialVersionUID = 1L;
    
    public static final WorkListLocationInflux LOCATION_DATA = new WorkListLocationInflux();
	/**
	 * 组员id
	 */
	@Column(name = "user_name", tag = true)
	private  String  userName;

    /**
     * 时间序列
     */
    @Column(name = "time")
    private long time;

	/**
	 * 经度
	 */
	@Column(name = "lon")
	private  String  lon;
	/**
	 * 纬度
	 */
	@Column(name = "lat")
	private  String  lat;

	@Override
	public int compareTo(WorkListLocationInflux tar) {
		int result = 0;
		if(this.time>tar.getTime()){
			result = 1;
		}else if(this.time==tar.getTime()){
			result = 0;
		}else {
			result = -1;
		}
		return result;
	}
    
	@Override
	public WorkListLocationInflux clone() {
		WorkListLocationInflux bean = null;
		try {
			bean =  (WorkListLocationInflux) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return bean;
	}
}
