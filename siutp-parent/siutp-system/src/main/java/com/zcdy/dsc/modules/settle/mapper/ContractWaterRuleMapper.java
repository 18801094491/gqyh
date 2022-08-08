package com.zcdy.dsc.modules.settle.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRule;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRuleItem;
import com.zcdy.dsc.modules.settle.param.ContractWaterRulePageParam;
import com.zcdy.dsc.modules.settle.vo.ContractWaterRuleVo;

/**
 * @Description: 合同水价规则
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-22
 * @Version: V1.0
 */
public interface ContractWaterRuleMapper extends BaseMapper<ContractWaterRule> {

	/**
	 * 描述: 分页查询合同水价规则
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月23日 上午11:05:20
	 * 版本: V1.0
	 */
	@Select({
		" <script>",
		" SELECT",
		" t.id,",
		" t1.contract_name contractName,",
		" t.rule_name ruleName,",
		" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'count_price_type' ) AND item_value = t.rule_type ) ruleTypeName",
		" FROM",
		" settle_contract_water_rule t",
		" LEFT JOIN settle_contract t1 on t1.id=t.contract_id",
		" where t.contract_id=#{param.contractId}",
		" and t.invalid_status="+StatusConstant.VALID,
		" <if test='param.ruleName!=null and param.ruleName!=\"\"'>",
		" and t1.contract_name  like concat('%',#{param.ruleName} ,'%') ",
		" </if>",
		" ORDER BY",
		" t.create_time DESC",
		" </script>",
	})
	IPage<ContractWaterRuleVo> selectPageData(Page<ContractWaterRuleVo> page,@Param("param") ContractWaterRulePageParam param);

	/**
	 * 描述: 通过水表跟时间查询当前日期价格
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月27日 下午3:17:42
	 * 版本: V1.0
	 */
	@Select({
		" SELECT",
		" price",
		" FROM",
		" settle_contract_water_rule_item t2",
		" WHERE",
		" rule_id = (",
		" SELECT t.id FROM settle_contract_water_rule t WHERE t.contract_id =#{contractId} AND t.invalid_status = '1'",
		"  )",
		" AND #{staticsDate}>= DATE_FORMAT(t2.start_time, '%Y-%m-%d')",
		" AND #{staticsDate}<= DATE_FORMAT(t2.end_time, '%Y-%m-%d')",
	})
	ContractWaterRuleItem queryPriceByContractAndDate(@Param("contractId") String contractId,@Param("staticsDate")String staticsDate);

}
