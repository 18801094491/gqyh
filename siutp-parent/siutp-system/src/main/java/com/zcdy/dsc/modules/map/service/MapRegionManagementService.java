package com.zcdy.dsc.modules.map.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipmentVo;
import com.zcdy.dsc.modules.map.entity.MapRegionManagement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.map.param.MapRegionParam;

/**
 * @Description: 地图-区域管理表
 * @Author: 在信汇通
 * @Date:   2020-12-02
 * @Version: V1.0
 */
public interface MapRegionManagementService extends IService<MapRegionManagement> {

    /**
     * 分页查询
     * @param page
     * @param mapRegionManagement
     * @return
     */
    IPage<MapRegionManagement> queryPageList(Page<MapRegionManagement> page, MapRegionParam mapRegionManagement);
}
