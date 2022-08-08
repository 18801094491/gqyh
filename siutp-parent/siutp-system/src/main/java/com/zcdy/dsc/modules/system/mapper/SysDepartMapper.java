package com.zcdy.dsc.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.system.entity.SysDepart;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * <p>
 * @author : songguang.jiao
 : Steve
 * @Since：   2019-01-22
 */
public interface SysDepartMapper extends BaseMapper<SysDepart> {
	/**
	 * 根据用户ID查询部门集合
	 * @param userId 用户id
	 * @return
	 */
	public List<SysDepart> queryUserDeparts(@Param("userId") String userId);

	/**
	 * 根据用户名查询部门
	 *
	 * @param username
	 * @return
	 */
	public List<SysDepart> queryDepartsByUsername(@Param("username") String username);

	/**
	 * 通过code查询id
	 * @param orgCode
	 * @return
	 */
	@Select("select id from sys_depart where org_code=#{orgCode}")
	public String queryDepartIdByOrgCode(@Param("orgCode") String orgCode);

	/**
	 * 通过id查询部门
	 * @param departId 部门id
	 * @return
	 */
	@Select("select id,parent_id from sys_depart where id=#{departId}")
	public SysDepart getParentDepartId(@Param("departId") String departId);
	
	/**
	 * 描述: 部门导出
	 * @author：  songguang.jiao
	 * 创建时间：  2020年3月30日 下午9:58:22
	 * 版本号: V1.0
	 */
	@Select("select " +
			"sd.depart_name  departName, " +
			"sd.depart_name_en departNameEn," +
			"sd.description description," +
			"case when sd.org_category='1' then '公司' else '部门' end  as orgCategory," +
			"case when sd.org_type='2' then '部门' when '3' then  '岗位' end  as orgType," +
			"sd.org_code orgCode," +
			"sd.mobile mobile," +
			"sd.fax fax," +
			"sd.address address," +
			"sd.memo  memo" +
			" from  sys_depart sd" +
			" where  sd.del_flag='0'")
    List<SysDepart> exportList();
}
