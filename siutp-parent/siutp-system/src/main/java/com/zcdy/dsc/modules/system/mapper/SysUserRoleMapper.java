package com.zcdy.dsc.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.system.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *@author : songguang.jiao
 
 * @since 2018-12-21
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

	/**
	 * 通过名称查找角色
	 * @param username 用户名
	 * @return
	 */
	@Select("select role_code from sys_role where id in (select role_id from sys_user_role where user_id = (select id from sys_user where username=#{username}))")
	List<String> getRoleByUserName(@Param("username") String username);

	/**
	 * 通过名称查找角色id
	 * @param username 用户名
	 * @return
	 */
	@Select("select id from sys_role where id in (select role_id from sys_user_role where user_id = (select id from sys_user where username=#{username}))")
	List<String> getRoleIdByUserName(@Param("username") String username);

}
