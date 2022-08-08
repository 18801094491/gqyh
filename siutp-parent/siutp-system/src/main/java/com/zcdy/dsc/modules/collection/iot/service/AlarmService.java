package com.zcdy.dsc.modules.collection.iot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.collection.iot.entity.Alarm;

/**
 * 描述: 设备报警规则
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02
 * 版本号: V1.0
 */
public interface AlarmService extends IService<Alarm> {

    /**
     * 保存报警规则
     * @param alarm
     */
    void saveAlarm(Alarm alarm);

    /**
     * 修改报警规则
     * @param alarm
     */
    void updateByAlarmId(Alarm alarm);

    /**
     * 分页查询报警规则
     * @param page
     * @param alarm
     * @return
     */
    IPage<Alarm> queryPageList(Page<Alarm> page, Alarm alarm);

    /**
     * 通过id查询报警规则
     * @param id
     * @return
     */
    Alarm getAlarmById(String id);

    /**
     * 更改报警规则状态
     * @param id
     * @param alarmStatusCode 状态值
     */
    void startOrStop(String id ,String alarmStatusCode);
}
