package com.zcdy.dsc.modules.settle.vo;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * 描述: 导出客户信息-详情
 * @author：  songguang.jiao
 * 创建时间：  2020年1月6日 上午9:21:13
 * 版本号: V1.0
 */
@Setter
@Getter
@ApiModel(value="ExportCutomerVo",description="导出客户信息-详情")
public class ExportCutomerVo {
		/**客户编码*/
		@Excel(name = "客户编码", width = 15)
	    @ApiModelProperty(value = "客户编码")
		private java.lang.String customerSn;
		/**客户名称*/
		@Excel(name = "客户名称", width = 15)
	    @ApiModelProperty(value = "客户名称")
		private java.lang.String customerName;
		/**客户地址*/
		@Excel(name = "客户地址", width = 25)
	    @ApiModelProperty(value = "客户地址")
		private java.lang.String customerAddress;
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
		//创建时间
		@Excel(name="创建日期",format="yyyy-MM-dd HH:mm:ss",width=20)
		@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	    @ApiModelProperty(value = "创建日期")
		private java.util.Date createTime;
}
