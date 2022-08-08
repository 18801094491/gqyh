package com.zcdy.dsc.modules.datacenter.statistic.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


/**
 * 描述: 历史用水量产销差统计入参  
 * 	timeType如果为空时,时间区间type默认按照每日(4)展示
 * @author：  songguang.jiao
 * 创建时间：  2020年3月23日 下午5:35:16
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="MeterFlowHistoryVo",description="历史用水量产销差统计入参")
public class MeterFlowHistoryVo {

	//无参数查询方式(1-7天,2-本月，3-本年)
	private String queryType;
	
	//时间区间查询类型
	@ApiModelProperty(value="时间区间查询类型(1-周,2-月,3-年,4-日)")
	private String periodTime;
	
	//开始时间
//	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
//    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value="开始时间")
	private String startTime;

	//结束时间
//	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
//    @DateTimeFormat(pattern="yyyy-MM-dd")
//	@ApiModelProperty(value="结束时间")
	private String endTime;
}
