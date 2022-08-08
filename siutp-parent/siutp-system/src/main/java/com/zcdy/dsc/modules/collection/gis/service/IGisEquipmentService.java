package com.zcdy.dsc.modules.collection.gis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.collection.gis.entity.GisEquipment;

/**
 * 描述: 模型设备
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-17
 * 版本号: V1.0
 */
public interface IGisEquipmentService extends IService<GisEquipment> {

	/**
	 * 通过查询modelId查询中间表
	 * @param modelId
	 * @return
	 */
	public GisEquipment getGisEquipmentByModelId(String modelId) ;
	
	
	/**
	 * 通过查询equipmentId查询中间表
	 * @param equipId
	 * @return
	 */
	public GisEquipment getByEquipId(String equipId);
	
	/**
	 * 设备资产解绑gis模型数据
	 */
	public void unbindGisData(String modelId);
	
}
