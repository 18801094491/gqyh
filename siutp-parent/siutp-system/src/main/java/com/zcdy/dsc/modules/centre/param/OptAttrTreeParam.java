package com.zcdy.dsc.modules.centre.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Description: 属性与树形结构关系
 * @Author: 在信汇通
 * @Date:   2021-03-10
 * @Version: V1.0
 */
@Data
@ApiModel(value="opt_attr_tree", description="属性与树形结构关系参数列表")
public class OptAttrTreeParam extends AbstractPageParam {
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
    /**删除标识0-正常,1-已删除*/
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
    private java.lang.Integer delFlag;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**修改人*/
    @ApiModelProperty(value = "修改人")
    private java.lang.String updateBy;
    /**修改时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
}
