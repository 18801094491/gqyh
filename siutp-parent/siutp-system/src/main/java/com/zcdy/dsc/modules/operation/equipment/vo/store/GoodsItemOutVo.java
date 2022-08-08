package com.zcdy.dsc.modules.operation.equipment.vo.store;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 货品出库
 * @author：  songguang.jiao
 * 创建时间： 2020年2月7日 下午7:51:27 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="GoodsItemOutVo",description="货品出库")
public class GoodsItemOutVo {
	
	//选择的货品ids
	@NotEmpty(message="选择的货品ids不能为空")
	@NotNull(message="选择的货品ids不能为空")
	@ApiModelProperty(value="选择的货品ids")
	String ids;
	
	//用户姓名id
	@NotEmpty(message="用户姓名不能为空")
	@NotNull(message="用户姓名不能为空")
	@ApiModelProperty(value="用户姓名id")
	String userId;
	
	
	//申请单号
	@ApiModelProperty(value="申请单号")
	private String applySn;
}
