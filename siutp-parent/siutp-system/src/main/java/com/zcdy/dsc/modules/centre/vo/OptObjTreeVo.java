package com.zcdy.dsc.modules.centre.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 对象与树的绑定关系
 * @Author: 在信汇通
 * @Date:   2021-03-11
 * @Version: V1.0
 */
@Data
@ApiModel(value="opt_obj_tree", description="对象与树的绑定关系Vo")
public class OptObjTreeVo {
    
    /**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
    /**对象id*/
    @ApiModelProperty(value = "对象id")
	private java.lang.String dataId;
    /**树形结构id*/
    @ApiModelProperty(value = "树形结构id")
	private java.lang.String treeId;
    /**对象类型id*/
    @ApiModelProperty(value = "对象类型id")
	private java.lang.String objId;
    /**所属中心*/
    @ApiModelProperty(value = "所属中心")
	private java.lang.String centre;
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
