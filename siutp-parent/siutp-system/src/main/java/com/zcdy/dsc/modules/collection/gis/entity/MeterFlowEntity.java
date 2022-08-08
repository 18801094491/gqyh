package com.zcdy.dsc.modules.collection.gis.entity;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 查询水表跟流量计每天累计用水
 * @author：  songguang.jiao
 * 创建时间：  2020年3月30日 下午5:53:27
 * 版本号: V1.0
 */
@Getter
@Setter
public class MeterFlowEntity {

	/**正向累计流量*/
    @ApiModelProperty(value = "正向累计流量")
	private BigDecimal positiveFlow;
	/**负向累计量*/
    @ApiModelProperty(value = "负向累计量")
	private BigDecimal navigateFlow;
	
}
