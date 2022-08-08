package com.zcdy.dsc.modules.collection.iot.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.zcdy.dsc.modules.collection.iot.entity.IotEquipRuleEntity;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipconfigEntity;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipmentRuleItemEntity;
import com.zcdy.dsc.modules.collection.iot.mapper.IoserverAlarmRuleDao;
import com.zcdy.dsc.modules.collection.iot.service.IoserverAlarmRuleService;

/**
 * @author： Roberto
 * 创建时间：2020年3月5日 下午7:35:15
 * 描述: <p>使用告警规则清洗数据Service</p>
 */
@Service("ioserverAlarmRuleService")
public class IoserverAlarmRuleServiceImpl implements IoserverAlarmRuleService{

	@Resource
	private IoserverAlarmRuleDao ioserverAlarmRuleDao;
	
	/*
	 * @see com.zcdy.dsc.modules.ioserver.service.IoserverAlarmRuleService#getAllEquipmentRules()
	 */
	@Cacheable(cacheNames="com:zcdy:dsc:iot:rule", key="#root.methodName")
	@Override
	public List<IotEquipRuleEntity> getAllEquipmentRules() {
		return ioserverAlarmRuleDao.selectAllEquipmentRules();
	}

	/*
	 * @see com.zcdy.dsc.modules.ioserver.service.IoserverAlarmRuleService#getAllRuleItems()
	 */
	@Cacheable(cacheNames="com:zcdy:dsc:iot:rule", key="#root.methodName")
	@Override
	public List<IotEquipmentRuleItemEntity> getAllRuleItems() {
		return ioserverAlarmRuleDao.selectAllRuleItems();
	}

	/*
	 * @see com.zcdy.dsc.modules.collection.iot.service.IoserverAlarmRuleService#getIotQualitys()
	 */
	@Cacheable(cacheNames="com:zcdy:dsc:iot:rule:config", key="#root.methodName")
	@Override
	public List<IotEquipconfigEntity> getIotQualitys() {
		return ioserverAlarmRuleDao.selectAllEquipmentConfig();
	}

    /* (non-Javadoc)
     * @see com.zcdy.dsc.modules.collection.iot.service.IoserverAlarmRuleService#findRuleEntity(java.lang.String)
     */
	@Cacheable(cacheNames="com:zcdy:dsc:iot:rule:config", key="#equipmentId")
    @Override
    public IotEquipRuleEntity findRuleEntity(String equipmentId) {
         return this.ioserverAlarmRuleDao.selectRuleEntity(equipmentId);
    }
}
