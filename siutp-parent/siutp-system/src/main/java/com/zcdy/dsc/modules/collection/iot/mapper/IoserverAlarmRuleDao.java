package com.zcdy.dsc.modules.collection.iot.mapper;

import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipRuleEntity;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipconfigEntity;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipmentRuleItemEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author： Roberto
 * 创建时间：2020年3月6日 下午3:46:52
 * 描述: <p>采集设备数据规则和告警规则数据库操作层</p>
 */
public interface IoserverAlarmRuleDao {

	/**
	 * @author：Roberto
	 * @create:2020年3月6日 下午3:47:53
	 * 描述:<p>查询所有的设备规则</p>
	 */
	@Select({
		"SELECT",
		"	t.id equipmentId,",
		"	oeq.equipment_sn equipmentSn,",
		"	oeq.equipment_name equipmentName,",
		"	typecategory.`name` equipmentType,",
		"	oeq.equipment_location equipmentLocation,",
		"	sectioncategory.`name` equipmentSection,",
		"	b.and_or andOr,",
		"	b.id ruleId,",
		"	b.alarm_level alarmLevel,",
		"	b.alarm_name alarmName,",
		"	b.rule_type statusType,",
		"	b.alarm ruleAlarm,",
		"	b.alarm_status alarmStatus,",
		"	c.alarm_value messageValue ",
//		"	c.alarm_value messageValue,",
//		"	config.check_quality checkQuality,",
//		"	config.alarm needAlarm,",
//		"	config.alarm_level qualityAlarmLevel,",
//		"	tpl.alarm_value qualityAlarmMessage",
		"FROM",
		"	iot_equipment t",
		"LEFT JOIN iot_equipment_rule b ON t.id = b.equipment_id",
		"LEFT JOIN iot_equipment_message_tpl c ON b.alarm_modle_id = c.id",
		//"LEFT JOIN iot_equipment_config config on t.id = config.equipment_id",
		//"LEFT JOIN iot_equipment_message_tpl tpl on config.alarm_model = tpl.id",
		"left join iot_opt_equipment eq on eq.iot_id = t.id",
		"left join opt_equipment oeq on oeq.id = eq.opt_id",
		"LEFT JOIN sys_category typecategory on typecategory.code = oeq.equipment_type",
		"LEFT JOIN sys_category sectioncategory on sectioncategory.code = oeq.equipment_section",
		//数据清洗要处理所有设备，但是采集策略要使用在运行设备
		//"where t.iot_status ="+StatusConstant.RUN
	})
	List<IotEquipRuleEntity> selectAllEquipmentRules();

	/**
	 * @author：Roberto
	 * @create:2020年3月6日 下午4:00:20
	 * 描述:<p>获取所有的规则规则</p>
	 */
	@Select({
		"SELECT",
		"	d.alarm_id ruleId,",
		"	d.alarm_rule alarmRule,",
		"	d.alarm_value alarmValue,",
		"	e.id variableId,",
		"	e.variable_name variableName,",
		"	e.variable_title variableTitle,",
		"	e.variable_unit variableUnit,",
		"	e.data_type dataType,",
		"	e.scale scale",
		"FROM",
		"	iot_equipment_rule_item d",
		
		"LEFT JOIN iot_variable_info e ON d.variable_id = e.id",
		"WHERE",
		"	e.del_flag = " + StatusConstant.WORKING,
		"AND e.working_status = " + StatusConstant.RUN
	})
	List<IotEquipmentRuleItemEntity> selectAllRuleItems();

	/**
	 * @author：Roberto
	 * @create:2020年4月1日 下午5:30:24
	 * 描述:<p></p>
	 */
	@Select({
		"SELECT",
		"	t.id equipmentId,",
		"	oeq.equipment_sn equipmentSn,",
		"	oeq.equipment_name equipmentName,",
		"	typecategory.`name` equipmentType,",
		"	oeq.equipment_location equipmentLocation,",
		"	sectioncategory.`name` equipmentSection,",
		"	config.check_quality checkQuality,",
		"	config.alarm needAlarm,",
		"	config.alarm_level qualityAlarmLevel,",
		"	tpl.alarm_value qualityAlarmMessage ",
		"FROM",
		"	iot_equipment t ",
		"LEFT JOIN iot_equipment_config config on t.id = config.equipment_id",
		"LEFT JOIN iot_equipment_message_tpl tpl on config.alarm_model = tpl.id",
		"left join iot_opt_equipment eq on eq.iot_id = t.id",
		"left join opt_equipment oeq on oeq.id = eq.opt_id",
		"LEFT JOIN sys_category typecategory on typecategory.code = oeq.equipment_type",
		"LEFT JOIN sys_category sectioncategory on sectioncategory.code = oeq.equipment_section",
		//数据清洗要处理所有设备，但是采集策略要使用在运行设备
		//"where t.iot_status ="+StatusConstant.RUN
	})
	List<IotEquipconfigEntity> selectAllEquipmentConfig();

    /**
     * @param equipmentId
     * @return 规则实体
     */
	@Select({
        "SELECT",
        "   t.id equipmentId,",
        "   oeq.equipment_sn equipmentSn,",
        "   oeq.equipment_name equipmentName,",
        "   typecategory.`name` equipmentType,",
        "   oeq.equipment_location equipmentLocation,",
        "   sectioncategory.`name` equipmentSection,",
        "   b.and_or andOr,",
        "   b.id ruleId,",
        "   b.alarm_level alarmLevel,",
        "   b.alarm_name alarmName,",
        "   b.rule_type statusType,",
        "   b.alarm ruleAlarm,",
        "   b.alarm_status alarmStatus,",
        "   c.alarm_value messageValue ",
        "FROM",
        "   iot_equipment t",
        "LEFT JOIN iot_equipment_rule b ON t.id = b.equipment_id",
        "LEFT JOIN iot_equipment_message_tpl c ON b.alarm_modle_id = c.id",
        "left join iot_opt_equipment eq on eq.iot_id = t.id",
        "left join opt_equipment oeq on oeq.id = eq.opt_id",
        "LEFT JOIN sys_category typecategory on typecategory.code = oeq.equipment_type",
        "LEFT JOIN sys_category sectioncategory on sectioncategory.code = oeq.equipment_section",
        "where b.alarm_status="+StatusConstant.RUN,
        " and t.id = #{value}",
        " limit 1"
        //数据清洗要处理所有设备，但是采集策略要使用在运行设备
        //"where t.iot_status ="+StatusConstant.RUN
    })
    IotEquipRuleEntity selectRuleEntity(String equipmentId);

}
