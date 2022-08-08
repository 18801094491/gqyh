package com.zcdy.dsc.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.system.entity.SysCategory;
import com.zcdy.dsc.modules.system.model.TreeSelectModel;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author : songguang.jiao
 * 描述: 分类字典
 * 版本号: V1.0
 */
public interface SysCategoryMapper extends BaseMapper<SysCategory> {

	/**
	 * 根据父级ID查询树节点数据
	 * @param pid 父id
	 * @param query
	 * @return
	 */
	public List<TreeSelectModel> queryListByPid(@Param("pid")  String pid,@Param("query") Map<String, String> query);

	/**
	 *
	 * @param code
	 * @return
	 */
	@Select("SELECT ID FROM sys_category WHERE CODE = #{code,jdbcType=VARCHAR}")
	public String queryIdByCode(@Param("code")  String code);
	
	/**
	 * 查询下拉选
	 * @param pid
	 * @return
	 */
	public List<SysCateDropdown> queryKeyValue(@Param("pid") String pid);

	/**
	 * 查询下拉选
	 * @param code
	 * @return
	 */
	public List<TreeSelectModel> queryKeyValueCode(@Param("code") String code);

	/**
	 * @author：Roberto
	 * @create:2020年1月16日 下午12:47:09
	 * 描述:<p></p>
	 */
	@Select("SELECT t.code,t.name as title from sys_category t where t.pid = (SELECT id from sys_category where code = #{value})")
	public List<SysCateDropdown> queryKeyValueByCode(String key);
	
}
