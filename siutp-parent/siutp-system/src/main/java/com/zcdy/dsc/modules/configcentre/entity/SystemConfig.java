package com.zcdy.dsc.modules.configcentre.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 系统参数配置管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-10
 * @Version: V1.0
 */
@Data
@TableName("system_config")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="system_config", description="系统参数配置管理")
public class SystemConfig {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	
	/**配置名称*/
    @ApiModelProperty(value = "配置名称")
	private java.lang.String configName;
	
	/**配置key*/
    @ApiModelProperty(value = "配置key")
	private java.lang.String configKey;
	
	/**配置值*/
    @ApiModelProperty(value = "配置值")
	private java.lang.String configValue;
	
	/**配置描述*/
    @ApiModelProperty(value = "配置描述")
	private java.lang.String configDescription;
	
	/**启停用状态1-启用,2-停用*/
    @ApiModelProperty(value = "启停用状态1-启用,2-停用")
	private java.lang.String configStatus;
	
    @ApiModelProperty(value = "可用值，json格式")
	private String accessValues;
}
