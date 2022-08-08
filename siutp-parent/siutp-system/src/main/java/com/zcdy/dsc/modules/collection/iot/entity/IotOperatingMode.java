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
 * @Description: 工况监控关联设备信息
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-05-21
 * @Version: V1.0
 */
@Data
@TableName("iot_operating_mode")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="iot_operating_mode", description="工况监控关联设备信息")
public class IotOperatingMode {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**资产设备id*/
	@Excel(name = "资产设备id", width = 15)
    @ApiModelProperty(value = "资产设备id")
	private java.lang.String equimentId;
}
