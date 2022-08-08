package com.zcdy.dsc.modules.objecttype.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.zcdy.dsc.modules.objecttype.param.SysObjectParam;

import java.util.List;
import java.util.Map;

/**
 * @Description: 对象类型
 * @Author: 在信汇通
 * @Date:   2021-03-05
 * @Version: V1.0
 */
@Data
@TableName("sys_object")
@ApiModel(value="sys_object", description="对象类型")
public class SysObject {

    public SysObject()
    {

    }

    public SysObject(SysObjectParam sysObjectParam)
    {
        this.id = sysObjectParam.getId();
        this.name = sysObjectParam.getName();
        this.objType = sysObjectParam.getObjType();
        this.tableName = sysObjectParam.getTableName();
        this.delFlag = sysObjectParam.getDelFlag();
        this.createBy = sysObjectParam.getCreateBy();
        this.createTime = sysObjectParam.getCreateTime();
        this.updateBy = sysObjectParam.getUpdateBy();
        this.updateTime = sysObjectParam.getUpdateTime();
    }
    
	/**id*/
    @TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**对象名称*/
    @ApiModelProperty(value = "对象名称")
	private java.lang.String name;
	/**对象类型*/
    @ApiModelProperty(value = "对象类型")
	private java.lang.String objType;
	/**对应表名*/
    @ApiModelProperty(value = "对应表名")
	private java.lang.String tableName;
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

    @ApiModelProperty(value = "属性列表")
    @TableField(exist = false)
	private List<Map<String, String>> fieldList;
}
