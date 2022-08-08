package com.zcdy.dsc.modules.datacenter.statistic.entity;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述:用水量、流量计统计
 * @author：  songguang.jiao
 * 创建时间：  2020年3月24日 下午3:30:48
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="MeterFlowDiffer")
public class MeterFlowCount {

	//统计日期(日,月,年)
	private String countDate;
	
	//统计类型
	private String equipType;
	
	//统计值
	private BigDecimal countValue;
}
