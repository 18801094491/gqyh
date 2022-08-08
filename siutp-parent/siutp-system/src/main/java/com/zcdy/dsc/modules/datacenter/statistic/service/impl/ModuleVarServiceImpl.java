package com.zcdy.dsc.modules.datacenter.statistic.service.impl;

import com.zcdy.dsc.modules.datacenter.statistic.entity.ModuleVar;
import com.zcdy.dsc.modules.datacenter.statistic.entity.ModuleVarOrder;
import com.zcdy.dsc.modules.datacenter.statistic.mapper.ModuleVarMapper;
import com.zcdy.dsc.modules.datacenter.statistic.mapper.ModuleVarOrderMapper;
import com.zcdy.dsc.modules.datacenter.statistic.service.ModuleVarService;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentVariableVO;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 描述: 统计模块变量关联表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-19
 * 版本号: V1.0
 */
@Service
public class ModuleVarServiceImpl extends ServiceImpl<ModuleVarMapper, ModuleVar> implements ModuleVarService {

	@Resource
	private ModuleVarMapper moduleVarMapper;
	
	@Resource
	private ModuleVarOrderMapper moduleVarOrderMapper;
	
	/*
	 * @see com.zcdy.dsc.modules.datacenter.statistic.service.ModuleVarService#queryModuleItem(java.lang.String)
	 */
	@Override
	public List<SysCateDropdown> queryModuleItem(String moduleId) {
		return this.moduleVarMapper.selectModuleItemData(moduleId);
	}

	/*
	 * @see com.zcdy.dsc.modules.datacenter.statistic.service.ModuleVarService#queryEquipAndVarByModuleId(java.lang.String, java.lang.String)
	 */
	@Override
	public List<EquipmentVariableVO> queryEquipAndVarByModuleId(String moduleId, String typeCode) {
		return this.moduleVarMapper.selectEquipAndVarByModuleId(moduleId, typeCode);
	}

	/*
	 * @see com.zcdy.dsc.modules.datacenter.statistic.service.ModuleVarService#queryAllEquipAndVarByModuleId(java.lang.String)
	 */
	@Override
	public List<EquipmentVariableVO> queryAllEquipAndVarByModuleId(String moduleId) {
		return this.moduleVarMapper.selectAllEquipAndVarByModuleId(moduleId);
	}

	/*
	 * @see com.zcdy.dsc.modules.datacenter.statistic.service.ModuleVarService#getVarOrder(java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getVarOrder(String moduleId) {
		QueryWrapper<ModuleVarOrder> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ModuleVarOrder::getModuleId, moduleId).select(ModuleVarOrder::getVarName, ModuleVarOrder::getDisplayOrder);
		List<Map<String, Object>> list = this.moduleVarOrderMapper.selectMaps(queryWrapper);
		return list;
	}

}
