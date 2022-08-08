package com.zcdy.dsc.modules.operation.work.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionRoute;
import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionRoutePoint;
import com.zcdy.dsc.modules.operation.work.mapper.WorkInspectionRouteMapper;
import com.zcdy.dsc.modules.operation.work.service.WorkInspectionRoutePointService;
import com.zcdy.dsc.modules.operation.work.service.WorkInspectionRouteService;
import com.zcdy.dsc.modules.operation.work.vo.WorkPointDropdown;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 巡检路线
 * @author: songguang.jiao
 * @date: 2020-07-01
 */
@Service
public class WorkInspectionRouteServiceImpl extends ServiceImpl<WorkInspectionRouteMapper, WorkInspectionRoute> implements WorkInspectionRouteService {

    @Resource
    private WorkInspectionRouteMapper workInspectionRouteMapper;

    @Resource
    private WorkInspectionRoutePointService workInspectionRoutePointService;

    @Override
    public List<WorkPointDropdown> pointList(String routId) {
        return workInspectionRouteMapper.pointList(routId);
    }

    @Override
    public List<WorkPointDropdown> dropdown() {
        return workInspectionRouteMapper.dropdown();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveOne(WorkInspectionRoute workInspectionRoute) {
        if (StringUtils.isEmpty(workInspectionRoute.getId())) {
            workInspectionRouteMapper.insert(workInspectionRoute);
        } else {
            workInspectionRouteMapper.updateById(workInspectionRoute);
        }
        String pointIds = workInspectionRoute.getPointIds();
        if (StringUtils.isNotEmpty(pointIds)) {
            workInspectionRoutePointService.remove(Wrappers.<WorkInspectionRoutePoint>lambdaQuery().eq(WorkInspectionRoutePoint::getRouteId,workInspectionRoute.getId()));
            String[] split = pointIds.trim().split(",");
            //去重后集合
            List<String> removeRepeat=new ArrayList<>(split.length);
            ArrayList<WorkInspectionRoutePoint> list = new ArrayList<>(split.length);
            for (String pointId : split) {
                //如果存在直接返回
                if(removeRepeat.contains(pointId)){
                    continue;
                }
                removeRepeat.add(pointId);
                WorkInspectionRoutePoint workInspectionRoutePoint = new WorkInspectionRoutePoint();
                workInspectionRoutePoint.setPointId(pointId);
                workInspectionRoutePoint.setRouteId(workInspectionRoute.getId());
                list.add(workInspectionRoutePoint);
            }
            if (list.size() > 0) {
                workInspectionRoutePointService.saveBatch(list);
            }
        }


    }
}
