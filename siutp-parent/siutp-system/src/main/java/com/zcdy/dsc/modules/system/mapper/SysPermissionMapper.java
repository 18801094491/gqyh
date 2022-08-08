package com.zcdy.dsc.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.system.entity.SysPermission;
import com.zcdy.dsc.modules.system.model.TreeModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *@author : songguang.jiao
 
 * @since 2018-12-21
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
	/**
	   * 通过父菜单ID查询子菜单
	 * @param parentId
	 * @return
	 */
	public List<TreeModel> queryListByParentId(@Param("parentId") String parentId);

	/**
	 *  根据用户查询用户权限
	 * @param username 用户名称
	 * @return
	 */
	public List<SysPermission> queryByUser(@Param("username") String username);

	/**
	 * 修改菜单状态字段： 是否子节点
	 * @param id
	 * @param leaf 是否叶子节点
	 * @return
	 */
	@Update("update sys_permission set is_leaf=#{leaf} where id = #{id}")
	public int setMenuLeaf(@Param("id") String id,@Param("leaf") int leaf);

	/**
	 * 获取模糊匹配规则的数据权限URL
	 * @return
	 */
	@Select("SELECT url FROM sys_permission WHERE del_flag = 0 and menu_type = 2 and url like '%*%'")
    public List<String> queryPermissionUrlWithStar();


	@Select("select p.id, p.name\n" +
			"from sys_user u\n" +
				"left join sys_user_role ur on ur.user_id = u.id \n" +
				"left join sys_role r on r.id= ur.role_id \n" +
				"left join sys_role_permission rp on rp.role_id = r.id \n" +
				"left join sys_permission p on p.id = rp.permission_id and p.del_flag = " + StatusConstant.WORKING + "\n" +
			"where u.username = #{username} " +
				"and p.menu_type = 0 " +
				"and u.del_flag = " + StatusConstant.WORKING)
	List<SysPermission> getRootPermissionByUser(String username);
}
