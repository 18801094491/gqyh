package com.zcdy.dsc.modules.operation.equipment.vo.knowlege;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 设备详情页面展示维保信息
 * @author：  songguang.jiao
 * 创建时间： 2020年2月27日 下午4:36:15 
 * 版本号: V1.0
 */

@Getter
@Setter
@ApiModel(value="KnowlegeEquip",description="设备详情页面展示维保信息")
public class KnowlegeEquipData {

	/**id*/
	@ApiModelProperty(value = "id")
	private java.lang.String id;
	/**知识内容*/
	@ApiModelProperty(value = "知识名称")
	private String knowlegeName;
	/**类型-维护分类字典*/
	@ApiModelProperty(value = "知识类型")
	private java.lang.String type;	
}
