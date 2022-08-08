package com.zcdy.dsc.modules.operation.alarm.param;

import com.zcdy.dsc.common.api.param.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Author: 在信汇通
 * @Date: 2020-12-24 16:05:03
 * @Version: V1.0
 */
@ApiModel(value = "BusinessWarnPageParam", description = "未处理告警信息列表参数")
@Getter
@Setter
public class BusinessWarnParam extends AbstractPageParam {
    @ApiModelProperty(value = "告警等级（0-一般、1-警告、2-严重、3-紧急）")
    private String warnLevel;
//    @ApiModelProperty(value = "告警状态（0-初始化，1-未处理，2-已处理，3-已关闭）")
    private String warnStatus;
}
