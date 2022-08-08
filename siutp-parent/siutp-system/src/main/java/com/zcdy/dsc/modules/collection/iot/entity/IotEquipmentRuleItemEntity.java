package com.zcdy.dsc.modules.collection.iot.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2020年3月6日 下午3:55:42
 * 描述: <p>采集设备数据规则具体的项目</p>
 */
@Setter
@Getter
public class IotEquipmentRuleItemEntity {

	//规则id
	private String ruleId;
	
	//规则内容
	private String alarmRule;
	
	//阈值 
	private String alarmValue;
	
	//采集变量id
	private String variableId;
	
	//变量名称
	private String variableName;
	
	//变量标题
	private String variableTitle;
	
	//变量单位
	private String variableUnit;
	
	//变量的数据类型
	private String dataType;
	
	//小数位控制
	private Integer scale;
}
