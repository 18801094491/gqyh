package com.zcdy.dsc.modules.operation.work.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;

/**
 *一级菜单查询下拉选
 * @author songguang.jiao
 * @date 2020/6/28 17:47
 */
@Getter
@Setter
@ApiOperation("WorkJobDataSourceDropdown")
public class WorkJobDataSourceDropdown {

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String dataName;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;
}
