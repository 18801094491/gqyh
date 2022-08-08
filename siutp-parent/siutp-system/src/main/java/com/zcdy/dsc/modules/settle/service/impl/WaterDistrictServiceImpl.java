package com.zcdy.dsc.modules.settle.service.impl;

import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.settle.entity.WaterDistrict;
import com.zcdy.dsc.modules.settle.mapper.WaterDistrictMapper;
import com.zcdy.dsc.modules.settle.service.WaterDistrictService;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 水表地区管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-21
 * @Version: V1.0
 */
@Service
public class WaterDistrictServiceImpl extends ServiceImpl<WaterDistrictMapper, WaterDistrict> implements WaterDistrictService {

	@Resource
	private WaterDistrictMapper waterDistrictMapper;
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void bindDistrict(String equipmentId, String districtId) {
		//这个水表其它绑定的地区改为失效
		WaterDistrict waterDistrict=new WaterDistrict();
		waterDistrict.setValidStatus(StatusConstant.INVALID);
		UpdateWrapper<WaterDistrict> updateWrapper=new UpdateWrapper<>();
		updateWrapper.lambda().eq(WaterDistrict::getEquipmentId, equipmentId);
		waterDistrictMapper.update(waterDistrict, updateWrapper);
		
		//重新插入一条数据
		waterDistrict.setEquipmentId(equipmentId);
		waterDistrict.setDistrictId(districtId);
		waterDistrict.setValidStatus(StatusConstant.VALID);
		waterDistrictMapper.insert(waterDistrict);
	}

}
