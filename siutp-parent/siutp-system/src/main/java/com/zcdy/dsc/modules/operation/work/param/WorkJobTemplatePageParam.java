package com.zcdy.dsc.modules.operation.work.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 工单模板入参
 * @author songguang.jiao
 * @date 2020/6/2411:33
 */
@Getter
@Setter
@ApiModel("WorkJobTemplatePageParam")
public class WorkJobTemplatePageParam extends AbstractPageParam {
    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称")
    private java.lang.String tplName;
    /**
     * 模板类型
     */
    @ApiModelProperty(value = "模板类型")
    private java.lang.String tplType;
    /**
     * 模板状态 1-启用,0-停用
     */
    @ApiModelProperty(value = "模板状态 1-启用,0-停用")
    private java.lang.String tplStatus;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String createUser;
}
