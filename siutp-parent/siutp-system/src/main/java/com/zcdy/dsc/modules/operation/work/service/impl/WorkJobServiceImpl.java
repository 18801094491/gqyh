package com.zcdy.dsc.modules.operation.work.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.operation.equipment.constant.DealStatusConstant;
import com.zcdy.dsc.modules.operation.work.entity.*;
import com.zcdy.dsc.modules.operation.work.mapper.WorkInspectionPlanMapper;
import com.zcdy.dsc.modules.operation.work.mapper.WorkInspectionRouteMapper;
import com.zcdy.dsc.modules.operation.work.mapper.WorkInspectionRoutePointMapper;
import com.zcdy.dsc.modules.operation.work.mapper.WorkJobMapper;
import com.zcdy.dsc.modules.operation.work.param.WokJobPageParam;
import com.zcdy.dsc.modules.operation.work.service.WorkInspectionPointService;
import com.zcdy.dsc.modules.operation.work.service.WorkJobInspectPointService;
import com.zcdy.dsc.modules.operation.work.service.WorkJobInspectorService;
import com.zcdy.dsc.modules.operation.work.service.WorkJobService;
import com.zcdy.dsc.modules.operation.work.vo.WorkInspectionPlanVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @description: 工单
 * @author: songguang.jiao
 * @date: 2020-07-08
 */
@Service
public class WorkJobServiceImpl extends ServiceImpl<WorkJobMapper, WorkJob> implements WorkJobService {


    @Resource
    private WorkInspectionPlanMapper workInspectionPlanMapper;

    @Resource
    private WorkJobMapper workJobMapper;

    @Resource
    private WorkInspectionPointService workInspectionPointService;

    @Resource
    private WorkJobInspectorService workJobInspectorService;

    @Resource
    private WorkJobInspectPointService workJobInspectPointService;

    @Resource
    private WorkInspectionRouteMapper workInspectionRouteMapper;

    @Resource
    private WorkInspectionRoutePointMapper workInspectionRoutePointMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addWorkJob(String planId, String localDate) {
        //查询巡检计划,生成派工单信息
        WorkInspectionPlanVo plan = workInspectionPlanMapper.selectByPlanId(planId);
        WorkInspectionRoute route = workInspectionRouteMapper.selectById(plan.getRouteId());

        WorkJob workJob = new WorkJob();
        workJob.setPlanId(planId);
        workJob.setRouteAttention(route.getAttention());
        workJob.setRouteRemark(route.getRemark());
        workJob.setTplId(plan.getTplId());
        workJob.setRouteName(route.getName());
        workJob.setWorkName(plan.getPlanName() + "(" + localDate + ")");
        //TODO
        workJob.setWorkSn("TODO");
        workJob.setWorkStatus(DealStatusConstant.UNDEAL);
        workJobMapper.insert(workJob);

        //生成巡检人列表
        String[] split = plan.getUsersId().split(",");
        List<WorkJobInspector> workJobInspectors = new ArrayList<>(split.length);
        for (String userId : split) {
            WorkJobInspector workJobInspector = new WorkJobInspector();
            workJobInspector.setJobId(workJob.getId());
            workJobInspector.setUserId(userId);
            workJobInspectors.add(workJobInspector);
        }
        workJobInspectorService.saveBatch(workJobInspectors);

        //生成巡检点
        List<WorkInspectionRoutePoint> points = workInspectionRoutePointMapper.selectList(Wrappers.<WorkInspectionRoutePoint>lambdaQuery().eq(WorkInspectionRoutePoint::getRouteId, plan.getRouteId()));
        List<String> listId = new ArrayList<>();
        points.forEach(item -> {
            listId.add(item.getPointId());
        });
        Collection<WorkInspectionPoint> workInspectionPoints = workInspectionPointService.listByIds(listId);
        List<WorkJobInspectPoint> workJobInspectPoints = new ArrayList<>(workInspectionPoints.size());
        workInspectionPoints.forEach(item -> {
            WorkJobInspectPoint workJobInspectPoint = new WorkJobInspectPoint();
            workJobInspectPoint.setWorkJobId(workJob.getId());
            BeanUtil.copyProperties(item, workJobInspectPoint);
            //默认未填报
            workJobInspectPoint.setRecordStatus(StatusConstant.INVALID);
            workJobInspectPoints.add(workJobInspectPoint);
        });
        workJobInspectPointService.saveBatch(workJobInspectPoints);
    }

    @Override
    public IPage<WorkJobVo> selectPageData(Page<WorkJobVo> page, WokJobPageParam param) {
        return workJobMapper.selectPageData(page, param);
    }


}
