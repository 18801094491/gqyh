package com.zcdy.dsc.modules.inspection.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 巡检计划
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@ApiModel(value="work_list_inspection_plan", description="巡检计划Vo")
public class InspPlanVo {
    
	/**id*/
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
	/**频次类型：1周2月3年*/
    @ApiModelProperty(value = "频次类型：1周2月3年")
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
}
