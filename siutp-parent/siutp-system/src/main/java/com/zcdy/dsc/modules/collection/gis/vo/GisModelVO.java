package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author： Roberto
 * 创建时间：2019年12月24日 下午5:02:31
 * 描述: <p>gis模型vo</p>
 */
@Setter
@Getter
@ToString
public class GisModelVO {

	/**id*/
    @ApiModelProperty(value = "id")
	private java.lang.String id;
	//名称
	@ApiModelProperty(value = "名称")
	private String modelName;
	//模型类型
	@ApiModelProperty(value = "模型类型")
	private String modelType;
	//模型类型Code
	@ApiModelProperty(value = "模型类型code")
	private String modelTypeCode;
	//打开状态
	@ApiModelProperty(value = "打开状态")
	private String modelOnImg;
	//正常状态
	@ApiModelProperty(value = "正常状态")
	private String modelImg;
	//关闭状态
	@ApiModelProperty(value = "关闭状态")
	private String modelOffImg;
	//异常状态
	@ApiModelProperty(value = "异常状态")
	private String modelWaringImg;
	//模型偏移量
	@ApiModelProperty(value = "模型偏移量")
	private String modelOffset;
	//模型显示位置,0-不偏移，1-上，2-右，3-下，4-左
	 @ApiModelProperty(value = "模型显示位置,0-不偏移，1-上，2-右，3-下，4-左")
	private java.lang.String modelPosition;
	 //层级
	 @ApiModelProperty(value = "层级")
	 private String modelLevel;
	//经度
    @ApiModelProperty(value = "经度")
	private java.lang.String longitude;
	//纬度
    @ApiModelProperty(value = "纬度")
	private java.lang.String latitude;
   //绑定状态值 0-已绑定  1-未绑定
    @ApiModelProperty(value=" 0-已绑定  1-未绑定")
    private String bindStatus;
    //宽度
    @ApiModelProperty(value = "纬度")
	private java.lang.String width;
    //高度
    @ApiModelProperty(value = "高度")
	private java.lang.String height;
    //设备类型编号
    @ApiModelProperty(value = "设备类型编号")
    private String typeSn;
}
