package com.zcdy.dsc.modules.centre.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zcdy.dsc.modules.centre.constant.CentreTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.zcdy.dsc.modules.centre.param.CentreTreeParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 四大中心树形结构
 * @Author: 在信汇通
 * @Date:   2021-02-20
 * @Version: V1.0
 */
@Data
@TableName("opt_label_tree")
@ApiModel(value="opt_label_tree", description="四大中心树形结构")
public class CentreTree {

    public CentreTree()
    {

    }

    public CentreTree(CentreTreeParam centreTreeParam)
    {
        this.id = centreTreeParam.getId();
        this.name = centreTreeParam.getName();
        this.parentId = centreTreeParam.getParentId();
        this.parentIds = centreTreeParam.getParentIds();
        this.centre = centreTreeParam.getCentre();
        this.objId = centreTreeParam.getObjId();
        this.objType = centreTreeParam.getObjType();
        this.delFlag = centreTreeParam.getDelFlag();
        this.createBy = centreTreeParam.getCreateBy();
        this.createTime = centreTreeParam.getCreateTime();
        this.updateBy = centreTreeParam.getUpdateBy();
        this.updateTime = centreTreeParam.getUpdateTime();
    }
    
	/**id*/
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**名称*/
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**上级id*/
    @ApiModelProperty(value = "上级id")
	private java.lang.String parentId;
	/**上级id集合*/
    @ApiModelProperty(value = "上级id集合")
	private java.lang.Object parentIds;
	/**所属中心*/
    @ApiModelProperty(value = "所属中心")
	private java.lang.String centre;
    /**对象类型id*/
    @ApiModelProperty(value = "对象类型id")
    private java.lang.String objId;
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

    /**id集合，批量删除用*/
    @ApiModelProperty(value = "id集合，批量删除用")
    @TableField(exist = false)
    private java.lang.String ids;

    /**子节点结合*/
    @ApiModelProperty(value = "子节点结合")
    @TableField(exist = false)
    private List<CentreTree> children;
    /**子节点数量*/
    @ApiModelProperty(value = "子节点数量")
    @TableField(exist = false)
    private Integer childrenNum;

    /**对象名称*/
    @ApiModelProperty(value = "对象名称")
    @TableField(exist = false)
    private String objName;
    /**数据类型*/
    @ApiModelProperty(value = "数据类型")
    @TableField(exist = false)
    private String objType;
    /**中心名称*/
    @ApiModelProperty(value = "中心名称")
    @TableField(exist = false)
    private String centreName;

    public String getCentreName()
    {
        if(centre == null)
        {
            return null;
        }
        return CentreTypeEnum.getTextByValue(centre);
    }

    /**
     * 是否叶子节点
     * @return
     */
    public Boolean isLeaf()
    {
        if(childrenNum == null)
        {
            return null;
        }
        return childrenNum == 0;
    }
}
