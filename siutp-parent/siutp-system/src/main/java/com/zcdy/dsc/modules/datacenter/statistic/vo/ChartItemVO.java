package com.zcdy.dsc.modules.datacenter.statistic.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author： Roberto
 * 创建时间：2020年3月14日 下午1:28:24
 * 描述: <p></p>
 */
@ApiModel(value="ChartItemVO",description="请求入参")
@Setter
@Getter
@ToString
public class ChartItemVO {

	
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
    
    @ApiModelProperty(value = "统计项目")
	public List<ItemVO> items;
	
    @ApiModelProperty(value = "是否自定义时间区间", notes="是-1，否-0,默认使用0", required=true, allowableValues="0,1")
	private String cycleTime;
	
	@ApiModelProperty(value = "开始时间字符串", notes="校验时间格式：HH:mm")
	private String startTime;
	
	@ApiModelProperty(value = "结束时间字符串", notes="校验时间格式：HH:mm")
	private String endTime;
	
	@ApiModelProperty(value = "展示顺序，降序")
	private int displayOrder;
}
