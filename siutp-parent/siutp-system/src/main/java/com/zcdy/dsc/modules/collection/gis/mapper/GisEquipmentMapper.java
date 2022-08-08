package com.zcdy.dsc.modules.collection.gis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.collection.gis.entity.GisEquipment;

/**
 * 描述: 模型设备
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-17
 * 版本号: V1.0
 */
public interface GisEquipmentMapper extends BaseMapper<GisEquipment> {
	
	/**
	 * 通过查询modelId中间表
	 * @param gisId
	 * @return
	 */
	public GisEquipment getGisEquipmentByModelId(String modelId) ;
	
	/**
	 * 通过查询equipId中间表
	 * @param equipId
	 * @return
	 */
	public GisEquipment getByEquipId(String equipId);
	
	/**
	 * 设备资产解绑gis模型数据
	 */
	public void unbindGisData(String modelId);
}
