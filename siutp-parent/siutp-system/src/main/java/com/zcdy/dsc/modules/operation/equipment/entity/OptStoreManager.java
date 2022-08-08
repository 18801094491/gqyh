package com.zcdy.dsc.modules.operation.equipment.entity;

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
 * 描述: 仓库管理员
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-07
 * 版本号: V1.0
 */
@Data
@TableName("opt_store_manager")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="opt_store_manager", description="仓库管理员")
public class OptStoreManager {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**仓库id*/
	@Excel(name = "仓库id", width = 15)
    @ApiModelProperty(value = "仓库id")
	private java.lang.String storeId;
	/**库管员id*/
	@Excel(name = "库管员id", width = 15)
    @ApiModelProperty(value = "库管员id")
	private java.lang.String manage;
}
