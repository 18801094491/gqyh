package com.zcdy.dsc.modules.rdp.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 大屏重点项目列表参数
 * @Author: 在信汇通
 * @Date: 2020-12-11 9:30:06
 * @Version: V1.0
 */
@ApiModel(value = "ProjectParam", description = "大屏重点项目列表参数")
@Getter
@Setter
public class ProjectParam {

    /**区域id*/
    @ApiModelProperty(value = "区域id")
    private String regionId;
}
