package com.zcdy.dsc.modules.collection.iot.entity;

import org.jeecgframework.poi.excel.annotation.Excel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: 报警信息模板
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02
 * 版本号: V1.0
 */
@Data
@TableName("iot_equipment_message_tpl")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="iot_equipment_message_tpl", description="报警信息模板")
public class AlarmModel {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**报警模板标题*/
	@Excel(name = "报警模板标题", width = 15)
    @ApiModelProperty(value = "报警模板标题")
	private java.lang.String alarmTitle;
	/**报警模板内容*/
	@Excel(name = "报警模板内容", width = 15)
    @ApiModelProperty(value = "报警模板内容")
	private java.lang.String alarmValue;
	/**创建时间*/
	@Excel(name = "创建时间", width = 15)
    @ApiModelProperty(value = "创建时间")
	private java.lang.String createTime;
}
