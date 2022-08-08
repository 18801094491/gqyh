package com.zcdy.dsc.modules.collection.iot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 描述: 采集代理管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-13
 * 版本号: V1.0
 */
@Data
@TableName("iot_proxy")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="iot_proxy", description="采集代理管理")
public class IotProxy {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**代理名称*/
	@Excel(name = "代理名称", width = 15)
    @ApiModelProperty(value = "代理名称")
	private java.lang.String proxyName;
	/**IP地址*/
	@Excel(name = "IP地址", width = 15)
    @ApiModelProperty(value = "IP地址")
	private java.lang.String ipAddress;
	/**代理类型*/
	@Excel(name = "代理类型", width = 15)
    @ApiModelProperty(value = "代理类型")
	private java.lang.String proxyType;
	/**心跳地址*/
	@Excel(name = "心跳地址", width = 15)
    @ApiModelProperty(value = "心跳地址")
	private java.lang.String heartbeatAddress;
	/**心跳监测状态*/
	@Excel(name = "心跳监测状态", width = 15)
    @ApiModelProperty(value = "心跳监测状态")
	private java.lang.String heartbeatStatus;
	/**心跳周期(值)*/
	@Excel(name = "心跳周期(值)", width = 15)
    @ApiModelProperty(value = "心跳周期(值)")
	private java.lang.Integer heartbeatCycle;
	/**心跳周期(单位)*/
	@Excel(name = "心跳周期(单位)", width = 15)
    @ApiModelProperty(value = "心跳周期(单位)")
	private String heartbeatUnit;
	/**代理状态*/
	@Excel(name = "代理状态", width = 15)
    @ApiModelProperty(value = "代理状态")
	private java.lang.String proxyStatus;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
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
