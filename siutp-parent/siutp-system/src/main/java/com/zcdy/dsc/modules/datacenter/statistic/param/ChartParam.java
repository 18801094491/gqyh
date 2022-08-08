package com.zcdy.dsc.modules.datacenter.statistic.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2020年3月13日 下午6:11:18
 * 描述: <p>统计事项参数</p>
 */
@ApiModel("ChartParam")
@Getter
@Setter
public class ChartParam extends AbstractPageParam {

	@ApiModelProperty(value="chartName", notes="统计名称，支持模糊查询")
	private String chartName;
	
	@ApiModelProperty(value="chartType", notes="图标类型")
	private String chartType;
	
	@ApiModelProperty(value="chartStatus", notes="启用-停用")
	private String chartStatus;
}
