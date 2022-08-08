package com.zcdy.dsc.modules.operation.alarm.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/***
 * 描述: 地图页面展示未处理数据
 * @author：  songguang.jiao
 * 创建时间： 2020年2月28日 下午2:18:55 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="BusinessUndealData",description="地图页面展示未处理数据")
public class BusinessUndealData {

	private String id;
	
	/**告警名称*/
    @ApiModelProperty(value = "告警名称")
	private java.lang.String warnName;
	
	/**告警时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "告警时间")
	private java.util.Date warnTime;

	/**告警名称*/
	@ApiModelProperty(value = "告警处理状态 0为初始化 1为未处理")
	private java.lang.String warnStatus ;
	
}
