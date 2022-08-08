package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author： Roberto 创建时间：2019年12月24日 下午5:10:32
 * <p>采集变量数据 </p>
 */
@Setter
@Getter
@ToString
@ApiModel(value = "IotDataVO", description = "采集变量数据")
public class IotDataVO {

    @ApiModelProperty(value = "变量名称", notes = "直接使用-中文形式")
    private String variableName;

    @ApiModelProperty(value = "变量数值", notes = "变量数值")
    private String varibleValue;
}
