package com.zcdy.dsc.modules.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.inspection.entity.InspPoint;
import com.zcdy.dsc.modules.inspection.param.EquipmentQueryParam;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: 巡检点
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface InspPointMapper extends BaseMapper<InspPoint> {

    @Select({"<script>" +
            "SELECT\n" +
            "        eq.id id,\n" +
            "        eq.equipment_sn equipmentSn,\n" +
            "        eq.equipment_name equipmentName,\n" +
            "        cat1.name equipmentType,\n" +
            "        cat2.name equipmentCategory,\n" +
            "        eq.equipment_mode equipmentMode,\n" +
            "        (SELECT name from sys_category where code=eq.equipment_mode) equipmentModeName,\n" +
            "        eq.equipment_specs equipmentSpecs,\n" +
            "        (SELECT name from sys_category where code=eq.equipment_specs) equipmentSpecsName,\n" +
            "        cat3.name equipmentSection,\n" +
            "        eq.equipment_location equipmentLocation,\n" +
            "        sup.supplier_name equipmentSupplier,\n" +
            "        cat4.name equipmentStatus,\n" +
            "        eq.equipment_img equipmentImg,\n" +
            "        eq.equipment_operating equipmentOperating,\n" +
            "        eq.equipment_purchase equipmentPurchase,\n" +
            "        eq.equipment_revstop equipmentRevstop,\n" +
            "        (SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code =\n" +
            "        'working_status' )AND item_value = eq.equipment_revstop) as equipmentRevstopText,\n" +
            "        ISNULL(gis.model_id) as bindStatus\n" +
            "        FROM\n" +
            "        opt_equipment eq\n" +
            "        LEFT JOIN sys_category cat1 on cat1.code=eq.equipment_type\n" +
            "        LEFT JOIN sys_category cat2 on cat2.code=eq.equipment_category\n" +
            "        LEFT JOIN sys_category cat3 on cat3.code=eq.equipment_section\n" +
            "        LEFT JOIN sys_category cat4 on cat4.code=eq.equipment_status\n" +
            "        LEFT JOIN opt_supplier sup on sup.id=eq.equipment_supplier\n" +
            "        left join gis_equipment gis on gis.equipment_id=eq.id\n" +
            "        where eq.del_flag= " + StatusConstant.WORKING + "\n" +
            "        <if test=\"equipmentSn != null and equipmentSn != ''\">\n" +
            "            and eq.equipment_sn like concat('%',#{equipmentSn},'%')\n" +
            "        </if>\n" +
            "        <if test=\"equipmentName != null and equipmentName != ''\">\n" +
            "            and eq.equipment_name like concat('%',#{equipmentName},'%')\n" +
            "        </if>\n" +
            "        ORDER BY eq.equipment_type desc, eq.equipment_sn asc" +
            "</script>"})
    List<OptEquipmentModel> getEquipmentList(EquipmentQueryParam query);

    @Select({"<script>" +
            "select p.id, p.name, p.code,\n" +
                "p.area_id, a.name areaName, p.important, \n" +
                "p.notices, p.type, p.longitude, p.latitude, \n" +
                "p.equipment_id equipmentId, \n" +
                "(select di.item_text\n" +
                    "from sys_dict d \n" +
                        "left join sys_dict_item di on d.id = di.dict_id\n" +
                    "where d.dict_code = #{query.queryTypeDictCode} and p.type = di.item_value ) typeDes, \n" +
                "(select di.item_text\n" +
                    "from sys_dict d \n" +
                        "left join sys_dict_item di on d.id = di.dict_id\n" +
                    "where d.dict_code = #{query.queryImportDictCode} and p.important = di.item_value ) importDes \n" +
            "from work_list_inspection_point p\n" +
                "left join work_list_inspection_area a on a.id = p.area_id\n" +
            "where p.del_flag = #{query.delFlag} " +
            "<if test='query.name != null and query.name != \"\"'>",
            "and p.name like concat('%',#{query.name},'%') ",
            "</if>" +
            "<if test='query.code != null and query.code != \"\"'>",
            "and p.code like concat('%',#{query.code},'%') ",
            "</if>" +
            "<if test='query.areaId != null and query.areaId != \"\"'>",
            "and p.area_id  = #{query.areaId} ",
            "</if>" +
            "<if test='query.type != null and query.type != \"\"'>",
            "and p.type  = #{query.type} ",
            "</if>" +
            "order by p.create_time desc" +
            "</script>"})
    IPage<InspPoint> selectPageDate(IPage<InspPoint> page, InspPoint query);
}
