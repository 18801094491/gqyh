package com.zcdy.dsc.modules.personal.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Roberto
 * @date 2020/05/21
 */
@ApiModel(value="DepartmentVO", description="部门vo")
@Setter
@Getter
@ToString
public class DepartmentVO {

    @ApiModelProperty(value="id", notes="唯一标识")
    private String id;

    @ApiModelProperty(value="父id", notes="父级")
    private String pid;

    @ApiModelProperty(value="部门名称", notes="部门名称")
    private String departName;

    @ApiModelProperty(value="用户数量", notes="用户数量")
    private Integer usercount;
    
    @ApiModelProperty(value="部门编码", notes="部门编码")
    private String orgCode;
}
