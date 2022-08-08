package com.zcdy.dsc.modules.datacenter.statistic.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author： Roberto
 * 创建时间：2020年3月20日 下午12:13:22
 * 描述: <p>平均值分析单项记录</p>
 */
@Setter
@Getter
@ToString
public class EquipmentVariableVO {

	private String variableName;
	private String variableTitle;
	private String variableUnit;
	private String variableType;
	private Integer scale;
	private String equipmentLocation;
	private String section;
	private String equipmentType;
	private String equipmentId;
	
	private String tableName;
	private String tableColumn;
}
