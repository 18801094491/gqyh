package com.zcdy.dsc.modules.map.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.map.entity.MapRiverInformation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.map.param.MapRiverParam;

/**
 * @Description: 地图-河流信息表
 * @Author: 在信汇通
 * @Date:   2020-12-10
 * @Version: V1.0
 */
public interface MapRiverInformationService extends IService<MapRiverInformation> {

  /**
   * 分页查询
   * @param page
   * @param mapRiverParam
   * @return
   */
  IPage<MapRiverInformation> queryPageData(Page<MapRiverInformation> page, MapRiverParam mapRiverParam);
}
