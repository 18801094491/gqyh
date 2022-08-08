package com.zcdy.dsc.modules.settle.mapper;

import com.zcdy.dsc.modules.settle.entity.FlowDayRecord;
import com.zcdy.dsc.modules.settle.entity.FlowDayRecordEntity;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 每日用水量记录
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-30
 * @Version: V1.0
 */
public interface FlowDayRecordMapper extends BaseMapper<FlowDayRecord> {

	/**
	 * 描述: 按月统计
	 * @author: songguang.jiao
	 * 创建时间:  2020年5月6日 下午3:23:26
	 * 版本: V1.0
	 */
	@Select({
		" SELECT",
		" DATE_FORMAT(t.count_date, '%Y-%m') countDate,",
		" t.count_code countCode,",
		" sum(t.count_value) countValue",
		" FROM",
		" flow_day_record t",
		" WHERE",
		" t.count_date >=#{localDateLeft} AND t.count_date <  #{localDateRight}",
		" GROUP BY",
		" DATE_FORMAT(t.count_date, '%Y-%m'),t.count_code",
		" ORDER BY t.count_date asc",
	})
	List<FlowDayRecordEntity> monthList(@Param("localDateLeft") String localDateLeft,@Param("localDateRight")  String localDateRight);

	/**
	 * 描述: 按日查询
	 * @author: songguang.jiao
	 * 创建时间:  2020年5月6日 下午3:48:57
	 * 版本: V1.0
	 */
	@Select({
		" SELECT",
		" DATE_FORMAT(t.count_date, '%Y-%m-%d') countDate,",
		" t.count_code countCode,",
		" sum(t.count_value) countValue",
		" FROM",
		" flow_day_record t",
		" WHERE",
		" t.count_date >=#{localDateLeft} AND t.count_date < #{localDateRight}",
		" GROUP BY",
		" DATE_FORMAT(t.count_date, '%Y-%m-%d'),t.count_code",
		" ORDER BY t.count_date asc",
	})
	List<FlowDayRecordEntity> dayList(@Param("localDateLeft") String localDateLeft,@Param("localDateRight")  String localDateRight);

}
