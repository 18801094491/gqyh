package com.zcdy.dsc.modules.centre.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 属性与树形结构关系
 * @Author: 在信汇通
 * @Date:   2021-03-10
 * @Version: V1.0
 */
@Data
@ApiModel(value="opt_attr_tree", description="属性与树形结构关系Vo")
public class OptAttrTreeVo {
    
    /**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
    /**树形结构id*/
    @ApiModelProperty(value = "树形结构id")
	private java.lang.String treeId;
    /**对象类型id*/
    @ApiModelProperty(value = "对象类型id")
	private java.lang.String objId;
    /**属性排序集合*/
    @ApiModelProperty(value = "属性排序集合")
	private java.lang.String attrs;
    /**属性名称排序集合（显示用）*/
    @ApiModelProperty(value = "属性名称排序集合（显示用）")
	private java.lang.String attrsName;
}
