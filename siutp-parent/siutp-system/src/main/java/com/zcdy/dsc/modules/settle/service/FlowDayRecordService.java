package com.zcdy.dsc.modules.settle.service;

import com.zcdy.dsc.modules.settle.entity.FlowDayRecord;
import com.zcdy.dsc.modules.settle.entity.FlowDayRecordEntity;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 每日用水量记录
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-30
 * @Version: V1.0
 */
public interface FlowDayRecordService extends IService<FlowDayRecord> {

	/**
	 * 描述: 按月统计
	 * @author: songguang.jiao
	 * 创建时间:  2020年5月6日 下午3:22:19
	 * 版本: V1.0
	 */
	List<FlowDayRecordEntity> monthList(String localDateLeft, String localDateRight);

	/**
	 * 描述: 按日查询
	 * @author: songguang.jiao
	 * 创建时间:  2020年5月6日 下午3:47:47
	 * 版本: V1.0
	 */
	List<FlowDayRecordEntity> dayList(String localDateLeft, String localDateRight);

}
