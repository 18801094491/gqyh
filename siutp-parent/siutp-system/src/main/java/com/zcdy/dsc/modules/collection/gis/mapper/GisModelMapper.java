package com.zcdy.dsc.modules.collection.gis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.collection.gis.entity.GisModel;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelImgVo;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelVO;
import com.zcdy.dsc.modules.collection.iot.entity.Iot2GisEntity;

/**
 * 描述: GIS模型
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-23
 * 版本号: V1.0
 */
public interface GisModelMapper extends BaseMapper<GisModel> {

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
			@Param("modelName")String modelName,@Param("modelTypeCode")String modelTypeCode);
	
	
	/**
	 * 查询gis详情
	 * @param id gisId
	 * @return
	 */
	 public GisModelVO getDetail(String id);

	 /**
	 * 描述:检验模型名称是否存在 
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月29日 下午1:56:51 
	 * 版本号: V1.0
	 */
	@Select({
		" <script>",
		" SELECT count(t.id) from gis_model t where t.model_name=#{modelName} ",
		" <if test='id!=null and id!=\"\"'>",
		"  and t.id!=#{id}",
		" </if>",
		" </script>",
	})
	public Boolean checkNameExist(@Param("id")String id,@Param("modelName") String modelName);

	/**
	 * 描述:检验模型是否绑定变量
	 * @author：  zengli
	 * 创建时间： 2020年8月29日 下午1:56:51
	 * 版本号: V1.0
	 */
	@Select({
			" <script>",
			"select count(1)  FROM model_variable t1",
			"where t1.model_id=#{id}",
			" </script>",
})
	public Boolean checkModelDetailExist(@Param("id")String id);

	/**
	 * @author：Roberto
	 * @create:2020年3月10日 下午1:28:57
	 * 描述:<p></p>
	 */
	@Select({
		"SELECT t.id gisId,d.id iotId",
		"FROM gis_model t",
		"LEFT JOIN gis_equipment b ON b.model_id = t.id",
		"LEFT JOIN iot_opt_equipment c ON b.equipment_id = c.opt_id",
		"LEFT JOIN iot_equipment d ON d.id = c.iot_id",
		"WHERE d.id IS NOT NULL AND t.del_flag = 0",
	})
	public List<Iot2GisEntity> selectIot2Gis();

	/**
	 * 微信小程序地图显示数据，压力和流量计
	 * @return
	 */
	public List<GisModelImgVo> queryListByIn();

	/**
	 * 根据模型gis_model获取iot_id
	 * @return
	 */
	@Select({
			"SELECT distinct d.id iotId",
			"FROM gis_model t",
			"LEFT JOIN gis_equipment b ON b.model_id = t.id",
			"LEFT JOIN iot_opt_equipment c ON b.equipment_id = c.opt_id",
			"LEFT JOIN iot_equipment d ON d.id = c.iot_id",
			"WHERE t.id=#{id} and d.id IS NOT NULL",
	})
	public String getIotByGisModel(@Param("id")String id);
}
