package com.zcdy.dsc.modules.datacenter.statistic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.datacenter.statistic.entity.ModuleVar;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentVariableVO;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 描述: 统计模块变量关联表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-19
 * 版本号: V1.0
 */
public interface ModuleVarMapper extends BaseMapper<ModuleVar> {

	/**
	 * @author：Roberto
	 * @create:2020年3月19日 下午6:31:14
	 * 描述:<p></p>
	 */
	@Select({
		"SELECT b.`code`, b.`name` title from statisc_module_var t LEFT JOIN sys_category b on t.var_type = b.`code` where t.statistic_project=#{moduleId}"
	})
	List<SysCateDropdown> selectModuleItemData(String moduleId);

	/**
	 * @author：Roberto
	 * @create:2020年3月20日 下午12:20:23
	 * 描述:<p></p>
	 */
	@Select({
		"<script>",
		"SELECT",
		"	b.variable_name variableName,",
		"	b.variable_title variableTitle,",
		"	b.variable_unit variableUnit,",
		"	b.variable_type variableType,",
		"	b.scale,",
		"	e.id equipmentId,",
		"	e.equipment_location,",
		"	f.`name` AS section,",
		"	g. NAME AS equipmentType",
		"FROM",
		"	statisc_module_var t",
		"LEFT JOIN iot_variable_info b ON t.var_type = b.variable_type",
		"LEFT JOIN iot_equipment_variable c ON c.variable_id = b.id",
		"LEFT JOIN iot_opt_equipment d ON d.iot_id = c.iot_id",
		"LEFT JOIN opt_equipment e ON e.id = d.opt_id",
		"LEFT JOIN sys_category f ON e.equipment_section = f.`code`",
		"LEFT JOIN sys_category g ON e.equipment_type = g.`code`",
		"WHERE",
		"	e.equipment_revstop = "+StatusConstant.RUN,
		"<if test='typeCode!=null and typeCode!=\"\"'>",
		"	and t.var_type=#{typeCode}",
		"</if>",
		"<if test='moduleId!=null and moduleId!=\"\"'>",
		"	and t.statistic_project=#{moduleId}",
		"</if>",
		"</script>"
	})
	List<EquipmentVariableVO> selectEquipAndVarByModuleId(@Param("moduleId") String moduleId,@Param("typeCode") String typeCode);

	/**
	 * @author：Roberto
	 * @create:2020年3月20日 下午5:54:38
	 * 描述:<p></p>
	 */
	@Select({
		"<script>",
		"SELECT",
		"	b.variable_name variableName,",
		"	b.variable_title variableTitle,",
		"	b.variable_unit variableUnit,",
		"	b.variable_type variableType,",
		"	b.scale,",
		"	e.id equipmentId,",
		"	e.equipment_location,",
		"	f.`name` AS section,",
		"	g. NAME AS equipmentType,",
		"	m.table_name tableName,",
		"	m.table_column tableColumn",
		"FROM",
		"	statisc_module_var t",
		"LEFT JOIN iot_variable_info b ON t.var_type = b.variable_type",
		"LEFT JOIN iot_equipment_variable c ON c.variable_id = b.id",
		"LEFT JOIN iot_opt_equipment d ON d.iot_id = c.iot_id",
		"LEFT JOIN opt_equipment e ON e.id = d.opt_id",
		"LEFT JOIN sys_category f ON e.equipment_section = f.`code`",
		"LEFT JOIN sys_category g ON e.equipment_type = g.`code`",
		"LEFT JOIN tb_column_var m on m.var_type=t.var_type",
		"WHERE",
		"	e.equipment_revstop = "+StatusConstant.RUN,
		"<if test='moduleId!=null and moduleId!=\"\"'>",
		"	and t.statistic_project=#{moduleId}",
		"	and m.module_id = #{moduleId}",
		"</if>",
		"</script>"
	})
	List<EquipmentVariableVO> selectAllEquipAndVarByModuleId(@Param("moduleId") String moduleId);

}
