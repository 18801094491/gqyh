package com.zcdy.dsc.modules.collection.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.collection.iot.entity.AlarmModel;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述: 报警信息模板
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02
 * 版本号: V1.0
 */
public interface AlarmModelMapper extends BaseMapper<AlarmModel> {

    /**
     * 通过id 跟标题查找模板列表
     * @param id 主键id
     * @param alarmTitle 标题
     * @return
     */
    List<AlarmModel> getAlarmModleByName(@Param("id") String id, @Param("alarmTitle") String alarmTitle);
}
