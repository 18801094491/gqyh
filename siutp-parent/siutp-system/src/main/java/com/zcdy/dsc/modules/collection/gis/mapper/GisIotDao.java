package com.zcdy.dsc.modules.collection.gis.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.collection.gis.vo.IotVariableInfoVo;

/**
 * 描述: GIsIot相关接口
 * @author：  songguang.jiao
 * 创建时间：  2020年1月6日 上午9:57:33
 * 版本号: V1.0
 */
public interface GisIotDao {


	@Select({
		"select count(*) from model_variable where variable_id=#{variableId} "
	})
	public Integer bindStatus(String variableId);
	
	
	
	/**
	 * 描述: Gis模型对应的已经绑定的变量
	 * @author：  songguang.jiao
	 * 创建时间：  2020年3月23日 下午12:28:25
	 * 版本号: V1.0
	 */
	@Select({
		" SELECT",
		" t2.display_order displayOrder,",
		" t.id,",
		" t.variable_name variableName,",
		" t.variable_title variableTitle",
		" FROM",
		" model_variable t2",
		" LEFT JOIN iot_variable_info t ON t.id = t2.variable_id",
		" WHERE",
		" t2.model_id = #{modelId}",
		" AND t.id IS NOT NULL",
		" ORDER BY t2.display_order desc",
			})
	public IPage<IotVariableInfoVo> getModelBindList(IPage<IotVariableInfoVo> page,@Param("modelId") String modelId);
	
	@Delete({
		"delete  from model_variable where variable_id=#{variableId} "
	})
	public void unBindIotVar(String variableId);
	
	
	@Select({"<script> "+
			" SELECT"
			+ " t.id,"
			+ " t.variable_name variableName,"
			+" (SELECT count(*) from model_variable m where m.variable_id=t.id) as bindStatus,"
			+ " t.variable_title variableTitle"
			+ " FROM"
			+ " iot_variable_info t "
			+ " where 1=1"
			+" <if test='variableName!=null and variableName!=\"\"' >"
			+" and t.variable_name like concat('%',#{variableName},'%')"
			+" </if>"
			+" <if test='variableTitle!=null and variableTitle!=\"\"' >"
			+" and t.variable_title like concat('%',#{variableTitle},'%')"
			+" </if>"
			+" order by t.create_time desc"
		+"</script>"
	})
	public IPage<IotVariableInfoVo> getIotInfoList(IPage<IotVariableInfoVo> page,@Param("variableName") String variableName,@Param("variableTitle") String variableTitle);
}
