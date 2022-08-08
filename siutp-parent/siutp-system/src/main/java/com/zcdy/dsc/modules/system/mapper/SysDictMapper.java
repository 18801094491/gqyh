package com.zcdy.dsc.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.common.system.vo.DictModel;
import com.zcdy.dsc.modules.system.entity.SysDict;
import com.zcdy.dsc.modules.system.model.DuplicateCheckVo;
import com.zcdy.dsc.modules.system.model.TreeSelectModel;
import com.zcdy.dsc.modules.system.vo.DictDropdown;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *@author : songguang.jiao
 
 * @since 2018-12-28
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

	/**
	 * 校验数据是否在系统中是否存在
	 * @param duplicateCheckVo
	 * @return
	 */
	public Long duplicateCheckCountSql(DuplicateCheckVo duplicateCheckVo);

	/**
	 * 添加页面校验
	 * @param duplicateCheckVo
	 * @return
	 */
	public Long duplicateCheckCountSqlNoDataId(DuplicateCheckVo duplicateCheckVo);

	/**
	 * 通过code查询字节点
	 * @param code
	 * @return
	 */
	public List<DictModel> queryDictItemsByCode(@Param("code") String code);

	/**
	 * 通过查询指定table的 text code 获取字典
	 * @param table 表
	 * @param text 说明
	 * @param code
	 * @return
	 */
	public List<DictModel> queryTableDictItemsByCode(@Param("table") String table,@Param("text") String text,@Param("code") String code);
	public List<DictModel> queryTableDictItemsByCodeAndFilter(@Param("table") String table,@Param("text") String text,@Param("code") String code,@Param("filterSql") String filterSql);


	public String queryDictTextByKey(@Param("code") String code,@Param("key") String key);

	public String queryTableDictTextByKey(@Param("table") String table,@Param("text") String text,@Param("code") String code,@Param("key") String key);

	public List<DictModel> queryTableDictByKeys(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("keyArray") String[] keyArray);

	/**
	 * 查询所有部门 作为字典信息 id -->value,departName -->text
	 * @return
	 */
	public List<DictModel> queryAllDepartBackDictModel();
	
	/**
	 * 查询所有用户  作为字典信息 username -->value,realname -->text
	 * @return
	 */
	public List<DictModel> queryAllUserBackDictModel();
	
	/**
	 * 通过关键字查询出字典表
	 * @param table
	 * @param text
	 * @param code
	 * @param keyword
	 * @return
	 */
	public List<DictModel> queryTableDictItems(@Param("table") String table,@Param("text") String text,@Param("code") String code,@Param("keyword") String keyword); 

	/**
	  * 根据表名、显示字段名、存储字段名 查询树
	 * @param table
	 * @param text
	 * @param code
	 * @param pid
	 * @param hasChildField
	 * @return
	 */
	List<TreeSelectModel> queryTreeList(@Param("query") Map<String, String> query,@Param("table") String table,@Param("text") String text,@Param("code") String code,@Param("pidField") String pidField,@Param("pid") String pid,@Param("hasChildField") String hasChildField);

	/**
	 * 删除
	 * @param id
	 */
	@Select("delete from sys_dict where id = #{id}")
	public void deleteOneById(@Param("id") String id);

	/**
	 * 查询被逻辑删除的数据
	 * @return
	 */
	@Select("select * from sys_dict where del_flag = 1")
	public List<SysDict> queryDeleteList();

	/**
	 * 修改状态值
	 * @param delFlag
	 * @param id
	 */
	@Update("update sys_dict set del_flag = #{flag,jdbcType=INTEGER} where id = #{id,jdbcType=VARCHAR}")
	public void updateDictDelFlag(@Param("flag") int delFlag, @Param("id") String id);
	
	/**
	 * 描述: 查询基础数据字典key-value
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月16日 下午7:05:11
	 * 版本号: V1.0
	 */
	@Select({
		" SELECT	s.item_value code,	s.item_text title FROM	sys_dict_item s",
		" WHERE	dict_id = (	select id from sys_dict where dict_code = #{code})"
	})
	public List<DictDropdown> getDictValue(String code);

	/**
	 * app根据code查询基础数据字典key-value
	 * @param code 字典编号
	 * @return
	 */
	@Select({
			" SELECT	s.item_value code,	s.item_text title FROM	sys_dict_item s",
			" WHERE	dict_id = (	select id from sys_dict where dict_code = #{code})"
	})
	public List<DictDropdown> getAppDictValue(String code);

}
