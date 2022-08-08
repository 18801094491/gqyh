package com.zcdy.dsc.modules.settle.service;

import com.zcdy.dsc.modules.settle.entity.WaterDistrict;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 水表地区管理
 * @author: 智能无人硬核心项目组
 * @Date:   2020-04-21
 * @Version: V1.0
 */
public interface WaterDistrictService extends IService<WaterDistrict> {

	/**
	 * 水表绑定地区
	 * @param equipmentId 水表id
	 * @param districtId 地区id
	 */
	void bindDistrict(String equipmentId, String districtId);

}
