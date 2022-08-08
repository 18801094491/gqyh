 package com.zcdy.dsc.common.system.base.controller;

import org.apache.shiro.SecurityUtils;

import com.zcdy.dsc.common.system.vo.LoginUser;

/**
  * 带用户信息的基类控制器
 * @author Roberto
 * @date 2020/05/14
 */
public interface AbstractPrincipalController {

    default LoginUser getCurrentUser(){
        return (LoginUser) SecurityUtils.getSubject().getPrincipal();
    }
}
