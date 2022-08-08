package com.zcdy.dsc.modules.operation.equipment.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.vo.store.SparepartBillParamVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.SparepartDropdown;
import com.zcdy.dsc.modules.operation.equipment.vo.store.SparepartVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.StoreBillVo;

/**
 * 描述: 库存自定义接口
 * @author：  songguang.jiao
 * 创建时间： 2020年2月3日 下午9:12:40 
 * 版本号: V1.0
 */
public interface StoreBillService {


	/**
	 * 描述:  分页备品备件查询库存列表
	 * @return
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月11日 下午9:23:38 
	 * 版本号: V1.0
	 */
    IPage<SparepartVo> queryPageData(Page<SparepartVo> page, String sparepartName, String sparepartModel, String sparepartSpecs,String storeId);
	
	/**
	 * 描述: 出入库单分页列表
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月3日 下午9:25:11 
	 * 版本号: V1.0
	 */
	public IPage<StoreBillVo> queryBillPageList(Page<StoreBillVo> page,String sparepartName,String batchSn,String billType);
	
	/**
	 * 描述: 入库单新增，自动生成入库单号
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月4日 上午10:12:49 
	 * 版本号: V1.0
	 */
	public void addInBill(SparepartBillParamVo billParamVo);
	
	/**
	 * 描述: 出库单新增，自动生成出库单号
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月4日 上午10:12:49 
	 * 版本号: V1.0
	 */
	public void addOutBill(SparepartBillParamVo billParamVo);
	
	
	/**
	 * 描述: 校验是否超过库存
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月4日 下午3:56:11 
	 * 版本号: V1.0
	 */
	public Boolean checkBillStore(SparepartBillParamVo billParamVo);

	/**
	 * 描述: 查询备品备件下拉列表
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月4日 下午1:35:53 
	 * 版本号: V1.0
	 */
	public List<SparepartDropdown> dropdown(String sparepartName);

}
