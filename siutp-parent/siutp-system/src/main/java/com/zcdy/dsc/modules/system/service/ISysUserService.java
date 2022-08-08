package com.zcdy.dsc.modules.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.system.vo.SysUserCacheInfo;
import com.zcdy.dsc.modules.system.entity.SysUser;
import com.zcdy.dsc.modules.system.model.SysUserSysDepartModel;
import com.zcdy.dsc.modules.system.vo.SysUserVo;
import com.zcdy.dsc.modules.system.vo.UserIdDropdown;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *@author : songguang.jiao
 
 * @since 2018-12-20
 */
public interface ISysUserService extends IService<SysUser> {

	/**
	 * 重置密码
	 *
	 * @param username
	 * @param oldpassword
	 * @param newpassword
	 * @param confirmpassword
	 * @return
	 */
	public Result<?> resetPassword(String username, String oldpassword, String newpassword, String confirmpassword);

	/**
	 * 修改密码
	 *
	 * @param sysUser
	 * @return
	 */
	public Result<?> changePassword(SysUser sysUser);

	/**
	 * 删除用户
	 * @param userId
	 * @return
	 */
	public boolean deleteUser(String userId);

	/**
	 * 批量删除用户
	 * @param userIds
	 * @return
	 */
	public boolean deleteBatchUsers(String userIds);
	
	public SysUser getUserByName(String username);
	
	/**
	 * 添加用户和用户角色关系
	 * @param user
	 * @param roles
	 */
	public void addUserWithRole(SysUser user,String roles);
	
	
	/**
	 * 修改用户和用户角色关系
	 * @param user
	 * @param roles
	 */
	public void editUserWithRole(SysUser user,String roles);

	/**
	 * 获取用户的授权角色
	 * @param username
	 * @return
	 */
	public List<String> getRole(String username);
	
	/**
	  * 查询用户信息包括 部门信息
	 * @param username
	 * @return
	 */
	public SysUserCacheInfo getCacheUser(String username);

	/**
	 * 根据部门Id查询
	 * @param
	 * @return
	 */
	public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username);

    /**
     * 根据部门 Id 和 QueryWrapper 查询
     *
     * @param page
     * @param departId
     * @param queryWrapper
     * @return
     */
    public IPage<SysUser> getUserByDepartIdAndQueryWrapper(Page<SysUser> page, String departId, QueryWrapper<SysUser> queryWrapper);

	/**
	 * 根据 orgCode 查询用户，包括子部门下的用户
	 *
	 * @param orgCode
	 * @param userParams 用户查询条件，可为空
	 * @param page 分页参数
	 * @return
	 */
	IPage<SysUserSysDepartModel> queryUserByOrgCode(String orgCode, SysUser userParams, IPage page);

	/**
	 * 根据角色Id查询
	 * @param
	 * @return
	 */
	public IPage<SysUser> getUserByRoleId(Page<SysUser> page,String roleId, String username);

	/**
	 * 通过用户名获取用户角色集合
	 *
	 * @param username 用户名
	 * @return 角色集合
	 */
	Set<String> getUserRolesSet(String username);

	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param username 用户名
	 * @return 权限集合
	 */
	Set<String> getUserPermissionsSet(String username);
	
	/**
	 * 根据用户名设置部门ID
	 * @param username
	 * @param orgCode
	 */
	void updateUserDepart(String username,String orgCode);
	
	/**
	 * 根据手机号获取用户名和密码
	 */
	public SysUser getUserByPhone(String phone);


	/**
	 * 根据邮箱获取用户
	 */
	public SysUser getUserByEmail(String email);


	/**
	 * 添加用户和用户部门关系
	 * @param user
	 * @param selectedParts
	 */
	void addUserWithDepart(SysUser user, String selectedParts);

	/**
	 * 编辑用户和用户部门关系
	 * @param user
	 * @param departs
	 */
	void editUserWithDepart(SysUser user, String departs);
	
	/**
	   * 校验用户是否有效
	 * @param sysUser
	 * @return
	 */
	Result checkUserIsEffective(SysUser sysUser);

	/**
	 * @author：admin
	 * 描述:<p></p>
	 */
	public SysUser getUserByNameOrPhone(String username);

	/**
	 * 描述: 校验用户密码使用时长是否超过30天
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月14日 上午11:17:34 
	 * 版本号: V1.0
	 */
	public Boolean checkUserPasswordTime(String username);
	
	/**
	 * 描述: 查询所有超时未更换密码用户
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月14日 下午1:30:50 
	 * 版本号: V1.0
	 */
	String getOverTimeUser();
	
	/**
	 * 描述: 批量更新用户发送状态
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月14日 下午1:37:49 
	 * 版本号: V1.0
	 */
	public void updateSendStatus(String users);
	
	/**
	 * 描述: 用户下拉列表(id-value)
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月11日 下午5:50:46 
	 * 版本号: V1.0
	 */
	List<UserIdDropdown> getUserIdList(String name);

	/**
	 * 描述: 导出查询
	 * @author：  songguang.jiao
	 * 创建时间：  2020年3月30日 上午10:34:00
	 * 版本号: V1.0
	 */
	public List<SysUser> queryExport(String username,Integer sex);


    IPage<SysUserVo> queryPage(Page<SysUserVo> page, SysUser user);

	/**
	 * 忘记密码后,重置密码
	 * @return
	 */
	Result<Object> forgetResetPassword(String password,String confirmPassword,String mobile);
}
