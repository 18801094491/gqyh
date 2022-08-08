package com.zcdy.dsc.modules.collection.iot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.collection.iot.entity.Alarm;
import org.apache.ibatis.annotations.Param;

/**
 * 描述: 设备报警规则
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02
 * 版本号: V1.0
 */
public interface AlarmMapper extends BaseMapper<Alarm> {

    /**
     * 增加报警规则
     * @param alarm
     */
    void saveAlarm(@Param("alarm") Alarm alarm);

    /**
     * 修改设备报警规则
     * @param alarm
     */
    void updateByAlarmId(@Param("alarm") Alarm alarm);

    /**
     * 分页查询
     * @param page
     * @param alarm
     * @return
     */
    IPage<Alarm> queryPageList( Page<Alarm> page, @Param("alarm") Alarm alarm);

    /***
     * 通过id查询规则
     * @param id 规则id
     * @return
     */
    Alarm getAlarmById(@Param("id") String id);

    /**
     * 更改规则状态
     * @param id 规则id
     * @param alarmStatusCode 规则状态
     */
    void startOrStop(@Param("id") String id,@Param("alarmStatusCode")String alarmStatusCode);
}
