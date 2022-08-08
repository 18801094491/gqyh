package com.zcdy.dsc.modules.datacenter.statistic.mapper;

import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentInfo;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentsVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description: 数据看板mapper
 * @Author: 在信汇通
 * @Date: 2020-12-29 11:04:37
 * @Version: V1.0
 */
public interface DataBoardMapper {
    /**
     * 获取设备资产列表
     * @param equipment_type 设备类型，参考数据字典
     * @return
     */
    @Results(id = "equipmentVarInfoList",
            value = {
                    @Result(property = "id",column = "id" ,id = true),
                    @Result(property = "name",column = "name" ),
                    @Result(property = "equipmentInfoList",javaType = List.class,many = @Many(select = "selectVarInfo"),column = "id"),
            })
    @Select({"select id , equipment_name name from opt_equipment where del_flag = 0 and equipment_type = #{equipment_type}"})
    List<EquipmentsVo> equipmentList(@Param("equipment_type") String equipment_type);

    /**
     * 获取资产设备参数列表
     * @param id 资产设备id
     * @return
     */
    @Select({
            "SELECT",
            "	e.variable_name varName,e.variable_title varTitle, e.variable_unit united, e.scale, e.variable_type variableType ",
            "FROM",
            "	opt_equipment t",
            "LEFT JOIN iot_opt_equipment b on t.id = b.opt_id",
            "LEFT JOIN iot_equipment c on b.iot_id = c.id",
            "LEFT JOIN iot_equipment_variable d on d.iot_id = c.id",
            "LEFT JOIN iot_variable_info e on e.id = d.variable_id",
            "where e.variable_name is not null",
            "and t.id=#{id}",
            "order by e.variable_name desc"
    })
    public List<EquipmentInfo> selectVarInfo(@Param("id") String id);
}
