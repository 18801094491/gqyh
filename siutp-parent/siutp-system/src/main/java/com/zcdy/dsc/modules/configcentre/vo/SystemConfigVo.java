package com.zcdy.dsc.modules.configcentre.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 系统参数配置vo
 * @author: songguang.jiao
 * 创建时间:  2020年4月10日 下午2:40:08
 * 版本: V1.0
 */
@Getter
@Setter
@ApiModel(value="SystemConfigVo",description="系统参数配置vo")
public class SystemConfigVo {

	/**id*/
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
