package com.zcdy.dsc.modules.operation.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.common.util.UUIDGenerator;
import com.zcdy.dsc.modules.operation.equipment.constant.*;
import com.zcdy.dsc.modules.operation.equipment.entity.OptGoodsItem;
import com.zcdy.dsc.modules.operation.equipment.entity.OptSparepart;
import com.zcdy.dsc.modules.operation.equipment.entity.OptStoreBill;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptSparepartMapper;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptStoreBillMapper;
import com.zcdy.dsc.modules.operation.equipment.mapper.StoreBillDao;
import com.zcdy.dsc.modules.operation.equipment.mapper.ToolDao;
import com.zcdy.dsc.modules.operation.equipment.service.StoreBillService;
import com.zcdy.dsc.modules.operation.equipment.vo.store.SparepartBillParamVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.SparepartDropdown;
import com.zcdy.dsc.modules.operation.equipment.vo.store.SparepartVo;
import com.zcdy.dsc.modules.operation.equipment.vo.store.StoreBillVo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : songguang.jiao
 */
@Service
public class StoreBillServiceImpl implements StoreBillService {

	@Autowired
	private OptStoreBillMapper optStoreBillMapper;

	@Autowired
	private StoreBillDao storeBillDao;

	@Autowired
	private OptSparepartMapper sparepartMapper;

	@Autowired
	private ToolDao toolDao;

	@Override
	public IPage<SparepartVo> queryPageData(Page<SparepartVo> page, String sparepartName,String sparepartModel, String sparepartSpecs,String storeId) {
		// 查询当前用户所有的仓库,没有仓库返回空
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> storeIdList = toolDao.queryStoreIdList(sysUser.getId());
		if (storeIdList.size() == 0 || storeIdList.isEmpty()) {
			return null;
		}
		IPage<SparepartVo> queryPageData = storeBillDao.queryPageData(page, sparepartName, sparepartModel,sparepartSpecs,storeId, storeIdList);

		List<SparepartVo> records = queryPageData.getRecords();
		// 判断是否需要报警，如果库存量小于等于预警值
		for (SparepartVo optSpaGetVo : records) {
			if (optSpaGetVo.getAmount() <= optSpaGetVo.getWarnAmount()) {
				optSpaGetVo.setState(SparpartWarningStatus.WARN);
				// 通过仓库id,找到所有的仓库管理员,发邮件报警
				String userIds = toolDao.queryManagerIdList(optSpaGetVo.getStoreId());
				if (!StringUtils.isEmpty(userIds)) {
					/*sysBaseAPI.sendSysAnnouncement("admin", userIds, "库存不足",
							"备品备件中id为  " + optSpaGetVo.getId() + " 的库存不足，请检查");*/
				}
			} else {
				optSpaGetVo.setState(SparpartWarningStatus.NORMAL);
			}
		}
		return queryPageData;
	}

	@Override
	public IPage<StoreBillVo> queryBillPageList(Page<StoreBillVo> page, String sparepartName, String batchSn,
			String billType) {
		// 查询当前用户所有的仓库
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<String> storeIdList = toolDao.queryStoreIdList(sysUser.getId());
		if (storeIdList.size() == 0 || storeIdList.isEmpty()) {
			// 当前用户当前用户没有管理仓库的权限
			return null;	
		}
		return storeBillDao.queryBillPageList(page, sparepartName, batchSn, storeIdList, billType);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public synchronized void addInBill(SparepartBillParamVo optStoreBill) {
		// 校验备品备件是否存在,存在就直接入库,不存在首先新增再入库
		QueryWrapper<OptSparepart> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(OptSparepart::getSparepartName, optStoreBill.getSparepartName())
				.eq(OptSparepart::getSparepartModel, optStoreBill.getSparepartModel())
				.eq(OptSparepart::getSparepartSpecs, optStoreBill.getSparepartSpecs())
				.eq(OptSparepart::getSupplierId, optStoreBill.getSupplierId())
				.eq(OptSparepart::getStoreId, optStoreBill.getStoreId());
		OptSparepart sparepart = sparepartMapper.selectOne(queryWrapper);
		// 如果没有对应备品备件就新增一个,并设置报警值为1
		if (sparepart == null) {
			sparepart=new OptSparepart(); 
			sparepart.setWarnAmount(1);
			BeanUtils.copyProperties(optStoreBill, sparepart);
			sparepartMapper.insert(sparepart);
		}
		// 产生入库单
		OptStoreBill storeBill = new OptStoreBill();
		BeanUtils.copyProperties(optStoreBill, storeBill);
		storeBill.setSparepartId(sparepart.getId());
		storeBill.setBillSn(getMaxInBill());
		storeBill.setBillType(BillType.IN);
		optStoreBillMapper.insert(storeBill);
		// 批量插入货品表
		List<OptGoodsItem> list = new ArrayList<>(optStoreBill.getAmount());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		Date date = new Date();
		for (int i = 1; i <= optStoreBill.getAmount(); i++) {
			OptGoodsItem goodsItem = new OptGoodsItem();
			goodsItem.setId(UUIDGenerator.generate());
			goodsItem.setBatchSn(optStoreBill.getBatchSn());
			goodsItem.setCreateBy(sysUser.getUsername());
			goodsItem.setCreateTime(date);
			goodsItem.setDataId(sparepart.getId());
			goodsItem.setDelFlag(DelFlagConstant.NORMAL);
			goodsItem.setItemSn(i + "");
			goodsItem.setItemStatus(GoodsItemStatus.NORMAL);
			goodsItem.setItemType(GoodsItemType.SPAREPART);
			list.add(goodsItem);
		}
		storeBillDao.insertGoodsItems(list);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public synchronized void addOutBill(SparepartBillParamVo optStoreBill) {
		// 产生出库单
		OptStoreBill storeBill = new OptStoreBill();
		BeanUtils.copyProperties(optStoreBill, storeBill);
		storeBill.setBillSn(this.getMaxOutBill());
		storeBill.setBillType(BillType.OUT);
		optStoreBillMapper.insert(storeBill);
		// 更改货品表状态
		List<OptGoodsItem> list = new ArrayList<>(optStoreBill.getAmount());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		Date date = new Date();
		for (int i = 1; i <= optStoreBill.getAmount(); i++) {
			OptGoodsItem goodsItem = new OptGoodsItem();
			goodsItem.setBatchSn(optStoreBill.getBatchSn());
			goodsItem.setDataId(optStoreBill.getSparepartId());
			goodsItem.setItemStatus(GoodsItemStatus.CHECKOUT);
			goodsItem.setItemType(GoodsItemType.SPAREPART);
			goodsItem.setUpdateBy(sysUser.getUsername());
			goodsItem.setUpdateTime(date);
			list.add(goodsItem);
		}
		storeBillDao.updateGoodsItemsStatus(list);
	}

	@Override
	public Boolean checkBillStore(SparepartBillParamVo optStoreBill) {
		Integer billStore = storeBillDao.checkBillStore(optStoreBill);
		if (billStore < optStoreBill.getAmount()) {
			return false;
		}
		return true;
	}

	// 获取最大出库编号
	private String getMaxOutBill() {
		String sn = "C" + new SimpleDateFormat("yyyyMMdd").format(new Date());
		String maxOutBill = storeBillDao.getMaxOutBill();
		if (StringUtils.isEmpty(maxOutBill)) {
			sn = sn + "001";
		} else {
			String leftPad = StringUtils.leftPad(Integer.parseInt(maxOutBill) + 1 + "", 3, "0");
			sn = sn + leftPad;
		}
		return sn;
	}

	// 获取最大入库编号
	private String getMaxInBill() {
		String sn = "R" + new SimpleDateFormat("yyyyMMdd").format(new Date());
		String maxInBill = storeBillDao.getMaxInBill();
		if (StringUtils.isEmpty(maxInBill)) {
			sn = sn + "001";
		} else {
			String leftPad = StringUtils.leftPad(Integer.parseInt(maxInBill) + 1 + "", 3, "0");
			sn = sn + leftPad;
		}
		return sn;
	}

	@Override
	public List<SparepartDropdown> dropdown(String sparepartName) {
		return storeBillDao.dropdown(sparepartName);
	}

}
