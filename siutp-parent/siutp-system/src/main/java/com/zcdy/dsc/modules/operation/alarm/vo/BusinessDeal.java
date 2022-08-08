package com.zcdy.dsc.modules.operation.alarm.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 处理报警信息
 * @author：  songguang.jiao
 * 创建时间： 2020年2月18日 下午2:27:35 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="BusinessDeal",description="处理报警信息")
public class BusinessDeal {

	 /**id*/
	@NotBlank(message="id 不能为空")
	@NotNull(message="id 不能为空")
    @ApiModelProperty(value = "id")
	private String id;
    
    /**告警时间*/
	@NotNull(message="告警时间 不能为空")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "告警时间")
	private java.util.Date warnTime;
	
	
	 /**备注*/
    @ApiModelProperty(value = "备注")
	private java.lang.String description;
}
