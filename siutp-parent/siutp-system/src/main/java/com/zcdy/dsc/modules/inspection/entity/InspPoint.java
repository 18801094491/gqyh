package com.zcdy.dsc.modules.inspection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zcdy.dsc.modules.inspection.constant.InspConstant;
import com.zcdy.dsc.modules.inspection.param.InspPointParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 巡检点
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@TableName("work_list_inspection_point")
@ApiModel(value="work_list_inspection_point", description="巡检点")
public class InspPoint {

	public InspPoint()
	{

	}

	public InspPoint(InspPointParam param)
	{
		this.id = param.getId();
		this.areaId = param.getAreaId();
		this.name = param.getName();
		this.code = param.getCode();
		this.type = param.getType();
		this.delFlag = param.getDelFlag();
	}
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**所属区域*/
    @ApiModelProperty(value = "所属区域")
	private java.lang.String areaId;
	/**巡检点名称*/
    @ApiModelProperty(value = "巡检点名称")
	private java.lang.String name;
	/**巡检点编号*/
    @ApiModelProperty(value = "巡检点编号")
	private java.lang.String code;
	/**是否重点1是0否*/
    @ApiModelProperty(value = "是否重点1是0否")
	private java.lang.Integer important;
	/**注意事项*/
    @ApiModelProperty(value = "注意事项")
	private java.lang.String notices;
	/**巡检点类型*/
    @ApiModelProperty(value = "巡检点类型")
	private java.lang.String type;
	/**经度*/
    @ApiModelProperty(value = "经度")
	private java.lang.String longitude;
	/**纬度*/
    @ApiModelProperty(value = "纬度")
	private java.lang.String latitude;
	/**关联设备id*/
    @ApiModelProperty(value = "关联设备id")
	private java.lang.String equipmentId;
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

	/**区域名称-显示用*/
	@ApiModelProperty(value = "区域名称-显示用")
	@TableField(exist = false)
	private String areaName;

	/**巡检点类型描述-显示用*/
	@ApiModelProperty(value = "巡检点类型描述-显示用")
	@TableField(exist = false)
	private String typeDes;

	/**是否重点描述-显示用*/
	@ApiModelProperty(value = "是否重点描述-显示用")
	@TableField(exist = false)
	private String importDes;

	/**
	 * 查询条件，巡检点类型在数据字典中的code
	 */
	@TableField(exist = false)
	private String queryTypeDictCode;

	/**
	 * 查询条件，巡检点是否重点在数据字典中的code
	 */
	@TableField(exist = false)
	private String queryImportDictCode;

	/**顺序*/
	@ApiModelProperty(value = "顺序")
	@TableField(exist = false)
	private java.lang.Integer seq;
}
