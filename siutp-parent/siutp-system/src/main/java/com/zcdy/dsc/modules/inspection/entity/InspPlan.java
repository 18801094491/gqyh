package com.zcdy.dsc.modules.inspection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.modules.inspection.param.InspPlanParam;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: 巡检计划
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@TableName("work_list_inspection_plan")
@ApiModel(value="work_list_inspection_plan", description="巡检计划")
public class InspPlan {

	public InspPlan()
	{

	}

	public InspPlan(InspPlanParam param)
	{
		this.id = param.getId();
		this.name = param.getName();
		this.code = param.getCode();
		this.areaId = param.getAreaId();
		this.routeId = param.getRouteId();
		this.delFlag = param.getDelFlag();
	}
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**名称*/
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**编号*/
    @ApiModelProperty(value = "编号")
	private java.lang.String code;
	/**所属区域*/
    @ApiModelProperty(value = "所属区域")
	private java.lang.String areaId;
	/**所属线路*/
    @ApiModelProperty(value = "所属线路")
	private java.lang.String routeId;
	/**对应班组*/
    @ApiModelProperty(value = "对应班组")
	private java.lang.String teamId;
	/**班组管理员*/
	@ApiModelProperty(value = "班组管理员")
	private java.lang.String teamLeaderId;
	/**开始时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
	private java.util.Date startDate;
	/**结束时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
	private java.util.Date overDate;
	/**频次类型：0天1周2月3年*/
    @ApiModelProperty(value = "频次类型：0天1周2月3年")
	private java.lang.String frequencyType;
	/**频次描述*/
    @ApiModelProperty(value = "频次描述")
	private java.lang.String frequencyDesc;
	/**启停用状态1启用0停用*/
    @ApiModelProperty(value = "启停用状态1启用0停用")
	private java.lang.String status;
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

	/**所属区域名称*/
	@ApiModelProperty(value = "所属区域名称")
	@TableField(exist = false)
	private java.lang.String areaName;
	/**所属线路名称*/
	@ApiModelProperty(value = "所属线路名称")
	@TableField(exist = false)
	private java.lang.String routeName;
	/**对应班组名称*/
	@ApiModelProperty(value = "对应班组名称")
	@TableField(exist = false)
	private java.lang.String teamName;
	/**班组管理员姓名*/
	@ApiModelProperty(value = "班组管理员姓名")
	@TableField(exist = false)
	private java.lang.String teamLeaderName;
	/**频次类型描述*/
	@ApiModelProperty(value = "频次类型描述")
	@TableField(exist = false)
	private java.lang.String typeDes;
	/**状态类型描述*/
	@ApiModelProperty(value = "状态类型描述")
	@TableField(exist = false)
	private java.lang.String statusDes;

	/**查询用类型在数据字典中的编码*/
	@ApiModelProperty(value = "查询用类型在数据字典中的编码")
	@TableField(exist = false)
	private java.lang.String queryTypeCode;
	/**查询用状态在数据字典中的编码*/
	@ApiModelProperty(value = "查询用状态在数据字典中的编码")
	@TableField(exist = false)
	private java.lang.String queryStatusCode;

	@ApiModelProperty(value = "巡检计划对应的巡检工单列表")
	@TableField(exist = false)
	private List<WorkList> workListList;

	@ApiModelProperty(value = "巡检计划对应的巡检点列表")
	@TableField(exist = false)
	private List<InspPoint> pointList;

	public String getShowDate()
	{
		return DateUtil.date2String(startDate, DateUtil.dateFormatStr) + " - " + DateUtil.date2String(overDate, DateUtil.dateFormatStr);
	}

	public String getShowFrequency()
	{
		return typeDes + ", " + frequencyDesc;
	}
}
