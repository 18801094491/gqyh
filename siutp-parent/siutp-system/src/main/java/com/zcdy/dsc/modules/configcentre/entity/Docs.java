package com.zcdy.dsc.modules.configcentre.entity;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 描述: 文档管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-08
 * 版本号: V1.0
 */
@Data
@TableName("sys_docs")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_docs", description="文档管理")
public class Docs {
    
	/**id*/
	@TableId(type = IdType.UUID)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	
	/**文档名称*/
    @ApiModelProperty(value = "文档名称")
	private java.lang.String docName;
	
	/**文档说明*/
    @ApiModelProperty(value = "文档说明")
	private java.lang.String docDescription;
	
	/**文档地址*/
    @ApiModelProperty(value = "文档地址")
	private java.lang.String docUrl;
	
	/**文档版本*/
    @ApiModelProperty(value = "文档版本")
	private java.lang.String docVersion;
	
	/**文档类型*/
    @ApiModelProperty(value = "文档类型编码")
	private java.lang.String docType;
    
    @ApiModelProperty(value = "文档类型")
	private java.lang.String docTypeTitle;
	
	/**栏目*/
    @ApiModelProperty(value = "栏目")
	private java.lang.String docColumn;
	
	/**是否公开*/
    @TableField(value="is_open")
    @ApiModelProperty(value = "是否公开")
	private java.lang.String open;
	
	/**文件类型*/
    @ApiModelProperty(value = "文件类型")
	private java.lang.String fileType;
	
	/**文件大小*/
    @ApiModelProperty(value = "文件大小")
	private java.lang.Integer fileSize;
	
	/**下载次数*/
    @ApiModelProperty(value = "下载次数")
	private java.lang.Integer downloadTimes;
	
	/**是否展示*/
    @TableField(value="is_show")
    @ApiModelProperty(value = "是否展示")
	private java.lang.String show;
	
	/**排序*/
    @ApiModelProperty(value = "排序")
	private java.lang.Integer displayOrder;
	
	/**删除状态（0，正常，1已删除）*/
	@TableLogic
    @ApiModelProperty(value = "删除状态（0，正常，1已删除）")
	private java.lang.String delFlag;
	
	/**创建人*/
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
	private java.util.Date createTime;
	
	/**更新人*/
	private java.lang.String updateBy;
	
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;

	private java.lang.Boolean status;
	
}
