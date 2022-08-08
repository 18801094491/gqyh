package com.zcdy.dsc.modules.operation.alarm.vo;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 柱状图查询当前7天报警数据
 * @author：  songguang.jiao
 * 创建时间： 2020年2月28日 下午3:48:30 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="WeekBusinissData",description="柱状图查询当前7天报警数据")
public class WeekBusinissData {

	//日期,近7天日期
	private List<String> time;
	
	//报警数据
	private List<Map<String, Object>> dayDatas;
}
