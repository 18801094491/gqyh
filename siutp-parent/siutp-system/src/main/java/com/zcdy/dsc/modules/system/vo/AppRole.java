package com.zcdy.dsc.modules.system.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="app权限对象", description="app权限对象")
public class AppRole
{
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色权限集合，逗号隔开
     */
    private String roles;

    /**
     * 是否默认权限
     */
    private String isDefault;
}
