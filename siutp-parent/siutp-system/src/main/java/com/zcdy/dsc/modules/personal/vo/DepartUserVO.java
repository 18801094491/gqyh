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
@ApiModel(value = "DepartUserVO", description = "部门人员信息")
@Setter
@Getter
@ToString
public class DepartUserVO {

    @ApiModelProperty(value="姓名", notes="姓名")
    private String realname;
    
    @ApiModelProperty(value="手机号码", notes="手机号码")
    private String phone;
    
    @ApiModelProperty(value="固话", notes="固话")
    private String telephone;
    
    @ApiModelProperty(value="电子邮箱", notes="电子邮箱")
    private String email;
    
    @ApiModelProperty(value="工号", notes="工号")
    private String workNo;
    
    @ApiModelProperty(value="组织名称", notes="组织名称")
    private String orgName;
    
    @ApiModelProperty(value="职位", notes="姓名")
    private String post;
    
}
