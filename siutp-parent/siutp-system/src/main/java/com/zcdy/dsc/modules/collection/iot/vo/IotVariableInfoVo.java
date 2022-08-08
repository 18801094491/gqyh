package com.zcdy.dsc.modules.collection.iot.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 描述: 采集变量信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-03
 * 版本号: V1.0
 */
@Data
@ApiModel(value="IotVariableInfoVo", description="采集变量信息")
public class IotVariableInfoVo{
	/**id*/
    @ApiModelProperty(value = "id")
	private String id;
	/**变量名称*/
	@Excel(name = "变量名称", width = 15)
    @ApiModelProperty(value = "变量名称")
	private String variableName;
	/**变量标题*/
	@Excel(name = "变量标题", width = 15)
    @ApiModelProperty(value = "变量标题")
	private String variableTitle;
	//变量类型
	@Excel(name = "变量类型", width = 15)
	@ApiModelProperty(value = "变量类型")
	private String variableTypeName;
	/**读写属性*/
	@Excel(name = "读写属性", width = 15)
	@ApiModelProperty(value = "读写属性")
	private String readTypeValue;
	/**数据类型*/
	@Excel(name = "数据类型", width = 15)
	@ApiModelProperty(value = "数据类型")
	private java.lang.String dataTypeValue;
	/**单位*/
	@Excel(name = "单位", width = 15)
	@ApiModelProperty(value = "单位")
	private java.lang.String variableUnit;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String remark;
	/**启停用前端需要true跟false*/
	@ApiModelProperty(value = "启停用")
	@Excel(name = "启停用", width = 15)
	private java.lang.String workingStatusValue;

	
	/**启停用前端需要true跟false*/
	@ApiModelProperty(value = "启停用")
	private java.lang.Boolean workingStatus;
	/**启停用状态的布尔值停用为false 启用为true*/
	@ApiModelProperty(value = "启停用状态")
	private String woStValue;
	/**规则*/
	@Excel(name = "规则", width = 15)
	@ApiModelProperty(value = "规则")
	private String rule;
	/**规则*/
	@ApiModelProperty(value = "规则的value")
	private String ruleValue;
	/**数据小数位数*/
	@ApiModelProperty(value = "数据小数位数")
	private Integer scale;
	/**读写状态code*/
	@ApiModelProperty(value = "读写状态code")
	private String readType;
	/**数据类型code：0 Integer,1 Short,2 String,3 Float,4 Double*/
	@ApiModelProperty(value = "数据类型code：0 Integer,1 Short,2 String,3 Float,4 Double")
	private java.lang.String dataType;
	@ApiModelProperty(value = "变量类型")
	private String variableType;
	
}
