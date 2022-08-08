package com.zcdy.dsc.modules.collection.gis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.collection.gis.entity.GisEquipment;
import com.zcdy.dsc.modules.collection.gis.mapper.GisEquipmentMapper;
import com.zcdy.dsc.modules.collection.gis.service.IGisEquipmentService;

/**
 * 描述: 模型设备
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-17
 * 版本号: V1.0
 */
@Service
public class GisEquipmentServiceImpl extends ServiceImpl<GisEquipmentMapper, GisEquipment> implements IGisEquipmentService {

	@Autowired
	private GisEquipmentMapper gisEquipmentMapper;
	
	@Override
	public GisEquipment getGisEquipmentByModelId(String gisId) {
		return gisEquipmentMapper.getGisEquipmentByModelId(gisId);
	}

	@Override
	public GisEquipment getByEquipId(String equipId) {
		return gisEquipmentMapper.getByEquipId(equipId);
	}

	@Override
	public void unbindGisData(String modelId) {
		gisEquipmentMapper.unbindGisData(modelId);
	}

}
