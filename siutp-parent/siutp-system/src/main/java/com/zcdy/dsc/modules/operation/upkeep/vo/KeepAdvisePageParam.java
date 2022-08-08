package com.zcdy.dsc.modules.operation.upkeep.vo;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备维护通知列表入参
 * @author songguang.jiao
 * @date 2020/6/815:35
 */
@Getter
@Setter
@ApiModel(value = "KeepAdvisePageParam")
public class KeepAdvisePageParam extends AbstractPageParam {

    @ApiModelProperty(value = "所属标段")
    private String equipmentSection;

    @ApiModelProperty("放置位置")
    private String equipmentLocation;
}
