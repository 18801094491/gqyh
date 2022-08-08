package com.zcdy.dsc.modules.operation.work.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author songguang.jiao
 * @date 2020/7/7/0007  15:45:24
 */
@Getter
@Setter
@ApiModel("WorkInspectionPlanPageParam")
public class WorkInspectionPlanPageParam extends AbstractPageParam {

    /**
     * 计划名称
     */
    @ApiModelProperty(value = "计划名称")
    private java.lang.String planName;
}
