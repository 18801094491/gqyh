package com.zcdy.dsc.modules.settle.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.settle.entity.SettleBatch;
import com.zcdy.dsc.modules.settle.param.CustometInfoParam;
import com.zcdy.dsc.modules.settle.vo.CustomerInfoData.CustomerCount;
import com.zcdy.dsc.modules.settle.vo.SettleBatchOptEquipInfoVo;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsVo;

/**
 * @Description: 结算批次
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-05-08
 * @Version: V1.0
 */
public interface SettleBatchMapper extends BaseMapper<SettleBatch> {

	/**
	 * 获取设备相关信息
	 * 
	 * @param param
	 *            equipmentId 设备id now 时间
	 * @return 设备信息
	 */
	@Select({"select ", "oe.id as equipmentId,",
			"oe.equipment_name as equipmentName,", "sc.id as contractId,",
			"sc.contract_name as contractName,",
			"sc.customer_id as customerId,",
			"scustomer.customer_name as customerName,",
			"scustomer.customer_type as customerType,",
			"scate.`name` as customerTypeName,",
			"swd.district_id as districtId,",
			"sd.district_name as districtName,",
			"IFNULL(sma.month_balance, '1') as equipmentType",
			"from ",
			"opt_equipment oe ",
			"left join settle_contract_water scw on scw.equipment_id = oe.id and scw.invalid_date >= #{now} and #{now} >= scw.create_time",
			"left join settle_contract sc on sc.id = scw.contract_id",
			"left join settle_customer scustomer on scustomer.id = sc.customer_id",
			"left join sys_category scate on scate.code = scustomer.customer_type",
			"left join settle_water_district swd on swd.equipment_id = oe.id and swd.valid_status = 1",
			"left join settle_district sd on swd.district_id = sd.id",
			"left join settle_meter_attr sma on sma.equipment_id = oe.id",
			"where ", "oe.id = #{equipmentId} "})
	public SettleBatchOptEquipInfoVo getOptEquipInfo(Map<String, String> param);

	/**
	 * 根据水表获取相关变量的名称
	 * 
	 * @param equipmentId
	 *            设备类型
	 * @return
	 */
	@Select({"select ", "variable_name", "from iot_variable_info ivi",
			"left join iot_equipment_variable iev on iev.variable_id = ivi.id",
			"left join iot_opt_equipment ioe on ioe.iot_id = iev.iot_id",
			"left join opt_equipment oe on oe.id = ioe.opt_id", "where ",
			"ivi.variable_type = 'A16A09'", "and oe.id = #{equipmentId}"})
	String getWaterVar(String equipmentId);

	/**
	 * 查询全部表底信息
	 * 
	 * @param param
	 * @return
	 */
	@Select({" <script>",
	        " SELECT", 
	        " t.customer_id customerId,",
	        " t2.customer_name customerName,",
			" (SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'customer_type' ) AND item_value = t2.customer_type ) customerType,",
			" (SELECT	count(1) AS signCount FROM	settle_contract sc	WHERE	sc.customer_id = t.customer_id	) AS signCount,",
			" t.contract_id contractId,",
			" t4.start_date contractValidateDate,",
			" t4.end_date contractInvalidateDate,",
			" t4.contract_name contractName,",
			" CONCAT('[',t7.`name`,'][',t5.equipment_location,'][',t5.equipment_sn,']') equipmentInfo,",
			" t6.district_name equipmentDistrictName,",
			" t.current_flow_date currentFlowDate,",
			" t.current_flow currentFlow,",
			" t8.current_flow_date previousFlowDate,",
			" t8.current_flow previousFlow,",
			" t.used_flow currentUsedFlow,", 
			" t.unit_price currentWaterPrice,",
			" t.total_cost currentCost,", 
			" (SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'settle_status' ) AND item_value = t.`status` ) status,",
			" t9.settle_date settleDate",
			" FROM",
			" settle_batch t",
			" LEFT JOIN settle_customer t2 ON t2.id = t.customer_id",
			" LEFT JOIN sys_category t3 ON t3.`code` = t2.customer_type",
			" LEFT JOIN settle_contract t4 ON t4.id = t.contract_id",
			" LEFT JOIN opt_equipment t5 ON t5.id = t.equipment_id",
			" LEFT JOIN settle_district t6 ON t6.id = t.district_id",
			" LEFT JOIN sys_category t7 ON t5.equipment_section = t7.`code`",
			" LEFT JOIN settle_batch t8 ON t8.id = t.previous_id",
			" LEFT JOIN settle_checklist t9 on t9.id=t.checklist_id",
			" where 1=1",
			" <if test='param.customerName!=null and param.customerName!=\"\"'>",
			" and t2.customer_name like concat('%',#{param.customerName},'%')",
			" </if>",
			" <if test='param.startTime!=null and param.startTime!=\"\"'>",
			" and DATE_FORMAT(t9.settle_date,'%Y-%m') &gt;=#{param.startTime}",
			" </if>",
			" <if test='param.endTime!=null and param.endTime!=\"\"'>",
			" and DATE_FORMAT(t9.settle_date,'%Y-%m')  &lt;=#{param.endTime}",
			" </if>",
			" <if test='param.customerName!=null and param.customerName!=\"\"'>",
			" and t2.customer_name like concat('%',#{param.customerName},'%')",
			" </if>", 
			" <if test='param.status!=null and param.status!=\"\"'>",
			" and t.`status`=#{param.status}",
			" </if>",
			" <if test='param.districtId!=null and param.districtId!=\"\"'>",
			" and t.district_id=#{param.districtId}", 
			" </if>", 
			" order by t.customer_id,t.current_flow_time",
			" </script>",
			})
	public List<SettlementStatisticsVo> listData(
			@Param("param") CustometInfoParam param);

	/**
	 * 客户信息合计
	 * @param param
	 * @return
	 */
	
	@Select({
	    "<script>",
	    " SELECT",
	    " t2.customer_name customerName,",
	    " SUM(t.used_flow) totalMeter,",
	    " SUM(t.total_cost) totalCost",
	    " FROM",
	    " settle_batch t",
	    " LEFT JOIN settle_customer t2 on t2.id=t.customer_id",
	    " LEFT JOIN settle_checklist t9 on t9.id=t.checklist_id",
	    " where t.status in ('01','02')",
	    " <if test='param.customerName!=null and param.customerName!=\"\"'>",
        " and t2.customer_name like concat('%',#{param.customerName},'%')",
        " </if>",
        " <if test='param.startTime!=null and param.startTime!=\"\"'>",
        " and DATE_FORMAT(t9.settle_date,'%Y-%m') &gt;=#{param.startTime}",
        " </if>",
        " <if test='param.endTime!=null and param.endTime!=\"\"'>",
        "  and DATE_FORMAT(t9.settle_date,'%Y-%m')  &lt;=#{param.endTime}",
        " </if>",
        " <if test='param.status!=null and param.status!=\"\"'>",
        " and t.`status`=#{param.status}",
        " </if>",
        " <if test='param.districtId!=null and param.districtId!=\"\"'>",
        " and t.district_id=#{param.districtId}", 
        " </if>", 
        " GROUP BY t.customer_id",
	    "</script>",
	})
	public List<CustomerCount> customerCount(@Param("param")CustometInfoParam param);
	
	/**
	 * 分页查询
	 * @param page
	 * @param param
	 * @return
	 */
	public IPage<SettlementStatisticsVo> historyData(Page<SettlementStatisticsVo> page, @Param("param") CustometInfoParam param);
	
	/**
	 * 导出
	 * @param page
	 * @param param
	 * @return
	 */
	public List<SettlementStatisticsVo> historyData(@Param("param") CustometInfoParam param);
}
