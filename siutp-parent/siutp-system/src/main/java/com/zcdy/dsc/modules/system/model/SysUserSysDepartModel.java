package com.zcdy.dsc.modules.system.model;

import com.zcdy.dsc.modules.system.entity.SysDepart;
import com.zcdy.dsc.modules.system.entity.SysUser;

import lombok.Data;

/**
 * 包含 SysUser 和 SysDepart 的 Model
 *@author : songguang.jiao
 
 */
@Data
public class SysUserSysDepartModel {

    private SysUser sysUser;
    private SysDepart sysDepart;

}
