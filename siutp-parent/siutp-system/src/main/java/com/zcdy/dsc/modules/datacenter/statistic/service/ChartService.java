package com.zcdy.dsc.modules.datacenter.statistic.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.datacenter.statistic.entity.Chart;
import com.zcdy.dsc.modules.datacenter.statistic.param.ChartParam;
import com.zcdy.dsc.modules.datacenter.statistic.vo.ChartVo;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentInfoVO;
import com.zcdy.dsc.modules.datacenter.statistic.vo.MeterFlowBarChart;
import com.zcdy.dsc.modules.datacenter.statistic.vo.MeterFlowHistoryVo;
import com.zcdy.dsc.modules.datacenter.statistic.vo.VarNameTitleVO;

/**
 * 描述: 统计事项
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-13
 * 版本号: V1.0
 */
public interface ChartService extends IService<Chart> {

	/**
	 * @author：Roberto
	 * @param page 
	 * @create:2020年3月14日 上午10:49:04
	 * 描述:<p>查询分页列表数据</p>
	 */
	IPage<ChartVo> pageQuery(Page<ChartVo> page, ChartParam param);

	/**
	 * @author：Roberto
	 * @create:2020年3月15日 上午10:42:12
	 * 描述:<p>获取所有的资产信息</p>
	 */
	List<EquipmentInfoVO> queryEquipment(String name);

	/**
	 * @author：Roberto
	 * @create:2020年3月15日 上午11:18:22
	 * 描述:<p>根据设备id查询 对应采集设备的采集变量</p>
	 */
	List<VarNameTitleVO> queryVarByOptId(String id);

	/**
	 * 描述: 历史用水量产销差统计
	 * @author：  songguang.jiao
	 * 创建时间：  2020年3月23日 下午5:51:27
	 * 版本号: V1.0
	 */
	MeterFlowBarChart  queryData(MeterFlowHistoryVo meterFlowHistoryVo);
	
	

}
