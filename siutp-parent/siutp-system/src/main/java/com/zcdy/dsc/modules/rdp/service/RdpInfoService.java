package com.zcdy.dsc.modules.rdp.service;

import com.zcdy.dsc.modules.collection.gis.vo.GisModelImgVo;
import com.zcdy.dsc.modules.rdp.param.ProjectParam;
import com.zcdy.dsc.modules.rdp.vo.*;

import java.util.Date;
import java.util.List;

/**
 * @Description: 大屏-数据模块
 * @Author: 在信汇通
 * @Date: 2020-12-08
 * @Version: V1.0
 */
public interface RdpInfoService {

    /**
     *  获取实时时间
     */
    String getTheTime(Date time);
    /**
     *  获取实时预警告警
     */
    TableDataVo getRealTimeEarlyWarningList();
    /**
     *  本周工单问题列表
     */
    TableDataVo getWorkJobList();
    /**
     *  水质实时数据检测
     */
    List<SunriseChartVo> getWaterQualityDataList();
    /**
     *  根据水质等级统计占比
     */
    List<PieChartVo> getWaterQualityScaleDataList();
    /**
     *  水文实时数据检测
     */
    List<HydrologysVo> getHydrologyAllList();

    /**
     * 根据设备类型获取设备资产数量
     * @param labelName 标签标识名称
     * @param equipmentType 设备类型
     * @return
     */
    NumDataVo getEquipmentNumByType(String labelName, String equipmentType);

    /**
     *  区域信息
     */
    List<RegionVo> getRegionList();
    /**
     *  河流信息
     */
    List<RiverVo> getRiverList();
    /**
     *  区域内项目列表
     */
    List<GisModelImgVo> getProjectList(ProjectParam projectParam);
    /**
     *  区域内项目列表
     */
    EquIotShowData getInfluxData();
}
