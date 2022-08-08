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
 * 描述: 采集变量信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-03
 * 版本号: V1.0
 */
@Data
@TableName("iot_variable_info")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="iot_variable_info对象", description="采集变量信息")
public class IotVariableInfo {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**变量名称*/
	@Excel(name = "变量名称", width = 15)
    @ApiModelProperty(value = "变量名称")
	private java.lang.String variableName;
	/**变量示意*/
	@Excel(name = "变量标题", width = 15)
    @ApiModelProperty(value = "变量示意")
	private java.lang.String variableTitle;

	/**单位*/
	@Excel(name = "单位", width = 15)
	@ApiModelProperty(value = "单位")
	private java.lang.String variableUnit;
	/**读写状态code*/
	@Excel(name = "读写属性", width = 15)
	@ApiModelProperty(value = "读写状态code")
	private java.lang.String readType;

	/**数据类型code：0 Integer,1 Short,2 String,3 Float,4 Double,5 Boolean*/
	@Excel(name = "数据类型", width = 15)
	@ApiModelProperty(value = "数据类型code：0 Integer,1 Short,2 String,3 Float,4 Double,5 Boolean")
	private java.lang.String dataType;


	/**小数位*/
	@ApiModelProperty(value = "小数位")
	private Integer scale;



	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private java.lang.String remark;
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
	/**删除标识0-正常,1-已删除*/
    @ApiModelProperty(value = "删除标识0-正常,1-已删除")
	private java.lang.Integer delFlag;
	/**是否起停用*/
	@Excel(name = "启停用", width = 15)
	@ApiModelProperty(value = "启停用状态")
	private java.lang.String workingStatus;
	//变量类型
	@Excel(name = "变量类型", width = 15)
	@ApiModelProperty(value = "变量类型")
	private String variableType;
}
