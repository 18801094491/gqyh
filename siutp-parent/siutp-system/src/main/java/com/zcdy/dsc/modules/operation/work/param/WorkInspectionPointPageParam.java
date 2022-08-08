package com.zcdy.dsc.modules.operation.work.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 巡检点分页参数
 * @author songguang.jiao
 * @date 2020/7/2 9:28
 */
@Getter
@Setter
@ApiModel("WorkInspectionPointPageParam")
public class WorkInspectionPointPageParam extends AbstractPageParam {

    /**
     * 巡检点名称
     */
    @ApiModelProperty(value = "巡检点名称")
    @NotBlank(message = "巡检点名称不能为空")
    private java.lang.String name;
    /**
     * 数据模板
     */
    @ApiModelProperty(value = "数据模板")
    @NotBlank(message = "数据模板不能为空")
    private java.lang.String tplId;
    /**
     * 类别
     */
    @ApiModelProperty(value = "类别")
    @NotBlank(message = "数据类别不能为空")
    private java.lang.String category;
}
