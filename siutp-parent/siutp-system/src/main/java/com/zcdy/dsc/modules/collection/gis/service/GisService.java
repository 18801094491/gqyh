package com.zcdy.dsc.modules.collection.gis.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.modules.collection.gis.entity.GisModelNav;
import com.zcdy.dsc.modules.collection.gis.vo.GisImageVo;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelResParamVo;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelResVo;
import com.zcdy.dsc.modules.collection.gis.vo.ResSnVo;

/**
 * 描述: GIS自定义接口
 * @author：  songguang.jiao
 * 创建时间： 2020年2月26日 上午10:12:52 
 * 版本号: V1.0
 */
public interface GisService {

	/**
	 * 描述: 修改GIS模型库
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月27日 上午9:22:00 
	 * 版本号: V1.0
	 */
	Result<Object> edit(GisModelResParamVo paramVo);

	/**
	 * 描述: 保存GIS模型库
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月27日 上午9:22:30 
	 * 版本号: V1.0
	 */
	Result<Object> save(GisModelResParamVo paramVo);

	/**
	 * 描述: 分页查询模型库
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月27日 上午10:25:00 
	 * 版本号: V1.0
	 */
	IPage<GisModelResVo> queryGisResPageData(Page<GisModelResVo> page, String modelTypeCode);

	/**
	 * 描述: 查询模型库列表
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月3日 上午10:57:10 
	 * 版本号: V1.0
	 */
	List<ResSnVo> queryResSn(String modelType);

	/**
	 * 描述:更新模型库 
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月3日 上午11:39:27 
	 * 版本号: V1.0
	 */
	void editImage(GisImageVo gisImageVo);

	/**
	 * 描述: 查询所有导航图标
	 * @author：  songguang.jiao
	 * 创建时间：  2020年3月27日 上午10:23:25
	 * 版本号: V1.0
	 */
	List<GisModelNav> queryAllGisNav();
}
