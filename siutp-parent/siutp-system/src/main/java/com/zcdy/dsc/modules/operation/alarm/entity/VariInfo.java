package com.zcdy.dsc.modules.operation.alarm.entity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 变量信息说明
 * @author：  songguang.jiao
 * 创建时间： 2020年2月20日 下午4:25:53 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="VariInfo",description="所有变量信息")
public class VariInfo {

	//设备类型
	private String typeName;
	
	//设备编号
	private String modelName;
	
	//变量示意
	private String varTitle;

	//单位
	private String united;
	
	//变量名称	
	private String varName;
	
}
