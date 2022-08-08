package com.zcdy.dsc.modules.collection.iot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.collection.iot.entity.AlarmRule;

import java.util.List;

/**
 * 描述: 设备报警规则变量表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02
 * 版本号: V1.0
 */
public interface AlarmRuleService extends IService<AlarmRule> {

    /**
     * 通过id删除规则变量表
     * @param id 规则id
     */
    void deleteByAlarmId(String id);

    /**
     * 保存规则
     * @param alarmRule
     */
    void saveAlarmRule(AlarmRule alarmRule);

    /**
     * 查询规则列表
     * @param id
     * @return
     */
    List<AlarmRule> getAlarmRuleByAlarmId(String id);
}
