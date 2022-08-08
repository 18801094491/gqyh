package com.zcdy.dsc.modules.operation.equipment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.datacenter.statistic.vo.MeterFlowMonthPriceVo;
import com.zcdy.dsc.modules.operation.equipment.entity.MeterFlow;
import com.zcdy.dsc.modules.operation.equipment.vo.MeterFlowVo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 描述: 水表日累计量信
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-19
 * 版本号: V1.0
 */
public interface MeterFlowService extends IService<MeterFlow> {

    MeterFlow getMeterFlowByDateAndEquipmentId(String equipmentId);

    void deleteMeterFlowByDateAndEquipmentId(String equipmentId);

    List<MeterFlow> querynetFlowDay(String daycount,String staticsDateEnd);

    List<MeterFlowVo> queryAllNetFlowDay();


	/**
	 * 查询所有客户水表往月抄表单价
	 * 2020-4-26 09:58:00
	 * @author tangchao
	 * @param page 分页对象
	 * @param customerId
	 * @return 水表往月抄表单价
	 */
	IPage<MeterFlowMonthPriceVo> queryAllWaterMonth(Page<MeterFlowMonthPriceVo> page, String customerId);
	/**
	 * 查询客户水表历史所有时间抄表水价
	 * 2020-4-26 09:58:00
	 * @author tangchao
	 * @param customerId 客户id, 允许为空, 为空查询全部客户用水量(总用水量)
	 * @param startDate 查询开始时间 如果为空 默认30天之内
	 * @param endDate 查询结束时间 如果为空 默认30天之内
	 * @return 水表往月抄表单价
	 */
	List<Map<String, Number>> queryAllDateWaterFlow(String customerId, String startDate, String endDate);

	
	/**
	 * 描述: 通过id集合跟时间查询累计量
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月28日 下午2:50:38
	 * 版本: V1.0
	 */
	String queryMeterRsLh(List<String> list, LocalDate localDate);

	/**
	 * 创建人:Roberto
	 * 创建时间:2020年4月30日 下午4:54:19
	 * 描述:<p>删除指定日期的水表统计数据</p>
	 */
	void deleteMeterByDateAndEquipmentId(String equipmentId, String yestodyStr);

	/**
	 * 创建人:Roberto
	 * 创建时间:2020年4月30日 下午4:58:27
	 * 描述:<p>执行查询指定日期的水表数据</p>
	 */
	MeterFlow getMeterByDateAndEquipmentId(String equipmentId, String prevYestoday);

	/**
	 * 描述: 查询昨日统计模块数据
	 * @author: songguang.jiao
	 * 创建时间:  2020年5月7日 上午11:01:26
	 * 版本: V1.0
	 */
	Map<String, MeterFlow> queryByDate(String string);

}
