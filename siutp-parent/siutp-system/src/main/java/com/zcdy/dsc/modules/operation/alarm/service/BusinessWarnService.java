package com.zcdy.dsc.modules.operation.alarm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.alarm.entity.BusinessWarn;

/**
 * 描述: 报警信息表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-18
 * 版本号: V1.0
 */
public interface BusinessWarnService extends IService<BusinessWarn> {

    /**
     * @param queryWrap
     * @return
     */
    boolean checkExists(QueryWrapper<BusinessWarn> queryWrap);

}
