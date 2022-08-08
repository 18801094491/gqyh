package com.zcdy.dsc.modules.collection.iot.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zcdy.dsc.modules.collection.iot.entity.IOTVarData;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.mapper.IotDataMapper;
import com.zcdy.dsc.modules.collection.iot.service.IotDataService;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentInfoVO;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptEquipmentMapper;

/**
 * @author： Roberto
 * 创建时间：2020年3月18日 下午3:41:54
 * 描述: <p></p>
 */
@Service("iotDataService")
public class IotDataServiceImpl implements IotDataService {

    @Resource
    private IotDataMapper iotDataMapper;

    @Resource
    private OptEquipmentMapper optEquipmentMapper;

    /*
     * @see com.zcdy.dsc.modules.collection.iot.service.IotDataService#queryVariableInfoByEquipmentId(java.lang.String)
     */
    @Override
    public List<VariableInfo> queryVariableInfoByEquipmentId(String equipmentId) {
        return iotDataMapper.selectVarInfo(equipmentId);
    }

    /*
     * @see com.zcdy.dsc.modules.collection.iot.service.IotDataService#queryIotHisData(java.lang.String)
     */
//	@DS("slave")
    @Deprecated
    @Override
    public List<IOTVarData> queryIotHisData(String sql) {
        return iotDataMapper.executeHisQuery(sql);
    }

    /*
     * @see com.zcdy.dsc.modules.collection.iot.service.IotDataService#queryEquipment(java.lang.String)
     */
    @Override
    public List<EquipmentInfoVO> queryEquipment(String typeCode, String name, String monthBalance) {
        return this.optEquipmentMapper.selectSimpleDataByCodeName(typeCode, name, monthBalance);
    }

    /*
     * @see com.zcdy.dsc.modules.collection.iot.service.IotDataService#queryEquipmentById(java.lang.String)
     */
    @Override
    public EquipmentInfoVO queryEquipmentById(String id) {
        return this.optEquipmentMapper.selectSimpleDataById(id);
    }
}
