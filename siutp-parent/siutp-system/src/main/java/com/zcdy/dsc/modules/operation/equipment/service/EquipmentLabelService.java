package com.zcdy.dsc.modules.operation.equipment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.equipment.entity.EquipmentLabel;

/**
 * @Description: 水表地区管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-21
 * @Version: V1.0
 */
public interface EquipmentLabelService extends IService<EquipmentLabel> {

	/**
	 * 描述: 设备绑定标签
	 * 创建人:  tangchao
	 * 创建时间:  2020-5-28 15:29:13
	 * 版本: V1.0
	 */
	void bindLabel(String equipmentId, String labelId);

}
