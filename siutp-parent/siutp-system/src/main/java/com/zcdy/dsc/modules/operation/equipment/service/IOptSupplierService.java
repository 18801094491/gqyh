package com.zcdy.dsc.modules.operation.equipment.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.equipment.entity.OptSupplier;
import com.zcdy.dsc.modules.operation.equipment.vo.SupplierListVo;
import com.zcdy.dsc.modules.operation.equipment.vo.SupplierVo;

/**
 * 描述: 供应商管理
 * @author： bthy
 * 创建时间：   2019-12-16
 * 版本号: V1.0
 */
public interface IOptSupplierService extends IService<OptSupplier> {

	/**
	 * 更改供应商状态
	 * @param supplierStatus
	 */
	public void startupAndShutdown(String id);
	
	
	/**
	 * 分页查询列表
	 * @return
	 */
	public IPage<SupplierVo>  getList(IPage<SupplierVo> page,String supplierSn,String supplierName,String startTime,String endTime);
	
	/**
	 * 查询供应商下拉列表
	 * @return
	 */
	public List<SupplierListVo> queryNameList(String name);

	/**
	 * 描述:检验供应商编号是否存在 
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月29日 下午1:55:58 
	 * 版本号: V1.0
	 */
	public Boolean checkSnExist(String id, String supplierSn);
	
}
