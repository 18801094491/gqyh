package com.zcdy.dsc.modules.collection.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.collection.iot.entity.AlarmRule;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述: 设备报警规则变量表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02
 * 版本号: V1.0
 */
public interface AlarmRuleMapper extends BaseMapper<AlarmRule> {

    /**
     * 通过id删除规则变量表
     * @param alarmId 规则id
     */
    void deleteByAlarmId(@Param("alarmId") String alarmId);

    /**
     * 保存规则
     * @param alarmRule
     */
    void saveAlarmRule(@Param("alarmRule") AlarmRule alarmRule);

    /**
     * 查询规则列表
     * @param id
     * @return
     */
    List<AlarmRule> getAlarmRuleByAlarmId(@Param("id") String id);
}
