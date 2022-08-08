package com.zcdy.dsc.modules.collection.iot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.collection.iot.entity.AlarmModel;
import com.zcdy.dsc.modules.collection.iot.mapper.AlarmModelMapper;
import com.zcdy.dsc.modules.collection.iot.service.AlarmModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述: 报警信息模板
 *
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02
 * 版本号: V1.0
 */
@Service
public class AlarmModelServiceImpl extends ServiceImpl<AlarmModelMapper, AlarmModel> implements AlarmModelService {
    @Autowired
    private AlarmModelMapper alarmModelMapper;

    @Override
    public List<AlarmModel> getAlarmModleByName(String id, String alarmTitle) {
        return alarmModelMapper.getAlarmModleByName(id, alarmTitle);
    }
}
