package com.zcdy.dsc.modules.settle.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.settle.constant.SettleConstant;
import com.zcdy.dsc.modules.settle.entity.SettleChecklist;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.settle.entity.SettleChecklistPage;
import com.zcdy.dsc.modules.settle.vo.SettleCheckQueryParam;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description 结算清单
 * @author  tangchao
 * @date 2020-05-11
 * @version V1.0
 */
public interface SettleChecklistMapper extends BaseMapper<SettleChecklist> {

    @Select({
            "<script>",
                "select " ,
                "scheck.id as id," ,
                "sc.customer_name as customerName," ,
                "(SELECT vsdi.`item_text` FROM v_sys_dict_item vsdi WHERE vsdi.dict_code='customer_type' and vsdi.item_value = sc.customer_type) as customerTypeName," ,
                "scheck.settle_date as settleDate," ,
                "scheck.total_cost as totalCost," ,
                "IFNULL(su.realname, '"+ SettleConstant.AUTO_SETTLE_PERSON +"') as realName" ,
                "from " ,
                "settle_checklist scheck " ,
                "left join settle_batch sb on sb.checklist_id = scheck.id" ,
                "left join settle_customer sc on sc.id = sb.customer_id" ,
                "left join sys_user su on su.username = scheck.settle_by" ,
                "where 1=1",
                "<if test=' param.likeCustomerName != null and param.likeCustomerName != \"\"'>",
                    "and sc.customer_name like concat('%',#{param.likeCustomerName},'%')",
                "</if>",
                "group by scheck.id order by settle_time desc",
            "</script>",
    })
    IPage<SettleChecklistPage> pageChecklist(Page<SettleChecklistPage> page, @Param("param") SettleCheckQueryParam param);

    /**
     * 获取结算单明细
     * @param checklistId 结算单id
     * @return 结算单明细
     */
    @Select({
            "SELECT" ,
                    "sb.customer_id AS customerId," ,
                    "sc.customer_name AS customerName," ,
                    "( SELECT isc.`name` FROM sys_category isc WHERE isc.CODE = sc.customer_type ) AS customerType," ,
                    "( SELECT count( 1 ) FROM settle_contract insc WHERE insc.customer_id = sc.id ) AS signCount," ,
                    "scon.id AS contractId," ,
                    "scon.contract_name AS contractName," ,
                    "scon.start_date AS contractValidateDate," ,
                    "scon.end_date AS contractInvalidateDate," ,
                    "sb.equipment_id AS equipment_id," ,
                    "concat( b.`name`, '[', c.`NAME`, oe.equipment_location, ']', '[', oe.equipment_sn, ']' ) AS equipmentInfo," ,
                    "swd.district_id AS equipmentDistrictId," ,
                    "sd.district_name AS equipmentDistrictName," ,
                    "sb.`current_flow_date` AS currentFlowDate," ,
                    "sb.`current_flow_time` AS currentFlowTime," ,
                    "sb.`current_flow` AS currentFlow," ,
                    "psb.`current_flow_date` AS previousFlowDate," ,
                    "psb.`current_flow_time` AS previousFlowTime," ,
                    "psb.`current_flow` AS previousFlow," ,
                    "sb.`used_flow` AS currentUsedFlow," ,
                    "sb.`unit_price` AS currentWaterPrice," ,
                    "sb.`total_cost` AS currentCost " ,
                    "FROM" ,
                    "settle_batch sb" ,
                    "LEFT JOIN settle_batch psb ON sb.previous_id = psb.id" ,
                    "LEFT JOIN settle_customer sc ON sc.id = sb.customer_id" ,
                    "LEFT JOIN settle_contract scon ON scon.id = sb.customer_id" ,
                    "LEFT JOIN opt_equipment oe ON oe.id = sb.equipment_id" ,
                    "LEFT JOIN sys_category b ON oe.equipment_type = b.`code`" ,
                    "LEFT JOIN sys_category c ON oe.equipment_section = c.`code`" ,
                    "LEFT JOIN settle_water_district swd ON swd.equipment_id = sb.equipment_id and swd.valid_status = 1 " ,
                    "AND swd.equipment_id" ,
                    "LEFT JOIN settle_district sd ON swd.district_id = sd.id " ,
                    "WHERE" ,
                    "1 = 1  " ,
                    "AND sb.checklist_id = #{checklistId} " ,
                    "ORDER BY" ,
                    "currentFlowTime DESC," ,
                    "previousFlowDate DESC;"
    })
    List<SettlementStatisticsVo> getCheckListDetail(String checklistId);
}
