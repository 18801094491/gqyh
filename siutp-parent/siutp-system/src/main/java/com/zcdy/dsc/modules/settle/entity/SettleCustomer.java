package com.zcdy.dsc.modules.settle.entity;

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
 * 描述: 客户信息管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-02
 * 版本号: V1.0
 */
@Data
@TableName("settle_customer")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SettleCustomer对象", description="客户信息管理")
public class SettleCustomer {
    
	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**客户名称*/
	@Excel(name = "客户名称", width = 15)
    @ApiModelProperty(value = "客户名称")
	private java.lang.String customerName;
	/**客户地址*/
    @ApiModelProperty(value = "客户地址")
	private java.lang.String customerAddress;
	/**客户编码*/
	@Excel(name = "客户编码", width = 15)
    @ApiModelProperty(value = "客户编码")
	private java.lang.String customerSn;
	/**客户类型*/
	@Excel(name = "客户类型", width = 15)
    @ApiModelProperty(value = "客户类型")
	private java.lang.String customerType;
	/**水价模式*/
	@Excel(name = "水价模式", width = 15)
    @ApiModelProperty(value = "水价模式")
	private java.lang.String priceMode;
	/**付款模式*/
	@Excel(name = "付款模式", width = 15)
    @ApiModelProperty(value = "付款模式")
	private java.lang.String payMode;
	/**水价(元/吨)*/
	@Excel(name = "水价(元/吨)", width = 15)
    @ApiModelProperty(value = "水价(元/吨)")
	private java.lang.String price;
	/**联系人姓名*/
    @ApiModelProperty(value = "联系人姓名")
	private java.lang.String contacterName;
	/**联系人电话*/
    @ApiModelProperty(value = "联系人电话")
	private java.lang.String contacterPhone;

	/**
	 * 客户税号
	 */
	@Excel(name = "客户税号", width = 30)
	@ApiModelProperty(value="客户税号")
	private String customeDuty;

	/**
	 * 客户银行账号
	 */
	@Excel(name = "客户银行账号", width = 30)
	@ApiModelProperty(value="客户银行账号")
	private String customeBankAccout;
	
	/**
	 * 客户状态
	 */
    @ApiModelProperty(value="客户状态")
    private String customerStatus;

	/**创建人*/
    @ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
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
}
