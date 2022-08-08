package com.zcdy.dsc.modules.worklist.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 工单信息
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@ApiModel(value="work_list", description="工单信息Vo")
public class WorkListVo {
    
	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**类型1巡检2维养3问题*/
    @ApiModelProperty(value = "类型1巡检2维养3问题")
	private java.lang.String type;
	/**名称*/
    @ApiModelProperty(value = "名称")
	private java.lang.String name;
	/**编号*/
    @ApiModelProperty(value = "编号")
	private java.lang.String code;
	/**班组id*/
    @ApiModelProperty(value = "班组id")
	private java.lang.String teamId;
	/**开始日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始日期")
	private java.util.Date startDate;
	/**结束日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束日期")
	private java.util.Date overDate;
	/**巡检计划id*/
    @ApiModelProperty(value = "巡检计划id")
	private java.lang.String planId;
	/**区域id*/
    @ApiModelProperty(value = "区域id")
	private java.lang.String areaId;
	/**线路id*/
    @ApiModelProperty(value = "线路id")
	private java.lang.String routeId;
	/**状态1已创建2部分完成3已完成4未完成*/
    @ApiModelProperty(value = "状态1已创建2部分完成3已完成4未完成")
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
