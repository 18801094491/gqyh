package com.zcdy.dsc.modules.collection.iot.service;

import java.util.List;

import com.zcdy.dsc.modules.collection.iot.entity.IotEquipRuleEntity;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipconfigEntity;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipmentRuleItemEntity;

/**
 * @author： Roberto
 * 创建时间：2020年3月5日 下午7:34:24
 * 描述: <p>使用告警规则清洗数据Service</p>
 */
public interface IoserverAlarmRuleService {

	/**
	 * @author：Roberto
	 * @create:2020年3月6日 下午3:43:23
	 * 描述:<p>获取所有采集设备的数据规则</p>
	 */
	List<IotEquipRuleEntity> getAllEquipmentRules();

	/**
	 * @author：Roberto
	 * @create:2020年3月6日 下午3:59:45
	 * 描述:<p>获取所有的规则规则</p>
	 */
	List<IotEquipmentRuleItemEntity> getAllRuleItems();

	/**
	 * @author：Roberto
	 * @create:2020年4月1日 下午5:26:15
	 * 描述:<p></p>
	 */
	List<IotEquipconfigEntity> getIotQualitys();

    /**
     * 仅适用于7标2标压力
     * @param equipmentId
     * @return 规则实体
     */
    IotEquipRuleEntity findRuleEntity(String equipmentId);

}
