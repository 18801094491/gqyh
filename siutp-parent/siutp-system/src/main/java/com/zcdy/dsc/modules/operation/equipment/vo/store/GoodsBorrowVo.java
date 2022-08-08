package com.zcdy.dsc.modules.operation.equipment.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 货物借用详情
 * @author：  songguang.jiao
 * 创建时间： 2020年2月7日 下午1:48:07 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="GoodsBorrowVo",description="货物借用详情")
public class GoodsBorrowVo {

	//货物id
	@ApiModelProperty(value="货物id")
	private String goodsId;
	
	//货物编码
	@ApiModelProperty(value="货物编码")
	private String itemSn;
	
	//借用次数
	@ApiModelProperty(value="借用次数")
	private Integer borrowTimes;
	
	//货物状态编码
	@ApiModelProperty(value="货物状态编码")
	private String itemStatus;
	
	//货物状态名称
	@ApiModelProperty(value="货物状态名称")
	private String itemStatusName;
	
}
