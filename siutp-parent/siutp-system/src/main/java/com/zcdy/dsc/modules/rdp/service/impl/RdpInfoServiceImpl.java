package com.zcdy.dsc.modules.rdp.service.impl;

import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.common.framework.redis.RedisOperation;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelImgVo;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelResImgVo;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointData;
import com.zcdy.dsc.modules.operation.alarm.constant.WarnStatusConstant;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.rdp.constant.EquipmentParamConstant;
import com.zcdy.dsc.modules.rdp.constant.TableConstant;
import com.zcdy.dsc.modules.rdp.mapper.RdpInfoMapper;
import com.zcdy.dsc.modules.rdp.param.ProjectParam;
import com.zcdy.dsc.modules.rdp.service.RdpInfoService;
import com.zcdy.dsc.modules.rdp.util.GetPositionUtil;
import com.zcdy.dsc.modules.rdp.util.NumberUtil;
import com.zcdy.dsc.modules.rdp.util.TimeUtil;
import com.zcdy.dsc.modules.rdp.vo.*;
import com.zcdy.dsc.modules.worklist.constant.WorkListConstant;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import com.zcdy.dsc.modules.worklist.service.WorkListService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.geom.Point2D;
import java.util.*;

/**
 * @Description: 大屏-数据模块
 * @Author: 在信汇通
 * @Date: 2020-12-08
 * @Version: V1.0
 */
@Service
public class RdpInfoServiceImpl implements RdpInfoService {

    @Resource
    private RdpInfoMapper rdpInfoMapper;
    @Resource
    private InfluxService influxService;
    @Resource(name = "stringRedisOperation")
    private RedisOperation<String> stringRedisOperation;

    @Autowired
    private WorkListService workListService;
    /**
     * 项目访问基础路径
     */
    @Value("${com.zcdy.dsc.file.request.domain}")
    protected String baseStoragePath;

    @Override
    public String getTheTime(Date time) {
        String theTime = TimeUtil.getTheDateTime(time);
        String weekOfDate = TimeUtil.getWeekOfDate(time);
        return theTime + " " + weekOfDate;
    }

    @Override
    public TableDataVo getRealTimeEarlyWarningList() {
        String[] keyListStr = TableConstant.REAL_TIME_EARLY_WARNING_KEY;
        String[] nameListStr = TableConstant.REAL_TIME_EARLY_WARNING_TEXT;
        TableDataVo tableDataVo = new TableDataVo();
        List<ColumnDataVo> columnData = new ArrayList<>();
        //返回列表数据对象
        ResponseDataVo responseData = new ResponseDataVo();
        //返回表头数据列表
        for (int i = 0; i < keyListStr.length; i++) {
            //返回表头对象
            ColumnDataVo columnDataVo = new ColumnDataVo();
            columnDataVo.setKey(keyListStr[i]);
            columnDataVo.setText(nameListStr[i]);
            columnData.add(columnDataVo);
        }
        //地图页面展示未处理和初始化报警数据
        List<RealTimeEarlyWarningVo> realTimeEarlyWarningVoList = rdpInfoMapper.queryRealTimeEarlyWarningVoList(WarnStatusConstant.UNDEAL, WarnStatusConstant.INIT);
        //返回参数-数据对象列表
        List<Map<String, Object>> resDataList = new ArrayList<>();
        if (realTimeEarlyWarningVoList != null && realTimeEarlyWarningVoList.size() != 0) {
            for (RealTimeEarlyWarningVo bwvo : realTimeEarlyWarningVoList) {
                //返回参数-数据对象
                Map<String, Object> resData = new HashMap<>();
                /*for (int i = 0; i < keyListStr.length; i++) {
                    resData.put(keyListStr[i], i == 0 ? bwvo.getWarnLevel() :
                            (i == 1 ? bwvo.getEquipmentType() :
                                    (i == 2 ? bwvo.getWarnContent() : bwvo.getWarnTime())));
                }*/
                resData.put(keyListStr[0], bwvo.getWarnLevel());
                resData.put(keyListStr[1], bwvo.getWarnType());
                resData.put(keyListStr[2], bwvo.getWarnContent());
                resData.put(keyListStr[3], bwvo.getWarnTime());
                resDataList.add(resData);
            }
            responseData.setData(resDataList);
        } else {
            responseData.setData(resDataList);
            responseData.setTotals(0);
        }
        tableDataVo.setColumnData(columnData);
        tableDataVo.setResponseData(responseData);
        return tableDataVo;
    }

    @Override
    public TableDataVo getWorkJobList() {
        String[] keyListStr = TableConstant.WORK_JOB_KEY;
        String[] nameListStr = TableConstant.WORK_JOB_TEXT;
        //返回数据对象
        TableDataVo tableDataVo = new TableDataVo();
        List<ColumnDataVo> columnData = new ArrayList<>();
        ResponseDataVo responseData = new ResponseDataVo();
        //返回列表数据对象
        //返回表头数据列表
        for (int i = 0; i < keyListStr.length; i++) {
            //返回表头对象
            ColumnDataVo columnDatavo = new ColumnDataVo();
            columnDatavo.setKey(keyListStr[i]);
            columnDatavo.setText(nameListStr[i]);
            columnData.add(columnDatavo);
        }

        WorkList workList = new WorkList();
        workList.setDelFlag(DelFlagConstant.NORMAL);
        workList.setQueryStatusCode(WorkListConstant.WORK_LIST_STATUS_CODE);
        Date today = new Date();//今天
        Date startDate = DateUtil.addDay(today, -6);//一周内
        String todayStr = DateUtil.date2String(today, DateUtil.dateFormatStr);//取日期
        String startStr = DateUtil.date2String(startDate, DateUtil.dateFormatStr);//取日期
        today = DateUtil.string2Date(todayStr + " 23:59:59", DateUtil.dateTimeFormatStr);//加上时间
        startDate = DateUtil.string2Date(startStr + " 00:00:00", DateUtil.dateTimeFormatStr);//加上时间
        workList.setStartDate(startDate);
        workList.setOverDate(today);
        List<WorkList> workLists = workListService.getWorkList7DayList(workList);
        //返回参数-数据对象列表（未完成-工单表未设计，无实际数据，所以先走else出个模拟数据）
        List<Map<String, Object>> resDataList = new ArrayList<>();
        if (workLists != null && workLists.size() != 0) {
            for (WorkList workList1 : workLists) {
                //返回参数-数据对象
                Map<String, Object> resData = new HashMap<>();
                resData.put(keyListStr[0], workList1.getName());
                resData.put(keyListStr[1], workList1.getStatusDes());
                resData.put(keyListStr[2], workList1.getLeaderName());
                resDataList.add(resData);
            }
            responseData.setData(resDataList);
        }
        tableDataVo.setResponseData(responseData);
        tableDataVo.setColumnData(columnData);
        return tableDataVo;
    }

    @Override
    public List<SunriseChartVo> getWaterQualityDataList() {
        //返回数据对象
        List<SunriseChartVo> sunriseChartDataList = new ArrayList<>();
        //初始化数据
        String[] waterQualityLevelList = TableConstant.WATER_QUALITY_TYPE;

        //获取水质设备实时数据
        //循环判断每个设备是几类水质
        //写一个判断水质是几类的方法

        //虚拟数据
        //List<EquipmentsVo> equipmentsVoList = getEquipments();
        //真实数据
        List<EquipmentsVo> equipmentsVoList = this.getEquipmentsVoList();
        //第一层
        for (int i = 0; i < waterQualityLevelList.length; i++) {
            List<SunriseChartVo> sunriseChartVoList = new ArrayList<>();
            //初始化最底层九参设备列表
            List<SunriseChartChildrenVo> sunriseChartChildrenVoList9 = new ArrayList<>();
            //初始化最底层七参设备列表
            List<SunriseChartChildrenVo> sunriseChartChildrenVoList7 = new ArrayList<>();
            //循环设备判断参数
            for (EquipmentsVo equipmentsVo : equipmentsVoList) {
                //如果设备的水质类型类型属于该类  则新增
                if (equipmentsVo != null) {
                    if (equipmentsVo.getType() != null) {
                        if (equipmentsVo.getType() == i + 1) {
                            SunriseChartVo sunriseChartVo = new SunriseChartVo();
                            //如果设备是九参
                            if (TableConstant.WATER_QUALITY_9.equals(equipmentsVo.getEquipmentClass())) {
                                SunriseChartChildrenVo sccv = new SunriseChartChildrenVo();
                                sccv.setName(equipmentsVo.getName());
                                sunriseChartChildrenVoList9.add(sccv);
                            } else if (TableConstant.WATER_QUALITY_7.equals(equipmentsVo.getEquipmentClass())) {
                                SunriseChartChildrenVo sccv = new SunriseChartChildrenVo();
                                sccv.setName(equipmentsVo.getName());
                                sunriseChartChildrenVoList7.add(sccv);
                            }
                        }
                    }
                }

            }
            //如果列表不为空则在对象数组中新增
            if (sunriseChartChildrenVoList9.size() != 0) {
                SunriseChartVo sunriseChartVo = new SunriseChartVo();
                sunriseChartVo.setName(TableConstant.WATER_QUALITY_9);
                sunriseChartVo.setChildren(sunriseChartChildrenVoList9);
                sunriseChartVoList.add(sunriseChartVo);
            }
            if (sunriseChartChildrenVoList7.size() != 0) {
                SunriseChartVo sunriseChartVo = new SunriseChartVo();
                sunriseChartVo.setName(TableConstant.WATER_QUALITY_7);
                sunriseChartVo.setChildren(sunriseChartChildrenVoList7);
                sunriseChartVoList.add(sunriseChartVo);
            }
            if (sunriseChartVoList.size() != 0) {
                SunriseChartVo sunriseChartData = new SunriseChartVo();
                sunriseChartData.setName(waterQualityLevelList[i]);
                sunriseChartData.setChildren(sunriseChartVoList);
                sunriseChartDataList.add(sunriseChartData);
            }
        }

        return sunriseChartDataList;
    }

    @Override
    public List<PieChartVo> getWaterQualityScaleDataList() {
        //返回数据对象
        List<PieChartVo> sunriseChartDataList = new ArrayList<>();
        //初始化数据
        String[] waterQualityLevelList = TableConstant.WATER_QUALITY_TYPE;
        //获取水质设备实时数据
        List<EquipmentsVo> equipmentsVoList = this.getEquipmentsVoList();
        //第一层
        for (int i = 0; i < waterQualityLevelList.length; i++) {
            //当前水质设备数量
            int currentWaterQualityCount = 0;
            //循环设备判断参数
            for (EquipmentsVo equipmentsVo : equipmentsVoList) {
                //如果设备的水质类型类型属于该类  则新增
                if (equipmentsVo != null && equipmentsVo.getType() != null && equipmentsVo.getType() == i + 1) currentWaterQualityCount ++;
            }
            PieChartVo pieChartData = new PieChartVo();
            pieChartData.setName(waterQualityLevelList[i]);
            pieChartData.setValue(NumberUtil.percentageResult(currentWaterQualityCount, equipmentsVoList.size()));
            sunriseChartDataList.add(pieChartData);
        }
        return sunriseChartDataList;
    }

    @Override
    public List<HydrologysVo> getHydrologyAllList() {
        //获取水文设备列表
        List<EquipmentsVo> equipmentsVoList = rdpInfoMapper.equipmentList(TableConstant.HYDROLOGY_TYPE);
        return this.getHydrologyVoList(equipmentsVoList);
    }

    @Override
    public NumDataVo getEquipmentNumByType(String labelName, String equipmentType) {
        NumDataVo numDataVo = new NumDataVo();
        numDataVo.setName(labelName);
        List<EquipmentsVo> equipmentsVoList = rdpInfoMapper.equipmentList(equipmentType);
        if (equipmentsVoList != null) {
            numDataVo.setNum(equipmentsVoList.size());
        } else {
            numDataVo.setNum(0);
        }
        return numDataVo;
    }

    @Override
    public List<RegionVo> getRegionList() {
        return rdpInfoMapper.regionList();
    }

    @Override
    public List<RiverVo> getRiverList() {
        return rdpInfoMapper.riverList();
    }

    @Override
    public List<GisModelImgVo> getProjectList(ProjectParam projectParam) {
        //获取区域信息
        RegionVo regionVo = rdpInfoMapper.getRegionById(projectParam);
        //获取全部重点项目的经纬度点
        List<GisModelImgVo> projectsVoList = rdpInfoMapper.getProjectList(TableConstant.PROJECT_CODE);

        List<GisModelImgVo> projectsVos = new ArrayList<>();
        //判断重点项目点在区域内的列表
        for (GisModelImgVo gisModelImgVo : projectsVoList) {
            String[] area = String.valueOf(regionVo.getRegionAddressInfo()).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\"lng\":", "").replaceAll("\"lat\":", "").split(",|;");
            //判断是否在围栏内的具体实现
            List<Point2D.Double> pts = new ArrayList<Point2D.Double>();
            Point2D.Double point = new Point2D.Double(Double.parseDouble(gisModelImgVo.getLongitude()), Double.parseDouble(gisModelImgVo.getLatitude()));
            for (int i = 0; i < area.length; i++) {
                pts.add(new Point2D.Double(Double.parseDouble(area[i]), Double.parseDouble(area[i + 1])));
                i++;
            }
            boolean b = GetPositionUtil.IsPtInPoly(point, pts);
            if (b) {
                // 如果Gis正常图片跟打开状态都为空,就不返回给全量地图数据
                if (StringUtils.isEmpty(gisModelImgVo.getImg()) && StringUtils.isEmpty(gisModelImgVo.getOnImg())) {
                    continue;
                }
                if (!StringUtils.isEmpty(gisModelImgVo.getImg())) {
                    GisModelResImgVo modelImg = new GisModelResImgVo();
                    modelImg.setImgUrl(baseStoragePath + gisModelImgVo.getImg());
                    modelImg.setWidth(gisModelImgVo.getWidth());
                    modelImg.setHeight(gisModelImgVo.getHeight());
                    gisModelImgVo.setModelImg(modelImg);
                }
                if (!StringUtils.isEmpty(gisModelImgVo.getWaringImg())) {
                    GisModelResImgVo modelImg = new GisModelResImgVo();
                    modelImg.setImgUrl(baseStoragePath + gisModelImgVo.getWaringImg());
                    modelImg.setWidth(gisModelImgVo.getWidth());
                    modelImg.setHeight(gisModelImgVo.getHeight());
                    gisModelImgVo.setModelWaringImg(modelImg);
                }
                if (!StringUtils.isEmpty(gisModelImgVo.getOnImg())) {
                    GisModelResImgVo modelImg = new GisModelResImgVo();
                    modelImg.setImgUrl(baseStoragePath + gisModelImgVo.getOnImg());
                    modelImg.setWidth(gisModelImgVo.getWidth());
                    modelImg.setHeight(gisModelImgVo.getHeight());
                    gisModelImgVo.setModelOnImg(modelImg);
                }
                if (!StringUtils.isEmpty(gisModelImgVo.getOffImg())) {
                    GisModelResImgVo modelImg = new GisModelResImgVo();
                    modelImg.setImgUrl(baseStoragePath + gisModelImgVo.getOffImg());
                    modelImg.setWidth(gisModelImgVo.getWidth());
                    modelImg.setHeight(gisModelImgVo.getHeight());
                    gisModelImgVo.setModelOffImg(modelImg);
                }
                projectsVos.add(gisModelImgVo);
            }
        }

        return projectsVos;
    }

    @Override
    public EquIotShowData getInfluxData() {
        List<EquipmentInfo> equipmentsVoList = new ArrayList<>();
        return getEquipmentEquIotShowData(equipmentsVoList);
    }

    /**
     * 根据设备变量处理设备对象 看看该对象是几级水质 是什么类型
     */
    private List<EquipmentsVo> getEquipmentsVoList() {
        //获取水质设备及变量列表
        List<EquipmentsVo> equipmentsVoList = rdpInfoMapper.equipmentList(TableConstant.WATER_TYPE);
        if (equipmentsVoList != null) {
            for (EquipmentsVo equipmentsVo : equipmentsVoList) {
                //获取设备的参数值
                EquIotShowData iotShowData = this.getEquipmentEquIotShowData(equipmentsVo.getEquipmentInfoList());
                if (iotShowData != null) {
                    List<EquIotDataVO> iotDataVOList = iotShowData.getEquIotDataVOS();
                    equipmentsVo.setEquipmentClass(iotShowData.getEquIotDataVOS().size() == 9 ? TableConstant.WATER_QUALITY_9 : TableConstant.WATER_QUALITY_7);
                    //处理设备参数，判断该设备所处环境属于几级水质
                    int type = this.isEquipmentType(iotDataVOList);
                    equipmentsVo.setType(type);
                }

            }
        }
        return equipmentsVoList;
    }

    /**
     * 根据设备获取设备变量
     */
    private EquIotShowData getEquipmentEquIotShowData(List<EquipmentInfo> varInfos) {
        EquIotShowData iotShowData = new EquIotShowData();

        if (varInfos.size() == 0) {
            return null;
        }
        Map<String, EquipmentInfo> varMap = new HashMap<String, EquipmentInfo>(varInfos.size());
        List<String> varNamesSqlParam = new ArrayList<>();
        Set<String> headerTitle = new TreeSet<>();
        varInfos.forEach(item -> {
            varMap.put(item.getVarName(), item);
            varNamesSqlParam.add(item.getVarName());
            //对阀门特殊处理
            headerTitle.add(item.getVarTitle());
//            item.getVariableType();
        });

        List<EquIotDataVO> data = new ArrayList<>(varInfos.size());
        int size = varNamesSqlParam.size();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select var_value from iot_point_data where time > now() - 10m and ");
        sqlBuilder.append("(");

        int index = 0;
        for (; index < size; index++) {
            if (index > 0) {
                sqlBuilder.append(" or ");
            }
            String var_name = varNamesSqlParam.get(index);
            sqlBuilder.append("var_name='");
            sqlBuilder.append(var_name);
            sqlBuilder.append("'");
        }
        sqlBuilder.append(") ");
        sqlBuilder.append(" group by var_name").append(" order by time desc");
        sqlBuilder.append(" limit ").append(1).append(" tz('Asia/Shanghai')");
        List<PointData> pointDatas = this.influxService.query(sqlBuilder.toString(), PointData.class);

        if (pointDatas.size() != 0) {
            for (PointData pointData : pointDatas) {
                EquIotDataVO dataVO = new EquIotDataVO();
                dataVO.setVariableName(pointData.getVarName());
                dataVO.setVaribleValue(pointData.getVarValue());
                for(EquipmentInfo equipmentInfo :varInfos){
                    if (equipmentInfo.getVarName().equals(pointData.getVarName())){
                        dataVO.setVariableType(equipmentInfo.getVariableType());
                    }
                }
                data.add(dataVO);
            }
        }
        iotShowData.setEquIotDataVOS(data);
        return iotShowData;
    }

    /**
     * 根据设备属性判断设备是几级水质
     * isEquipmentType
     */
    private int isEquipmentType(List<EquIotDataVO> equIotDataVOS) {
        int type = 1;
        if (equIotDataVOS != null) {
            for (EquIotDataVO equIotDataVO : equIotDataVOS) {
                if(equIotDataVO.getVariableType() != null) {
                    switch (equIotDataVO.getVariableType()) {
                        case EquipmentParamConstant.AD_CODE:
                            if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.AD_1 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.AD_1) {
                                int t = 1;
                                break;
                            } else if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.AD_2 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.AD_2) {
                                int t = 2;
                                type = Math.max(t, type);
                                break;
                            } else if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.AD_3 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.AD_3) {
                                int t = 3;
                                type = Math.max(t, type);
                                break;
                            } else if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.AD_4 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.AD_4) {
                                int t = 4;
                                type = Math.max(t, type);
                                break;
                            } else {
                                int t = 5;
                                type = Math.max(t, type);
                                break;
                            }
                        case EquipmentParamConstant.COD_CODE:
                            if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.COD_1 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.COD_1) {
                                int t = 1;
                                break;
                            } else if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.COD_2 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.COD_2) {
                                int t = 2;
                                type = Math.max(t, type);
                                break;
                            } else if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.COD_3 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.COD_3) {
                                int t = 3;
                                type = Math.max(t, type);
                                break;
                            } else if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.COD_4 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.COD_4) {
                                int t = 4;
                                type = Math.max(t, type);
                                break;
                            } else {
                                int t = 5;
                                type = Math.max(t, type);
                                break;
                            }
                        case EquipmentParamConstant.ZD_CODE:
                            if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.ZD_1 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.ZD_1) {
                                int t = 1;
                                break;
                            } else if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.ZD_2 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.ZD_2) {
                                int t = 2;
                                type = Math.max(t, type);
                                break;
                            } else if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.ZD_3 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.ZD_3) {
                                int t = 3;
                                type = Math.max(t, type);
                                break;
                            } else if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.ZD_4 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.ZD_4) {
                                int t = 4;
                                type = Math.max(t, type);
                                break;
                            } else {
                                int t = 5;
                                type = Math.max(t, type);
                                break;
                            }
                        case EquipmentParamConstant.ZL_CODE:
                            if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.ZL_1 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.ZL_1) {
                                int t = 1;
                                break;
                            } else if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.ZL_2 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.ZL_2) {
                                int t = 2;
                                type = Math.max(t, type);
                                break;
                            } else if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.ZL_3 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.ZL_3) {
                                int t = 3;
                                type = Math.max(t, type);
                                break;
                            } else if (Double.parseDouble(equIotDataVO.getVaribleValue()) < EquipmentParamConstant.ZL_4 || Double.parseDouble(equIotDataVO.getVaribleValue()) == EquipmentParamConstant.ZL_4) {
                                int t = 4;
                                type = Math.max(t, type);
                                break;
                            } else {
                                int t = 5;
                                type = Math.max(t, type);
                                break;
                            }
                        default:
                            break;
                    }
                }
            }
        }
        return type;
    }

    /**
     * 水文设备的参数处理
     */
    private List<HydrologysVo> getHydrologyVoList(List<EquipmentsVo> equipmentsVoList) {
        List<HydrologysVo> hydrologysVoList = new ArrayList<>();
        if (equipmentsVoList != null) {
            for (EquipmentsVo equipmentsVo : equipmentsVoList) {
                HydrologysVo hydrologysVo = new HydrologysVo();
                //获取设备的参数值
                EquIotShowData iotShowData = this.getEquipmentEquIotShowData(equipmentsVo.getEquipmentInfoList());
                if (iotShowData != null) {
                    List<EquIotDataVO> data = iotShowData.getEquIotDataVOS();
                    if (data != null) {
                        for (EquIotDataVO equIotDataVO : data) {
                            hydrologysVo.setNo(equIotDataVO.getVariableName());
                            if(equIotDataVO.getVariableType() != null) {
                                switch (equIotDataVO.getVariableType()) {
                                    case EquipmentParamConstant.JCYW_CODE:
                                        hydrologysVo.setSedimentThickness(equIotDataVO.getVaribleValue());
                                        break;
                                    case EquipmentParamConstant.LL_CODE:
                                        hydrologysVo.setCurrentSpeed(equIotDataVO.getVaribleValue());
                                        break;
                                    case EquipmentParamConstant.YL_CODE:
                                        hydrologysVo.setRainfall(equIotDataVO.getVaribleValue());
                                        break;
                                    case EquipmentParamConstant.DNHD_CODE:
                                        hydrologysVo.setDetectionOfLiquidLevel(equIotDataVO.getVaribleValue());
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                }

                hydrologysVoList.add(hydrologysVo);
            }
        }

        return hydrologysVoList;
    }
}
