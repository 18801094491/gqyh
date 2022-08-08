package com.zcdy.dsc.modules.collection.gis.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 监控中心展示采集设备流量统计
 * @author：  songguang.jiao
 * 创建时间：  2020年3月22日 下午5:12:36
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="采集设备流量统计",description="采集设备流量统计")
public class IotEuipFlowVo {

	//流量计累计统计值
	@ApiModelProperty(value="流量计累计统计值")
	private String meterFlowTotal;
	
	//流量计累计统计值
	@ApiModelProperty(value="流量计累计统计值")
	private String meterFlowTotalFormat;
	
	//水表累计流量统计值
	@ApiModelProperty(value="水表累计流量统计值")
	private String waterMeterFlowTotal;
	
	//水表累计流量统计值
	@ApiModelProperty(value="水表累计流量统计值")
	private String waterMeterFlowTotalFormat;
	
	@ApiModelProperty(value="产销差")
	private String subtract;
	
	@ApiModelProperty(value="产销差")
	private String subtractFormat;
	//所有流量计统计
	@ApiModelProperty(value="所有流量计统计")
	private List<MeterFlowData> meterFlowDatas;
	
	@Getter
	@Setter
	@ApiModel(value="流量计统计",description="流量计统计")
	public static final  class MeterFlowData{
		@ApiModelProperty(value="采集设备名称")
		private String equipName;
		
		@ApiModelProperty(value="采集值统计")
		private String value;
		
		@ApiModelProperty(value="采集值统计")
		private String valueFormat;
	}
	
}
