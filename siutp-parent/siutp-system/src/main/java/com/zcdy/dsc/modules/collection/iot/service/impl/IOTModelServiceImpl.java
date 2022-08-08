package com.zcdy.dsc.modules.collection.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcdy.dsc.modules.collection.gis.entity.GisModel;
import com.zcdy.dsc.modules.collection.gis.mapper.GisModelMapper;
import com.zcdy.dsc.modules.collection.gis.vo.IotEquipInfoVo;
import com.zcdy.dsc.modules.collection.iot.entity.Iot2GisEntity;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.mapper.IOTModelMapper;
import com.zcdy.dsc.modules.collection.iot.service.IOTModelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author： Roberto
 * 创建时间：2019年12月25日 下午4:30:08
 * 描述: <p>GIS模型自定义业务逻辑层实现类</p>
 */
@Service
public class IOTModelServiceImpl implements IOTModelService {

	@Resource
	private IOTModelMapper iotModelMapper;
	
	@Resource
	private GisModelMapper gisModelMapper;

	@Override
	public Map<String, VariableInfo> getAllVarinfo() {
		List<VariableInfo> infos = this.iotModelMapper.selectAllVar();
		Map<String, VariableInfo> map = new HashMap<String, VariableInfo>(infos.size());
		infos.forEach(item -> {
			if (!StringUtils.isEmpty(item.getVarName())) {
				map.put(item.getVarName(), item);
			}
		});
		return map;
	}

	/**
	 * 根据模型id查询模型绑定的变量
	 */
	@Cacheable(cacheNames="com:zcdy:dsc:model:vars", key="#modelId")
	@Override
	public List<VariableInfo> getVarsByModelId(String modelId) {
		return this.iotModelMapper.selectVarsByModelId(modelId);
	}

	/*
	 * @see com.zcdy.dsc.modules.collection.iot.service.IOTModelService#getAllVarinfoData()
	 */
	@Cacheable(cacheNames="com:zcdy:dsc:vars", key="#root.methodName")
	@Override
	public List<VariableInfo> getAllVarinfoData() {
		return this.iotModelMapper.selectAllVar();
	}

	/*
	 * @see com.zcdy.dsc.modules.collection.iot.service.IOTModelService#getAllGISModel()
	 */
	@Cacheable(cacheNames="com:zcdy:dsc:models", key="#root.methodName")
	@Override
	public List<GisModel> getAllGisModel() {
		QueryWrapper<GisModel> query = new QueryWrapper<GisModel>();
		return this.gisModelMapper.selectList(query);
	}

	/*
	 * @see com.zcdy.dsc.modules.collection.iot.service.IOTModelService#getIot2Gis()
	 */
	@Cacheable(cacheNames="com:zcdy:dsc:iot2gis", key="#root.methodName")
	@Override
	public List<Iot2GisEntity> getIot2Gis() {
		return gisModelMapper.selectIot2Gis();
	}

	/*
	 * @see com.zcdy.dsc.modules.collection.iot.service.IOTModelService#getOneVarNameRandom(java.lang.String)
	 */
	@Cacheable(cacheNames="com:zcdy:dsc:iot:onevar", key="#equipmentId",unless="#result==null")
	@Override
	public String getOneVarNameRandom(String equipmentId) {
		return this.iotModelMapper.selectOneVarNameRandom(equipmentId);
	}

    @Override
    public List<IotEquipInfoVo> getEquipInfoByModelType(String typeCode) {
         return iotModelMapper.selectEquipInfoByModelType(typeCode);
    }
}
