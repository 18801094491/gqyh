package com.zcdy.dsc.modules.datacenter.statistic.service;

import com.zcdy.dsc.modules.datacenter.statistic.vo.HydrologysVo;

import java.util.List;

/**
 * @Description: 数据看板Service
 * @Author: 在信汇通
 * @Date: 2020-12-29 10:58:32
 * @Version: V1.0
 */
public interface DataBoardService {
    /**
     *  获取水文实时数据检测
     */
    List<HydrologysVo> getHydrologyAllList();
}
