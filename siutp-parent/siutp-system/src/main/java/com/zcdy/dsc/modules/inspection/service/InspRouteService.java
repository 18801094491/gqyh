package com.zcdy.dsc.modules.inspection.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.inspection.entity.InspRoute;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 巡检路线
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface InspRouteService extends IService<InspRoute> {
    /**
     * 保存路线和与巡检点的对应关系
     * @param route
     * @return
     */
    boolean saveRoute(InspRoute route);

    /**
     * 添加路线和与巡检点的对应关系
     * @param route
     * @param userName
     * @return
     */
    boolean saveRoutePoints(InspRoute route, String userName);

    /**
     * 通过id删除巡检路线及其与巡检点的对应关系
     * @param route
     * @return
     */
    boolean deleteRoute(InspRoute route);

    /**
     * 批量删除巡检路线及其与巡检点的对应关系
     * @param ids
     * @param username
     * @return
     */
    boolean deleteRoutes(String ids, String username);

    /**
     * 查询列表
     * @param page
     * @param query
     * @return
     */
    IPage<InspRoute> selectPageData(IPage<InspRoute> page, InspRoute query);

    /**
     * 根据id获得路线及其包含巡检点
     * @param id
     * @return
     */
    InspRoute getOneById(String id);

    /**
     * 更新路线及其所属巡检点
     * @param inspRoute
     * @return
     */
    boolean updateRouteById(InspRoute inspRoute);
}
