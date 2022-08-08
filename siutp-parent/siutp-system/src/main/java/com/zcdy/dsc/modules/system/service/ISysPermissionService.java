package com.zcdy.dsc.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.common.exception.BusinessException;
import com.zcdy.dsc.modules.system.entity.SysPermission;
import com.zcdy.dsc.modules.system.model.TreeModel;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *@author : songguang.jiao
 
 * @since 2018-12-21
 */
public interface ISysPermissionService extends IService<SysPermission> {
	
	public List<TreeModel> queryListByParentId(String parentId);
	
	/**真实删除*/
	public void deletePermission(String id) throws BusinessException;
	/**逻辑删除*/
	public void deletePermissionLogical(String id) throws BusinessException;
	
	public void addPermission(SysPermission sysPermission) throws BusinessException;
	
	public void editPermission(SysPermission sysPermission) throws BusinessException;
	
	public List<SysPermission> queryByUser(String username);
	
	/**
	 * 根据permissionId删除其关联的SysPermissionDataRule表中的数据
	 * 
	 * @param id
	 * @return
	 */
	public void deletePermRuleByPermId(String id);
	
	/**
	  * 查询出带有特殊符号的菜单地址的集合
	 * @return
	 */
	public List<String> queryPermissionUrlWithStar();

	/**
	 * 查询用户拥有的一级菜单权限
	 * @param username
	 * @return
	 */
	List<SysPermission> getRootPermissionByUser(String username);
}
