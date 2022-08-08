package com.zcdy.dsc.modules.collection.iot.mapper;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zcdy.dsc.modules.collection.iot.vo.FlowmeterCumulativeStatisticsVo;

import java.util.List;

/**
 * @author : songguang.jiao
 */
public interface FlowmeterCumulativeStatisticsMapper {

    /**
     * 根据流量计id,查询绑定变量信息.
     *
     * @param 
     * @return 设备信息列表
     * @author tangchao
     * @since 2020-4-28 13:54:14
     */
    /*@Select({
            "<script>",
                "select",
                "oe.id as optEquipmentId,",
                "concat( b.`name`, '[', c.NAME, oe.equipment_location, ']', '[', oe.equipment_sn, ']' ) as equipmentName,",
                "ivi.variable_name as variableName,",
                "ivi.variable_type as variableType,",
                "ivi.variable_title as variableTitle",
                "from iot_equipment ie, ",
                "iot_equipment_variable iev , ",
                "iot_variable_info ivi,",
                "opt_equipment oe LEFT JOIN sys_category b ON oe.equipment_type = b.`code` LEFT JOIN sys_category c ON oe.equipment_section = c.`code` ,",
                "iot_opt_equipment ioe",
                "where ie.id = iev.iot_id and iev.variable_id = ivi.id and oe.id = ioe.opt_id and ioe.iot_id = ie.id",
                "and ie.iot_category = 'A03A06'",
                "<if test='equipmentIds != null and equipmentIds.length != 0'>",
                    "and oe.id in ",
                    "<foreach item='item' index='index' collection='equipmentIds' separator=',' open='(' close=')'>",
                        "#{item}",
                    "</foreach>",
                "</if>",
                "order by ie.iot_name, variable_type",
            "</script>",
    })*/
    
    @Select({
            "<script>",
            "select  " ,
                    "optEquipmentId, " ,
                    "max(equipmentName) as equipmentName, " ,
                    "GROUP_CONCAT(columnName) as columnName, " ,
                    "GROUP_CONCAT(variableName) as variableName, " ,
                    "GROUP_CONCAT(variableType) as variableType, " ,
                    "GROUP_CONCAT(variableTitle) as variableTitle " ,
                    "from ( " ,
                    " select " ,
                    " oe.id as optEquipmentId, " ,
                    " concat( b.`name`, '[', c.NAME, oe.equipment_location, ']', '[', oe.equipment_sn, ']' ) as equipmentName, " ,
                    " concat( c.NAME, oe.equipment_location, ivi.variable_title) as columnName, " ,
                    " ivi.variable_name as variableName, " ,
                    " ivi.variable_type as variableType, " ,
                    " ivi.variable_title as variableTitle " ,
                    " from iot_equipment ie,  " ,
                    " iot_equipment_variable iev ,  " ,
                    " iot_variable_info ivi, " ,
                    " opt_equipment oe LEFT JOIN sys_category b ON oe.equipment_type = b.`code` LEFT JOIN sys_category c ON oe.equipment_section = c.`code` , " ,
                    " iot_opt_equipment ioe " ,
                    " where ie.id = iev.iot_id and iev.variable_id = ivi.id and oe.id = ioe.opt_id and ioe.iot_id = ie.id " ,
                    " and ie.iot_category = 'A03A06' and ivi.variable_type = 'A16A24'" ,
                    "<if test='equipmentIds != null and equipmentIds.length != 0'>",
                        "and oe.id in ",
                        "<foreach item='item' index='index' collection='equipmentIds' separator=',' open='(' close=')'>",
                            "#{item}",
                        "</foreach>",
                    "</if>",
                    " order by ie.iot_name, variable_type " ,
                    ") a " ,
                    "group by a.optEquipmentId",
            "</script>",
    })
    List<FlowmeterCumulativeStatisticsVo> getVariableByEquipmentId(@Param("equipmentIds") String[] equipmentIds);


}
