package com.zcdy.dsc.modules.operation.equipment.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author songguang.jiao
 * @date 2020/6/217:05
 */
@Getter
@Setter
@ApiModel(value = "KnowlegePageParam")
public class KnowlegePageParam extends AbstractPageParam {

    @ApiModelProperty(value = "知识名称")
    private String knowlegeName;
    @ApiModelProperty(value = "设备类型")
    private String equipmentType;
    @ApiModelProperty(value = "知识类型")
    private String type;
    @ApiModelProperty(value = "知识规格")
    private String equipmentModel;
    @ApiModelProperty(value = "知识型号")
    private String equipmentSpecs;
    @ApiModelProperty(value = "供应商")
    private String supplier;
}
