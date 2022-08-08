package com.zcdy.dsc.modules.operation.alarm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.alarm.entity.IotEquipmentRuleWarn;

/**
 * 描述: 告警信息配置
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-02
 * 版本号: V1.0
 */
public interface IotEquipmentRuleWarnService extends IService<IotEquipmentRuleWarn> {

    void deleteIotEquipmentRuleWarn();

    IPage<IotEquipmentRuleWarn> queryPageList(Page<IotEquipmentRuleWarn> page);
}
