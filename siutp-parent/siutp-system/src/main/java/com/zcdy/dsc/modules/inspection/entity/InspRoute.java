package com.zcdy.dsc.modules.inspection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zcdy.dsc.modules.inspection.param.InspRouteParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: 巡检路线
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@TableName("work_list_inspection_route")
@ApiModel(value="work_list_inspection_route", description="巡检路线")
public class InspRoute {

	public InspRoute()
	{

	}

	public InspRoute(InspRouteParam param)
	{
		this.id = param.getId();
		this.name = param.getName();
		this.code = param.getCode();
		this.areaId = param.getAreaId();
		this.delFlag = param.getDelFlag();
	}
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**所属区域*/
    @ApiModelProperty(value = "所属区域")
	private java.lang.String areaId;
	/**线路名称*/
    @ApiModelProperty(value = "线路名称")
	private java.lang.String name;
	/**线路编号*/
    @ApiModelProperty(value = "线路编号")
	private java.lang.String code;
	/**线路描述*/
    @ApiModelProperty(value = "线路描述")
	private java.lang.String description;
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

	/**包含的巡检点列表*/
	@ApiModelProperty(value = "包含的巡检点列表")
	@TableField(exist = false)
	private List<InspRoutePoint> pointList;

	/**区域名称*/
	@ApiModelProperty(value = "区域名称")
	@TableField(exist = false)
	private String areaName;
}
