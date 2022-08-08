package com.zcdy.dsc.modules.operation.equipment.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.operation.equipment.entity.OptSupplier;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptSupplierMapper;
import com.zcdy.dsc.modules.operation.equipment.service.IOptSupplierService;
import com.zcdy.dsc.modules.operation.equipment.vo.SupplierListVo;
import com.zcdy.dsc.modules.operation.equipment.vo.SupplierVo;

/**
 * 描述: 供应商管理
 * @author： bthy
 * 创建时间：   2019-12-16
 * 版本号: V1.0
 */
@Service
public class OptSupplierServiceImpl extends ServiceImpl<OptSupplierMapper, OptSupplier> implements IOptSupplierService {
	
	@Autowired
	private OptSupplierMapper optSupplierMapper;
	
	@Override
	public void startupAndShutdown(String id) {
		optSupplierMapper.startupAndShutdown(id);
	}

	@Override
	public IPage<SupplierVo> getList(IPage<SupplierVo> page,String supplierSn,String supplierName,String startTime,String endTime) {
		return optSupplierMapper.getList(page,supplierSn,supplierName,startTime,endTime);
	}

	@Override
	public List<SupplierListVo> queryNameList(String name) {
		return optSupplierMapper.queryNameList(name);
	}

	@Override
	public Boolean checkSnExist(String id, String supplierSn) {
		return optSupplierMapper.checkSnExist(id,supplierSn);
	}


}
