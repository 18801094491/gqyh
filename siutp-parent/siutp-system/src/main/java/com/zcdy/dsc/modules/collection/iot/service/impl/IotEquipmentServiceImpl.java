package com.zcdy.dsc.modules.collection.iot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipment;
import com.zcdy.dsc.modules.collection.iot.mapper.IotEquipmentMapper;
import com.zcdy.dsc.modules.collection.iot.service.IotEquipmentService;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipmentVo;

/**
 * 描述: 模型设备维护
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-26
 * 版本号: V1.0
 */
@Service
public class IotEquipmentServiceImpl extends ServiceImpl<IotEquipmentMapper, IotEquipment> implements IotEquipmentService {
    @Autowired
    private IotEquipmentMapper iotEquipmentMapper;
    @Override
    public List<IotEquipment> queryIotEquipmentByName(String iotSn, String iotName,String id) {
        return iotEquipmentMapper.queryIotEquipmentByName(iotSn,iotName,id);
    }

    @Override
    public IPage<IotEquipmentVo> queryPageList(Page<IotEquipmentVo> page, IotEquipment iotEquipment) {
        return iotEquipmentMapper.queryPageList(page,iotEquipment);
    }

    @Override
    public void updateCycleByCate(String iotCategory, String iotCycle) {
        iotEquipmentMapper.updateCycleByCate(iotCategory,iotCycle);
    }

	/*
	 * @see com.zcdy.dsc.modules.collection.iot.service.IotEquipmentService#listIdsByType(java.lang.String)
	 */
	@Override
	public List<String> listIdsByType(String typeCode) {
		return this.iotEquipmentMapper.selectIdsByType(typeCode);
	}

	@Override
	public String queryEquipmentById(String id) {
		return iotEquipmentMapper.queryEquipmentById(id);
	}


}
