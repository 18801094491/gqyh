package com.zcdy.dsc.modules.settle.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.settle.param.EquipmentMeterPageParam;
import com.zcdy.dsc.modules.settle.param.SettleWaterParam;
import com.zcdy.dsc.modules.settle.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author： Roberto 创建时间：2020年1月2日 下午2:05:27 描述:
 * <p>
 * 客户信息管理相关的自定义接口
 * </p>
 */
public interface CustomerDao {

    /**
     * 查询最后一个客户编号
     * 
     * @return
     */
    @Select({"SELECT MAX(SUBSTR(customer_sn,2)) from settle_customer"})
    public String getLastSnNum();

    /**
     * 分页查询客户信息列表
     * 
     * @param page
     * @param customerSn
     * @param customerName
     * @param startTime
     * @param endTime
     * @return
     */
    @Select({
        "<script>",
        " SELECT", 
        " t.id id,", 
        " t.customer_name customerName,",
        " t.customer_sn customerSn,",
        " t.customer_address customerAddress,",
        " t.customer_type customerType,",
        " t1.item_text customerTypeName,",
        " t.customer_status customerStatus,",
        " t2.item_text customerStatusName,",
        " cat1.name priceMode,",
        " cat2.name payMode,", 
        " t.price price,", 
        " t.create_time createTime, ",
        " t.custome_duty customeDuty,",
        " t.custome_bank_accout customeBankAccout", 
        " FROM", 
        " settle_customer t",
        " LEFT JOIN sys_category cat1 on t.price_mode=cat1.code",
        " LEFT JOIN sys_category cat2 on t.pay_mode=cat2.code", 
        " LEFT JOIN v_sys_dict_item t1 on t.customer_type=t1.item_value and t1.dict_code='customer_type'",
        " LEFT JOIN v_sys_dict_item t2 on t.customer_status=t2.item_value and t2.dict_code='customer_status'",
        " where t.del_flag='0'",
        " <if test='customerName!=null and customerName!=\"\"' >",
        " and t.customer_name like concat('%',#{customerName},'%')",
        " </if>",
        " <if test='customerSn!=null  and customerSn!=\"\"' >", 
        " and t.customer_sn like concat('%',#{customerSn},'%')",
        " </if >", 
        " <if test='startTime!=null and startTime!=\"\"'>",
        " and  date_format(t.create_time,'%Y-%m-%d')&gt;=#{startTime}", 
        " </if>",
        " <if test ='endTime!=null and endTime!=\"\"'>", 
        " and  date_format(t.create_time,'%Y-%m-%d')&lt;=#{endTime}",
        " </if>", 
        " ORDER BY t.create_time desc",
        "</script>"})
    public IPage<CustomerVo> getList(IPage<CustomerVo> page, @Param("customerSn") String customerSn,
        @Param("customerName") String customerName, @Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * 查看客户信息详情
     * 
     * @param id
     * @return
     */
    @Select({
        " SELECT", 
        " t.id id,",
        " t.customer_name customerName,",
        " t.customer_type customerType,",
        " t1.item_text customerTypeName,",
        " ",
        " ",
        " t.customer_status customerStatus,",
        " t2.item_text customerStatusName,",
        " t.price_mode priceMode,",
        " t.pay_mode payMode,",
        " t.price price,",
        " t.contacter_name contacterName,",
        " t.customer_address customerAddress,", 
        " t.custome_duty customeDuty,",
        " t.custome_bank_accout customeBankAccout,", 
        " t.customer_sn customerSn,", 
        " t.contacter_phone contacterPhone",
        " FROM", 
        " settle_customer t",
        " LEFT JOIN sys_category cat1 on t.price_mode=cat1.code",
        " LEFT JOIN sys_category cat2 on t.pay_mode=cat2.code", 
        " LEFT JOIN v_sys_dict_item t1 on t.customer_type=t1.item_value and t1.dict_code='customer_type'",
        " LEFT JOIN v_sys_dict_item t2 on t.customer_status=t2.item_value and t2.dict_code='customer_status'",
        " where t.id=#{id}"

    })
    public CustomerVo getDetail(String id);

    /**
     * 客户信息导出列表
     * 
     * @param customerSn
     * @param customerName
     * @param startTime
     * @param endTime
     * @return
     */
    @Select({" <script>", " SELECT", " t.customer_name customerName,", " t.customer_address customerAddress,",
        " cat.name customerType,", " cat1.name priceMode,", " cat2.name payMode,", " t.price price,",
        " t.create_time createTime,", " t.customer_sn customerSn", " FROM", " settle_customer t",
        " LEFT JOIN sys_category cat on t.customer_type=cat.code",
        " LEFT JOIN sys_category cat1 on t.price_mode=cat1.code",
        " LEFT JOIN sys_category cat2 on t.pay_mode=cat2.code", " where t.del_flag='0'",
        " <if test='customerName!=null and customerName!=\"\"' >",
        " and t.customer_name like concat('%',#{customerName},'%')", " </if>",
        " <if test='customerSn!=null  and customerSn!=\"\"' >", " and t.customer_sn like concat('%',#{customerSn},'%')",
        " </if >", " <if test='startTime!=null and startTime!=\"\"'>",
        " and  date_format(t.create_time,'%Y-%m-%d')&gt;=#{startTime}", " </if>",
        " <if test ='endTime!=null and endTime!=\"\"'>", " and  date_format(t.create_time,'%Y-%m-%d')&lt;=#{endTime}",
        " </if>", " </script>",})
    public List<ExportCutomerVo> exportCutomerList(@Param("customerSn") String customerSn,
        @Param("customerName") String customerName, @Param("startTime") String startTime,
        @Param("endTime") String endTime);

    /**
     * 查询出所有水表信息
     * @author songguang.jiao
     * @date 2020/05/19 09:58:32
     * @param page
     * @param param
     * @return
     */
    @Select({
        " <script>", 
        " SELECT",
        " t.id id,", 
        " t.equipment_sn equipmentSn,",
        " (SELECT count(*) from settle_customer_equipment settle where settle.equipment_id=t.id and bind_status='00') as bindStatus,",
        " CONCAT(cat.name,'[',t.equipment_location,'][',t.equipment_sn,']') as equipmentInfo,", 
        " t.equipment_name equipmentName",
        " FROM", " opt_equipment t",
        " LEFT JOIN gis_equipment t2 ON t2.equipment_id = t.id",
        " LEFT JOIN model_variable t3 on t3.model_id=t2.model_id",
        " LEFT JOIN sys_category cat on cat.code=t.equipment_section",
        " WHERE",
        " t.equipment_type = 'A03A03' and t.del_flag='0'", 
        " and t3.variable_id is not null",
        " <if test='param.equipmentSn!=null and param.equipmentSn!=\"\"' >",
        " and t.equipment_sn like concat('%',#{param.equipmentSn},'%')",
        " </if>",
        " <if test='param.equimentSection!=null and param.equimentSection!=\"\"' >",
        " and t.equipment_section = #{param.equimentSection}",
        " </if>",
        " <if test='param.equipmemtLocation!=null and param.equipmemtLocation!=\"\"' >",
        " and t.equipment_location like concat('%',#{param.equipmemtLocation},'%')",
        " </if>",
        " <if test='param.equipmentName!=null  and param.equipmentName!=\"\"' >",
        " and t.equipment_name like concat('%',#{param.equipmentName},'%')", 
        " </if >", 
        " GROUP BY t.id",
        " order by bindStatus asc",
        " </script>",})
    public IPage<EquipWatermeterVo> getWatermeterList(IPage<EquipWatermeterVo> page,
        @Param("param")EquipmentMeterPageParam   param);

    /**
     * 查询水表是否被绑定
     * 
     * @param equipmentId
     * @return
     */
    @Select({" SELECT count(*) from settle_customer_equipment where  equipment_id=#{equipmentId} and bind_status='00'"})
    public Integer getBindEquipStatus(String equipmentId);

    /**
     * 逻辑删除, 修改绑定状态 修改人: chaotang
     * 
     * @param id
     * @param now
     */
    @Update({" update settle_customer_equipment set bind_status='01', unbind_time=#{now} where id=#{id} "})
    public void unBindCustomerEquip(@Param("id") String id, @Param("now") String now);

    /**
     * 通过客户id查询已关联水表相关信息 ,添加绑定状态 修改人: chaotang // TODO 未关联资产附属属性中的水表号 attribute_en=？
     * 
     * 
     * @param customerId
     *            用户id
     * @return
     */
    @Select({"select cq.id as id, ", "eq.id as equipmentId, ", "gis.model_id as modelId, ",
        "eq.equipment_sn as equipmentSn, ", "oea.attribute_val as customerWaterSn, ",
        "eq.equipment_name equipmentName, ", "cq.bind_status as bindStatus, ",
        "eq.equipment_location as equipmentLocation, ", "DATE_FORMAT(cq.bind_time,'%Y-%m-%d %H:%i:%s') as  bindTime, ",
        "DATE_FORMAT(cq.unbind_time,'%Y-%m-%d %H:%i:%s')  as unbindTime ", "from opt_equipment eq  ",
        "left join settle_customer_equipment cq on eq.id = cq.equipment_id ",
        "left join gis_equipment gis on gis.equipment_id = eq.id ",
        "left join opt_equipment_attr oea on oea.equipment_id = eq.id ",
        "where eq.id = cq.equipment_id and customer_id = #{customerId} ",
        "order by cq.bind_status asc, bindTime, unbindTime desc",})

    public List<CustomerWaterInfoVo> getBindWateInfoList(String customerId);

    /**
     * 分页查询合同列表
     * 
     * @param page
     * @param contractSn
     * @param contractName
     * @param customer
     * @return
     */
    @Select({" <script>", " SELECT", " t.id,", " t.type,",
        " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'contract_type' ) AND item_value = t.type ) typeName,",
        " t.customer_id customerId,", " t2.customer_name constomerName,", " t.contract_sn contractSn,",
        " t.contract_name contractName,", " t.start_date startDate,", " t.end_date endDate,", " t.file_url fileUrl,",
        " t.contract_use contractUse,", " t.sign_date signDate,", " t.remarks remarks ", " FROM", " settle_contract t",
        " LEFT JOIN settle_customer t2 on t.customer_id=t2.id", " where t.del_flag='0'",
        " <if test='contractSn!=null and contractSn!=\"\"'>", " and t.contract_sn like concat('%',#{contractSn},'%')",
        " </if>", " <if test='contractName!=null and contractName!=\"\"'>",
        " and t.contract_name like concat('%',#{contractName},'%')", " </if>",
        " <if test='customer!=null and customer!=\"\"'>", " and t2.customer_name like concat('%',#{customer},'%')",
        " </if>", " order by t.create_time desc", " </script>",})
    public IPage<ContractVo> queryContractList(Page<ContractVo> page, @Param("contractSn") String contractSn,
        @Param("contractName") String contractName, @Param("customer") String customer);

    /**
     * 查询合同详情信息
     * 
     * @param id
     * @return
     */
    @Select({" SELECT", " t2.customer_name constomerName,", " t.contract_sn contractSn,",
        " t.contract_name contractName,", " t.start_date startDate,", " t.end_date endDate,", " t.file_url fileUrl,",
        " t.sign_date signDate,", " t.remarks remarks,", " t.type type,",
        " (SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'contract_type' ) AND item_value = t.type) typeName,",
        " t.contract_use contractUse", " FROM", " settle_contract t",
        " LEFT JOIN settle_customer t2 on t.customer_id=t2.id", " where t.id=#{id}"})
    ContractVo queryDetailById(String id);

    /**
     * 查询客户名称列表
     * 
     * @param customerName
     * @return
     */
    @Select({" <script>", " SELECT", " t.id,", " t.customer_name customerName", " FROM", " settle_customer t", " WHERE",
        " t.del_flag = '0'", " <if test='customerName!=null and customerName!=\"\"'>",
        "  and t.customer_name like concat('%',#{customerName},'%')", " </if>", " order by t.create_time desc",
        " </script>",})
    List<CustomerDropdown> queryCustomerNames(@Param("customerName") String customerName);

    /**
     * 检验客户是否存在
     * 
     * @param customerId
     * @return
     */
    @Select({"select count(*) from settle_customer where id=#{customerId} "})
    Integer checkCustomeExsit(String customerId);

    /**
     * 水表管理-分页列表查询
     * @author songguang.jiao
     * @date 2020/05/19 09:58:46
     * @param page
     * @param param
     * @return
     */
    @Select({
        " <script>", 
        " SELECT", 
        " t.id waterId,", 
        " scwr.price waterPrice,", 
        " t.equipment_sn waterSn,",
        " t.equipment_name waterName,", 
        " (SELECT `name` FROM sys_category where `code`=t.equipment_specs) waterSpecs,", 
        " (SELECT `name` FROM sys_category where `code`=t.equipment_mode) waterMode,",
        " cat.NAME waterSection,",
        " (SELECT t4.id from settle_district t4 LEFT JOIN settle_water_district t3 on t3.district_id=t4.id where t3.equipment_id=t.id and t3.valid_status='1') districtId,",
        " (SELECT t4.district_name from settle_district t4 LEFT JOIN settle_water_district t3 on t3.district_id=t4.id where t3.equipment_id=t.id and t3.valid_status='1') districtName,",
        " t.equipment_location waterLocation,", 
        "  IFNULL(t5.month_balance,'1')  as remoteStatus,",
        " t7.latitude latitude,",
        " t7.longitude longitude,",
        " t7.model_img modelImg,",
        " t2.customer_name customerName", 
        " FROM", 
        " opt_equipment t",
        " LEFT JOIN sys_category cat ON cat.CODE = t.equipment_section",
        " LEFT JOIN settle_customer_equipment t1 ON t1.equipment_id = t.id",
        " LEFT JOIN settle_customer t2 ON t2.id = t1.customer_id ",
        " left join settle_contract_water scw on   t.id=scw.equipment_id and scw.invalid_date &gt;=now()",
        " left join settle_contract_water_rule_item scwr on scwr.rule_id = scw.rule_id and scwr.start_time &lt;=now() and scwr.end_time &gt;=now()",
        " LEFT JOIN settle_meter_attr t5 on t5.equipment_id=t.id", 
        " LEFT JOIN gis_equipment t6 on t6.equipment_id=t.id",
        " LEFT JOIN gis_model t7 on t7.id=t6.model_id",
        " WHERE",
        " t.equipment_type = 'A03A03' and t1.bind_status='00'", 
        " <if test='param.waterName!=null and param.waterName!=\"\"'>",
        "  and t.equipment_name like concat('%',#{param.waterName},'%')", 
        " </if>",
        " <if test='param.waterSn!=null and param.waterSn!=\"\"'>", 
        "  and t.equipment_sn like concat('%',#{param.waterSn},'%')",
        " </if>", 
        " <if test='param.customerName!=null and param.customerName!=\"\"'>",
        "  and t2.customer_name like concat('%',#{param.customerName},'%')", 
        " </if>",
        " <if test='param.equimentLocation!=null and param.equimentLocation!=\"\"'>",
        "  and t.equipment_location like concat('%',#{param.equimentLocation},'%')", 
        " </if>",
        " <if test='param.equimentSection!=null and param.equimentSection!=\"\"'>",
        "  and t.equipment_section =#{param.equimentSection}", 
        " </if>",
        " order by t1.customer_id",
        " </script>",
        })
    IPage<CustomerMeterInfoVo> queryWaterPageData(Page<CustomerMeterInfoVo> page,@Param("param") SettleWaterParam param);

   /**
    * 检验合同编号是否重复
    * @author songguang.jiao
    * @date 2020/05/19 09:58:08
    * @param id
    * @param contractSn 
    * @return
    */
    @Select({" <script>", " SELECT count(t.id) from  settle_contract t where t.contract_sn=#{contractSn} ",
        " <if test='id!=null and id!=\"\"'>", "  and t.id!=#{id}", " </if>", " </script>",})
    public Boolean checkSnExist(@Param("id") String id, @Param("contractSn") String contractSn);

    /**
     * 检验水表当前时间是否绑定合同
     * 
     * @param id
     * @return
     */
    @Select({
        "SELECT count(id) FROM settle_contract_water where CURRENT_TIMESTAMP<=invalid_date and CURRENT_TIMESTAMP>=create_time and  equipment_id=",
        " (SELECT t.equipment_id from settle_customer_equipment t where id=#{id})",})
    public Integer checkMeterBindContract(String id);

}
