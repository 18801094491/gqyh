package com.zcdy.dsc.modules.datacenter.statistic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.datacenter.statistic.entity.SmsUsers;
import com.zcdy.dsc.modules.system.entity.SysUser;

import java.util.List;

/**
 * 描述: 短信发送用户 @author： 智能无人硬核心项目组 创建时间： 2020-04-02 版本号: V1.0
 */
public interface SmsUsersService extends IService<SmsUsers> {

    /**
     * 通过模块查询对应用户手机号和用户登录名
     * 
     * @param moduleId
     *            模块id
     * @return
     */
    public List<SysUser> queryUsersInfo(String moduleId);

}
