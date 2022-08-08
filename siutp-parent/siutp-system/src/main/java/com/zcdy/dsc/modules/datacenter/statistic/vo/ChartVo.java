package com.zcdy.dsc.modules.datacenter.statistic.vo;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 统计事项结果列表
 * @author：  songguang.jiao
 * 创建时间： 2020年3月15日 下午5:25:11 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="ChartVo",description="统计事项结果列表")
public class ChartVo {

	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	
	/**统计名称*/
	@Excel(name = "统计名称", width = 15)
    @ApiModelProperty(value = "统计名称")
	private String statisticName;
	
	/**图标类型*/
	@Excel(name = "图标类型", width = 15)
    @ApiModelProperty(value = "图标类型")
	private String chartType;
	
	/**统计周期*/
	@Excel(name = "统计周期", width = 15)
    @ApiModelProperty(value = "统计周期")
	private Integer statisticCycle;
	
	/**统计周期类型,分、时、日、月*/
	@Excel(name = "统计周期类型,分、时、日、月", width = 15)
    @ApiModelProperty(value = "统计周期类型,分、时、日、月")
	private String statisticCycleType;
	
	/**起启用状态*/
	@Excel(name = "启停用状态", width = 15)
    @ApiModelProperty(value = "启停用状态")
	private boolean statisticStatus;
	
	@ApiModelProperty(value = "是否自定义时间区间", notes="是-1，否-0", required=true, allowableValues="0,1")
	private String cycleTime;
	
	@ApiModelProperty(value = "开始时间字符串", notes="校验时间格式：HH:mm")
	private String startTime;
	
	@ApiModelProperty(value = "结束时间字符串", notes="校验时间格式：HH:mm")
	private String endTime;
	
	@ApiModelProperty(value = "展示顺序，降序")
	private int displayOrder;
}