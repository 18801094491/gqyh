package com.zcdy.dsc.modules.inspection.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.inspection.entity.InspRoute;
import com.zcdy.dsc.modules.inspection.entity.InspRoutePoint;
import com.zcdy.dsc.modules.inspection.mapper.InspRouteMapper;
import com.zcdy.dsc.modules.inspection.mapper.InspRoutePointMapper;
import com.zcdy.dsc.modules.inspection.service.InspRouteService;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description: 巡检路线
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Service
public class InspRouteServiceImpl extends ServiceImpl<InspRouteMapper, InspRoute> implements InspRouteService {

    @Autowired
    private InspRoutePointMapper inspRoutePointMapper;

    @Override
    @Transactional
    public boolean saveRoute(InspRoute route)
    {
        List<InspRoutePoint> pointList = route.getPointList();
        route.setPointList(null);
        super.save(route);
        if(pointList != null && pointList.size() > 0)
        {
            for(InspRoutePoint irp : pointList)
            {
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                irp.setId(uuid);
                irp.setRouteId(route.getId());
                irp.setCreateBy(route.getCreateBy());
                irp.setCreateTime(new Date());
            }
            baseMapper.insertRoutePointList(pointList);
        }
        return true;
    }

    @Override
    public boolean saveRoutePoints(InspRoute route, String userName) {
        List<InspRoutePoint> pointList = route.getPointList();
        if(pointList != null && pointList.size() > 0)
        {
            for(InspRoutePoint irp : pointList)
            {
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                irp.setId(uuid);
                irp.setRouteId(route.getId());
                irp.setCreateBy(userName);
                irp.setCreateTime(new Date());
            }
            baseMapper.insertRoutePointList(pointList);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteRoute(InspRoute route)
    {
        super.updateById(route);
        InspRoutePoint irp = new InspRoutePoint();
        irp.setRouteId(route.getId());
        irp.setDelFlag(DelFlagConstant.DELETED);
        irp.setUpdateBy(route.getUpdateBy());
        irp.setUpdateTime(new Date());
        baseMapper.delRoutePoint(irp);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteRoutes(String ids, String username)
    {
        List<String> list = Arrays.asList(ids.split(","));
        List<InspRoute> entityList = new ArrayList<>();
        for(String s : list)
        {
            InspRoute inspRoute = new InspRoute();
            inspRoute.setId(s);
            inspRoute.setDelFlag(DelFlagConstant.DELETED);//删除标记
            inspRoute.setUpdateTime(new Date());
            inspRoute.setUpdateBy(username);
            entityList.add(inspRoute);
        }
        boolean ok = super.updateBatchById(entityList);//批量逻辑删除
        InspRoutePoint irp = new InspRoutePoint();
        irp.setIds(list);
        irp.setDelFlag(DelFlagConstant.DELETED);
        irp.setUpdateTime(new Date());
        irp.setUpdateBy(username);
        baseMapper.delRoutePoints(irp);
        return true;
    }

    @Override
    public IPage<InspRoute> selectPageData(IPage<InspRoute> page, InspRoute query) {
        return baseMapper.selectPageData(page, query);
    }

    @Override
    public InspRoute getOneById(String id) {
        return baseMapper.getOneById(id);
    }

    @Override
    @Transactional
    public boolean updateRouteById(InspRoute inspRoute) {
        List<InspRoutePoint> pointList = inspRoute.getPointList();
        inspRoute.setPointList(null);
        baseMapper.updateById(inspRoute);//更新自己
        InspRoutePoint irp = new InspRoutePoint();
        irp.setRouteId(inspRoute.getId());
        irp.setDelFlag(DelFlagConstant.DELETED);
        irp.setUpdateTime(new Date());
        irp.setUpdateBy(inspRoute.getUpdateBy());
        //删除原关系
        inspRoutePointMapper.delRoutePointsByRouteId(irp);
        //插入新关系
        if(pointList != null && pointList.size() > 0)
        {
            for(InspRoutePoint inspRoutePoint : pointList)
            {
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                inspRoutePoint.setId(uuid);
                inspRoutePoint.setRouteId(inspRoute.getId());

                inspRoutePoint.setCreateBy(inspRoute.getUpdateBy());
                inspRoutePoint.setCreateTime(new Date());
            }
            baseMapper.insertRoutePointList(pointList);
        }
        return true;
    }
}
