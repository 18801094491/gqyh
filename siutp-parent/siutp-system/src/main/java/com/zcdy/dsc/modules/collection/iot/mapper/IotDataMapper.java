package com.zcdy.dsc.modules.collection.iot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zcdy.dsc.modules.collection.iot.entity.IOTVarData;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;

/**
 * @author： Roberto
 * 创建时间：2020年3月18日 下午3:43:09
 * 描述: <p></p>
 */
public interface IotDataMapper {

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
		"and t.id=#{value}",
		"order by e.variable_name desc"
	})
	public List<VariableInfo> selectVarInfo(@Param("value") String value);

	/**
	 * @author：Roberto
	 * @create:2020年3月18日 下午5:35:59
	 * 描述:<p></p>
	 */
	@Select({
		"${value}"
	})
	public List<IOTVarData> executeHisQuery(@Param("value")String value);
}
