package com.zcdy.dsc.modules.operation.work.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 巡检路线入参
 * @author songguang.jiao
 * @date 2020/7/2 14:26
 */
@Getter
@Setter
@ApiModel("WorkInspectionRoutePageParam")
public class WorkInspectionRoutePageParam extends AbstractPageParam {

    @ApiModelProperty("名称")
    private String name;
}
