package com.zcdy.dsc.modules.operation.work.entity;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: 第三方维修团队管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-15
 * 版本号: V1.0
 */
@Data
@TableName("work_third_team")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="work_third_team", description="第三方维修团队管理")
public class WorkThirdTeam {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**团队编号*/
	@Excel(name = "团队编号", width = 15)
    @ApiModelProperty(value = "团队编号")
	private java.lang.String teamSn;
	/**团队名称*/
	@Excel(name = "团队名称", width = 15)
    @ApiModelProperty(value = "团队名称")
	private java.lang.String teamName;
	/**协议起始日期*/
	@Excel(name = "协议起始日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "协议起始日期")
	private java.util.Date agreeStart;
	/**协议截至日期*/
	@Excel(name = "协议截至日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "协议截至日期")
	private java.util.Date agreeEnd;
	/**联系人姓名*/
	@Excel(name = "联系人姓名", width = 15)
    @ApiModelProperty(value = "联系人姓名")
	private java.lang.String contactName;
	/**联系人电话*/
	@Excel(name = "联系人电话", width = 15)
    @ApiModelProperty(value = "联系人电话")
	private java.lang.String contactPhone;
	/**联系人职位*/
	@Excel(name = "联系人职位", width = 15)
    @ApiModelProperty(value = "联系人职位")
	private java.lang.String contactPosition;
	/**团队评级*/
	@Excel(name = "团队评级", width = 15)
    @ApiModelProperty(value = "团队评级")
	private java.lang.String teamRating;
	/**资质附件路径*/
	@Excel(name = "资质附件路径", width = 15)
    @ApiModelProperty(value = "资质附件路径")
	private java.lang.String fileUrl;
	/**资质附件名称*/
	@Excel(name = "资质附件名称", width = 15)
    @ApiModelProperty(value = "资质附件名称")
	private java.lang.String fileName;
	/**创建人，派单人*/
	@Excel(name = "创建人，派单人", width = 15)
    @ApiModelProperty(value = "创建人，派单人")
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
	private java.util.Date createTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
	private java.lang.String updateBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
	private java.util.Date updateTime;
	/**删除标识0-正常,1-已删除*/
	@Excel(name = "删除标识0-正常,1-已删除", width = 15)
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
	private java.lang.Integer delFlag;
}
