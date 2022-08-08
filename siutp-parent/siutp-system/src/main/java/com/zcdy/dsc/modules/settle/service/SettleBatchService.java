package com.zcdy.dsc.modules.settle.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.settle.entity.SettleBatch;
import com.zcdy.dsc.modules.settle.param.CustometInfoParam;
import com.zcdy.dsc.modules.settle.vo.CustomerInfoData.CustomerCount;
import com.zcdy.dsc.modules.settle.vo.CustomerInfoData.CustomerMonthInfo;
import com.zcdy.dsc.modules.settle.vo.SettleBatchOptEquipInfoVo;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsVo;

/**
 * @Description: 结算批次
 * @author tangchao
 * @date 2020-5-8 14:42:34
 * @version V1.0
 */
public interface SettleBatchService extends IService<SettleBatch> {

	/**
	 * 获取设备某个时刻的信息
	 * 
	 * @param optEquipmentId
	 *            设备id
	 * @param date
	 *            时间
	 * @return 设备信息
	 */
	public SettleBatchOptEquipInfoVo getOptEquipInfo(String optEquipmentId,
			String date);

	/**
	 * 调度器运行接口
	 * 
	 * @param date
	 *            时间
	 */
	void executeAll(String date);

	/**
	 * 查找水表最后的一条批次数据
	 *
	 * @param equipmentId
	 *            设备id
	 * @return null 参数不正确 如果有上一条, 返回上一次批次 如果没有, 说明为第一条数据, 返回初始化空数据
	 */
	SettleBatch getPreviousSettleBatch(String equipmentId, String time);

	/**
	 * 查找水表最后的一条批次数据
	 * 
	 * @param equipmentId
	 *            设备id
	 * @return null 参数不正确 如果有上一条, 返回上一次批次 如果没有, 说明为第一条数据, 返回初始化空数据
	 */
	SettleBatch getPreviousSettleBatch(String equipmentId);

	/**
	 * 生成设备抄表记录
	 * 
	 * @param optId
	 *            设备id
	 * @param status
	 *            状态
	 * @return 批次数据
	 * @author tangchao
	 * @see com.zcdy.dsc.modules.settle.constant.SettleConstant status
	 * @since 2020-5-8 10:47:02
	 */
	public void executeEquipmentNow(String optId, String status);

	/**
	 * 查询全部结算单
	 * 
	 * @param param
	 * @return
	 */
	public List<CustomerMonthInfo> queryMonthInfo(
			CustometInfoParam param);

	/**
	 * 客户信息统计计算
	 * 
	 * @param param
	 * @return
	 */
	public List<CustomerCount> customerCount(CustometInfoParam param);

	/**
	 * 客户信息历史查询
	 * @param page
	 * @param param
	 * @return
	 */
    public IPage<SettlementStatisticsVo> queryHistory(Page<SettlementStatisticsVo> page,CustometInfoParam param);

    /**
     * 导出历史信息
     * @param param
     * @return
     */
    public List<SettlementStatisticsVo> exportHistory(CustometInfoParam param);

}
