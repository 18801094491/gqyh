package com.zcdy.dsc.modules.worklist.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 工单任务
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@ApiModel(value="work_list_matter", description="工单任务Vo")
public class WorkListMatterVo {
    
	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**工单id*/
    @ApiModelProperty(value = "工单id")
	private java.lang.String listId;
	/**标题*/
    @ApiModelProperty(value = "标题")
	private java.lang.String title;
	/**描述*/
    @ApiModelProperty(value = "描述")
	private java.lang.Object description;
	/**类型1巡检2维养3问题*/
    @ApiModelProperty(value = "类型1巡检2维养3问题")
	private java.lang.String type;
	/**任务经度*/
    @ApiModelProperty(value = "任务经度")
	private java.lang.String matterLongitude;
	/**任务纬度*/
    @ApiModelProperty(value = "任务纬度")
	private java.lang.String matterLatitude;
	/**提交人id*/
    @ApiModelProperty(value = "提交人id")
	private java.lang.String subId;
	/**提交时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "提交时间")
	private java.util.Date subTime;
	/**处理人id*/
    @ApiModelProperty(value = "处理人id")
	private java.lang.String solveId;
	/**处理时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "处理时间")
	private java.util.Date solveTime;
	/**处理经度*/
    @ApiModelProperty(value = "处理经度")
	private java.lang.String solveLongitude;
	/**处理纬度*/
    @ApiModelProperty(value = "处理纬度")
	private java.lang.String solveLatitude;
	/**处理描述*/
    @ApiModelProperty(value = "处理描述")
	private java.lang.Object solveDesc;
	/**状态0已取消1已创建2已分配3已完成4未完成*/
    @ApiModelProperty(value = "状态0已取消1已创建2已分配3已完成4未完成")
	private java.lang.String status;
	/**关联设备id*/
    @ApiModelProperty(value = "关联设备id")
	private java.lang.String equipmentId;
	/**是否发现问题*/
    @ApiModelProperty(value = "是否发现问题")
	private java.lang.String hasMatter;
	/**问题id*/
    @ApiModelProperty(value = "问题id")
	private java.lang.String matterId;
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
