package com.zcdy.dsc.modules.operation.equipment.vo.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 备品备件下拉选列表
 * @author：  songguang.jiao
 * 创建时间： 2020年2月4日 下午1:30:07 
 * 版本号: V1.0
 */
@Getter
@Setter
public class SparepartDropdown {

	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**备件名称*/
    @ApiModelProperty(value = "备件名称")
	private java.lang.String sparepartName;
}
