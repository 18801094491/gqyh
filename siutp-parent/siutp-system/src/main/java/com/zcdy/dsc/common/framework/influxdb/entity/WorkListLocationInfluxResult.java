package com.zcdy.dsc.common.framework.influxdb.entity;

import lombok.Getter;
import lombok.Setter;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author： 在信汇通
 * 创建时间：2021年4月2日
 * 描述: <p>时序数据-工单组员定位数据</p>
 */
@Getter
@Setter
@Measurement(name="worklist_location")
public class WorkListLocationInfluxResult implements Serializable, Comparable<WorkListLocationInfluxResult>, Cloneable{
	
    private static final long serialVersionUID = 1L;
    
    public static final WorkListLocationInfluxResult LOCATION_DATA = new WorkListLocationInfluxResult();
	/**
	 * 组员id
	 */
	@Column(name = "user_name", tag = true)
	private  String  userName;

    /**
     * 时间序列
     */
    @Column(name = "time")
    private Instant time;

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
	public int compareTo(WorkListLocationInfluxResult tar) {
		return this.time.compareTo(tar.getTime());
	}
    
	@Override
	public WorkListLocationInfluxResult clone() {
		WorkListLocationInfluxResult bean = null;
		try {
			bean =  (WorkListLocationInfluxResult) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return bean;
	}
}
