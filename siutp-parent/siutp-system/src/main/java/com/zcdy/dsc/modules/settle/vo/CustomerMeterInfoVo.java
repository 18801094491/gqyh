package com.zcdy.dsc.modules.settle.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: 客户水表信息
 * @author： songguang.jiao
 * 创建时间： 2020年2月17日 上午9:07:03
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value = "MeterInfoVo", description = "客户水表水价管理")
public class CustomerMeterInfoVo {

	//水表id
	@ApiModelProperty(value = "水表id")
	private String waterId;

	//编号
	@ApiModelProperty(value = "编号")
	private String waterSn;

	// 水表名称
	@ApiModelProperty(value = "水表名称")
	private java.lang.String waterName;
	
	// 水表名称
	@ApiModelProperty(value = "地区名称")
	private java.lang.String districtName;
	
	@ApiModelProperty(value = "地区名称id")
	private java.lang.String districtId;

	// 规格
	@ApiModelProperty(value = "规格")
	private String waterSpecs;

	//型号
	@ApiModelProperty(value = "型号")
	private java.lang.String waterMode;
	
	//所属标段
	@ApiModelProperty(value = "所属标段")
	private java.lang.String waterSection;

	//所在位置
	@ApiModelProperty(value = "所在位置")
	private java.lang.String waterLocation;
	
	//客户名称
	@ApiModelProperty(value = "客户名称")
	private String  customerName;

	//水价
	@ApiModelProperty(value = "水价")
	private String  waterPrice;
	
	//是否非远端水表 0-否,1-是
	@ApiModelProperty(value = "是否非远端水表")
	private String  remoteStatus;
	
	/**经度*/
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
    /**纬度*/
    @ApiModelProperty(value = "纬度")
    private java.lang.String latitude;
    
    /**
     * 模型图片
     */
    @ApiModelProperty(value="模型图片")
    private String modelImg;
}
