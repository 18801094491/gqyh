package com.zcdy.dsc.modules.operation.equipment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 备品备件vo类
 * @author songguang.jiao
 * @date 2020/6/213:45
 */
@Getter
@Setter
@ApiModel(value ="OptSparepartsVo" )
public class OptSparepartsVo {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private java.lang.String categoryName;
    /**
     * 备件名称
     */
    @ApiModelProperty(value = "备件名称")
    private java.lang.String sparepartsName;
    /**
     * 备件规格
     */
    @ApiModelProperty(value = "备件规格")
    private java.lang.String sparepartsModel;
    /**
     * 备件规格
     */
    @ApiModelProperty(value = "备件规格名称")
    private java.lang.String sparepartsModelName;
    /**
     * 备件型号
     */
    @ApiModelProperty(value = "备件型号")
    private java.lang.String sparepartsSpces;
    /**
     * 备件型号
     */
    @ApiModelProperty(value = "备件型号名称")
    private java.lang.String sparepartsSpcesName;
}
