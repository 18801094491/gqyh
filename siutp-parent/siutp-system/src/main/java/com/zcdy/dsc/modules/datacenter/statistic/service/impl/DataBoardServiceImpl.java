package com.zcdy.dsc.modules.datacenter.statistic.service.impl;

import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointData;
import com.zcdy.dsc.modules.datacenter.statistic.mapper.DataBoardMapper;
import com.zcdy.dsc.modules.datacenter.statistic.service.DataBoardService;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquIotDataVO;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquIotShowData;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentInfo;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentsVo;
import com.zcdy.dsc.modules.datacenter.statistic.vo.HydrologysVo;
import com.zcdy.dsc.modules.rdp.constant.EquipmentParamConstant;
import com.zcdy.dsc.modules.rdp.constant.TableConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description:
 * @Author: 在信汇通
 * @Date: 2020-12-29 10:59:43
 * @Version: V1.0
 */

@Service
public class DataBoardServiceImpl implements DataBoardService {
    @Resource
    private InfluxService influxService;
    @Resource
    private DataBoardMapper dataBoardMapper;

    @Override
    public List<HydrologysVo> getHydrologyAllList() {
        //获取水文设备列表
        List<EquipmentsVo> equipmentsVoList = dataBoardMapper.equipmentList(TableConstant.HYDROLOGY_TYPE);
        return this.getHydrologyVoList(equipmentsVoList);
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
        sqlBuilder.append("select var_value from iot_point_data where time > now() - 5m and ");
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
        sqlBuilder.append(" limit ").append(1);
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

                hydrologysVoList.add(hydrologysVo);
            }
        }

        return hydrologysVoList;
    }
}
