package com.zcdy.dsc.modules.collection.iot.entity;

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
 * 描述: 采集数据
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-31
 * 版本号: V1.0
 */
@Data
@TableName("iot_var_data")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="iot_var_data对象", description="采集数据")
public class IOTVarData {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**varName*/
	@Excel(name = "varName", width = 15)
    @ApiModelProperty(value = "varName")
	private java.lang.String varName;
	/**varValue*/
	@Excel(name = "varValue", width = 15)
    @ApiModelProperty(value = "varValue")
	private java.lang.String varValue;
	/**timestamp*/
	@Excel(name = "timestamp", width = 15)
    @ApiModelProperty(value = "timestamp")
	private java.lang.Long timestamp;
	/**createTime*/
	@Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "createTime")
	private java.util.Date createTime;
	
	@Excel(name = "质量戳", width = 1)
    @ApiModelProperty(value = "qualityStamp")
	private String qualityStamp;
}
