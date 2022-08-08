package com.zcdy.dsc.modules.rdp.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 大屏重点监控配置Vo类
 * @Author: 在信汇通
 * @Date:   2020-12-10
 * @Version: V1.0
 */
@ApiModel(value="RdpMonitorPointConfVo", description="大屏重点监控配置Vo类")
@Getter
@Setter
public class RdpMonitorPointConfVo {
    
	/**主键id*/
    @ApiModelProperty(value = "主键id")
	private String id;
	/**监控设备排列序号（1-5）*/
	@Excel(name = "监控设备排列序号（1-5）", width = 15)
    @ApiModelProperty(value = "监控设备排列序号（1-5）")
	private Integer positioNo;
	/**监控设备唯一编码*/
	@Excel(name = "监控设备唯一编码", width = 15)
    @ApiModelProperty(value = "监控设备唯一编码")
	private String indexCode;
	/**监控设备名称*/
	@Excel(name = "监控设备名称", width = 15)
    @ApiModelProperty(value = "监控设备名称")
	private String monitorName;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
	private String remarks;
	/**海康监控平台在配置文件中的唯一标识*/
	@ApiModelProperty(value = "海康监控平台在配置文件中的唯一标识")
	private String hkMonitorKey;
}
