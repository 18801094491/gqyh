package com.zcdy.dsc.modules.operation.equipment.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.operation.equipment.entity.Store;
import com.zcdy.dsc.modules.operation.equipment.entity.StoreTree;
import com.zcdy.dsc.modules.operation.equipment.vo.UserCodeName;
import com.zcdy.dsc.modules.operation.equipment.vo.store.BorrowHistoryVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.GoodsApplyListVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.GoodsBorrowVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.StoreDropdownVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.StoreUserName;
import com.zcdy.dsc.modules.operation.equipment.vo.store.StoreVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.ToolBillParamVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.ToolExportVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.ToolVo;

/**
 * 描述: 工具相关自定义接口
 * @author：  songguang.jiao
 * 创建时间：  2020年1月9日 上午10:56:11
 * 版本号: V1.0
 */
public interface IToolService {

	/**
	 * 描述: 分页查询仓库列表
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月9日 上午11:11:21
	 * 版本号: V1.0
	 */
	public IPage<StoreVo> queryStorePageList(IPage<StoreVo> page,String storeName,String storeSn);
	
	/**
	 * 描述: 添加仓库
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月9日 下午12:35:57
	 * 版本号: V1.0
	 */
	public void addStore(Store store);
	
	/**
	 * 描述: 分页查询工具列表
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月9日 下午1:09:44
	 * 版本号: V1.0
	 */
	public IPage<ToolVo> queryToolList(IPage<ToolVo> page,String toolName,String storeId,String toolModel);
	
	
	/**
	 * 描述: 查询仓库地址下拉选
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月9日 下午6:03:27
	 * 版本号: V1.0
	 */
	public List<StoreDropdownVo> queryStoreList();

	/**
	 * 描述: 更改仓库启停用状态
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月19日 下午3:36:58
	 * 版本号: V1.0
	 */
	public void editStoreStatus(String id);

	/**
	 * 描述: 查询用户下拉列表
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月19日 下午4:11:31
	 * 版本号: V1.0
	 */
	public List<StoreUserName> getUserList(String name);
	

	/**
	 * 描述: 增加工具库存
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月6日 下午5:40:13 
	 * 版本号: V1.0
	 */
	public void addToolStore(ToolBillParamVo optToolBill);

	/**
	 * 描述: 运维工具导出
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 上午10:04:32 
	 * 版本号: V1.0
	 */
	public List<ToolExportVo> exportCutomerList(String toolName,String storeId,String toolModel);

	/**
	 * 描述: 查询运维工具借用详情
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午4:31:11 
	 * 版本号: V1.0
	 */
	public IPage<GoodsBorrowVo> queryGoodsBorrowDetail(IPage<GoodsBorrowVo> page,String toolid,String itemSn,String batchSn);

	/**
	 * 描述: 批量更改货品状态
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午5:52:44 
	 * 版本号: V1.0
	 */
	public void changeGoodsStatus(String[] splitIds,String userId,String applySn);
	
	/**
	 * 描述: 校验是否存在非在库货品
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午6:16:00 
	 * 版本号: V1.0
	 */
	public Integer checkOtherGoodsStatus(String[] ids);

	/**
	 * 描述: 更改货物状态
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午10:03:56 
	 * 版本号: V1.0
	 */
	public void editGoodsStatus(String id, String itemStatus);

	/**
	 * 描述: 货物归还
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月7日 下午10:10:35 
	 * 版本号: V1.0
	 */
	public void backGoodsItem(String id, String itemStatus, String description);

	/**
	 * 描述: 运维工具借用历史
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月8日 下午3:40:34 
	 * 版本号: V1.0
	 */
	public IPage<BorrowHistoryVo> queryBorrowHistory(IPage<BorrowHistoryVo> page,String userName,String goodsId,String startTime,String endTime);

	/**
	 * 描述: 通过借用人查询未处理的申请单
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月8日 下午8:32:53 
	 * 版本号: V1.0
	 */
	public List<GoodsApplyListVo> queryGoodsApply(String userId);

	/**
	 * 描述: 用户下拉选code-value
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月10日 上午11:34:48 
	 * 版本号: V1.0
	 */
	public List<UserCodeName> getUserCodeNameList(String name);


	/**
	 * 描述:检验仓库名称是否存在 
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月29日 下午2:07:29 
	 * 版本号: V1.0
	 */
	public Boolean checkSoreNameExist(String id, String storeName);

	/**
	 * 描述:  删除仓库与管理员关系
	 * @param userId 
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月7日 下午4:48:39 
	 * 版本号: V1.0
	 */
	public void deleteStoreManager(String userId);

	/**
	 * 描述:  通过用户查看所管理的仓库
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月7日 下午4:58:57 
	 * 版本号: V1.0
	 */
	public List<String> queryStoreIdList(String manager);

	/**
	 * 描述:  查询树状仓库列表
	 * @return
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月7日 下午5:09:42 
	 * 版本号: V1.0
	 */
	List<StoreTree> getTreeStore();
	
}
