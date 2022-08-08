package com.zcdy.dsc.modules.datacenter.statistic.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 描述: 历史数据通过时间维度统计
 * @author : songguang.jiao
 * 创建时间:  2020年4月17日 下午6:10:56
 * 版本: V1.0
 */
@Getter
@Setter
@ApiModel("AvgDataParam")
public class TimeDimensionParam {

	@ApiModelProperty(value="资产设备id", notes="资产设备id", required=true)
	private String equipmentId;
		
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	@ApiModelProperty(value="开始时间", notes="格式：2020-03-15 23:32", required=false)
	private Date left;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	@ApiModelProperty(value="结束时间", notes="格式：2020-03-15 23:32", required=false)
	private Date right;
	
	@ApiModelProperty(value="页码", notes="页码")
	private Integer pageNo;
	
	@ApiModelProperty(value="每页的容量", notes="每页的容量")
	private Integer pageSize;
	
	@ApiModelProperty(value="时间戳", notes="连接20s有效")
	private Long timestamp;
	
	@ApiModelProperty(value="token", notes="token字符串")
	private String token;
	
	@ApiModelProperty(value="时间类型", notes="时间类型")
	private String timeType;
}
