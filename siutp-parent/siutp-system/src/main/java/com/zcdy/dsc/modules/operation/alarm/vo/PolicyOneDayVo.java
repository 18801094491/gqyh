package com.zcdy.dsc.modules.operation.alarm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 查询当天报警数据
 * @author：  songguang.jiao
 * 创建时间： 2020年3月8日 下午2:39:14 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="PolicyOneDayVo",description="查询当天报警数据")
public class PolicyOneDayVo {

	//告警类别
	@ApiModelProperty(value="告警类别")
	private String item;
	
	//统计数字
	@ApiModelProperty(value="统计数字")
	private int count;
	
}
