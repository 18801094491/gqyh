package com.zcdy.dsc.modules.datacenter.statistic.entity;

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
 * 描述: 统计模块变量顺序表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-28
 * 版本号: V1.0
 */
@Data
@TableName("statisc_module_var_order")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="statisc_module_var_order", description="统计模块变量顺序表")
public class ModuleVarOrder {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	
	/**统计项目id*/
	@Excel(name = "统计项目id", width = 15)
    @ApiModelProperty(value = "统计项目id")
	private java.lang.String moduleId;
	
	/**变量类型编码*/
	@Excel(name = "变量类型编码", width = 15)
    @ApiModelProperty(value = "变量类型编码")
	private java.lang.String varType;
	
	/**变量名称*/
	@Excel(name = "变量名称", width = 15)
    @ApiModelProperty(value = "变量名称")
	private java.lang.String varName;
	
	/**展示顺序，升序排列*/
	@Excel(name = "展示顺序，升序排列", width = 15)
    @ApiModelProperty(value = "展示顺序，升序排列")
	private java.lang.Integer displayOrder;
	
}
