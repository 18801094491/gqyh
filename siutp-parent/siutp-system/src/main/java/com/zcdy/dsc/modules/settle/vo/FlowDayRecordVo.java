package com.zcdy.dsc.modules.settle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 每日用水量统计-表格数据
 * @author: songguang.jiao
 * 创建时间:  2020年5月6日 下午4:13:52
 * 版本: V1.0
 */
@Getter
@Setter
@ApiModel(value="FlowDayRecordVo")
public class FlowDayRecordVo {

	/**
	 * 统计日期
	 */
	@ApiModelProperty(value="统计日期")
	private String countDate;
	
	/**
	 * 河东水厂
	 */
	@ApiModelProperty(value="河东水厂")
	private String flowHd;

	/**
	 * 行政区
	 */
	@ApiModelProperty(value="行政区")
	private String flowXzq;
	
	/**
	 * 行政区入楼
	 */
	@ApiModelProperty(value="行政区入楼")
	private String flowXzrl;
	
	/**
	 * 行政区绿化
	 */
	@ApiModelProperty(value="行政区绿化")
	private String flowXzlh;
	
	/**
	 * 镜河补水
	 */
	@ApiModelProperty(value="镜河补水")
	private String flowFzg;
	
	/**
	 * 华电北燃
	 */
	@ApiModelProperty(value="华电北燃")
	private String flowHdbr;
	
}
