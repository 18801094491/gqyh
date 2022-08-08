package com.zcdy.dsc.modules.operation.equipment.service.impl;

import com.zcdy.dsc.modules.collection.gis.vo.EquipmentAttrVO;
import com.zcdy.dsc.modules.operation.equipment.entity.OptEquipmentAttr;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptEquipmentAttrMapper;
import com.zcdy.dsc.modules.operation.equipment.service.IOptEquipmentAttrService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 描述: 资产设备属性信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-20
 * 版本号: V1.0
 */
@Service
public class OptEquipmentAttrServiceImpl extends ServiceImpl<OptEquipmentAttrMapper, OptEquipmentAttr> implements IOptEquipmentAttrService {

	@Autowired
	private OptEquipmentAttrMapper attrMapper;
	
	@Override
	public List<EquipmentAttrVO> queryAttrList(String equipmentId) {
		return attrMapper.queryAttrList(equipmentId);
	}

}
