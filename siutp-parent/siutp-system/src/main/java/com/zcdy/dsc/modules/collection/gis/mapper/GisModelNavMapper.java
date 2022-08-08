package com.zcdy.dsc.modules.collection.gis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.collection.gis.entity.GisModelNav;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelNavVo;

/**
 * 描述: GIS模型类型对应导航配置
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-27
 * 版本号: V1.0
 */
public interface GisModelNavMapper extends BaseMapper<GisModelNav> {

	/**
	 * 描述:gis模型导航列表查询
	 * @author：  songguang.jiao
	 * 创建时间：  2020年4月7日 上午10:59:24
	 * 版本号: V1.0
	 */
	@Select({
		" <script>",
		" SELECT",
		" t.id,",
		" t.model_type modelType,",
		" t1.name modelTypeName,",
		" t.nav_name navName,",
		" t.model_thumb modelThumb,",
		" t.data_show dataShow,",
		" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'is_show' ) AND item_value = t.data_show ) dataShowName,",
		" t.nav_status navStatus,",
		" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = t.nav_status ) navStatusName",
		" FROM",
		" gis_model_nav t",
		" LEFT JOIN sys_category t1 on t1.code=t.model_type",
		" where 1=1",
		" <if test='modelType!=null and modelType!=\"\"'>",
		" and t.model_type=#{modelType}",
		" </if>",
		" </script>",
	})
	List<GisModelNavVo> queryList(@Param("modelType") String modelType);

}
