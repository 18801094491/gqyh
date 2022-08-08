package com.zcdy.dsc.modules.rdp.entity;

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
 * @Description: 大屏重点监控配置
 * @Author: 在信汇通
 * @Date:   2020-12-10
 * @Version: V1.0
 */
@Data
@TableName("rdp_monitor_point_conf")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="rdp_monitor_point_conf", description="大屏重点监控配置")
public class RdpMonitorPointConf {
    
	/**主键id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "主键id")
	private java.lang.String id;
	/**监控设备排列序号（1-5）*/
	@Excel(name = "监控设备排列序号（1-5）", width = 15)
    @ApiModelProperty(value = "监控设备排列序号（1-5）")
	private java.lang.Integer positioNo;
	/**监控设备唯一编码*/
	@Excel(name = "监控设备唯一编码", width = 15)
    @ApiModelProperty(value = "监控设备唯一编码")
	private java.lang.String indexCode;
	/**海康监控平台在配置文件中的唯一标识*/
	@ApiModelProperty(value = "海康监控平台在配置文件中的唯一标识")
	private java.lang.String hkMonitorKey;
	/**监控设备名称*/
	@Excel(name = "监控设备名称", width = 15)
    @ApiModelProperty(value = "监控设备名称")
	private java.lang.String monitorName;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private java.lang.String remarks;
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
	/**更新人*/
	@Excel(name = "更新人", width = 15)
    @ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
	private java.util.Date updateTime;
}
