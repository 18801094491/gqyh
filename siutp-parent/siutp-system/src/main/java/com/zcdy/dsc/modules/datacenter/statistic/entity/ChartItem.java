package com.zcdy.dsc.modules.datacenter.statistic.entity;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: 统计要素
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-13
 * 版本号: V1.0
 */
@Data
@TableName("statistic_chart_item")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="statistic_chart_item", description="统计要素")
public class ChartItem {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	
	/**统计id*/
	@Excel(name = "统计id", width = 15)
    @ApiModelProperty(value = "统计id")
	private String statisticId;
	
	/**设备台帐id*/
	@Excel(name = "设备台帐id", width = 15)
    @ApiModelProperty(value = "设备台帐id")
	private String optId;
	
	/**变量名称*/
	@Excel(name = "变量名称", width = 15)
    @ApiModelProperty(value = "变量名称")
	private String variableName;
	
	/**显示序列*/
	@Excel(name = "显示序列", width = 15)
    @ApiModelProperty(value = "显示序列")
	private String serialName;
	
}
