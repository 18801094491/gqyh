package com.zcdy.dsc.modules.operation.work.service;

import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionRoute;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.work.vo.WorkPointDropdown;

import java.util.List;

/**
 * @description: 巡检路线
 * @author: songgguang.jiao
 * @date:   2020-07-01
 */
public interface WorkInspectionRouteService extends IService<WorkInspectionRoute> {

    /**
     * 巡检线路已选中下拉列表
     * @param routId id
     * @return
     */
    List<WorkPointDropdown> pointList(String routId);

    /**
     * 巡检路线下拉选
     * @return
     */
    List<WorkPointDropdown> dropdown();

    /**
     * 增加巡检点
     * @param workInspectionRoute 参数
     */
    void saveOne(WorkInspectionRoute workInspectionRoute);
}
