package com.zcdy.dsc.modules.inspection.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.inspection.entity.InspPlan;
import com.zcdy.dsc.modules.inspection.mapper.InspPlanMapper;
import com.zcdy.dsc.modules.inspection.param.WorkTeamDateParam;
import com.zcdy.dsc.modules.inspection.service.InspPlanService;
import com.zcdy.dsc.modules.operation.work.entity.WorkTeam;
import com.zcdy.dsc.modules.operation.work.mapper.WorkTeamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 巡检计划
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Service
public class InspPlanServiceImpl extends ServiceImpl<InspPlanMapper, InspPlan> implements InspPlanService {

    @Autowired
    private WorkTeamMapper workTeamMapper;

    @Override
    public IPage<InspPlan> selectPageDate(IPage<InspPlan> page, InspPlan query) {
        return baseMapper.selectPageDate(page, query);
    }

    @Override
    public List<WorkTeam> getWorkTeamAndMemberListByDate(WorkTeamDateParam workTeamDateParam) {
        return workTeamMapper.getWorkTeamAndMemberListByDate(workTeamDateParam);
    }

    @Override
    public InspPlan getOneById(InspPlan plan) {
        return baseMapper.getOneById(plan);
    }

    @Override
    public List<InspPlan> getPlanAndWorkList(InspPlan plan) {
        return baseMapper.getInspPlanAndPointAndWorkListAnd(plan);
    }
}
