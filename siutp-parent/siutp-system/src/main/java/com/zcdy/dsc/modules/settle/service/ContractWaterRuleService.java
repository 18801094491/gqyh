package com.zcdy.dsc.modules.settle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.settle.entity.Contract;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRule;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRuleItem;
import com.zcdy.dsc.modules.settle.param.ContractWaterRulePageParam;
import com.zcdy.dsc.modules.settle.vo.ContractWaterRuleItemVo;
import com.zcdy.dsc.modules.settle.vo.ContractWaterRuleVo;

/**
 * @Description: 合同水价规则
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-22
 * @Version: V1.0
 */
public interface ContractWaterRuleService extends IService<ContractWaterRule> {

	/**
	 * 描述:添加水价规则 
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月23日 上午9:57:13
	 * 版本: V1.0
	 */
	public void one(ContractWaterRuleItemVo contractWaterRule,Contract contract);

	/**
	 * 描述: 分页查询数据规则
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月23日 上午11:03:21
	 * 版本: V1.0
	 */
	IPage<ContractWaterRuleVo> queryPageData(Page<ContractWaterRuleVo> page, ContractWaterRulePageParam param);

	/**
	 * 描述: 通过水表跟时间查询当前设备水价
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月27日 下午3:16:10
	 * 版本: V1.0
	 */
	public ContractWaterRuleItem queryPriceByContractAndDate(String contractId, String staticsDate);

}
