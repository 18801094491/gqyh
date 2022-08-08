package com.zcdy.dsc.modules.settle.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.settle.entity.ContractWater;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRule;
import com.zcdy.dsc.modules.settle.vo.ContractWaterVo;
import com.zcdy.dsc.modules.settle.vo.CustomerWaterInfoVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Description: 合同绑定水表信息
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-04-22
 * @Version: V1.0
 */
public interface ContractWaterMapper extends BaseMapper<ContractWater> {
    /**
     * 自定义 分页查询
     * Fix 合同过期后查询不到水表绑定的信息
     * @author chaotang 2020年4月27日11:22:06
     * @param page
     * @return
     */
    @Select({
            "SELECT" ,
                    "scw.id AS id," ,
                    "oe.equipment_sn AS equipmentSn," ,
                    "concat('[',cat.`name`,']', '[',oe.equipment_location ,']', '[', oe.equipment_sn, ']') as equipmentName," ,
                    "scwr.id AS ruleId," ,
                    "scwr.rule_name AS ruleName ," ,
                    "scw.invalid_date as invalidDate," ,
                    "DATEDIFF(scw.invalid_date,#{param.now}) > 0 as isExpired" ,
                    "FROM" ,
                    "settle_contract_water scw" ,
                    "left join settle_contract_water_rule scwr on scwr.id = scw.rule_id" ,
                    "left join opt_equipment oe on scw.equipment_id = oe.id" ,
                    "left join sys_category cat ON cat.CODE = oe.equipment_section" ,
                    "where 1=1" ,
                    "AND scwr.invalid_status =  1" ,
                    "AND scw.invalid_date >= #{param.now}" ,
                    "AND scw.contract_id = #{param.contractId}"
    })
    IPage<ContractWaterVo> selecContractWatertPage(IPage<ContractWaterVo> page, @Param(value = "param") Map<String,String> param);


    @Select({
            "select" ,
                    "oe.id as equipmentId," ,
                    "concat('[',cat.`name`,']', '[',oe.equipment_location ,']', '[', oe.equipment_sn, ']') as equipmentName" ,
                    "from settle_customer_equipment sce, " ,
                    "opt_equipment oe " ,
                    "LEFT JOIN sys_category cat ON cat.CODE = oe.equipment_section" ,
                    "where " ,
                    "oe.id = sce.equipment_id " ,
                    "and sce.bind_status = '00'" ,
                    "and not exists (select 1 from settle_contract_water iscw where iscw.equipment_id = oe.id and iscw.invalid_date > #{now})" ,
                    "and sce.customer_id = #{customerId}",
    })
    List<CustomerWaterInfoVo> queryCustomerBindedWater(Map<String, String> param);

    @Select({
        "select " ,
            "id, " ,
            "rule_name" ,
            "from " ,
            "settle_contract_water_rule " ,
            "where contract_id = #{customerId} and invalid_status='1'"
    })
    List<ContractWaterRule> queryContractRule(@Param(value = "customerId") String contractId);
}
