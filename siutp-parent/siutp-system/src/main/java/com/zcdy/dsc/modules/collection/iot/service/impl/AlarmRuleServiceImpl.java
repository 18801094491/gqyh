package com.zcdy.dsc.modules.collection.iot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.collection.iot.entity.AlarmRule;
import com.zcdy.dsc.modules.collection.iot.mapper.AlarmRuleMapper;
import com.zcdy.dsc.modules.collection.iot.service.AlarmRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述: 设备报警规则变量表
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02
 * 版本号: V1.0
 */
@Service
public class AlarmRuleServiceImpl extends ServiceImpl<AlarmRuleMapper, AlarmRule> implements AlarmRuleService {
    @Resource
    private AlarmRuleMapper alarmRuleMapper;

    @Override
    public void deleteByAlarmId(String alarmId) {
        alarmRuleMapper.deleteByAlarmId(alarmId);
    }

    @Override
    public void saveAlarmRule(AlarmRule alarmRule) {
        alarmRuleMapper.saveAlarmRule(alarmRule);
    }

    @Override
    public List<AlarmRule> getAlarmRuleByAlarmId(String id) {
        return alarmRuleMapper.getAlarmRuleByAlarmId(id);
    }
}
