package com.zcdy.dsc.modules.collection.iot.service;

import java.util.List;

import com.zcdy.dsc.modules.collection.iot.entity.IOTVarData;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentInfoVO;

/**
 * @author： Roberto
 * 创建时间：2020年3月18日 下午3:31:44
 * 描述: <p></p>
 */
public interface IotDataService {

	List<VariableInfo> queryVariableInfoByEquipmentId(String equipmentId);

	/**
	 * @author：Roberto
	 * @create:2020年3月18日 下午5:30:34
	 * 描述:<p></p>
	 */
	List<IOTVarData> queryIotHisData(String string);

	/**
	 * @author：Roberto
	 * @create:2020年3月18日 下午7:33:26
	 * 描述:<p></p>
	 */
	List<EquipmentInfoVO> queryEquipment(String typeCode, String name, String monthBalance);

	/**
	 * 创建人:Roberto
	 * 创建时间:2020年4月29日 上午3:18:53
	 * 描述:<p></p>
	 */
	EquipmentInfoVO queryEquipmentById(String id);
}
