package com.zcdy.dsc.modules.settle.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.common.util.UUIDGenerator;
import com.zcdy.dsc.constant.ScaleConstant;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.settle.constant.ContactRuleTypeConstant;
import com.zcdy.dsc.modules.settle.entity.Contract;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRule;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRuleItem;
import com.zcdy.dsc.modules.settle.mapper.ContractWaterRuleItemMapper;
import com.zcdy.dsc.modules.settle.mapper.ContractWaterRuleMapper;
import com.zcdy.dsc.modules.settle.param.ContractWaterRulePageParam;
import com.zcdy.dsc.modules.settle.service.ContractWaterRuleService;
import com.zcdy.dsc.modules.settle.service.IContractService;
import com.zcdy.dsc.modules.settle.vo.ContractWaterRuleItemVo;
import com.zcdy.dsc.modules.settle.vo.ContractWaterRuleVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 合同水价规则
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-22
 * @Version: V1.0
 */
@Service
public class ContractWaterRuleServiceImpl extends ServiceImpl<ContractWaterRuleMapper, ContractWaterRule> implements ContractWaterRuleService {

	@Resource
	private ContractWaterRuleMapper contractWaterRuleMapper;
	
	@Resource
	private IContractService contractService;
	
	@Resource
	private ContractWaterRuleItemMapper contractWaterRuleItemMapper;
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void one(ContractWaterRuleItemVo param,Contract contract) {
		ContractWaterRule contractWaterRule=new ContractWaterRule(); 
		BeanUtil.copyProperties(param, contractWaterRule);
		if(StringUtils.isEmpty(contractWaterRule.getId())){
			contractWaterRule.setInvalidStatus(StatusConstant.VALID);
			contractWaterRuleMapper.insert(contractWaterRule);
		}else{
			contractWaterRuleMapper.updateById(contractWaterRule);
		}
		
		//首先删除原来计价信息
		LambdaQueryWrapper<ContractWaterRuleItem> wrapper = Wrappers.lambdaQuery(new ContractWaterRuleItem()).eq(ContractWaterRuleItem::getRuleId,contractWaterRule.getId());
		contractWaterRuleItemMapper.delete(wrapper);
		//如果是手工计价
		List<ContractWaterRuleItem> saveRuleItems=new ArrayList<ContractWaterRuleItem>();
		if(param.getRuleType().equals(ContactRuleTypeConstant.HAND_RULE)){
			List<ContractWaterRuleItem> items = param.getItems();
			for (ContractWaterRuleItem item : items) {
				//存入数据库
				ContractWaterRuleItem ruelItem=new ContractWaterRuleItem();
				ruelItem.setId(UUIDGenerator.generate());
				ruelItem.setRuleId(contractWaterRule.getId());
				ruelItem.setPrice(item.getPrice());
				ruelItem.setStartTime(item.getStartTime());
				ruelItem.setEndTime(item.getEndTime());
				saveRuleItems.add(ruelItem);
			}
		}else{
			//将阶梯计价规则转换为，分段计价方式存储
			this.parseTimeData(DateUtil.convertDateToLocalDate(contract.getStartDate()), DateUtil.convertDateToLocalDate(contract.getEndDate()),contractWaterRule.getBenchPrice(),
					contractWaterRule,contract, saveRuleItems);
		}
		if(saveRuleItems.size()>0){
			contractWaterRuleItemMapper.insertBentch(saveRuleItems);
		}
	}

	@Override
	public IPage<ContractWaterRuleVo> queryPageData(Page<ContractWaterRuleVo> page, ContractWaterRulePageParam param) {
		return contractWaterRuleMapper.selectPageData(page, param);
	}

	/** 
	 * 描述: 转换成时间区间存储价格   前3个参数位变动参数,中间为原始参数方便比较,最后一个参数为返回值
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月23日 下午4:19:33
	 * 版本: V1.0
	 */
	private void parseTimeData(LocalDateTime startDate,LocalDateTime endDate,String curruntPrice,ContractWaterRule contractWaterRule,Contract contract,List<ContractWaterRuleItem> saveRuleItems){
		//  最新价格
		BigDecimal newPrice=new BigDecimal(curruntPrice);
		//区间开始日期跟截至日期
		LocalDateTime beginDate = startDate;
		LocalDateTime lastDate = startDate.plusMonths(Integer.parseInt(contractWaterRule.getSetTime())).minusDays(1);
		int compareTo = lastDate.compareTo(DateUtil.convertDateToLocalDate(contract.getEndDate()));
		if(compareTo>=0){
			//将最后一天赋值未合同截至日期
			ContractWaterRuleItem ruelItem=new ContractWaterRuleItem();
			ruelItem.setId(UUIDGenerator.generate());
			ruelItem.setRuleId(contractWaterRule.getId());
			ruelItem.setPrice(newPrice.toString());
			ruelItem.setStartTime(DateUtil.convertLocalDateToDate(beginDate));
			ruelItem.setEndTime(contract.getEndDate());
			saveRuleItems.add(ruelItem);
		}else{
			ContractWaterRuleItem ruelItem=new ContractWaterRuleItem();
			ruelItem.setId(UUIDGenerator.generate());
			ruelItem.setRuleId(contractWaterRule.getId());
			ruelItem.setPrice(newPrice.toString());
			ruelItem.setStartTime(DateUtil.convertLocalDateToDate(beginDate));
			ruelItem.setEndTime(DateUtil.convertLocalDateToDate(lastDate));
			saveRuleItems.add(ruelItem);
			//计算起止日期跟价格
			beginDate=startDate.plusMonths(Integer.parseInt(contractWaterRule.getSetTime()));
			lastDate = lastDate.plusMonths(Integer.parseInt(contractWaterRule.getSetTime())).minusDays(1);
			newPrice=newPrice.multiply(new BigDecimal(contractWaterRule.getSetUp())).divide(new BigDecimal(100)).add(newPrice).setScale(ScaleConstant.PRICE, RoundingMode.HALF_UP);
			this.parseTimeData(beginDate, lastDate, newPrice.toString(),contractWaterRule,contract,saveRuleItems);
		}
	}

	@Override
	public ContractWaterRuleItem queryPriceByContractAndDate(String contractId, String staticsDate) {
		return contractWaterRuleMapper.queryPriceByContractAndDate(contractId,staticsDate);
	}
	
}
