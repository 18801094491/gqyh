package com.zcdy.dsc.modules.settle.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户信息查询入参
 * 
 * @author songguang.jiao
 * @date 2020/05/11 17:59:57
 */
@Getter
@Setter
@ApiModel(value = "CustometInfoParam")
public class CustometInfoParam extends AbstractPageParam{

    /**
     * 结算开始时间
     */
    @ApiModelProperty("结算开始时间")
    private String startTime;

    /**
     * 结算结束时间
     */
    @ApiModelProperty("结算结束时间")
    private String endTime;

    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String customerName;

    /**
     * 结算状态
     */
    @ApiModelProperty("结算状态")
    private String status;

    /**
     * 地区id
     */
    @ApiModelProperty("地区id")
    private String districtId;
}
