package com.zcdy.dsc.modules.datacenter.statistic.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2020年3月15日 上午11:44:04
 * 描述: <p></p>
 */
@Setter
@Getter
public class ChartSerial {

	private String id;
	
	private String statisticName;
	
	private String chartType;
	
	private String statisticCycle;
	
	private String statisticCycleType;
	
	private String variableName;
	
	private String serialName;
	
	private String equipmentSn;
	
	private String equipmentSection;
	
	private String equipmentType;
	
	private String equipmentLocation;

	private String equipmentName;

}
