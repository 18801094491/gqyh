package com.zcdy.dsc.modules.settle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.settle.vo.ManualContractInfoVo;
import com.zcdy.dsc.modules.settle.vo.ManualFlowFormVo;
import com.zcdy.dsc.modules.settle.vo.SettleBatchOptEquipInfoVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author tangchao
 * @date 2020/5/22
 */
public interface ManualFlowMapper  extends BaseMapper<ManualFlowFormVo> {

    /**
     * 根据设备id, 和日期 获取合同
     * @return 合同信息
     */
    @Select({
            "select" ,
                    "sc.id as contractId," ,
                    "sc.contract_name as contractName," ,
                    "sc.customer_id as customerId," ,
                    "scwri.price as price," ,
                    "scwri.id as ruleItemId" ,
                    "from settle_contract sc " ,
                    "left join settle_contract_water scw on scw.contract_id = sc.id" ,
                    "left join settle_contract_water_rule scwr on scwr.contract_id = sc.id and scwr.invalid_status = 1 and scw.rule_id = scwr.id" ,
                    "left join settle_contract_water_rule_item scwri on scwri.rule_id = scwr.id " ,
                    "where 1=1" ,
                    "and scw.equipment_id = #{equipmentId}" ,
                    "and scwri.start_time <= #{date}" ,
                    "and scwri.end_time >= #{date}"
    })
    public List<ManualContractInfoVo> listContractByOptDate(@Param("equipmentId") String equipmentId, @Param("date") String date);

    /**
     * 根据水价ID获取设备基础信息
     * @param ruleItemId 水价id
     * @return info
     */
    @Select({
        "select " ,
                "oe.id AS equipmentId," ,
                "sc.id AS contractId," ,
                "sc.contract_name AS contractName," ,
                "sc.customer_id AS customerId," ,
                "scustomer.customer_name AS customerName," ,
                "scustomer.customer_type AS customerType," ,
                "scate.`name` AS customerTypeName," ,
                "swd.district_id AS districtId," ,
                "sd.district_name AS districtName," ,
                "IFNULL( sma.month_balance, '1' ) AS equipmentType  " ,
                "from " ,
                "settle_contract_water_rule_item scwri" ,
                "LEFT JOIN settle_contract_water_rule scwr ON scwr.id = scwri.rule_id " ,
                "LEFT JOIN settle_contract sc ON sc.id = scwr.contract_id" ,
                "LEFT JOIN settle_customer scustomer ON scustomer.id = sc.customer_id" ,
                "left join sys_category scate on scate.`code` = scustomer.customer_type" ,
                "LEFT JOIN settle_contract_water scw ON scw.rule_id = scwr.id AND scw.equipment_id = #{equipmentId}" ,
                "LEFT JOIN opt_equipment oe ON oe.id = scw.equipment_id" ,
                "left join settle_meter_attr sma on sma.equipment_id = scw.equipment_id" ,
                "left join settle_water_district swd on swd.equipment_id = scw.equipment_id and swd.valid_status = 1" ,
                "left join settle_district sd on sd.id = swd.district_id" ,
                "where  scwri.id = #{ruleItemId} "
    })
    SettleBatchOptEquipInfoVo getByRuleItemId(@Param("ruleItemId") String ruleItemId, @Param("equipmentId") String equipmentId);

}
