package com.zcdy.dsc.modules.operation.equipment.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 *分页查询备品备件
 * @author songguang.jiao
 * @date 2020/06/02 13:41:41
 */
@Getter
@Setter
@ApiModel(value ="OptSparepartsPageParam" )
public class OptSparepartsPageParam extends AbstractPageParam {
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
     * 备件型号
     */
    @ApiModelProperty(value = "备件型号")
    private java.lang.String sparepartsSpces;
}
