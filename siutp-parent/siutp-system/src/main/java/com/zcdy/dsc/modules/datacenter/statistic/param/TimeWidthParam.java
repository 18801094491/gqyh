package com.zcdy.dsc.modules.datacenter.statistic.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2020年3月15日 下午11:30:06
 * 描述: <p>时间区间参数，避免转化问题</p>
 */
@Getter
@Setter
@ApiModel("TimeWidthParam")
public class TimeWidthParam {

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	@ApiModelProperty(value="开始时间", notes="格式：2020-03-15 23:32")
	private Date left;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	@ApiModelProperty(value="结束时间", notes="格式：2020-03-15 23:32")
	private Date right;
}
