package com.zcdy.dsc.modules.operation.equipment.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.common.util.UUIDGenerator;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.constant.GoodsItemStatus;
import com.zcdy.dsc.modules.operation.equipment.constant.GoodsItemType;
import com.zcdy.dsc.modules.operation.equipment.entity.OptGoodsItem;
import com.zcdy.dsc.modules.operation.equipment.entity.OptTool;
import com.zcdy.dsc.modules.operation.equipment.entity.OptToolBill;
import com.zcdy.dsc.modules.operation.equipment.entity.OptToolBorrow;
import com.zcdy.dsc.modules.operation.equipment.entity.Store;
import com.zcdy.dsc.modules.operation.equipment.entity.StoreTree;
import com.zcdy.dsc.modules.operation.equipment.mapper.OperatToolMapper;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptToolBillMapper;
import com.zcdy.dsc.modules.operation.equipment.mapper.StoreBillDao;
import com.zcdy.dsc.modules.operation.equipment.mapper.ToolDao;
import com.zcdy.dsc.modules.operation.equipment.service.IStoreService;
import com.zcdy.dsc.modules.operation.equipment.service.IToolService;
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
 * 描述: 工具相关自定义接口实现类
 * @author：  songguang.jiao
 * 创建时间：  2020年1月9日 上午10:55:47
 * 版本号: V1.0
 */
@Service
public class ToolServiceImpl implements IToolService {

	@Autowired
	private IStoreService storeService;
	
	@Autowired
	private ToolDao toolsDao;
	
	@Autowired
	private OptToolBillMapper toolBillMapper;
	
	@Autowired
	private OperatToolMapper operatToolMapper;
	
	@Autowired
	private StoreBillDao storeBillDao;
	
	@Override
	public IPage<StoreVo> queryStorePageList(IPage<StoreVo> page, String storeName, String storeSn) {
		return toolsDao.queryStorePageList(page, storeName, storeSn);
	}

	@Override
	public synchronized void addStore(Store store) {
		store.setStoreSn(this.getStoreSn());
		storeService.save(store);
	}
	
	//生成仓库编号 C+6位编号
	private String getStoreSn() {
		String cutomerSn = "C";
		String lastSnNum = toolsDao.getLastStoreSn();
		if (StringUtils.isEmpty(lastSnNum)) {
			cutomerSn = cutomerSn + "000001";
		} else {
			String number = StringUtils.leftPad(Integer.parseInt(lastSnNum)+1+"", 6, "0");
			cutomerSn = cutomerSn + number;
		}
		return cutomerSn;
	}

	@Override
	public IPage<ToolVo> queryToolList(IPage<ToolVo> page, String toolName, String storeId,String toolModel) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> storeList =toolsDao.queryStoreIdList(sysUser.getId());
		if(storeList.size()==0 || storeList.isEmpty()){
			return null;
		}
		return toolsDao.queryToolList(page, toolName, storeId,toolModel,storeList);
	}


	@Override
	public List<StoreDropdownVo> queryStoreList() {
		return toolsDao.queryStoreList();
	}

	@Override
	public void editStoreStatus(String id) {
		toolsDao.editStoreStatus(id);
	}

	@Override
	public List<StoreUserName> getUserList(String name) {
		return toolsDao.getUserList(name);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public synchronized void addToolStore(ToolBillParamVo optToolBill) {
		// 校验备品备件是否存在,存在就直接入库,不存在首先新增再入库
		QueryWrapper<OptTool> queryWrapper=new QueryWrapper<>();
		queryWrapper.lambda().eq(OptTool::getToolName, optToolBill.getToolName())
		.eq(OptTool::getToolModel, optToolBill.getToolModel())
		.eq(OptTool::getStoreId, optToolBill.getStoreId())
		.eq(OptTool::getSupplierId, optToolBill.getSupplierId());
		OptTool optTool = operatToolMapper.selectOne(queryWrapper);
		if(optTool==null){
			optTool=new OptTool();
			BeanUtils.copyProperties(optToolBill, optTool);
			operatToolMapper.insert(optTool);
		}
		//插入入库单，更新工具表，插入货品表
		OptToolBill bill=new OptToolBill();
		BeanUtils.copyProperties(optToolBill, bill);
		bill.setToolId(optTool.getId());
		bill.setBillSn(this.getMaxInBill());
		toolBillMapper.insert(bill);
		List<OptGoodsItem> list=new ArrayList<>(optToolBill.getAmount());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		Date date=new Date();
		for (int i = 1; i <= optToolBill.getAmount(); i++) {
			OptGoodsItem goodsItem=new OptGoodsItem();
			goodsItem.setId(UUIDGenerator.generate());
			goodsItem.setBatchSn(optToolBill.getBatchSn());
			goodsItem.setCreateBy(sysUser.getUsername());
			goodsItem.setCreateTime(date);
			goodsItem.setDataId(optTool.getId());
			goodsItem.setDelFlag(DelFlagConstant.NORMAL);
			goodsItem.setItemSn(""+i);
			goodsItem.setItemStatus(GoodsItemStatus.NORMAL);
			goodsItem.setItemType(GoodsItemType.TOOLS);
			list.add(goodsItem);
		}
		storeBillDao.insertGoodsItems(list);
		
	}
	//获取最大入库编号
	private String getMaxInBill(){
		String sn ="R"+ new SimpleDateFormat("yyyyMMdd").format(new Date());
		String maxInBill = toolsDao.getMaxInBill();
		if(StringUtils.isEmpty(maxInBill)){
			sn=sn+"001";
		}else{
			String leftPad = StringUtils.leftPad(Integer.parseInt(maxInBill)+1+"", 3,"0");
			sn=sn+leftPad;
		}
		return sn;
	}

	@Override
	public List<ToolExportVo> exportCutomerList(String toolName,String storeId,String toolModel) {
		 return toolsDao.exportCutomerList(toolName, storeId, toolModel);
	}

	@Override
	public IPage<GoodsBorrowVo> queryGoodsBorrowDetail(IPage<GoodsBorrowVo> page,String toolid,String itemSn,String batchSn) {
		return toolsDao.queryGoodsBorrowDetail(page,toolid,itemSn,batchSn);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void changeGoodsStatus(String[] splitIds,String userId,String applySn) {
		//更新货品为已出库，产生借用历史,更新申请单为已处理
		toolsDao.updateGoodsStatus(splitIds, GoodsItemStatus.BORROW);
		if(!StringUtils.isEmpty(applySn)){
			toolsDao.updateApplyDealStatus(applySn);
		}
		List<OptToolBorrow> list=new ArrayList<OptToolBorrow>(splitIds.length);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		Date date=new Date();
		for (int i = 0; i < splitIds.length; i++) {
			OptToolBorrow toolBorrow=new OptToolBorrow();
			toolBorrow.setId(UUIDGenerator.generate());
			toolBorrow.setApplySn(applySn);
			toolBorrow.setBackStatus("0");
			toolBorrow.setBorrowTime(date);
			toolBorrow.setDelFlag(DelFlagConstant.NORMAL);
			toolBorrow.setGoodsId(splitIds[i]);
			toolBorrow.setUserId(userId);
			toolBorrow.setCreateBy(sysUser.getUsername());
			toolBorrow.setCreateTime(date);
			list.add(toolBorrow);
		}
		toolsDao.insertToolBorrow(list);
	}

	@Override
	public Integer checkOtherGoodsStatus(String[] ids) {
		return toolsDao.checkOtherGoodsStatus(ids);
	}

	@Override
	public void editGoodsStatus(String id, String itemStatus) {
		toolsDao.editGoodsStatus(id, itemStatus);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void backGoodsItem(String id, String itemStatus, String description) {
		//更改货物状态,更新借用历史备注信息
		toolsDao.editGoodsStatus(id, itemStatus);
		toolsDao.updateToolBorrowDesc(id, description);
	}

	@Override
	public IPage<BorrowHistoryVo> queryBorrowHistory(IPage<BorrowHistoryVo> page, String userName, String goodsId,String startTime,String endTime) {
		return toolsDao.queryBorrowHistory(page, userName, goodsId,startTime,endTime);
	}

	@Override
	public List<GoodsApplyListVo> queryGoodsApply(String userId) {
		return toolsDao.queryGoodsApply(userId);
	}

	@Override
	public List<UserCodeName> getUserCodeNameList(String name) {
		return toolsDao.getUserCodeNameList(name);
	}


	@Override
	public Boolean checkSoreNameExist(String id, String storeName) {
		return toolsDao.checkSoreNameExist(id,storeName);
	}

	@Override
	public void deleteStoreManager(String userId) {
		toolsDao.deleteStoreManager(userId);
	}

	@Override
	public List<String> queryStoreIdList(String manager) {
		return toolsDao.queryStoreIdList(manager);
	}

	@Override
	public List<StoreTree> getTreeStore() {
		return toolsDao.getTreeStore();
	}

}
