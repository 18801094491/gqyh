package com.zcdy.dsc.modules.operation.equipment.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询基础信息表
 *
 * @author songguang.jiao
 * @date 2020/05/28 15:44:53
 */
@Getter
@Setter
@ApiModel(value = "OptBaseInfoPageParam")
public class OptBaseInfoPageParam extends AbstractPageParam {

    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private java.lang.String equipmentType;
    /**
     * 设备型号
     */
    @ApiModelProperty(value = "设备型号")
    private java.lang.String equipmentModel;
    /**
     * 设备规格
     */
    @ApiModelProperty(value = "设备规格")
    private java.lang.String equipmentSpecs;
    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商")
    private java.lang.String equipmentSupplier;

}
