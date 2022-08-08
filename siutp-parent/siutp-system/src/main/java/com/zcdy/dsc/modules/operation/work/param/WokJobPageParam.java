package com.zcdy.dsc.modules.operation.work.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 工单分页查询
 * @author songguang.jiao
 * @date 2020/7/8/0008  11:56:48
 */
@Getter
@Setter
@ApiModel("WokJobPageParam")
public class WokJobPageParam extends AbstractPageParam {

    /**
     * 工单名称
     */
    @ApiModelProperty("工单名称")
    private String workName;

    /**
     * 路线名称
     */
    @ApiModelProperty("路线名称")
    private String routeName;
}
