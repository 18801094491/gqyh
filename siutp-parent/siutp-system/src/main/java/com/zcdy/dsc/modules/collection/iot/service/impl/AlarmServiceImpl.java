package com.zcdy.dsc.modules.collection.iot.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.collection.iot.entity.Alarm;
import com.zcdy.dsc.modules.collection.iot.mapper.AlarmMapper;
import com.zcdy.dsc.modules.collection.iot.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述: 设备报警规则
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02
 * 版本号: V1.0
 */
@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, Alarm> implements AlarmService {
    @Autowired
    private AlarmMapper alarmMapper;


    @Override
    public void saveAlarm(Alarm alarm) {
        alarmMapper.saveAlarm(alarm);
    }


    @Override
    public void updateByAlarmId(Alarm alarm) {
        alarmMapper.updateByAlarmId(alarm);
    }

    @Override
    public IPage<Alarm> queryPageList(Page<Alarm> page, Alarm alarm) {
        return alarmMapper.queryPageList(page, alarm);
    }

    @Override
    public Alarm getAlarmById(String id) {

        return alarmMapper.getAlarmById(id);
    }

    @Override
    public void startOrStop(String id, String alarmStatusCode) {
        alarmMapper.startOrStop(id, alarmStatusCode);
    }
}
