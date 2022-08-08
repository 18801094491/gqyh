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
import com.zcdy.dsc.modules.centre.param.OptObjTreeParam;

/**
 * @Description: 对象与树的绑定关系
 * @Author: 在信汇通
 * @Date:   2021-03-11
 * @Version: V1.0
 */
@Data
@TableName("opt_obj_tree")
@ApiModel(value="opt_obj_tree", description="对象与树的绑定关系")
public class OptObjTree {

    public OptObjTree()
    {

    }

    public OptObjTree(OptObjTreeParam optObjTreeParam)
    {
        this.id = optObjTreeParam.getId();
        this.dataId = optObjTreeParam.getDataId();
        this.treeId = optObjTreeParam.getTreeId();
        this.objId = optObjTreeParam.getObjId();
        this.centre = optObjTreeParam.getCentre();
        this.delFlag = optObjTreeParam.getDelFlag();
        this.createBy = optObjTreeParam.getCreateBy();
        this.createTime = optObjTreeParam.getCreateTime();
        this.updateBy = optObjTreeParam.getUpdateBy();
        this.updateTime = optObjTreeParam.getUpdateTime();
    }
    
	/**id*/
    @TableId(type = IdType.UUID)
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

    /**对象类型*/
    @ApiModelProperty(value = "对象类型")
    @TableField(exist = false)
    private java.lang.String objType;
}
