package com.zcdy.dsc.modules.worklist.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zcdy.dsc.modules.system.entity.SysUser;
import com.zcdy.dsc.modules.worklist.param.MyWorkListParam;
import com.zcdy.dsc.modules.worklist.param.WorkListParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: 工单信息
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@TableName("work_list")
@ApiModel(value="work_list", description="工单信息")
public class WorkList {

	public WorkList()
	{

	}

	public WorkList(WorkListParam workListParam)
	{
		this.name = workListParam.getName();
		this.status = workListParam.getStatus();
		this.startDate = workListParam.getStartDate();
		this.overDate = workListParam.getOverDate();
	}

	public WorkList(MyWorkListParam workListParam)
	{
		this.name = workListParam.getName();
		this.code = workListParam.getCode();
		this.status = workListParam.getStatus();
		this.type = workListParam.getType();
		this.startDate = workListParam.getStartDate();
		this.overDate = workListParam.getOverDate();
		this.searchStatus = workListParam.getSearchStatus();
	}
    
	/**id*/
	@TableId(type = IdType.UUID)
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
	/**班组管理员id*/
	@ApiModelProperty(value = "班组管理员id")
	private java.lang.String teamLeaderId;
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

	/**
	 * 问题id集合，用于创建工单时更新与问题的关系
	 */
	@TableField(exist = false)
	@ApiModelProperty(value = "问题id集合，用于创建工单时更新与问题的关系")
	private List<String> matterIdList;
	/**
	 * 工单状态在数据字典中的code，查询用
	 */
	@TableField(exist = false)
	@ApiModelProperty(value = "工单状态在数据字典中的code，查询用")
	private String queryStatusCode;
	@TableField(exist = false)
	@ApiModelProperty(value = "查询用，doing进行中、finish已结束")
	private String searchStatus;
	@TableField(exist = false)
	@ApiModelProperty(value = "工单类型在数据字典中的code，查询用")
	private String queryTypeCode;
	@TableField(exist = false)
	@ApiModelProperty(value = "工单任务状态在数据字典中的code，查询用")
	private String queryMatterStatusCode;
	@TableField(exist = false)
	@ApiModelProperty(value = "工单任务类型在数据字典中的code，查询用")
	private String queryMatterTypeCode;
	@TableField(exist = false)
	@ApiModelProperty(value = "工单任务的任务类型在数据字典中的code，查询用")
	private String queryMatterMatterTypeCode;
	@TableField(exist = false)
	@ApiModelProperty(value = "工单任务级别在数据字典中的code，查询用")
	private String queryMatterLevelCode;
	@TableField(exist = false)
	@ApiModelProperty(value = "状态描述")
	private String statusDes;
	@TableField(exist = false)
	@ApiModelProperty(value = "类型描述")
	private String typeDes;
	@TableField(exist = false)
	@ApiModelProperty(value = "任务类型描述")
	private String matterTypeDes;
	@TableField(exist = false)
	@ApiModelProperty(value = "级别描述描述")
	private String levelDes;
	@TableField(exist = false)
	@ApiModelProperty(value = "班组名称")
	private String teamName;
	@TableField(exist = false)
	@ApiModelProperty(value = "班组管理员姓名")
	private String leaderName;
	@TableField(exist = false)
	@ApiModelProperty(value = "巡检计划名称")
	private String planName;
	@TableField(exist = false)
	@ApiModelProperty(value = "巡检区域名称")
	private String areaName;
	@TableField(exist = false)
	@ApiModelProperty(value = "巡检路线名称")
	private String routeName;
	@TableField(exist = false)
	@ApiModelProperty(value = "任务列表")
	private  List<WorkListMatter> matterList;
	/**班组管理员username，发送系统消息用*/
	@TableField(exist = false)
	@ApiModelProperty(value = "班组管理员username，发送系统消息用")
	private java.lang.String teamLeaderUsername;
	/**班组成员username，我的工单查询用*/
	@TableField(exist = false)
	@ApiModelProperty(value = "班组成员username，我的工单查询用")
	private java.lang.String teamMemberUsername;
	@TableField(exist = false)
	@ApiModelProperty(value = "班组成员集合")
	private  List<SysUser> userList;
}
