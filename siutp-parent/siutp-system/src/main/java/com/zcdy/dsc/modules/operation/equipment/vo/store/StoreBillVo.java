package com.zcdy.dsc.modules.operation.equipment.vo.store;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 出入库列表实体类
 * @author：  songguang.jiao
 * 创建时间： 2020年2月3日 下午9:14:52 
 * 版本号: V1.0
 */
@Getter
@Setter
public class StoreBillVo {
	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**批次号*/
    @ApiModelProperty(value = "批次号")
	private java.lang.String batchSn;
	/**备件id*/
    @ApiModelProperty(value = "备件id")
	private java.lang.String sparepartId;
    /**备件名称*/
    @ApiModelProperty(value = "备件名称")
	private java.lang.String sparepartName;
	/**数量*/
    @ApiModelProperty(value = "数量")
	private java.lang.Integer amount;
	/**仓库id*/
    @ApiModelProperty(value = "仓库id")
	private java.lang.String storeId;
    /**仓库名*/
    @ApiModelProperty(value = "仓库名称")
	private java.lang.String storeName;
    /**经办人*/
    @ApiModelProperty(value = "经办人")
	private java.lang.String createBy;	
    /**入库日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "入库日期")
	private java.util.Date createTime;
	 /**规格*/
    @ApiModelProperty(value = "规格")
	private String sparepartSpecs;
    /**生产日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "生产日期")
	private java.util.Date productDate;
	/**有效期止*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "有效期止")
	private java.util.Date endDate;
}
