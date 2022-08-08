package com.zcdy.dsc.modules.collection.gis.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.modules.collection.gis.entity.GisModel;
import com.zcdy.dsc.modules.collection.gis.mapper.GisModelMapper;
import com.zcdy.dsc.modules.collection.gis.service.IGisModelService;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelImgVo;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelVO;
import com.zcdy.dsc.modules.collection.iot.entity.ModelStatusEntity;

/**
 * 描述: GIS模型
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-23
 * 版本号: V1.0
 */
@Service
public class GisModelServiceImpl extends ServiceImpl<GisModelMapper, GisModel> implements IGisModelService {

	@Autowired
	private GisModelMapper gisModelMapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public List<GisModelImgVo> queryList() {
		return gisModelMapper.queryList();
	}

	@Override
	public GisModelVO getDetail(String id) {
		return gisModelMapper.getDetail(id);
	}

	@Override
	public IPage<GisModelVO> queryPageList(IPage<GisModelVO> page, String modelName, String modelTypeCode) {
		return gisModelMapper.queryPageList(page, modelName, modelTypeCode);
	}

	@Override
	public Boolean checkNameExist(String id, String modelName) {
		return gisModelMapper.checkNameExist(id,modelName);
	}

	@Override
	public Boolean checkModelDetailExist(String id) {

			return gisModelMapper.checkModelDetailExist(id);
	}
	
	@Override
	public String getStatus(String id) {
		String status="0";
		String statusConstant = stringRedisTemplate.opsForValue().get(String.format(RedisKeyConstantV2.MODEL_LAST_STATUS_KEY, id));
		if(statusConstant!=null){
			ModelStatusEntity statusEntity = JSON.parseObject(statusConstant, ModelStatusEntity.class);
			status=statusEntity.getStatus();
		}

		if("warn".equals(status)){
			status="1";
		}else {
			status="0";
		}


		return status;
	}

	@Override
	public List<GisModelImgVo> queryListByIn() {
		return gisModelMapper.queryListByIn();
	}

	@Override
	public String getIotByGisModel(String id){
		return gisModelMapper.getIotByGisModel(id);
	}
}
