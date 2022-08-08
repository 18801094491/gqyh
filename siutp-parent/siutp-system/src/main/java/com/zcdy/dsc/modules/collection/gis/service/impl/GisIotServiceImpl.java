package com.zcdy.dsc.modules.collection.gis.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.collection.gis.mapper.GisIotDao;
import com.zcdy.dsc.modules.collection.gis.service.IGisIotService;
import com.zcdy.dsc.modules.collection.gis.vo.IotVariableInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 
 * 描述: 模型跟变量相关接口
 * @author：  songguang.jiao
 * 创建时间：  2020年1月6日 上午9:55:44
 * 版本号: V1.0
 */
@Service
public class GisIotServiceImpl implements IGisIotService {

	@Resource
	private GisIotDao gisIotDao;
	
	
	@Override
	public Integer bindStatus(String variableId) {
		return gisIotDao.bindStatus(variableId);
	}


	@Override
	public IPage<IotVariableInfoVo> getModelBindList(IPage<IotVariableInfoVo> page, String modelId) {
		return gisIotDao.getModelBindList(page, modelId);
	}


	@Override
	public void unBindIotVar(String variableId) {
		gisIotDao.unBindIotVar(variableId);
	}


	@Override
	public IPage<IotVariableInfoVo> getIotInfoList(IPage<IotVariableInfoVo> page, String variableName,
			String variableTitle) {
		return gisIotDao.getIotInfoList(page, variableName, variableTitle);
	}
	
	
}
