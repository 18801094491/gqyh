package com.zcdy.dsc.modules.operation.equipment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 描述: 水表日累计量信
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-19
 * 版本号: V1.0
 */
@Data
@TableName("meter_flow")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="meter_flow", description="水表日累计量信")
public class MeterFlow {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**设备id*/
	@Excel(name = "设备id", width = 15)
    @ApiModelProperty(value = "设备id")
	private java.lang.String equipmentId;
	/**正向当天流量*/
	@Excel(name = "正向当天流量", width = 15)
    @ApiModelProperty(value = "正向当天流量")
	private java.lang.String positiveFlowDay;
	/**正向累计流量*/
	@Excel(name = "正向累计流量", width = 15)
    @ApiModelProperty(value = "正向累计流量")
	private java.lang.String positiveFlow;
	/**负向当天流量*/
	@Excel(name = "负向当天流量", width = 15)
    @ApiModelProperty(value = "负向当天流量")
	private java.lang.String navigateFlowDay;
	/**负向累计量*/
	@Excel(name = "负向累计量", width = 15)
    @ApiModelProperty(value = "负向累计量")
	private java.lang.String navigateFlow;
	/**净用累计流量*/
	@Excel(name = "净用累计流量", width = 15)
    @ApiModelProperty(value = "净用累计流量")
	private java.lang.String netFlow;
	/**净用当天流量*/
	@Excel(name = "净用当天流量", width = 15)
    @ApiModelProperty(value = "净用当天流量")
	private java.lang.String netFlowDay;
	/**统计日期*/
	@Excel(name = "统计日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "统计日期")
	private java.util.Date staticsDate;
	/**统计时间*/
	@Excel(name = "统计时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "统计时间")
	private java.util.Date staticsTime;
}
