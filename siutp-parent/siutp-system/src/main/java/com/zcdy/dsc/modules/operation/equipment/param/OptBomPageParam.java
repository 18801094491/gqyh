package com.zcdy.dsc.modules.operation.equipment.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * @author songguang.jiao
 * @date 2020/6/214:39
 */
@Getter
@Setter
@ApiModel("OptBomPageParam")
public class OptBomPageParam extends AbstractPageParam {

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
    /**
     * 备件名称
     */
    @ApiModelProperty(value = "备件名称")
    private java.lang.String sparepartsName;
    /**
     * bom类型
     */
    @ApiModelProperty(value = "bom类型")
    private java.lang.String bomType;
    /**
     * 基础信息id
     */
    @ApiModelProperty(value = "基础信息id")
    @NotEmpty(message = "基础信息id不能为空")
    private java.lang.String basicId;

}
