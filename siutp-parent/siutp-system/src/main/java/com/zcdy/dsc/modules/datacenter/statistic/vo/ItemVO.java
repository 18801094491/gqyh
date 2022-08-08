package com.zcdy.dsc.modules.datacenter.statistic.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author： Roberto
 * 创建时间：2020年3月14日 下午1:39:36
 * 描述: <p>统计项目vo</p>
 */
@Setter
@Getter
@ToString
@ApiModel("ItemVO")
public class ItemVO {

	@ApiModelProperty(value="设备id", notes="设备id")
	private String equipmentId;
	
	@ApiModelProperty(value="序列", notes="序列")
	private List<SerialVO> serials;
}
