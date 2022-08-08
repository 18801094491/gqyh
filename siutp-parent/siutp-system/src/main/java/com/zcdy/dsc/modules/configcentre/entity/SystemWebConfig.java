package com.zcdy.dsc.modules.configcentre.entity;

import java.util.Date;

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
 * 描述: 系统界面配置信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-02
 * 版本号: V1.0
 */
@Data
@TableName("system_web_config")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="system_web_config", description="系统界面配置信息")
public class SystemWebConfig {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	
	/**系统名称*/
    @ApiModelProperty(value = "系统名称")
	private java.lang.String systemName;
	
	/**系统简称*/
    @ApiModelProperty(value = "系统简称")
	private java.lang.String systemNameSimple;
	
	/**系统简介*/
	@Excel(name = "系统简介", width = 15)
    @ApiModelProperty(value = "系统简介")
	private java.lang.String systemSummary;
	
	/**横向logo*/
    @ApiModelProperty(value = "横向logo")
	private java.lang.String horizontalLogo;
	
	/**纵向logo*/
    @ApiModelProperty(value = "纵向logo")
	private java.lang.String verticalLogo;
	
	/**极简logo*/
    @ApiModelProperty(value = "极简logo")
	private java.lang.String simpleLogo;
	
	/**版权信息*/
    @ApiModelProperty(value = "版权信息")
	private java.lang.String copyright;
	
	/**登录背景图片地址*/
    @ApiModelProperty(value = "登录背景图片地址")
	private java.lang.String loginBg;
	
	 /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 创建人
     */
    private String createBy;
}
