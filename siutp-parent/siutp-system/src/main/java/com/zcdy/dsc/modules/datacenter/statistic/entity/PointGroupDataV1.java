package com.zcdy.dsc.modules.datacenter.statistic.entity;

import java.io.Serializable;
import java.util.Date;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2020年3月12日 下午5:25:36
 * 描述: <p>时序数据-变量数据映射实体类</p>
 */
@ApiModel("com.zcdy.dsc.modules.datacenter.statistic.entity.PointGroupDataV1")
@Getter
@Setter
@Measurement(name="iot_point_data")
public class PointGroupDataV1 implements Serializable, Comparable<PointGroupDataV1>{
	
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="统计项目", notes="统计项目")
    @Excel(name = "统计项目", width = 15)
    private String variableTitle;
   
    @ApiModelProperty(value="设备类型名称", notes="设备类型名称，直接使用")
    @Excel(name = "设备类型", width = 15)
    private String equipmentType;
    
    @ApiModelProperty(value="设备标段", notes="设备标段")
    @Excel(name = "标段", width = 15)
    private String equipmentSection;
    
    @ApiModelProperty(value="设备位置", notes="设备位置")
    @Excel(name = "所在位置", width = 15)
    private String equipmentLocation;

    //显示顺序，升序
    @ApiModelProperty(value="显示顺序",notes="显示顺序")
    private int displayOrder=0;
    
    @ApiModelProperty(value="平均值", notes="平均值，直接使用")
    @Column(name = "avgValue")
    @Excel(name = "平均值", width = 15)
    private  String  avgValue;
    
    @ApiModelProperty(value="最大值", notes="最大值，直接使用")
    @Column(name = "maxValue")
    @Excel(name = "峰值", width = 15)
    private  String  maxValue;
    
    @ApiModelProperty(value="峰值发生时间", notes="峰值发生时间")
    @Excel(name = "峰值发生时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date  maxTime;
    
    @ApiModelProperty(value="最小值", notes="最小值，直接使用")
    @Column(name = "minValue")
    @Excel(name = "谷值", width = 15)
    private  String  minValue;
    
    @ApiModelProperty(value="谷值发生时间", notes="谷值发生时间")
    @Excel(name = "谷值发生时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private  Date  minTime;
    
    //变量名称
    @ApiModelProperty(hidden=true)
    @Column(name = "var_name", tag=true)
    private  String varName;

	/*
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PointGroupDataV1 o) {
		return this.displayOrder-o.displayOrder;
	}
}
