package com.zcdy.dsc.modules.operation.equipment.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zcdy.dsc.modules.collection.gis.vo.EquipmentAttrVO;
import com.zcdy.dsc.modules.operation.equipment.entity.OptEquipmentAttr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 描述: 资产设备属性信息
 * @author： 智能无人硬核心项目组
 * 创建时间： 2019-12-20
 * 版本号: V1.0
 */
public interface OptEquipmentAttrMapper extends BaseMapper<OptEquipmentAttr> {

	/**
	 * 查询设备属性列表
	 * @param equipmentId
	 * @return
	 */
	List<EquipmentAttrVO> queryAttrList(@Param("equipmentId") String equipmentId);
}
