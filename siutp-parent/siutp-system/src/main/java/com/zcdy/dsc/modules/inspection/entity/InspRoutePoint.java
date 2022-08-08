package com.zcdy.dsc.modules.inspection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: 巡检路线与巡检点关系
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@TableName("work_list_inspection_route_points")
@ApiModel(value="work_list_inspection_route_points", description="巡检路线与巡检点关系")
public class InspRoutePoint {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private String id;
	/**线路id*/
    @ApiModelProperty(value = "线路id")
	private String routeId;
	/**巡检点id*/
    @ApiModelProperty(value = "巡检点id")
	private String pointId;
	/**顺序*/
    @ApiModelProperty(value = "顺序")
	private Integer seq;
	/**删除标识0-正常,1-已删除*/
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
	private Integer delFlag;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
	private String updateBy;
	/**修改时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;

	/**
	 * id集合，仅用于批量删除
	 */
	@ApiModelProperty(value = "id集合，用于批量删除")
	@TableField(exist = false)
	private List<String> ids;

	@ApiModelProperty(value = "巡检点名称")
	@TableField(exist = false)
	private String name;

	@ApiModelProperty(value = "巡检点编号")
	@TableField(exist = false)
	private String code;
	@ApiModelProperty(value = "经度")
	@TableField(exist = false)
	private String longitude;
	@ApiModelProperty(value = "纬度")
	@TableField(exist = false)
	private String latitude;


}
