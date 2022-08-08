package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author： Roberto
 * 创建时间：2019年12月24日 下午5:08:24
 * 描述: <p></p>
 */
@Setter
@Getter
@ToString
public class EquipmentAttrVO {

	//属性名称
    @ApiModelProperty(value = "属性名称")
	private java.lang.String attributeCn;
	//属性值
    @ApiModelProperty(value = "属性值")
	private java.lang.String attributeVal;
}
