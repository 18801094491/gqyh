package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
/**
 * 描述: GIS地图全量信息
 * @author：  songguang.jiao
 * 创建时间： 2020年3月2日 下午3:00:43 
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="GisModelImgVo",description="GIS地图全量信息")
public class GisModelImgVo {
	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	/**模型名称*/
    @ApiModelProperty(value = "模型名称")
	private java.lang.String modelName;
	/**模型类型*/
    @ApiModelProperty(value = "模型类型")
	private java.lang.String modelTypeCode;
    /**正常图片信息*/
	private String img;
    /**异常图片信息*/
	private String waringImg;
    /**开启图片信息*/
	private String onImg;
    /**关闭图片信息*/
	private String offImg;
	/**正常图片信息*/
    @ApiModelProperty(value = "正常图片信息")
	private GisModelResImgVo modelImg;
    /**异常图片信息*/
    @ApiModelProperty(value = "异常图片信息")
	private GisModelResImgVo modelWaringImg;
    /**开启图片信息*/
    @ApiModelProperty(value = "开启图片信息")
	private GisModelResImgVo modelOnImg;
    /**关闭图片信息*/
	@ApiModelProperty(value = "关闭图片信息")
	private GisModelResImgVo modelOffImg;
	/**模型偏移量*/
    @ApiModelProperty(value = "模型偏移量")
	private java.lang.String modelOffset;
	/**模型显示位置,，0-不偏移，1-上，2-右，3-下，4-左*/
    @ApiModelProperty(value = "模型显示位置,，0-不偏移，1-上，2-右，3-下，4-左")
	private java.lang.String modelPosition;
	/**
	 * 层级
	 */
    @ApiModelProperty(value = "层级")
	private Integer modelLevel;
	/**经度*/
    @ApiModelProperty(value = "经度")
	private java.lang.String longitude;
	/**纬度*/
    @ApiModelProperty(value = "纬度")
	private java.lang.String latitude;
    /**
     * 高度
     */
    private Integer height;
   /**
    * 宽度
    */
    private Integer width;

    /**
     * 位置
     */
    private String equipLocation;
	/**资产设备编号*/
	@ApiModelProperty(value = "资产设备编号")
    private String equipmentSn;
	/**设备所在位置*/
	@ApiModelProperty(value = "所在位置")
    private String equipmentLocation;
	/**所属标段*/
	@ApiModelProperty(value = "所属标段")
	private String equipmentSection;
	/**海康监控唯一标识*/
	@ApiModelProperty(value = "海康监控唯一标识")
	private String hkMonitorCode;
	/**海康监控配置唯一标识*/
	@ApiModelProperty(value = "海康监控配置唯一标识")
	private String hkMonitorKey;

}
