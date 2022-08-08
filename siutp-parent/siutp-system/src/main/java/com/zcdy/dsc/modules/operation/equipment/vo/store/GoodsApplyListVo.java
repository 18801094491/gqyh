package com.zcdy.dsc.modules.operation.equipment.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 申请单下拉选
 * @author：  songguang.jiao
 * 创建时间： 2020年2月8日 下午8:16:12 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="GoodsApplyListVo",description=" 申请单下拉选")
public class GoodsApplyListVo {

	//申请编号
	@ApiModelProperty(value="申请编号")
	private String applySn;
	
	//申请时间
	@ApiModelProperty(value="申请编号值")
	private String 	applySnValue;
	
}
