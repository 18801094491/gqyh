package com.zcdy.dsc.modules.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.inspection.entity.InspRoutePoint;

import java.util.List;

/**
 * @Description: 巡检路线与巡检点对应关系
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface InspRoutePointService extends IService<InspRoutePoint> {
    /**
     * 批量删除巡检路线与巡检点对应关系
     * @param ids
     * @param username
     * @return
     */
    boolean deleteRoutePoints(String ids, String username);
}
