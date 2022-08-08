package com.zcdy.dsc.modules.settle.mapper;

import com.zcdy.dsc.modules.settle.entity.ContractWaterRuleItem;

import java.util.List;

import org.apache.ibatis.annotations.Insert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 合同水价规则详情表
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-22
 * @Version: V1.0
 */
public interface ContractWaterRuleItemMapper extends BaseMapper<ContractWaterRuleItem> {

	/**
	 * 描述: 批量插入价格列表
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月23日 上午10:14:54
	 * 版本: V1.0
	 */
	@Insert({
		" <script>",
		" INSERT INTO `settle_contract_water_rule_item` (`id`, `rule_id`, `start_time`, `end_time`, `price`)",
		" VALUES",
		" <foreach collection='list' item='item' index='index' separator=','>",
		" (",
		" #{item.id},",
		" #{item.ruleId},",
		" #{item.startTime},",
		" #{item.endTime},",
		" #{item.price}",
		" )",
		" </foreach>",
		" </script>",
	})
	void insertBentch(List<ContractWaterRuleItem> saveRuleItems);

}
