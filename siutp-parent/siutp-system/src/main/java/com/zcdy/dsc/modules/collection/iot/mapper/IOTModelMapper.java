package com.zcdy.dsc.modules.collection.iot.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.zcdy.dsc.modules.collection.gis.vo.IotEquipInfoVo;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;

/**
 * 描述: 采集数据
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-24
 * 版本号: V1.0
 */
public interface IOTModelMapper {

	@Select({
		"SELECT variable_name varName, variable_title varTitle, variable_unit united from iot_variable_info"
	})
	public List<VariableInfo> selectAllVar();

	/**
	 * @author：Roberto
	 * @create:2020年1月6日 下午5:28:37
	 * 描述:<p>根据模型id查询绑定变量</p>
	 */
	@Select({
		"SELECT t.variable_name varName,t.variable_type variableType, t.variable_title varTitle, t.variable_unit united, ",
		"t.scale, b.display_order displayOrder from iot_variable_info t ",
		"LEFT JOIN model_variable b on t.id = b.variable_id where b.model_id = #{value}",
		" ORDER BY display_order desc"
	})
	public List<VariableInfo> selectVarsByModelId(String modelId);

	/**
	 * @author：Roberto
	 * @create:2020年3月28日 下午4:21:55
	 * 描述:<p>随机获取一个采集设备的变量名称</p>
	 */
	@Select({
		"SELECT t.variable_name from iot_variable_info t LEFT JOIN iot_equipment_variable b on t.id=b.variable_id",
		" where b.iot_id=#{value} and t.working_status=1 and t.del_flag=0 ORDER BY t.variable_name desc LIMIT 1"
	})
	public String selectOneVarNameRandom(String equipmentId);

	/**
	 * 通过设备类型查询对应设备信息
	 * @author songguang.jiao
	 * @date 2020/05/20 12:01:25
	 * @param typeCode
	 * @return
	 */
	@Select({
	    " select ",
	    " t.id gisId,",
	    " CONCAT(t5.`name`,'[',t3.equipment_location,'][',t3.equipment_sn,']') as equipmentName",
	    " FROM",
	    " gis_model t",
	    " LEFT JOIN gis_equipment t2 on t2.model_id=t.id",
	    " LEFT JOIN opt_equipment t3 on t3.id=t2.equipment_id",
	    " LEFT JOIN sys_category t5 on t5.`code`=t3.equipment_section",
	    " WHERE t.model_type_code = #{typeCode}",
	})
    public List<IotEquipInfoVo> selectEquipInfoByModelType(String typeCode);

}
