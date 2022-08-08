package com.zcdy.dsc.modules.rdp.controller;

import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.modules.collection.gis.entity.IotShowData;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelImgVo;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.rdp.constant.TableConstant;
import com.zcdy.dsc.modules.rdp.param.ProjectParam;
import com.zcdy.dsc.modules.rdp.service.RdpInfoService;
import com.zcdy.dsc.modules.rdp.vo.*;
import com.zcdy.dsc.modules.worklist.constant.WorkListConstant;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import com.zcdy.dsc.modules.worklist.service.WorkListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description: 大屏-数据
 * @Author: 在信汇通
 * @Date: 2020-12-08 11:40:03
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "大屏-数据")
@RestController
@RequestMapping("/rdp/info")
public class RdpInfoController {

    @Autowired
    private RdpInfoService rdpInfoService;

    @Autowired
    private WorkListService workListService;

    /**
     * 获取当前时间
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-当前时间")
    @ApiOperation(value = "大屏-数据-当前时间", notes = "大屏-数据-当前时间")
    @GetMapping(value = "/time")
    public ResultData<String> getTheTime() {
        ResultData<String> resultData = new ResultData<String>();
        try {
            Date time = new Date();
            String theTime = rdpInfoService.getTheTime(time);
            resultData.setData(theTime);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 实时预警告警
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-实时预警告警")
    @ApiOperation(value = "大屏-数据-实时预警告警", notes = "大屏-数据-实时预警告警")
    @GetMapping(value = "/warning")
    public ResultData<TableDataVo> getRealTimeEarlyWarningList() {
        ResultData<TableDataVo> resultData = new ResultData<TableDataVo>();
        try {
            TableDataVo tableDataVo = rdpInfoService.getRealTimeEarlyWarningList();
            resultData.setData(tableDataVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 本周工单问题列表
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-本周工单问题列表")
    @ApiOperation(value = "大屏-数据-本周工单问题列表", notes = "大屏-数据-本周工单问题列表")
    @GetMapping(value = "/work-job")
    public ResultData<TableDataVo> getWorkJobList() {
        ResultData<TableDataVo> resultData = new ResultData<TableDataVo>();
        try {
            TableDataVo tableDataVo = rdpInfoService.getWorkJobList();
            resultData.setData(tableDataVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 水质实时数据检测
     *
     * @return
     */
/*    @AutoLog(value = "大屏-数据-水质实时数据检测")
    @ApiOperation(value = "大屏-数据-水质实时数据检测", notes = "大屏-数据-水质实时数据检测")
    @GetMapping(value = "/water-quality/list")
    public ResultData<List<SunriseChartVo>> getWaterQualityDataList() {
        ResultData<List<SunriseChartVo>> resultData = new ResultData<List<SunriseChartVo>>();
        try {
            List<SunriseChartVo> sunriseChartDataList = rdpInfoService.getWaterQualityDataList();
            resultData.setData(sunriseChartDataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }*/

    /**
     * 水质实时数据检测
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-水质实时数据检测")
    @ApiOperation(value = "大屏-数据-水质实时数据检测", notes = "大屏-数据-水质实时数据检测")
    @GetMapping(value = "/water-quality/list")
    public ResultData<List<PieChartVo>> getWaterQualityDataList() {
        ResultData<List<PieChartVo>> resultData = new ResultData<>();
        try {
            resultData.setData(rdpInfoService.getWaterQualityScaleDataList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 水文实时数据检测
     * type
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-水文实时数据检测")
    @ApiOperation(value = "大屏-数据-水文实时数据检测", notes = "大屏-数据-水文实时数据检测")
    @GetMapping(value = "/hydrology/list")
    public ResultData<List<HydrologysVo>> getHydrologyList() {
        ResultData<List<HydrologysVo>> resultData = new ResultData<List<HydrologysVo>>();
        try {
            List<HydrologysVo> sunriseChartDataList = rdpInfoService.getHydrologyAllList();
            resultData.setData(sunriseChartDataList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 工单已解决问题数量
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-工单已解决问题数量")
    @ApiOperation(value = "大屏-数据-工单已解决问题数量", notes = "大屏-数据-工单已解决问题数量")
    @GetMapping(value = "/resolved")
    public ResultData<NumDataVo> getWorkJobResolvedNum() {
        ResultData<NumDataVo> resultData = new ResultData<NumDataVo>();
        try {
            Integer num = workListService.getWorkListNumByStatus(WorkListConstant.WORK_LIST_STATUS_COMPLETE);
            NumDataVo numDataVo = new NumDataVo();
            numDataVo.setNum(num);
            resultData.setData(numDataVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 工单待处理数量
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-工单待处理数量")
    @ApiOperation(value = "大屏-数据-工单待处理数量", notes = "大屏-数据-工单待处理数量")
    @GetMapping(value = "/unresolved")
    public ResultData<NumDataVo> getWorkJobUnresolvedNum() {
        ResultData<NumDataVo> resultData = new ResultData<NumDataVo>();
        try {
            Integer num = workListService.getWorkListNumByStatus(WorkListConstant.WORK_LIST_STATUS_ALLOT);
            NumDataVo numDataVo = new NumDataVo();
            numDataVo.setNum(num);
            resultData.setData(numDataVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 水文监测
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-水文监测数量")
    @ApiOperation(value = "大屏-数据-水文监测数量", notes = "大屏-数据-水文监测数量")
    @GetMapping(value = "/hydrology")
    public ResultData<NumDataVo> hydrologyNum() {
        ResultData<NumDataVo> resultData = new ResultData<>();
        try {
            NumDataVo numDataVo = rdpInfoService.getEquipmentNumByType("水文监测", TableConstant.HYDROLOGY_TYPE);
            resultData.setData(numDataVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 水质监测
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-水质监测设备数量")
    @ApiOperation(value = "大屏-数据-水质监测设备数量", notes = "大屏-数据-水质监测设备数量")
    @GetMapping(value = "/water-quality")
    public ResultData<NumDataVo> waterQualityNum() {
        ResultData<NumDataVo> resultData = new ResultData<>();
        try {
            NumDataVo numDataVo = rdpInfoService.getEquipmentNumByType("水质监测", TableConstant.WATER_TYPE);
            resultData.setData(numDataVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 机房设备
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-机房设备数量")
    @ApiOperation(value = "大屏-数据-机房设备数量", notes = "大屏-数据-机房设备数量")
    @GetMapping(value = "/engine")
    public ResultData<NumDataVo> engineNum() {
        ResultData<NumDataVo> resultData = new ResultData<>();
        try {
            NumDataVo numDataVo = rdpInfoService.getEquipmentNumByType("机房设备", TableConstant.ENGINE_TYPE);
            resultData.setData(numDataVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 广播设备
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-广播设备数量")
    @ApiOperation(value = "大屏-数据-广播设备数量", notes = "大屏-数据-广播设备数量")
    @GetMapping(value = "/broadcast")
    public ResultData<NumDataVo> broadcastNum() {
        ResultData<NumDataVo> resultData = new ResultData<>();
        try {
            NumDataVo numDataVo = rdpInfoService.getEquipmentNumByType("广播设备", TableConstant.BROADCAST_TYPE);
            resultData.setData(numDataVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 视频监控
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-视频监控")
    @ApiOperation(value = "大屏-数据-视频监控", notes = "大屏-数据-视频监控")
    @GetMapping(value = "/monitor")
    public ResultData<NumDataVo> monitorNum() {
        ResultData<NumDataVo> resultData = new ResultData<>();
        try {
            NumDataVo numDataVo = rdpInfoService.getEquipmentNumByType("视频监控", TableConstant.VIDEO_SURVEILLANCE_TYPE);
            resultData.setData(numDataVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 区域信息
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-区域信息")
    @ApiOperation(value = "大屏-数据-区域信息", notes = "大屏-数据-区域信息")
    @GetMapping(value = "/regions")
    public ResultData<List<RegionVo>> getRegionList() {
        ResultData<List<RegionVo>> resultData = new ResultData<List<RegionVo>>();
        try {
            List<RegionVo> regionList = rdpInfoService.getRegionList();
            resultData.setData(regionList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 河流信息
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-河流信息")
    @ApiOperation(value = "大屏-数据-河流信息", notes = "大屏-数据-河流信息")
    @GetMapping(value = "/rivers")
    public ResultData<List<RiverVo>> getRiverList() {
        ResultData<List<RiverVo>> resultData = new ResultData<List<RiverVo>>();
        try {
            List<RiverVo> riverList = rdpInfoService.getRiverList();

            resultData.setData(riverList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 区域内项目列表
     *
     * @return
     */
    @AutoLog(value = "大屏-数据-区域内重点项目列表")
    @ApiOperation(value = "大屏-数据-区域内重点项目列表", notes = "大屏-数据-区域内重点项目列表")
    @GetMapping(value = "/project")
    public ResultData<List<GisModelImgVo>> getProjectList(ProjectParam projectParam) {
        ResultData<List<GisModelImgVo>> resultData = new ResultData<List<GisModelImgVo>>();
        try {
            List<GisModelImgVo> projectList = rdpInfoService.getProjectList(projectParam);
            resultData.setData(projectList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     *  测试连接 influxdb 数据库查询数据
     *
     */
    @GetMapping(value = "/getInfluxData")
    public ResultData<EquIotShowData> getInfluxData() {
        ResultData<EquIotShowData> resultData = new ResultData<EquIotShowData>();
        try {
            EquIotShowData projectList = rdpInfoService.getInfluxData();
            resultData.setData(projectList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }


}
