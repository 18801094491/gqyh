package com.zcdy.dsc.modules.collection.iot.vo;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 采集代理数据列表
 * @author：  songguang.jiao
 * 创建时间： 2020年3月13日 下午2:21:56 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="IotProxyVo",description="采集代理数据列表")
public class IotProxyVo {
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**代理名称*/
    @ApiModelProperty(value = "代理名称")
	private java.lang.String proxyName;
	/**IP地址*/
    @ApiModelProperty(value = "IP地址")
	private java.lang.String ipAddress;
	/**代理类型*/
    @ApiModelProperty(value = "代理类型")
	private java.lang.String proxyType;
    /**代理类型*/
    @ApiModelProperty(value = "代理类型code")
	private java.lang.String proxyTypeCode;
	/**心跳地址*/
    @ApiModelProperty(value = "心跳地址")
	private java.lang.String heartbeatAddress;
	/**心跳监测状态*/
    @ApiModelProperty(value = "心跳监测状态")
	private java.lang.String heartbeatStatus;
    /**心跳监测状态*/
    @ApiModelProperty(value = "心跳监测状态code")
	private java.lang.String heartbeatStatusCode;
	/**心跳周期(值)*/
    @ApiModelProperty(value = "心跳周期(值)")
	private java.lang.Integer heartbeatCycle;
    /**心跳周期(单位)*/
	@Excel(name = "心跳周期(单位)", width = 15)
    @ApiModelProperty(value = "心跳周期(单位)")
	private java.lang.String heartbeatUnit;
	/**心跳周期(单位)*/
	@Excel(name = "心跳周期(单位)", width = 15)
    @ApiModelProperty(value = "心跳周期(单位)")
	private java.lang.Integer heartbeatUnitCode;
	/**代理状态*/
    @ApiModelProperty(value = "代理状态")
	private java.lang.String proxyStatus;
    /**代理状态*/
    @ApiModelProperty(value = "代理状态code")
	private java.lang.String proxyStatusCode;
	
}
