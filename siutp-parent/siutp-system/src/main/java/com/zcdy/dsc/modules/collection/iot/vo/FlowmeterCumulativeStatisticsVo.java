package com.zcdy.dsc.modules.collection.iot.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zcdy.dsc.modules.collection.iot.constant.FlowmeterCumulativeStatisticsEnum;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointData;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

/**
 * @author 唐超 流量累计统计
 */
@Data
public class FlowmeterCumulativeStatisticsVo {

	@Excel(name = "序号", width = 10, orderNum = "10")
	private String serialNo;
	private String optEquipmentId;
	private String equipmentName;
	// 列名称
	private String columnName;
	// 变量名称
	@JsonIgnore
	private String[] variableName;
	@JsonIgnore
	private String[] variableType;
	@JsonIgnore
	private String[] variableTitle;
	@Excel(name = "时间", width = 41, orderNum = "20")
	// 统计时间
	private String statisticsTime;
	// 累计流量
	@Excel(name = "累计流量[m³]", width = 15, orderNum = "30")
	private String totalFlow;
	// 正累计流量
	@Excel(name = "正累计流量[m³]", width = 15, orderNum = "40")
	private String positiveTotalFlow;
	// 瞬时流量
	@Excel(name = "流量计瞬时流量[m³/h]", width = 21, orderNum = "50")
	private String instantaneousFlow;
	// 流速
	@Excel(name = "流量计流速[m/s]", width = 17, orderNum = "60")
	private String flowRate;
	// 负累计流量
	@Excel(name = "负累计流量[m³]", width = 15, orderNum = "70")
	private String negativeTotalFlow;
	// 液位
	private String waterLevel;

	public FlowmeterCumulativeStatisticsVo() {

	}

	public FlowmeterCumulativeStatisticsVo(List<PointData> pointDataList) {
		this(null, pointDataList);
	}

	public FlowmeterCumulativeStatisticsVo(FlowmeterCumulativeStatisticsVo fcsv) {
		this(fcsv, null);
	}

	public FlowmeterCumulativeStatisticsVo(FlowmeterCumulativeStatisticsVo fcsv, List<PointData> pointDataList) {
		if (fcsv != null) {
			this.optEquipmentId = fcsv.getOptEquipmentId();
			this.equipmentName = fcsv.getEquipmentName();
			this.variableType = fcsv.getVariableType();
			this.variableName = fcsv.getVariableName();
			this.variableTitle = fcsv.getVariableTitle();
			this.assValueByPointData(pointDataList);
		}
	}

	public void assValueByPointData(List<PointData> pointDataList) {
		if (pointDataList == null || pointDataList.size() == 0) {
			return;
		}
		if (variableType == null || variableType.length == 0) {
			return;
		}
		for (int i = 0; i < variableType.length; i++) {
			String type = variableType[i];
			for (PointData p : pointDataList) {
				if (variableName[i].equals(p.getVarName())) {
					FlowmeterCumulativeStatisticsEnum.valueOf(type.toUpperCase()).setValue(this, p.getVarValue());
				}
			}
		}
	}

	public void setVariableName(String variableName) {
		if (StringUtils.isBlank(variableName)) {
			this.variableName = new String[] {};
			return;
		}
		this.variableName = variableName.split(",");
	}

	public void setVariableType(String variableType) {
		if (StringUtils.isBlank(variableType)) {
			this.variableType = new String[] {};
			return;
		}
		this.variableType = variableType.split(",");
	}

	public void setVariableTitle(String variableTitle) {
		if (StringUtils.isBlank(variableTitle)) {
			this.variableTitle = new String[] {};
			return;
		}
		this.variableTitle = variableTitle.split(",");
	}

	public void setTotalFlow(String totalFlow) {
		this.totalFlow = totalFlow;
	}

	public void setPositiveTotalFlow(String positiveTotalFlow) {
		this.positiveTotalFlow = positiveTotalFlow;
	}

	public void setFlowRate(String flowRate) {
		this.flowRate = flowRate;
	}

	public void setNegativeTotalFlow(String negativeTotalFlow) {
		this.negativeTotalFlow = negativeTotalFlow;
	}

	public void setInstantaneousFlow(String instantaneousFlow) {
		this.instantaneousFlow = instantaneousFlow;
	}

	public void setWaterLevel(String waterLevel) {
		this.waterLevel = waterLevel;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public boolean isNull() {
		return (this.totalFlow == null && this.positiveTotalFlow == null && this.instantaneousFlow == null
				&& this.flowRate == null && this.negativeTotalFlow == null && this.waterLevel == null);
	}
}
