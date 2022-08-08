package com.zcdy.dsc.modules.datacenter.statistic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: 统计事项
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-13
 * 版本号: V1.0
 */
@Data
@TableName("statistic_chart")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="statistic_chart", description="统计事项")
public class Chart {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	
	/**统计名称*/
    @ApiModelProperty(value = "统计名称")
	private String statisticName;
	
	/**图标类型*/
    @ApiModelProperty(value = "图标类型")
	private String chartType;
	
	/**统计周期*/
    @ApiModelProperty(value = "统计周期")
	private Integer statisticCycle;
	
	/**统计周期类型,分、时、日、月*/
    @ApiModelProperty(value = "统计周期类型,分、时、日、月")
	private String statisticCycleType;
	
	/**起启用状态*/
    @ApiModelProperty(value = "启停用状态")
	private String statisticStatus;
	
	@ApiModelProperty(value = "是否自定义时间区间", notes="是-1，否-0,默认使用0", required=true, allowableValues="0,1")
	private String cycleTime;
	
	@ApiModelProperty(value = "开始时间字符串", notes="校验时间格式：HH:mm")
	private String startTime;
	
	@ApiModelProperty(value = "结束时间字符串", notes="校验时间格式：HH:mm")
	private String endTime;
	
	@ApiModelProperty(value = "展示顺序，降序")
	private int displayOrder;
}
