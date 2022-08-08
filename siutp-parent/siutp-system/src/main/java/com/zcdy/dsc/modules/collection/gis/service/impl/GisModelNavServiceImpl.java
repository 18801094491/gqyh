package com.zcdy.dsc.modules.collection.gis.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.collection.gis.entity.GisModelNav;
import com.zcdy.dsc.modules.collection.gis.mapper.GisModelNavMapper;
import com.zcdy.dsc.modules.collection.gis.service.GisModelNavService;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelNavVo;

/**
 * 描述: GIS模型类型对应导航配置
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-27
 * 版本号: V1.0
 */
@Service
public class GisModelNavServiceImpl extends ServiceImpl<GisModelNavMapper, GisModelNav> implements GisModelNavService {

	@Resource
	private GisModelNavMapper gisModelNavMapper;
	
	@Override
	public List<GisModelNavVo> queryList(String modelType) {
		return gisModelNavMapper.queryList(modelType);
	}

}
