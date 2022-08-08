package com.zcdy.dsc.modules.rdp.mapper;

import com.zcdy.dsc.modules.collection.gis.vo.GisModelImgVo;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.rdp.param.ProjectParam;
import com.zcdy.dsc.modules.rdp.vo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Description: 大屏-数据
 * @Author: han
 * @Date: 2020-12-08 11:40:03
 * @Version: V1.0
 */
public interface RdpInfoMapper {
    /**
     * 描述: 查询未处理的告警事件
     */
    @Select({
            " select",
            " t.id id,",
            " t.warn_sn warnSn,",
            " t.confirm_status confirmStatus,",
            " t.warn_name warnName,",
            " t.warn_level warnLevelCode,",
            " t.warn_content warnContent,",
            " t.rule_content ruleContent,",
            " (SELECT t3.name from sys_category t3 where t3.code=t2.equipment_type) as equipmentType,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_level' ) AND item_value = t.warn_level ) warnLevel,",
            " t.warn_type warnTypeCode,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_type' ) AND item_value = t.warn_type ) warnType,",
            " t.warn_time warnTime,",
            " t.warn_status warnStatusCode,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_status' ) AND item_value = t.warn_status ) warnStatus,",
            " t.warn_way warnWayCode,",
            " ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'warn_way' ) AND item_value = t.warn_way ) warnWay,",
            " t.operate_time operateTime,",
            " t.duration duration,",
            " t.operator operatorId,",
            " (SELECT realname from sys_user where id=t.operator) operator,",
            " t.description ",
            " FROM	",
            " business_warn t",
            " LEFT JOIN iot_opt_equipment t1 on t1.iot_id=t.iot_id",
            " LEFT JOIN opt_equipment t2 on t2.id=t1.opt_id" +
                    " left join iot_equipment ie on t.iot_id=ie.id",
            " where  t.warn_status=#{undeal2}" +
                    " AND t.warn_level IN ( select warn_level_code from iot_equipment_rule_warn where type='0'   )" +
                    " and ie.iot_category IN ( select warn_level_code from iot_equipment_rule_warn where type='1'  ) "
    })
    List<RealTimeEarlyWarningVo> queryRealTimeEarlyWarningVoList(@Param("undeal1") String undeal1, @Param("undeal2") String undeal2);

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
     * 区域列表
     */
    @Select({"SELECT\n" +
            "region.id,\n" +
            "region.region_name,\n" +
            "region.region_address_info,\n" +
            "region.region_point_longitude,\n" +
            "region.region_point_latitude,\n" +
            "region.create_by,\n" +
            "region.create_time,\n" +
            "region.update_by,\n" +
            "region.update_time,\n" +
            "region.active,\n" +
            "region.del_flag\n" +
            "FROM\n" +
            "map_region_management AS region"})
    List<RegionVo> regionList();

    /**
     * 河流列表
     */
    @Select({"SELECT\n" +
            "river.id,\n" +
            "river.river_name,\n" +
            "river.river_level,\n" +
            "river.river_start_longitude,\n" +
            "river.river_start_latitude,\n" +
            "river.river_end_longitude,\n" +
            "river.river_end_latitude,\n" +
            "river.river_address_info,\n" +
            "river.create_by,\n" +
            "river.create_time,\n" +
            "river.update_by,\n" +
            "river.update_time,\n" +
            "river.active,\n" +
            "river.del_flag,\n" +
            "river.river_style,\n" +
            "river.river_weight,\n" +
            "river.river_color,\n" +
            "river.river_opacity\n" +
            "FROM\n" +
            "map_river_information AS river\n"})
    List<RiverVo> riverList();

    /**
     * 根据id获取区域信息
     */
    @Select({"SELECT\n" +
            "region.id,\n" +
            "region.region_name,\n" +
            "region.region_address_info,\n" +
            "region.region_point_longitude,\n" +
            "region.region_point_latitude,\n" +
            "region.create_by,\n" +
            "region.create_time,\n" +
            "region.update_by,\n" +
            "region.update_time,\n" +
            "region.active,\n" +
            "region.del_flag\n" +
            "FROM\n" +
            "map_region_management AS region\n" +
            "WHERE\n" +
            "region.id = #{projectParam.regionId}\n"})
    RegionVo getRegionById(@Param("projectParam") ProjectParam projectParam);

    /**
     * 根据id获取区域信息
     */
    @Select({"SELECT\n" +
            "\tgis.id id,\n" +
            "\tgis.model_type_code modelTypeCode," +
            "gis.model_name modelName,\n" +
            "\tgis.model_img img,\n" +
            "\tgis.model_waring_img AS waringImg,\n" +
            "\tgis.model_on_img AS onImg,\n" +
            "\tgis.model_off_img AS offImg,\n" +
            "\tgis.width width,\n" +
            "\tgis.height height,\n" +
            "\tgis.model_offset modelOffset,\n" +
            "\tgis.longitude longitude,\n" +
            "\tgis.latitude latitude,\n" +
            "\tgis.model_position modelPosition,\n" +
            "\tCONCAT(t2.`name`,'[',t1.equipment_location,']') as equipLocation,\n" +
            "\tgis.model_level modelLevel\n" +
            "FROM\n" +
            "\tgis_model gis\n" +
            "LEFT JOIN gis_equipment gise on gis.id=gise.model_id\n" +
            "LEFT JOIN opt_equipment t1 on t1.id=gise.equipment_id\n" +
            "LEFT JOIN sys_category t2 on t2.`code`=t1.equipment_section\n" +
            "where\n" +
            " gis.model_type_code = #{model_type_code}"})
    List<GisModelImgVo> getProjectList(@Param("model_type_code") String model_type_code);

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
