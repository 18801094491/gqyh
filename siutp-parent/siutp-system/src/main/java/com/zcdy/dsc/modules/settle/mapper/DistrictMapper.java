package com.zcdy.dsc.modules.settle.mapper;

import com.zcdy.dsc.modules.settle.entity.District;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.settle.vo.DistrictTreeVo;
import com.zcdy.dsc.modules.settle.vo.DistrictWaterCustomerInfoVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Description: 区域配置
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-20
 * @Version: V1.0
 */
public interface DistrictMapper extends BaseMapper<District> {
    List<DistrictTreeVo> getTreeByParentId(@Param("id") String id);
    List<DistrictTreeVo> getTreeById(@Param("id") String id);

    @Select({
            "select" ,
                    "sd.id," ,
                    "swd.district_id," ,
                    "sd.parent_ids," ,
                    "concat(sd.parent_ids, sd.id, ',%')," ,
                    "sd.district_name as districtName," ,
                    "sc.customer_name as customerName," ,
                    "sc.customer_type as customerType," ,
                    "(select name from sys_category where code = sc.customer_type) as customerTypeName," ,
                    "s.contract_use as contractUse," ,
                    "0.0 as price," ,
                    "oe.id as equipmentId," ,
                    "oe.equipment_name as equipmentName," ,
                    "oe.equipment_sn as equipmentSn," ,
                    "mfm.positive_flow as lastMonthFlow, " ,
                    "'0' as currentDayFlow," ,
                    "(SELECT" ,
                            "ivi.variable_name variableName" ,
                            "FROM" ,
                            "iot_variable_info ivi" ,
                            "LEFT JOIN iot_equipment_variable iev ON ivi.id = iev.variable_id" ,
                            "LEFT JOIN iot_equipment ie ON iev.iot_id = ie.id " ,
                            "LEFT JOIN iot_opt_equipment ioe on ie.id = ioe.iot_id" ,
                            "WHERE" ,
                            "1 = 1" ,
                            "AND ioe.opt_id = oe.id" ,
                            "AND iev.iot_id IS NOT NULL" ,
                            "AND ivi.variable_type = 'A16A09') as variableName," ,
                    "s.contract_name as contractName," ,
                    "s.contract_sn as contractSn," ,
                    "s.end_date as endDate," ,
                    "datediff(s.end_date, #{now}) as remainDay," ,
                    "IFNULL((select config_value >= (datediff(s.end_date,  #{now})) from system_config isc where isc.config_key = 'contract.remind.remaining.days' and isc.config_status = '1'),0) as isWarning" ,
                    "from  opt_equipment oe" ,
                    "left join meter_flow_month mfm on oe.id = mfm.equipment_id and DATE_FORMAT(mfm.statics_date,'%Y%m') =  DATE_FORMAT(date_sub( #{now}, INTERVAL 1 MONTH),'%Y%m')" ,
                    "left join settle_contract_water scw on scw.equipment_id = oe.id and scw.invalid_date >  #{now}" ,
                    "left join settle_contract s on s.id = scw.contract_id",
                    "left join settle_customer_equipment sce on oe.id = sce.equipment_id and bind_status='00'" ,
                    "left join settle_customer sc on sc.id = sce.customer_id" ,
                    "left join settle_water_district swd on swd.equipment_id = oe.id and swd.valid_status = 1" ,
                    "left join settle_district sd on swd.district_id = sd.id" ,
                    "where" ,
                    "sd.id = #{districtId} " ,
                    "OR sd.parent_ids LIKE concat(#{districtParentIds}, '%' )",
                    "order by CONVERT(districtName USING gbk) asc,  CONVERT(customerName USING gbk) asc",
    })
    List<DistrictWaterCustomerInfoVo> queryWaterAndCustomerInfoByDistrictId(Map<String, String> param);

    /**
     * 根据模糊关键字查询树形结构
     * @param keyword
     * @return
     */
    List<DistrictTreeVo> getTreeByKeyword(@Param("keyword") String keyword);
}
