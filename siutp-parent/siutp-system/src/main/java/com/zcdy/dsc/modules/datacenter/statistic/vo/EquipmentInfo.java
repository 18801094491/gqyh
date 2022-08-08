package com.zcdy.dsc.modules.datacenter.statistic.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2019年12月25日 下午4:28:09
 * 描述: <p>Ioserver变量信息</p>
 */
@Setter
@Getter
public class EquipmentInfo {

	private String varTitle;

	private String united;
	
	private String varName;
	
	private Integer scale;
	
	private String variableType;

	private Integer displayOrder;
}
