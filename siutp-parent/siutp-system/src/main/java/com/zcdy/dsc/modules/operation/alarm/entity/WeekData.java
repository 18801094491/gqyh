package com.zcdy.dsc.modules.operation.alarm.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 数据库近7天数据
 * @author：  songguang.jiao
 * 创建时间： 2020年2月28日 下午4:30:34 
 * 版本号: V1.0
 */
@Getter
@Setter
public class WeekData {

	//时间日期
	private String date;
	
	//总报警数
	private Integer sumNum;
	
	//未处理
	private Integer unDealNum;
	
	//已处理
	private Integer dealNum;
}