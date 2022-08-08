package com.zcdy.dsc.modules.inspection.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.inspection.entity.InspPlan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.inspection.param.WorkTeamDateParam;
import com.zcdy.dsc.modules.operation.work.entity.WorkTeam;

import java.util.List;

/**
 * @Description: 巡检计划
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface InspPlanService extends IService<InspPlan> {
    /**
     * 分页查询
     * @param page
     * @param query
     * @return
     */
    IPage<InspPlan> selectPageDate(IPage<InspPlan> page, InspPlan query);

    /**
     * 通过巡检计划有效期获取符合的班组
     * @param workTeamDateParam
     * @return
     */
    List<WorkTeam> getWorkTeamAndMemberListByDate(WorkTeamDateParam workTeamDateParam);

    /**
     * 根据id查询
     * @param plan
     * @return
     */
    InspPlan getOneById(InspPlan plan);

    /**
     * 获取巡检计划和与其对应的巡检任务列表
     * @return
     */
    List<InspPlan> getPlanAndWorkList(InspPlan plan);
}
