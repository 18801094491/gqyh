package com.zcdy.dsc.modules.map.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipmentVo;
import com.zcdy.dsc.modules.map.entity.MapRegionManagement;
import com.zcdy.dsc.modules.map.mapper.MapRegionManagementMapper;
import com.zcdy.dsc.modules.map.param.MapRegionParam;
import com.zcdy.dsc.modules.map.service.MapRegionManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 地图-区域管理表
 * @Author: 在信汇通
 * @Date:   2020-12-02
 * @Version: V1.0
 */
@Service
public class MapRegionManagementServiceImpl extends ServiceImpl<MapRegionManagementMapper, MapRegionManagement> implements MapRegionManagementService {

    @Autowired
    private MapRegionManagementMapper mapRegionManagementMapper;

    @Override
    public IPage<MapRegionManagement> queryPageList(Page<MapRegionManagement> page, MapRegionParam mapRegionManagement) {
        return mapRegionManagementMapper.queryPageList(page,mapRegionManagement);
    }
}
