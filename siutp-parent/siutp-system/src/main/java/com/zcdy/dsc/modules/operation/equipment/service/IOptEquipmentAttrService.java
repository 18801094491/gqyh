package com.zcdy.dsc.modules.operation.equipment.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.collection.gis.vo.EquipmentAttrVO;
import com.zcdy.dsc.modules.operation.equipment.entity.OptEquipmentAttr;

/**
 * 描述: 资产设备属性信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-20
 * 版本号: V1.0
 */
public interface IOptEquipmentAttrService extends IService<OptEquipmentAttr> {

	/**
	 * 查询设备属性列表
	 * @param equipmentId
	 * @return
	 */
	List<EquipmentAttrVO> queryAttrList(String equipmentId);
}
