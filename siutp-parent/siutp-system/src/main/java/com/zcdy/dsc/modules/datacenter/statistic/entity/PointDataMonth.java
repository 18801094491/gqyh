package com.zcdy.dsc.modules.datacenter.statistic.entity;

import java.util.Date;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name = "iot_point_data_month")
public class PointDataMonth  {


	// 时间序列
	@Column(name = "time")
	private Long time;

	// 变量名称
	@Column(name = "var_name", tag = true)
	private String varName;

	// 变量值
	@Column(name = "min_value")
	private Double minValue;
	// 变量值
	@Column(name = "max_value")
	private Double maxValue;
	// 变量值
	@Column(name = "avg_value")
	private Double avgValue;

	private Date dateTime;

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public Date getDateTime() {
		this.dateTime = new Date(this.time);
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Double getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(Double avgValue) {
		this.avgValue = avgValue;
	}


}
