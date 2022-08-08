package com.zcdy.dsc.modules.operation.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 查询告警事件参数对象
 * 
 * @author Roberto
 * @date 2020/05/14
 */
@ApiModel(value = "AlarmParam", description = "告警事件参数对象")
@Getter
@Setter
public class AlarmParam extends AbstractPageParam {

    @ApiModelProperty("查询参数")
    private String param;


    @ApiModelProperty(value="模型类型")
    private String equipType;
}
