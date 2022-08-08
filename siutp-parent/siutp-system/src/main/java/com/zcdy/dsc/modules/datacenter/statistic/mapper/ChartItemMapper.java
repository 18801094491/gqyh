package com.zcdy.dsc.modules.datacenter.statistic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.datacenter.statistic.entity.ChartItem;
import com.zcdy.dsc.modules.datacenter.statistic.entity.ChartSerial;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 描述: 统计要素
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-13
 * 版本号: V1.0
 */
public interface ChartItemMapper extends BaseMapper<ChartItem> {

	/**
	 * @author：Roberto
	 * @create:2020年3月15日 上午11:49:13
	 * 描述:<p></p>
	 */
	@Select({
		"SELECT",
		"	t.id,",
		"	t.statistic_name statisticName,",
		"	t.chart_type chartType,",
		"	t.statistic_cycle statisticCycle,",
		"	taba.item_text statisticCycleType,",
		"	b.variable_name variableName,",
		"	b.serial_name serialName,",
		"	c.equipment_name equipmentName,",
		"	c.equipment_type equipmentType,",
		"	c.equipment_location equipmentLocation",
		"FROM",
		"	statistic_chart t",
		"LEFT JOIN statistic_chart_item b ON t.id = b.statistic_id",
		"LEFT JOIN opt_equipment c on b.opt_id = c.id",
		"LEFT JOIN ",
		"(SELECT b.item_text,b.item_value from sys_dict a LEFT JOIN sys_dict_item b on a.id = b.dict_id where a.dict_code='cycle_type') as taba",
		"on t.statistic_cycle_type = taba.item_value",
		"where t.statistic_status = " + StatusConstant.WORKING
	})
	List<ChartSerial> selectSerialData();

	/**
	 * @author：Roberto
	 * @create:2020年3月15日 下午5:59:29
	 * 描述:<p></p>
	 */
	@Select({
		"SELECT",
		"	t.id,",
		"	t.statistic_name statisticName,",
		"	t.chart_type chartType,",
		"	t.statistic_cycle statisticCycle,",
		"	taba.item_text statisticCycleType,",
		"	b.variable_name variableName,",
		"	b.serial_name serialName,",
		"	d.name equipmentSection,",
		"	c.equipment_sn equipmentSn,",
		"	c.equipment_type equipmentType,",
		"	c.equipment_location equipmentLocation,",
		"	c.equipment_name equipmentName",
		"FROM",
		"	statistic_chart t",
		"LEFT JOIN statistic_chart_item b ON t.id = b.statistic_id",
		"LEFT JOIN opt_equipment c on b.opt_id = c.id",
		"LEFT JOIN ",
		"(SELECT b.item_text,b.item_value from sys_dict a LEFT JOIN sys_dict_item b on a.id = b.dict_id where a.dict_code='cycle_type') as taba",
		"on t.statistic_cycle_type = taba.item_value",
		"left join sys_category d on d.code = c.equipment_section",
		"where t.statistic_status = " + StatusConstant.RUN,
		"and t.id=#{value}"
	})
	List<ChartSerial> selectSerialDataByChartId(String id);

}
