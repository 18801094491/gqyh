package com.zcdy.dsc.modules.operation.alarm.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.operation.alarm.entity.IotEquipmentRuleWarn;
import com.zcdy.dsc.modules.operation.alarm.mapper.IotEquipmentRuleWarnMapper;
import com.zcdy.dsc.modules.operation.alarm.service.IotEquipmentRuleWarnService;

/**
 * 描述: 告警信息配置
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-02
 * 版本号: V1.0
 */
@Service
public class IotEquipmentRuleWarnServiceImpl extends ServiceImpl<IotEquipmentRuleWarnMapper, IotEquipmentRuleWarn> implements IotEquipmentRuleWarnService {
    @Autowired
    IotEquipmentRuleWarnMapper iotEquipmentRuleWarnMapper;
    @Override
    public void deleteIotEquipmentRuleWarn() {
        iotEquipmentRuleWarnMapper.deleteIotEquipmentRuleWarn();
    }

    @Override
    public IPage<IotEquipmentRuleWarn> queryPageList(Page<IotEquipmentRuleWarn> page) {
        return iotEquipmentRuleWarnMapper.queryPageList(page);
    }
}
