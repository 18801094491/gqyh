package com.zcdy.dsc.modules.datacenter.statistic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.datacenter.statistic.entity.ChartItem;
import com.zcdy.dsc.modules.datacenter.statistic.entity.ChartSerial;

import java.util.List;

/**
 * 描述: 统计要素
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-13
 * 版本号: V1.0
 */
public interface ChartItemService extends IService<ChartItem> {

	/**
	 * 无参查询列表
	 * @return
	 */
	List<ChartSerial> querySerialData();

	/**
	 * 通过id查询列表
	 * @param id
	 * @return
	 */
	List<ChartSerial> querySerialDataByChartId(String id);

}
