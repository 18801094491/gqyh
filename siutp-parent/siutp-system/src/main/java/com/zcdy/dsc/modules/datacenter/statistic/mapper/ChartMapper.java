package com.zcdy.dsc.modules.datacenter.statistic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.datacenter.statistic.entity.Chart;
import com.zcdy.dsc.modules.datacenter.statistic.entity.MeterFlowCount;
import com.zcdy.dsc.modules.datacenter.statistic.param.ChartParam;
import com.zcdy.dsc.modules.datacenter.statistic.vo.ChartVo;
import com.zcdy.dsc.modules.datacenter.statistic.vo.VarNameTitleVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 描述: 统计事项
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-13
 * 版本号: V1.0
 */
public interface ChartMapper extends BaseMapper<Chart> {

	/**
	 * 分页列表
	 * @param page 分页参数
	 * @param param 其它参数
	 * @return
	 */
	@Select({
		"<script>",
		"SELECT",
		"	t.id,",
		"	t.statistic_name statisticName,",
		"	t.statistic_cycle statisticCycle,",
		"	t.statistic_status statisticStatus,",
		"	t.cycle_time cycleTime,",
		"	t.start_time startTime,",
		"	t.end_time endTime,",
		"	t.display_order displayOrder,",
		"	tempb.item_text chartType,",
		"	tempc.item_text statisticCycleType",
		"FROM",
		"	statistic_chart t",
		"LEFT JOIN (",
		"	SELECT",
		"		b.item_text,",
		"		b.item_value",
		"	FROM",
		"		sys_dict_item b",
		"	LEFT JOIN sys_dict c ON b.dict_id = c.id",
		"	WHERE",
		"		c.dict_code = 'working_status'",
		") tempa ON t.statistic_status = tempa.item_value",
		"LEFT JOIN (",
		"	SELECT",
		"		b.item_text,",
		"		b.item_value",
		"	FROM",
		"		sys_dict_item b",
		"	LEFT JOIN sys_dict c ON b.dict_id = c.id",
		"	WHERE",
		"		c.dict_code = 'chart_type'",
		") tempb ON t.chart_type = tempb.item_value",
		"LEFT JOIN (",
		"	SELECT",
		"		b.item_text,",
		"		b.item_value",
		"	FROM",
		"		sys_dict_item b",
		"	LEFT JOIN sys_dict c ON b.dict_id = c.id",
		"	WHERE",
		"		c.dict_code = 'cycle_type'",
		") tempc ON t.statistic_cycle_type = tempc.item_value",
		"where 1=1 ",
		"<if test='param.chartName!=null and param.chartName!=\"\"'>",
		"and t.statistic_name like concat('%',#{param.chartName},'%')",
		"</if>",
		"<if test='param.chartType!=null and param.chartType!=\"\"'>",
		"and t.chart_type = #{param.chartType}",
		"</if>",
		"<if test='param.chartStatus!=null and param.chartStatus!=\"\"'>",
		"and t.statistic_status = #{param.chartStatus}",
		"</if>",
		" order by t.display_order desc",
		"</script>"
	})
	IPage<ChartVo> selectChartList(Page<ChartVo> page, @Param("param") ChartParam param);

	/**
	 * 通过设备id查询列表
	 * @param id
	 * @return
	 */
	@Select({
		"SELECT",
		"		e.variable_name variableName,e.variable_title variableTitle",
		"FROM",
		"	opt_equipment t",
		"LEFT JOIN iot_opt_equipment b on t.id = b.opt_id",
		"LEFT JOIN iot_equipment c on b.iot_id = c.id",
		"LEFT JOIN iot_equipment_variable d on d.iot_id = c.id",
		"LEFT JOIN iot_variable_info e on e.id = d.variable_id",
		"where e.variable_name is not null",
		"and t.id=#{value}"
	})
	List<VarNameTitleVO> selectVarInfoByOptId(String id);

	/**
	 * 按日统计用水量
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Select({
		" SELECT",
		" t.statics_date countDate,",
		" t1.equipment_type equipType,",
		" sum(t.net_flow) countValue",
		" FROM",
		" meter_flow t",
		" LEFT JOIN opt_equipment t1 ON t1.id = t.equipment_id",
		" WHERE",
		"  DATE_FORMAT(t.statics_date, '%Y-%m-%d')>=#{startTime} and  DATE_FORMAT(t.statics_date, '%Y-%m-%d') <= #{endTime}",
		" GROUP BY",
		" t.statics_date,t1.equipment_type",
	})
	List<MeterFlowCount> queryByDay(@Param("startTime") String startTime,@Param("endTime") String endTime);

	
	/**
	 * 描述: 按月统计用水量
	 * @author：  songguang.jiao
	 * 创建时间：  2020年3月24日 下午4:18:26
	 * 版本号: V1.0
	 */
	@Select({
		" SELECT",
		" DATE_FORMAT(t.statics_date, '%Y-%m')  countDate,",
		" t1.equipment_type equipType,",
		" sum(t.net_flow) countValue",
		" FROM",
		" meter_flow t",
		" LEFT JOIN opt_equipment t1 ON t1.id = t.equipment_id",
		" WHERE",
		" DATE_FORMAT(t.statics_date, '%Y-%m-%d') >= #{startTime}",
		" AND DATE_FORMAT(t.statics_date, '%Y-%m-%d') <= #{endTime}",
		" GROUP BY",
		" DATE_FORMAT(t.statics_date, '%Y-%m'),",
		" t1.equipment_type",
	})
	List<MeterFlowCount> queryByMonth(@Param("startTime") String startTime,@Param("endTime") String endTime);
	
	
	/**
	 * 描述: 按年统计用水量
	 * @author：  songguang.jiao
	 * 创建时间：  2020年3月24日 下午4:22:50
	 * 版本号: V1.0
	 */
	@Select({
		" SELECT",
		" DATE_FORMAT(t.statics_date, '%Y') countDate,",
		" t1.equipment_type equipType,",
		" sum(t.net_flow) countValue",
		" FROM	",
		" meter_flow t",
		" LEFT JOIN opt_equipment t1 ON t1.id = t.equipment_id",
		" WHERE",
		" DATE_FORMAT(t.statics_date, '%Y-%m-%d') >= #{startTime}",
		" AND DATE_FORMAT(t.statics_date, '%Y-%m-%d') <= #{endTime}",
		" GROUP BY",
		" DATE_FORMAT(t.statics_date, '%Y'),t1.equipment_type",
	})
	List<MeterFlowCount> queryByYear(@Param("startTime") String startTime,@Param("endTime") String endTime);
	
}
