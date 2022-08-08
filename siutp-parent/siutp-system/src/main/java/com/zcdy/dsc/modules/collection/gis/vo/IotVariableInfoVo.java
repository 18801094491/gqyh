package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述: GIS模型页面绑定变量返回值
 * @author：  songguang.jiao
 * 创建时间：  2020年1月6日 上午11:09:21
 * 版本号: V1.0
 */
@Getter
@Setter
@ApiModel(value="IotVariableInfoVo2")
public class IotVariableInfoVo {

	@ApiModelProperty(value = "id")
	private String id;
	//变量名称
    @ApiModelProperty(value = "变量名称")
	private java.lang.String variableName;
	//变量示意
    @ApiModelProperty(value = "变量示意")
	private java.lang.String variableTitle;
    //绑定状态0-未绑定,1-已绑定
    @ApiModelProperty(value = "绑定状态,0-未绑定,1-已绑定")
    private String bindStatus;
    @ApiModelProperty(value = "显示顺序")
   	private java.lang.String displayOrder;
}
