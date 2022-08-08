package com.zcdy.dsc.modules.settle.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.settle.entity.Contract;
import com.zcdy.dsc.modules.settle.entity.ContractWater;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRule;
import com.zcdy.dsc.modules.settle.entity.SettleCustomer;
import com.zcdy.dsc.modules.settle.mapper.*;
import com.zcdy.dsc.modules.settle.param.EquipmentMeterPageParam;
import com.zcdy.dsc.modules.settle.param.SettleWaterParam;
import com.zcdy.dsc.modules.settle.service.ISettleService;
import com.zcdy.dsc.modules.settle.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author： Roberto
 * 创建时间：2020年1月2日 下午2:07:41
 * 描述: <p>自定义接口实现类</p>
 */
@Service
public class SettleServiceImpl implements ISettleService {

	@Resource
	private CustomerMapper customerMapper;

	@Resource
	private CustomerDao customerDao;
	
	@Resource
	private ContractMapper contractMapper;
	
	@Resource
	private ContractWaterMapper contractWaterMapper;
	
	@Resource
	private ContractWaterRuleMapper contractWaterRuleMapper;

	@Override
	public synchronized  void addCustomerInfo(SettleCustomer settleCustomer) {
		settleCustomer.setCustomerSn(this.getCustomerNum());
		customerMapper.insert(settleCustomer);
	}

	@Override
	public IPage<CustomerVo> getList(IPage<CustomerVo> page, String customerSn, String customerName, String startTime,
			String endTime) {
		return customerDao.getList(page, customerSn, customerName, startTime, endTime);
	}
	
	/**
	 * 设置客户编号生成规则 K+6位数字，递增
	 * @return
	 */
	public String getCustomerNum(){
		String cutomerSn = "K";
		String lastSnNum = customerDao.getLastSnNum();
		if (StringUtils.isEmpty(lastSnNum)) {
			cutomerSn = cutomerSn + "000001";
		} else {
			String number = StringUtils.leftPad(Integer.parseInt(lastSnNum)+1+"", 6, "0");
			cutomerSn = cutomerSn + number;
		}
		return cutomerSn;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void update(SettleCustomer customer) {
		customerMapper.updateById(customer);
	}

	@Override
	public CustomerVo getDetail(String id) {
		return this.customerDao.getDetail(id);
	}


	@Override
	public IPage<EquipWatermeterVo> getWatermeterList(IPage<EquipWatermeterVo> page, EquipmentMeterPageParam param) {
		return customerDao.getWatermeterList(page, param);
	}

	@Override
	public Integer getBindEquipStatus(String equipmentId) {
		return customerDao.getBindEquipStatus(equipmentId);
	}

	@Override
	public void unBindCustomerEquip(String id, String now) {
		customerDao.unBindCustomerEquip(id, now);
	}

	@Override
	public List<CustomerWaterInfoVo> getBindWateInfoList(String customerId) {
		return customerDao.getBindWateInfoList(customerId);
	}

	@Override
	public List<ExportCutomerVo> exportCutomerList(String customerSn,String customerName,
			   String startTime,String endTime) {
		return customerDao.exportCutomerList(customerSn,customerName,startTime,endTime);
	}

	@Override
	public IPage<ContractVo> queryContractList(Page<ContractVo> page, String contractSn, String contractName,String customer) {
		return customerDao.queryContractList(page, contractSn, contractName,customer);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void deleteContract(String id) {
		contractMapper.deleteById(id);
		//关联的水表全部解绑
		UpdateWrapper<ContractWater> updateWrapper = new UpdateWrapper<>();
		updateWrapper.lambda().eq(ContractWater::getContractId, id);
		ContractWater updateObj = new ContractWater();
		updateObj.setInvalidDate(DateUtil.convertLocalDateToDate(LocalDateTime.now().minusDays(1)));
		contractWaterMapper.update(updateObj, updateWrapper);
		
		//合同规则置为无效
		UpdateWrapper<ContractWaterRule> uWrapper=new UpdateWrapper<>();
		uWrapper.lambda().eq(ContractWaterRule::getContractId, id);
		ContractWaterRule rule=new ContractWaterRule();
		rule.setInvalidStatus(StatusConstant.INVALID);
		contractWaterRuleMapper.update(rule, uWrapper);
	}

	@Override
	public ContractVo queryDetailById(String id) {
		return customerDao.queryDetailById(id);
	}

	@Override
	public List<CustomerDropdown> queryCustomerNames(String customerName) {
		return customerDao.queryCustomerNames(customerName);
	}

	@Override
	public Integer checkCustomeExsit(String constomerId) {
		return customerDao.checkCustomeExsit(constomerId);
	}

	@Override
	public IPage<CustomerMeterInfoVo> queryWaterPageData(Page<CustomerMeterInfoVo> page, SettleWaterParam param) {
		return customerDao.queryWaterPageData(page, param);
	}


	@Override
	public Boolean checkSnExist(String id, String contractSn) {
		return customerDao.checkSnExist(id,contractSn);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public String editContract(ContractParam contractParamVo) {
		String message="修改成功";
		Contract contractInfo = contractMapper.selectById(contractParamVo.getId());
		//客户名称修改,只是解绑水表
		LocalDateTime localDateTime = LocalDateTime.now();
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		if(!contractParamVo.getCustomerId().equals(contractInfo.getCustomerId())){
		    UpdateWrapper<ContractWater> updateWrapper = new UpdateWrapper<>();
		    updateWrapper.lambda().eq(ContractWater::getContractId, contractInfo.getId()).gt(ContractWater::getInvalidDate, localDateTime.format(pattern));
		    ContractWater updateObj = new ContractWater();
		    updateObj.setInvalidDate(DateUtil.convertLocalDateToDate(localDateTime));
		    contractWaterMapper.update(updateObj, updateWrapper);
		    message="修改合同客户后,需要重新绑定水表";
		}
		// 如果更新了合同起止日期,规则置为无效,就解绑全部水表
		if (contractInfo.getStartDate().compareTo(contractParamVo.getStartDate()) != 0
				|| contractInfo.getEndDate().compareTo(contractParamVo.getEndDate()) != 0) {
			UpdateWrapper<ContractWater> updateWrapper = new UpdateWrapper<>();
	        updateWrapper.lambda().eq(ContractWater::getContractId, contractInfo.getId()).gt(ContractWater::getInvalidDate, localDateTime.format(pattern));
			ContractWater updateObj = new ContractWater();
			updateObj.setInvalidDate(DateUtil.convertLocalDateToDate(localDateTime));
			contractWaterMapper.update(updateObj, updateWrapper);
			
			//规则置为无效
			UpdateWrapper<ContractWaterRule> uWrapper=new UpdateWrapper<>();
			uWrapper.lambda().eq(ContractWaterRule::getContractId, contractParamVo.getId());
			ContractWaterRule rule=new ContractWaterRule();
			rule.setInvalidStatus(StatusConstant.INVALID);
			contractWaterRuleMapper.update(rule, uWrapper);
			message="修改合同起止日期,原有水价规则将全部失效,并且需要重新绑定水表";
		}
		Contract contract = new Contract();
		BeanUtil.copyProperties(contractParamVo, contract);
		contractMapper.updateById(contract);
		return message;
	}

    @Override
    public Integer checkMeterBindContract(String id) {
         return customerDao.checkMeterBindContract(id);
    }

	
}
