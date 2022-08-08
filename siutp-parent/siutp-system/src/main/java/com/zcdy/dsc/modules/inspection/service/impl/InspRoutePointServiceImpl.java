package com.zcdy.dsc.modules.inspection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.inspection.entity.InspRoute;
import com.zcdy.dsc.modules.inspection.entity.InspRoutePoint;
import com.zcdy.dsc.modules.inspection.mapper.InspRoutePointMapper;
import com.zcdy.dsc.modules.inspection.service.InspRoutePointService;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 巡检路线与巡检点对应关系
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Service
public class InspRoutePointServiceImpl extends ServiceImpl<InspRoutePointMapper, InspRoutePoint> implements InspRoutePointService {

    @Override
    public boolean deleteRoutePoints(String ids, String username) {
        List<String> list = Arrays.asList(ids.split(","));
        List<InspRoutePoint> entityList = new ArrayList<>();
        for(String s : list)
        {
            InspRoutePoint point = new InspRoutePoint();
            point.setId(s);
            point.setDelFlag(DelFlagConstant.DELETED);//删除标记
            point.setUpdateTime(new Date());
            point.setUpdateBy(username);
            entityList.add(point);
        }
        return super.updateBatchById(entityList);//批量逻辑删除
    }
}
