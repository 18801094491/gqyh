package com.zcdy.dsc.modules.inspection.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.inspection.entity.InspPoint;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.inspection.entity.InspRoute;
import com.zcdy.dsc.modules.inspection.param.EquipmentQueryParam;
import com.zcdy.dsc.modules.inspection.vo.EquipmentQueryVo;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;

import java.util.List;

/**
 * @Description: 巡检点
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface InspPointService extends IService<InspPoint> {
    /**
     * 获取资产设备列表，不分页
     * @param query
     * @return
     */
    List<OptEquipmentModel> getEquipmentList(EquipmentQueryParam query);

    /**
     * 分页查询
     * @param page
     * @param query
     * @return
     */
    IPage<InspPoint> selectPageDate(IPage<InspPoint> page, InspPoint query);
}
