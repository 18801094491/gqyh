package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.zcdy.dsc.modules.operation.equipment.entity.OptBasicInfomation;
import com.zcdy.dsc.modules.operation.equipment.param.OptBaseInfoPageParam;
import com.zcdy.dsc.modules.operation.equipment.vo.OptBaseInfoVo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 设备台账基本信息
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-05-28
 * @Version: V1.0
 */
public interface OptBasicInfomationMapper extends BaseMapper<OptBasicInfomation> {

    /**
     * 分页查询
     *
     * @param param
     * @return
     * @author songguang.jiao
     * @date 2020/05/28 15:53:46
     */
    @Select({
            " <script>",
            " SELECT",
            " t.id,",
            " t.base_name baseName,",
            " t.equipment_model equipmentModel,",
            " t2.`name` equipmentModelName,",
            " t.equipment_specs equipmentSpecs,",
            " t3.`name` equipmentSpecsName,",
            " t.equipment_type equipmentType,",
            " t4.`name` equipmentTypeName,",
            " t.equipment_supplier equipmentSupplier,",
            " t5.supplier_name equipmentSupplierName",
            " FROM opt_basic_infomation t",
            " LEFT JOIN sys_category t2 ON t2.`code` = t.equipment_model",
            " LEFT JOIN sys_category t3 ON t3.`code` = t.equipment_specs",
            " LEFT JOIN sys_category t4 ON t4.`code` = t.equipment_type",
            " LEFT JOIN opt_supplier t5 ON t5.id = t.equipment_supplier",
            " where t.del_flag='0'",
            " <if test='param.equipmentType!=null and param.equipmentType!=\"\"'>",
            " and t.equipment_type=#{param.equipmentType} ",
            " </if>",
            " <if test='param.equipmentSpecs!=null and param.equipmentSpecs!=\"\"'>",
            " and t.equipment_specs=#{param.equipmentSpecs} ",
            " </if>",
            " <if test='param.equipmentModel!=null and param.equipmentModel!=\"\"'>",
            " and t.equipment_model=#{param.equipmentModel} ",
            " </if>",
            " <if test='param.equipmentSupplier!=null and param.equipmentSupplier!=\"\"'>",
            " and t.equipment_supplier=#{param.equipmentSupplier} ",
            " </if>",
            " order by t.create_time desc",
            " </script>",
    })
    IPage<OptBaseInfoVo> selectPageData(Page<OptBaseInfoVo> page, @Param("param") OptBaseInfoPageParam param);

}
