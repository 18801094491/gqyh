package com.zcdy.dsc.modules.operation.equipment.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 仓库名称下拉选
 * @author： songguang.jiao
 * 创建时间： 2020年1月9日 上午11:02:27
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "StoreLocationVo", description = "仓库名称下拉选")
public class StoreDropdownVo {

	/**仓库ID*/
    @ApiModelProperty(value = "仓库ID")
	private java.lang.String storeId;
	/**仓库地址*/
    @ApiModelProperty(value = "仓库地址")
	private java.lang.String storePosition;
}
