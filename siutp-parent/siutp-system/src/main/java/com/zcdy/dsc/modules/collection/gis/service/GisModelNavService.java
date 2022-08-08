package com.zcdy.dsc.modules.collection.gis.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.collection.gis.entity.GisModelNav;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelNavVo;

/**
 * 描述: GIS模型类型对应导航配置
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-27
 * 版本号: V1.0
 */
public interface GisModelNavService extends IService<GisModelNav> {

	/**
	 * 描述: 查询列表
	 * @author：  songguang.jiao
	 * 创建时间：  2020年4月7日 上午10:56:34
	 * 版本号: V1.0
	 */
	List<GisModelNavVo> queryList(String modelType);

	
}
