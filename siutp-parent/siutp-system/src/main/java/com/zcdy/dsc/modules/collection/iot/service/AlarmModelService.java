package com.zcdy.dsc.modules.collection.iot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.collection.iot.entity.AlarmModel;

import java.util.List;

/**
 * 描述: 报警信息模板
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02
 * 版本号: V1.0
 */
public interface AlarmModelService extends IService<AlarmModel> {

    /**
     * 通过id 跟标题查找模板列表
     * @param id 主键id
     * @param alarmTitle 标题
     * @return
     */
    List<AlarmModel> getAlarmModleByName(String id,String alarmTitle);
}
