package com.zcdy.dsc.modules.settle.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.operation.equipment.entity.IotEquipmentPrice;
import com.zcdy.dsc.modules.settle.constant.SettleConstant;
import com.zcdy.dsc.modules.settle.vo.SettleBatchOptEquipInfoVo;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsQueryParam;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsVo;

/**
 * @author tangchao
 * @date 2020-5-9
 */
public interface SettlementStatisticsMapper {
    /**
     * 通过客户查询未结算数据
     * 1. 之前未结算抄表记录
     * 2. 当前时间至上次抄表记录时间的抄表数据
     *
     * @param customerId 客户id
     * @return 未结算记录
     */
    @Select({
            "SELECT",
            "sb.id as id,",
            "psb.id as previousId,",
            "sb.customer_id AS customerId,",
            "sc.customer_name AS customerName,",
            "( SELECT vsdi.`item_text` FROM v_sys_dict_item vsdi WHERE vsdi.dict_code='customer_type' and vsdi.item_value = sc.customer_type ) AS customerType,",
            "( SELECT count( 1 ) FROM settle_contract insc WHERE insc.customer_id = sc.id ) AS signCount,",
            "scon.id AS contractId,",
            "scon.contract_name AS contractName,",
            "scon.start_date AS contractValidateDate,",
            "scon.end_date AS contractInvalidateDate,",
            "sb.equipment_id AS equipment_id,",
            "concat( b.`name`, '[', c.`NAME`, oe.equipment_location, ']', '[', oe.equipment_sn, ']' ) AS equipmentInfo,",
            "swd.district_id AS equipmentDistrictId,",
            "sd.district_name AS equipmentDistrictName,",
            "sb.`current_flow_date` AS currentFlowDate,",
            "sb.`current_flow_time` AS currentFlowTime,",
            "sb.`current_flow` AS currentFlow,",
            "psb.`current_flow_date` AS previousFlowDate,",
            "psb.`current_flow_time` AS previousFlowTime,",
            "psb.`current_flow` AS previousFlow,",
            "sb.`used_flow` AS currentUsedFlow,",
            "sb.`unit_price` AS currentWaterPrice,",
            "sb.`total_cost` AS currentCost,",
            "sb.`status` as status,",
            "sb.equipment_id",
            "FROM",
            "settle_batch sb",
            "LEFT JOIN settle_batch psb ON sb.previous_id = psb.id",
            "LEFT JOIN settle_customer sc ON sc.id = sb.customer_id",
            "LEFT JOIN settle_contract scon ON scon.id = sb.contract_id",
            "LEFT JOIN opt_equipment oe ON oe.id = sb.equipment_id",
            "LEFT JOIN sys_category b ON oe.equipment_type = b.`code`",
            "LEFT JOIN sys_category c ON oe.equipment_section = c.`code`",
            "LEFT JOIN settle_water_district swd ON swd.equipment_id = sb.equipment_id and swd.valid_status = 1 ",
            "LEFT JOIN settle_district sd ON swd.district_id = sd.id",
            "WHERE",
            "1 = 1 ",
            "AND sb.STATUS = '" + SettleConstant.STATUS_UNSETTLEMENT + "' ",
            "AND sb.customer_id = #{customerId}",
            "ORDER BY",
            "currentFlowTime DESC,",
            "previousFlowDate DESC"
    })
    List<SettlementStatisticsVo> getSettleByCustomerId(String customerId);

    /**
     * 根据参数分页获取数据
     * @param page 分页信息
     * @param param 查询参数
     * @return 结算数据
     */
    @Select({
            "<script>",
            "SELECT",
            "sb.id as id,",
            "sb.customer_id AS customerId,",
            "sc.customer_name AS customerName,",
            "( SELECT vsdi.`item_text` FROM v_sys_dict_item vsdi WHERE vsdi.dict_code='customer_type' and vsdi.item_value = sc.customer_type ) AS customerType,",
            "( SELECT count( 1 ) FROM settle_contract insc WHERE insc.customer_id = sc.id ) AS signCount,",
            "scon.id AS contractId,",
            "scon.contract_name AS contractName,",
            "scon.start_date AS contractValidateDate,",
            "scon.end_date AS contractInvalidateDate,",
            "sb.equipment_id AS equipment_id,",
            "concat( b.`name`, '[', c.`NAME`, oe.equipment_location, ']', '[', oe.equipment_sn, ']' ) AS equipmentInfo,",
            "swd.district_id AS equipmentDistrictId,",
            "sd.district_name AS equipmentDistrictName,",
            "sb.`current_flow_date` AS currentFlowDate,",
            "sb.`current_flow_time` AS currentFlowTime,",
            "sb.`current_flow` AS currentFlow,",
            "psb.`current_flow_date` AS previousFlowDate,",
            "psb.`current_flow_time` AS previousFlowTime,",
            "psb.`current_flow` AS previousFlow,",
            "sb.`used_flow` AS currentUsedFlow,",
            "sb.`unit_price` AS currentWaterPrice,",
            "sb.`total_cost` AS currentCost,",
            "sb.`status` as status,",
            "sb.equipment_id,",
            "(select item_text from v_sys_dict_item where dict_code = 'settle_status' and item_value = sb.STATUS) as statusTitle",
            "FROM",
            "settle_batch sb",
            "LEFT JOIN settle_batch psb ON sb.previous_id = psb.id",
            "LEFT JOIN settle_customer sc ON sc.id = sb.customer_id",
            "LEFT JOIN settle_contract scon ON scon.id = sb.contract_id",
            "LEFT JOIN opt_equipment oe ON oe.id = sb.equipment_id",
            "LEFT JOIN sys_category b ON oe.equipment_type = b.`code`",
            "LEFT JOIN sys_category c ON oe.equipment_section = c.`code`",
            "LEFT JOIN settle_water_district swd ON swd.equipment_id = sb.equipment_id and swd.valid_status = 1 ",
            "AND swd.equipment_id",
            "LEFT JOIN settle_district sd ON swd.district_id = sd.id",
            "WHERE",
            "1 = 1 ",
            "AND (sb.status='"+ SettleConstant.STATUS_UNSETTLEMENT +"' or not exists (select 1 from settle_batch isb where isb.previous_id = sb.id and isb.status != '"+ SettleConstant.STATUS_UNSETTLEMENT +"'))",
            "<if test='param.equipmentSn!=null and param.equipmentSn!=\"\"'>",
            "and oe.equipment_sn like concat('%',#{param.equipmentSn},'%') ",
            "</if>",
            "<if test='param.equipmentId !=null and param.equipmentId != \"\"'>",
            "and oe.id = #{param.equipmentId}",
            "</if>",
            "<if test='param.equipmentLocation!=null and param.equipmentLocation!=\"\"'>",
            "and oe.equipment_location like concat('%',#{param.equipmentLocation},'%') ",
            "</if>",
            "<if test='param.equipmentSection!=null and param.equipmentSection!=\"\"'>",
            "and oe.equipment_section like concat('%',#{param.equipmentSection},'%') ",
            "</if>",
            "AND sb.STATUS in ('" + SettleConstant.STATUS_UNSETTLEMENT + "', '"+ SettleConstant.STATUS_INIT_SETTLEMENT +"') ",
            "AND sb.equipment_type = '" + SettleConstant.EQUIP_NOT_CYCLE + "' ",
            "ORDER BY",
            "currentFlowTime DESC,",
            "previousFlowDate DESC",
            "</script>"
    })
    IPage<SettlementStatisticsVo> pageSettle(Page<SettlementStatisticsVo> page, @Param("param") SettlementStatisticsQueryParam param);

    /**
     * 获取水表单价
     *
     * @param param 参数
     * @return 单价
     */
    @Select({
            "SELECT",
                    "price",
                    "FROM",
                    "  settle_contract_water_rule_item scwri",
                    "LEFT JOIN settle_contract_water_rule scwr ON scwr.id = scwri.rule_id and scwr.invalid_status = '1' " ,
                    "left join settle_contract_water scw on scwr.id = scw.rule_id AND scw.create_time <= #{time} AND scw.invalid_date >= #{time}" ,
                    "WHERE",
                    "1 = 1",
                    "AND scwri.start_time <= #{date} AND scwri.end_time >= #{date}",
                    "AND scw.equipment_id = #{equipmentId} ",
    })
    BigDecimal getUnitPrice(Map<String, String> param);

    /**
     * 获取变量信息
     *
     * @param param 查找参数
     * @return 变量信息
     */
    @Select({
            "select",
            "concat( b.`name`, '[', c.`NAME`, oe.equipment_location, ']', '[', oe.equipment_sn, ']' ) as equipmentInfo,",
            "ioe.opt_id as equipmentId,",
            "oe.equipment_name as equipmentName,",
            "sc.id as contractId,",
            "sc.contract_name as contractName,",
            "sc.start_date as contractValidateDate,",
            "sc.end_date as contractInvalidateDate,",
            "scustomer.id as customerId,",
            "scustomer.customer_name as customerName,",
            "(SELECT vsdi.`item_text` FROM v_sys_dict_item vsdi WHERE vsdi.dict_code='customer_type' and vsdi.item_value = scustomer.customer_type) as customerType,",
            "'' as customerTypeName,",
            "sd.id as equipmentDistrictId,",
            "sd.district_name as equipmentDistrictName,",
            "IFNULL(sma.month_balance, '1') as equipmentType,",
            "ivi.id as variableId,",
            "ivi.variable_name as variableName,",
            "ivi.variable_title as variableTitle,",
            "IFNULL(sma.month_balance, '1') as equipmentType",
            "from ",
            "iot_variable_info ivi ",
            "left join iot_equipment_variable iev on iev.variable_id = ivi.id",
            "left join iot_equipment ie on ie.id = iev.iot_id",
            "left join iot_opt_equipment ioe on ioe.iot_id = ie.id",
            "left join opt_equipment oe on oe.id = ioe.opt_id",
            "left join settle_contract_water oce on oce.equipment_id = oe.id",
            "left join settle_contract sc on sc.id = oce.contract_id",
            "left join settle_customer scustomer on scustomer.id = sc.customer_id",
            "left join settle_water_district swd on swd.equipment_id  = oe.id and swd.valid_status = 1",
            "left join settle_district sd on sd.id = swd.district_id",
            "left join settle_meter_attr sma on sma.equipment_id = opt_id",
            "LEFT JOIN sys_category b ON oe.equipment_type = b.`code` LEFT JOIN sys_category c ON oe.equipment_section = c.`code`",
            "where",
            "ivi.variable_type = 'A16A09'",
            "and #{nowTime} <= oce.invalid_date and #{nowTime} >= oce.create_time",
            "and sma.month_balance = '0'",
            "and sc.customer_id = #{customerId}"
    })
    List<SettleBatchOptEquipInfoVo> getWaterVarByCustomerId(Map<String, String> param);

    /**
     * 获取客户的签约次数
     *
     * @param customerId 客户id
     * @return 签约次数
     */
    @Select({
            "select ",
            "count(1) as signCount ",
            "from settle_contract sc ",
            "where sc.customer_id = #{customerId}"
    })
    int getSignCount(String customerId);

    /**
     * 根据条件查找符合的id
     *
     * @param queryParam 查询条件
     * @return 客户ids
     */
    @Select({
            "<script>",
                "select",
                "distinct scus.id as customerId",
                "from opt_equipment oe",
                "left join settle_contract_water scw on scw.equipment_id = oe.id",
                "left join settle_contract sc on sc.id = scw.contract_id",
                "left join settle_customer scus on scus.id = sc.customer_id",
                "left join settle_meter_attr sma on sma.equipment_id = oe.id",
                "where  1=1",
                "and scus.id is not null",
                "<if test='likeCustomerName != null and likeCustomerName != \"\"' >",
                    "and scus.customer_name like concat('%',#{likeCustomerName},'%')",
                "</if>",
            "</script>",
    })
    List<String> getCustomerIds(SettlementStatisticsQueryParam queryParam);

    /**
     * 获取到期抄表的水表信息
     *
     * @param date     日期
     * @param dateTime 日期时间
     * @param typeCode 变量类型
     * @return 到期水表信息
     */
    @Select({
            "<script>",
            "SELECT t.price,c.equipment_id optId, io.iot_id iotId,var.variable_name variableName,con.customer_id customerId,con.id contractId",
            "FROM settle_contract_water_rule_item t",
            "LEFT JOIN settle_contract_water c on t.rule_id = c.rule_id",
            "LEFT JOIN opt_equipment opt on opt.id = c.equipment_id",
            "LEFT JOIN iot_opt_equipment io on io.opt_id = opt.id",
            "LEFT JOIN iot_equipment_variable ev  on ev.iot_id = io.iot_id",
            "LEFT JOIN iot_variable_info var on var.id = ev.variable_id",
            "LEFT JOIN settle_contract con on con.id = c.contract_id",
            "LEFT JOIN settle_contract_water_rule rule on rule.id = t.rule_id",
            "LEFT JOIN settle_meter_attr sma on sma.equipment_id = opt.id",
            "where 1=1",
            "and sma.month_balance = '0'",
            "and rule.invalid_status=" + StatusConstant.VALID,
            "and var.variable_type = #{typeCode}",
            "<if test='date!=null and date!=\"\"'>",
            "and t.end_time = #{date}",
            "</if>",
            "and c.invalid_date >= #{dateTime}",
            "</script>"
    })
    List<IotEquipmentPrice> getEndWithTodayRule(@Param("date") String date, @Param("dateTime") String dateTime, @Param("typeCode") String typeCode);
}
