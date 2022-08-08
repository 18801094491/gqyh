package com.zcdy.dsc.common.framework.influxdb.entity;

import lombok.Getter;
import lombok.Setter;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.io.Serializable;

/**
 * @author： Roberto
 * 创建时间：2020年3月12日 下午5:25:36
 * 描述: <p>时序数据-变量数据映射实体类</p>
 */
@Getter
@Setter
@Measurement(name="iot_point_data")
public class PointData implements Serializable, Comparable<PointData>, Cloneable{
	
    private static final long serialVersionUID = 1L;
    
    public static final PointData POINT_DATA = new PointData();
    
    /**
     * 时间序列
     */
    @Column(name = "time")
    private long time;
    
	/**
	 * 变量名称
	 */
    @Column(name = "var_name", tag=true)
    private  String varName;

    /**
     * 变量值
     */
    @Column(name = "var_value")
    private  Double  varValue;
    
    /**
     * 质量戳
     */
    @Column(name = "quality_stamp")
    private  Integer  qualityStamp;

	@Override
	public int compareTo(PointData tar) {
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
	public PointData clone() {
		PointData bean = null;
		try {
			bean =  (PointData) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return bean;
	}
}
