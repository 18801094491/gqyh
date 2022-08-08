package com.zcdy.dsc.modules.datacenter.statistic.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.datacenter.statistic.entity.ModuleVar;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentVariableVO;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;

/**
 * 描述: 统计模块变量关联表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-19
 * 版本号: V1.0
 */
public interface ModuleVarService extends IService<ModuleVar> {

	/**
	 * @author：Roberto
	 * @create:2020年3月19日 下午6:29:37
	 * 描述:<p></p>
	 */
	List<SysCateDropdown> queryModuleItem(String moduleId);

	/**
	 * @author：Roberto
	 * @create:2020年3月20日 下午12:19:32
	 * 描述:<p></p>
	 */
	List<EquipmentVariableVO> queryEquipAndVarByModuleId(String moduleId, String typeCode);

	/**
	 * @author：Roberto
	 * @create:2020年3月20日 下午5:53:30
	 * 描述:<p></p>
	 */
	List<EquipmentVariableVO> queryAllEquipAndVarByModuleId(String moduleId);

	/**
	 * @author：Roberto
	 * @create:2020年3月28日 下午5:10:09
	 * 描述:<p>获取统计功能配置的变量展示顺序</p>
	 */
	List<Map<String, Object>> getVarOrder(String moduleId);
}
