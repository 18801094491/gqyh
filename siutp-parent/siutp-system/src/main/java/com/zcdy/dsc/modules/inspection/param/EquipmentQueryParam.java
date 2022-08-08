package com.zcdy.dsc.modules.inspection.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 巡检点-获取资产设备参数列表
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Data
@ApiModel(value="opt_equipment", description="巡检点-获取资产设备参数列表")
public class EquipmentQueryParam extends AbstractPageParam
{
    /**设备编码*/
    @ApiModelProperty(value = "设备编码")
    private String equipmentSn;
    /**设备名称*/
    @ApiModelProperty(value = "设备名称")
    private String equipmentName;
}
