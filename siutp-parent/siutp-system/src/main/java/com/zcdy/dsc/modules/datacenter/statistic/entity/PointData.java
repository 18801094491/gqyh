package com.zcdy.dsc.modules.datacenter.statistic.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @author： Roberto
 * 创建时间：2020年3月12日 下午5:25:36
 * 描述: <p>时序数据-变量数据映射实体类</p>
 */
@Measurement(name = "iot_point_data")
public class PointData implements Serializable, Comparable<PointData> {

	private static final long serialVersionUID = 1L;

	// 时间序列
	@Column(name = "time")
	private Instant time;

	// 变量名称
	@Column(name = "var_name", tag = true)
	private String varName;

	// 变量值
	@Column(name = "var_value")
	@Excel(width = 15, name = "变量值")
	private String varValue;

	@Excel(width = 20,name = "时间", format = "yyyy-MM-dd HH:mm:ss.SSS", orderNum="1")
	private Date dateTime;

	@Override
	public int compareTo(PointData tar) {
		return this.time.compareTo(tar.getTime());
	}

	public Instant getTime() {
		return time;
	}

	public void setTime(Instant time) {
		this.time = time;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public String getVarValue() {
		return varValue;
	}

	public void setVarValue(String varValue) {
		this.varValue = varValue;
	}

	public Date getDateTime() {
		this.dateTime = Date.from(this.time);
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}
