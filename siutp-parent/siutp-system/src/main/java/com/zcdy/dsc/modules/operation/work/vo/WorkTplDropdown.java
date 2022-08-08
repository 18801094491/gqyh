package com.zcdy.dsc.modules.operation.work.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据模板下拉选
 * @author songguang.jiao
 * @date 2020/7/2 10:03
 */
@Getter
@Setter
@ApiModel("WorkTplDropdown")
public class WorkTplDropdown {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("名称")
    private String tplName;
}
