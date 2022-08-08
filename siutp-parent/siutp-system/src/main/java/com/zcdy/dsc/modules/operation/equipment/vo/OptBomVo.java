package com.zcdy.dsc.modules.operation.equipment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author songguang.jiao
 * @date 2020/6/214:36
 */
@Getter
@Setter
@ApiModel(value = "OptBomVo")
public class OptBomVo {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**
     * bom类型
     */
    @ApiModelProperty(value = "bom类型")
    private java.lang.String bomType;
    /**
     * bom类型
     */
    @ApiModelProperty(value = "bom类型")
    private java.lang.String bomTypeName;
    /**
     * 备件id
     */
    @ApiModelProperty(value = "备件id")
    private java.lang.String sparepartsId;
    /**
     * 备件名称
     */
    @ApiModelProperty(value = "备件名称")
    private java.lang.String sparepartsName;
    /**
     * 备件规格
     */
    @ApiModelProperty(value = "备件规格名称")
    private java.lang.String sparepartsModelName;
    /**
     * 备件型号
     */
    @ApiModelProperty(value = "备件型号名称")
    private java.lang.String sparepartsSpcesName;
}
