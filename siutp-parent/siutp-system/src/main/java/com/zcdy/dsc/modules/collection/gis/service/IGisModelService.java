package com.zcdy.dsc.modules.collection.gis.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.collection.gis.entity.GisModel;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelImgVo;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelVO;
import org.apache.ibatis.annotations.Param;

/**
 * 描述: GIS模型
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-23
 * 版本号: V1.0
 */
public interface IGisModelService extends IService<GisModel> {

	/**
	 * 加载gis地图所有模型
	 * @return
	 * 
	 */
	public List<GisModelImgVo> queryList();
	
	/**
	 * gis模型分页查询
	 * @param page
	 * @param modelName
	 * @param modelType
	 * @return
	 */
	public IPage<GisModelVO> queryPageList(IPage<GisModelVO> page,
			String modelName,String modelTypeCode);
	
	
	/**
	 * 查询gis详情
	 * @param id id
	 * @return
	 */
	 public GisModelVO getDetail(String id);

	 /**
	  * 描述:检验模型名是否重复 
	  * @author：  songguang.jiao
	  * 创建时间： 2020年2月29日 下午2:58:01 
	  * 版本号: V1.0
	  */
	public Boolean checkNameExist(String id, String modelName);

    String getStatus(String id);

	/**
	 * 微信地图加载数据流量计和压力
	 * @return
	 */
	public List<GisModelImgVo> queryListByIn();

	/**
	 * 根据gis_model id获取iot_id
	 * @param id
	 * @return
	 */
	public String getIotByGisModel(String id);

	/**
	 * 根据id
	 * @param id
	 * @return
	 */
	public Boolean checkModelDetailExist(String id);
}
