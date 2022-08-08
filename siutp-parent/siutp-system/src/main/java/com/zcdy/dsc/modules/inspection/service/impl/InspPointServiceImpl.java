package com.zcdy.dsc.modules.inspection.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.inspection.entity.InspPoint;
import com.zcdy.dsc.modules.inspection.mapper.InspPointMapper;
import com.zcdy.dsc.modules.inspection.param.EquipmentQueryParam;
import com.zcdy.dsc.modules.inspection.service.InspPointService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 巡检点
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Service
public class InspPointServiceImpl extends ServiceImpl<InspPointMapper, InspPoint> implements InspPointService {

    @Override
    public List<OptEquipmentModel> getEquipmentList(EquipmentQueryParam query) {
        return baseMapper.getEquipmentList(query);
    }

    @Override
    public IPage<InspPoint> selectPageDate(IPage<InspPoint> page, InspPoint query) {
        return baseMapper.selectPageDate(page, query);
    }
}
