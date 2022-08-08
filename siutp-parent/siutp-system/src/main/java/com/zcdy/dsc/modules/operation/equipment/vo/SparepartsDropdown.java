package com.zcdy.dsc.modules.operation.equipment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 备品备件下拉选
 * @author songguang.jiao
 * @date 2020/6/415:00
 */
@Getter
@Setter
@ApiModel("SparePartsDropDown")
public class SparepartsDropdown {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "备件名称")
    private String sparepartsName;
}
