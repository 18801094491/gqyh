package com.zcdy.dsc.modules.centre.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.zcdy.dsc.modules.centre.param.OptAttrTreeParam;

/**
 * @Description: 属性与树形结构关系
 * @Author: 在信汇通
 * @Date:   2021-03-10
 * @Version: V1.0
 */
@Data
@TableName("opt_attr_tree")
@ApiModel(value="opt_attr_tree", description="属性与树形结构关系")
public class OptAttrTree {

    public OptAttrTree()
    {

    }

    public OptAttrTree(OptAttrTreeParam optAttrTreeParam)
    {
        this.id = optAttrTreeParam.getId();
        this.treeId = optAttrTreeParam.getTreeId();
        this.objId = optAttrTreeParam.getObjId();
        this.attrs = optAttrTreeParam.getAttrs();
        this.attrsName = optAttrTreeParam.getAttrsName();
        this.delFlag = optAttrTreeParam.getDelFlag();
        this.createBy = optAttrTreeParam.getCreateBy();
        this.createTime = optAttrTreeParam.getCreateTime();
        this.updateBy = optAttrTreeParam.getUpdateBy();
        this.updateTime = optAttrTreeParam.getUpdateTime();
    }
    
	/**id*/
    @TableId(type = IdType.UUID)
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

    /**对象名称*/
    @ApiModelProperty(value = "对象名称")
    @TableField(exist = false)
	private String objName;

    /**
     * 属性名转换，给前端显示用
     * @return
     */
    public String getName()
    {
        return attrsName;
    }
}
