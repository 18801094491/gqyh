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
 * 描述: 统计模块变量关联表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-19
 * 版本号: V1.0
 */
@Data
@TableName("statisc_module_var")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="statisc_module_var", description="统计模块变量关联表")
public class ModuleVar {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	
	/**变量类型*/
	@Excel(name = "变量类型", width = 15)
    @ApiModelProperty(value = "变量类型")
	private java.lang.String varType;
	
	/**统计功能id*/
	@Excel(name = "统计功能id", width = 15)
    @ApiModelProperty(value = "统计功能id")
	private java.lang.String statisticProject;
	
}
