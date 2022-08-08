package com.zcdy.dsc.modules.map.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.map.entity.MapRiverInformation;
import com.zcdy.dsc.modules.map.mapper.MapRiverInformationMapper;
import com.zcdy.dsc.modules.map.param.MapRiverParam;
import com.zcdy.dsc.modules.map.service.MapRiverInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 地图-河流信息表
 * @Author: 在信汇通
 * @Date:   2020-12-10
 * @Version: V1.0
 */
@Service
public class MapRiverInformationServiceImpl extends ServiceImpl<MapRiverInformationMapper, MapRiverInformation> implements MapRiverInformationService {

    @Autowired
    private MapRiverInformationMapper mapRiverInformationMapper;
    @Override
    public IPage<MapRiverInformation> queryPageData(Page<MapRiverInformation> page, MapRiverParam mapRiverParam) {
        return mapRiverInformationMapper.queryPageData(page,mapRiverParam);
    }
}
