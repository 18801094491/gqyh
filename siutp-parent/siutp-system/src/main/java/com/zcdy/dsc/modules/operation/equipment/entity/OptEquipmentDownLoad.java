package com.zcdy.dsc.modules.operation.equipment.entity;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 下载模板
 * @author：  songguang.jiao
 * 创建时间： 2020年3月9日 下午6:14:09 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="OptEquipmentDownLoad",description="下载模板")
public class OptEquipmentDownLoad {

		/**设备编码*/
		@Excel(name = "设备编号", width = 15)
	    @ApiModelProperty(value = "设备编号")
		private java.lang.String equipmentSn;
		/**设备名称*/
		@Excel(name = "设备名称", width = 15)
	    @ApiModelProperty(value = "设备名称")
		private java.lang.String equipmentName;
		/**设备类别，数据字典*/
		@ApiModelProperty(value = "设备类别")
		@Excel(name = "设备类别", width = 15)
		private java.lang.String equipmentCategory;
		 //设备类型
	    @ApiModelProperty(value = "设备类型")
	    @Excel(name = "设备类型", width = 15)
		private java.lang.String equipmentType;
		/**所属标段，数据字典*/
	    @ApiModelProperty(value = "所属标段")
	    @Excel(name = "所属标段", width = 15)
		private java.lang.String equipmentSection;
	    //所在位置
	    @ApiModelProperty(value = "放置位置")
	    @Excel(name = "放置位置", width = 15)
		private java.lang.String equipmentLocation;
		/**供应商*/
	    @ApiModelProperty(value = "供应商")
	    @Excel(name = "供应商", width = 15)
		private java.lang.String equipmentSupplier;
	    /**投入运营时间*/
	    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	    @DateTimeFormat(pattern="yyyy-MM-dd")
	    @Excel(name = "投入运营时间", width = 15)
	    @ApiModelProperty(value = "投入运营时间(yyyy-MM-dd)")
	    private String equipmentOperating;
	    /**购置时间*/
	    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	    @DateTimeFormat(pattern="yyyy-MM-dd")
	    @ApiModelProperty(value = "购置时间(yyyy-MM-dd)")
	    @Excel(name = "购置时间", width = 15)
	    private String equipmentPurchase;
	   
	   
}
