package com.zcdy.dsc.modules.collection.gis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.collection.gis.entity.GisModel;
import com.zcdy.dsc.modules.collection.gis.entity.GisModelNav;
import com.zcdy.dsc.modules.collection.gis.entity.GisModelResItem;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelResImgVo;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelResVo;
import com.zcdy.dsc.modules.collection.gis.vo.ResSnVo;

/**
 * 描述: GIS相关 dao接口
 * @author：  songguang.jiao
 * 创建时间： 2020年2月26日 上午10:14:33 
 * 版本号: V1.0
 */
public interface GisDao {


	/**
	 * 描述: 批量插入图片
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月27日 上午9:46:46 
	 * 版本号: V1.0
	 */
	@Insert({
		" <script>",
		" INSERT INTO `gis_model_res_item` ( `id`, `res_id`, `img_url`, `image_type`, `width`, `height`, `create_by`, `create_time`,  `del_flag` )",
		" VALUES",
		" <foreach collection='list' item='item' index='index' separator=','>",
		" (",
		" #{item.id},",
		" #{item.resId},",
		" #{item.imgUrl},",
		" #{item.imageType},",
		" #{item.width},",
		" #{item.height},",
		" #{item.createBy},",
		" #{item.createTime},",
		" #{item.delFlag}",
		" )",
		" </foreach>",
		" </script>",
})
	public void insertGisModelItem(@Param("list") List<GisModelResItem> list);

	/**
	 * 描述: 删除原有图片地址
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月27日 上午10:08:57 
	 * 版本号: V1.0
	 */
	@Delete(" delete from  gis_model_res_item where res_id=#{id}")
	void deleteGisModelItem(@Param("id") String id);

	/**
	 * 描述: 分页查询res模型库
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月27日 上午10:27:23 
	 * 版本号: V1.0
	 */
	@Results(id="selectResItem",
			value={
					@Result(property="id",column="id"),
					@Result(property="list",javaType=List.class,column="id",many=@Many(select="queryResItem"))
			})
	@Select({
		" <script>",
		" SELECT",
		" t.id,",
		" t.res_sn resSn,",
		" t.legend_show legendShow,",
		" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'is_show' ) AND item_value = t.legend_show ) legendShowName,",
		" t.model_type_code modelTypeCode,",
		" (SELECT name from sys_category where code=t.model_type_code) modelTypeName",
		" FROM",
		" gis_model_res t where t.del_flag='0'",
		" <if test='modelTypeCode!=null and modelTypeCode!=\"\"'>",
		" and t.model_type_code = #{modelTypeCode}",
		" </if>",
		" </script>"
	})
	IPage<GisModelResVo> queryGisResData(Page<GisModelResVo> page,@Param("modelTypeCode") String modelTypeCode);

	/**
	 * 描述: 查询GIS模型库图片地址信息
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月27日 上午10:38:20 
	 * 版本号: V1.0
	 */
	@Select({
		" SELECT",
		" t.img_url imgUrl,",
		" t.image_type imageType,",
		" t.width width,",
		" t.height height ",
		" FROM",
		" gis_model_res_item t",
		" where ",
		" t.res_id=#{resId}",
	})
	List<GisModelResImgVo> queryResItem(@Param("resId") String resId);

	/**
	 * 描述: 查询模型库编号
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月3日 上午10:57:59 
	 * 版本号: V1.0
	 */
	@Select({
		"<script>",
		" SELECT id,res_sn resSn from gis_model_res t where 1=1",
		" <if test='modelType!=null and modelType!=\"\"'>",
		" and t.model_type_code=#{modelType}",
		" </if>",
		"</script>"
	})
	List<ResSnVo> queryResSn(@Param("modelType") String modelType);

	/**
	 * 描述: 通过ids批量更新状态
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月3日 下午12:28:14 
	 * 版本号: V1.0
	 */
	@Update({
		" <script>",
		" <foreach collection='list' item='item' index='index' separator=';'>",
		" update gis_model",
		" <set>",
		" <if test='item.modelImg!=null'>",
		" model_img=#{item.modelImg},",
		" </if>",
		" <if test='item.modelWaringImg!=null'>",
		" model_waring_img=#{item.modelWaringImg},",
		" </if>",
		" <if test='item.modelOnImg!=null'>",
		" model_on_img=#{item.modelOnImg},",
		" </if>",
		" <if test='item.modelOffImg!=null'>",
		" model_off_img=#{item.modelOffImg},",
		" </if>",
		" <if test='item.height!=null'>",
		" height=#{item.height},",
		" </if>",
		" <if test='item.width!=null'>",
		" width=#{item.width}",
		" </if>",
		" </set>",
		" where id=#{item.id}",
		" </foreach>",
		" </script>",
	})
	void updateGisModelList(@Param("list") List<GisModel> list);

	/**
	 * 描述: 批量更新同一类图片
	 * @author：  songguang.jiao
	 * 创建时间： 2020年3月3日 下午12:43:38 
	 * 版本号: V1.0
	 */
	@Update({
		" UPDATE `gis_model` ",
		" SET  height = #{model.height}, ",	
		" model_on_img = #{model.modelOnImg},",
		" `model_off_img` = #{model.modelOffImg},",
		" `model_waring_img` = #{model.modelWaringImg},",
		" model_img = #{model.modelImg},",
		"  width = #{model.width}",
		" WHERE",
		" model_type_code = (SELECT model_type_code from gis_model_res where id=#{resId})",
	})
	void updateGisModelOne(@Param("model") GisModel model,@Param("resId") String resId);

	/**
	 * 描述: 查询所有导航图片
	 * @author：  songguang.jiao
	 * 创建时间：  2020年3月27日 上午10:24:49
	 * 版本号: V1.0
	 */
	@Select({
		" SELECT",
		" t.data_show dataShow,",
		" t.nav_name navName,",
		" t.model_thumb modelThumb",
		" FROM",
		" gis_model_nav t",
	})
	public List<GisModelNav> queryAllGisNav();
}
