package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentTreeQueryParam;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentTreeVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author tangchao
 * 设备台账树
 * @date 2020/5/28
 */
public interface OptEquipmentTreeMapper {

    /**
     * 查询设备台账 分页
     *
     * @param page
     * @param param 参数
     * @return 分页结果
     */
    @Select({
            "<script>",
            "SELECT",
            "eq.id id,",
            "eq.equipment_sn equipmentSn,",
            "eq.equipment_name equipmentName,",
            "cat1.name equipmentType,",
            "cat2.name equipmentCategory,",
            "eq.equipment_mode equipmentMode,",
            "(SELECT name from sys_category where code=eq.equipment_mode) equipmentModeName,",
            "eq.equipment_specs equipmentSpecs,",
            "(SELECT name from sys_category where code=eq.equipment_specs) equipmentSpecsName,",
            "cat3.name equipmentSection,",
            "eq.equipment_location equipmentLocation,",
            "sup.supplier_name equipmentSupplier,",
            "cat4.name  equipmentStatus,",
            "eq.equipment_img equipmentImg,",
            "eq.equipment_operating equipmentOperating,",
            "eq.equipment_purchase equipmentPurchase,",
            "eq.equipment_revstop equipmentRevstop,",
            "(SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' )AND item_value = eq.equipment_revstop) as equipmentRevstopText,",
            "ol.id as labelId,",
            "ol.label_name as labelName",
            "FROM",
            "opt_equipment eq",
            "LEFT JOIN sys_category cat1 on cat1.code=eq.equipment_type",
            "LEFT JOIN sys_category cat2 on cat2.code=eq.equipment_category",
            "LEFT JOIN sys_category cat3 on cat3.code=eq.equipment_section",
            "LEFT JOIN sys_category cat4 on cat4.code=eq.equipment_status",
            "LEFT JOIN opt_supplier sup  on sup.id=eq.equipment_supplier",
            "LEFT JOIN opt_equipment_label oel  on oel.equipment_id=eq.id",
            "LEFT JOIN opt_label ol  on ol.id=oel.label_id",
            "where eq.del_flag='0'",
            "<if test=\"param.equipmentSupplier!=null and param.equipmentSupplier!=''\">",
            "and eq.equipment_supplier=#{param.equipmentSupplier}",
            "</if>",
            "<if test=\"param.equipmentRevstop!=null and param.equipmentRevstop!=''\">",
            "and eq.equipment_revstop=#{param.equipmentRevstop}",
            "</if>",
            "<if test=\"param.equipmentSn!=null and param.equipmentSn!=''\">",
            "and eq.equipment_sn like concat('%',#{param.equipmentSn},'%')",
            "</if>",
            "<if test=\"param.labelId!=null and param.labelId != '' and param.labelParentIds != null and param.labelParentIds != '' \">",
            "and (ol.id=#{param.labelId} or ol.parent_ids LIKE concat(#{param.labelParentIds}, '%' ))",
            "</if>",
            "<if test=\"param.optLocation!=null and param.optLocation!=''\">",
            "and eq.equipment_location like concat('%',#{param.optLocation},'%')",
            "</if>",
            "group by eq.id",
            "ORDER BY eq.equipment_section desc,eq.equipment_type desc, eq.equipment_sn asc,eq.equipment_supplier desc,eq.equipment_mode desc",
            "</script>",
    })
    IPage<OptEquipmentTreeVo> pageIndex(Page page, @Param("param") OptEquipmentTreeQueryParam param);

}
