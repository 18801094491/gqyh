package com.zcdy.dsc.modules.personal.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Roberto
 * @date 2020/05/21
 */
@ApiModel(value="DepartmentWithUserVO", description="部门带人员")
@Setter
@Getter
@ToString
public class DepartmentWithUserVO {

    @ApiModelProperty(value="部门信息", notes="部门信息列表")
    private List<DepartmentVO> departments;
    
    @ApiModelProperty(value="人员信息", notes="人员信息列表")
    private List<DepartUserVO> departmentUsers;
}
