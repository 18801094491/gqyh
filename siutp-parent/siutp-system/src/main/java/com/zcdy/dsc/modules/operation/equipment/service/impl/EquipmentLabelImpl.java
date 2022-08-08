package com.zcdy.dsc.modules.operation.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.operation.equipment.entity.EquipmentLabel;
import com.zcdy.dsc.modules.operation.equipment.mapper.EquipmentLabelMapper;
import com.zcdy.dsc.modules.operation.equipment.service.EquipmentLabelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 水表地区管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-21
 * @Version: V1.0
 */
@Service
public class EquipmentLabelImpl extends ServiceImpl<EquipmentLabelMapper, EquipmentLabel> implements EquipmentLabelService {
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void bindLabel(String equipmentId, String labelId) {
		//这个水表其它绑定的标签失效
		EquipmentLabel waterDistrict=new EquipmentLabel();
		waterDistrict.setValidStatus(StatusConstant.INVALID);
		UpdateWrapper<EquipmentLabel> updateWrapper=new UpdateWrapper<>();
		updateWrapper.lambda().eq(EquipmentLabel::getEquipmentId, equipmentId);
		this.baseMapper.update(waterDistrict, updateWrapper);
		
		//重新插入一条数据
		waterDistrict.setEquipmentId(equipmentId);
		waterDistrict.setLabelId(labelId);
		waterDistrict.setValidStatus(StatusConstant.VALID);
		this.baseMapper.insert(waterDistrict);
	}

}
