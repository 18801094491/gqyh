package com.zcdy.dsc.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.api.ISysBaseApi;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.common.system.vo.SysUserCacheInfo;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.common.util.PasswordUtil;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.system.constant.CacheConstant;
import com.zcdy.dsc.modules.system.constant.TimeConstant;
import com.zcdy.dsc.modules.system.entity.*;
import com.zcdy.dsc.modules.system.mapper.*;
import com.zcdy.dsc.modules.system.model.SysUserSysDepartModel;
import com.zcdy.dsc.modules.system.service.ISysUserService;
import com.zcdy.dsc.modules.system.vo.SysUserVo;
import com.zcdy.dsc.modules.system.vo.UserIdDropdown;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p> 用户表 服务实现类 </p>
 *@author : songguang.jiao
 * : scott
 * 
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

	@Autowired
	private SysUserMapper userMapper;
	@Autowired
	private SysPermissionMapper sysPermissionMapper;
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	@Autowired
	private SysUserDepartMapper sysUserDepartMapper;
	@Autowired
	private ISysBaseApi sysBaseApi;
	@Autowired
	private SysDepartMapper sysDepartMapper;
	@Autowired
	private SysUserCheckMapper sysUserCheckMapper;

	@Override
	@CacheEvict(value = { CacheConstant.SYS_USERS_CACHE }, allEntries = true)
	@Transactional(rollbackFor=Exception.class)
	public Result<?> resetPassword(String username, String oldpassword, String newpassword, String confirmpassword) {
		SysUser user = userMapper.getUserByName(username);
		String passwordEncode = PasswordUtil.encrypt(username, oldpassword, user.getSalt());
		if (!user.getPassword().equals(passwordEncode)) {
			return Result.error("旧密码输入错误!");
		}
		if (ConvertUtils.isEmpty(newpassword)) {
			return Result.error("新密码不允许为空!");
		}
		if (!newpassword.equals(confirmpassword)) {
			return Result.error("两次输入密码不一致!");
		}
		String password = PasswordUtil.encrypt(username, newpassword, user.getSalt());
		this.userMapper.update(new SysUser().setPassword(password),
				new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, user.getId()));
		this.userMapper.updatePasswordTime(username);
		return Result.ok("密码重置成功!");
	}

	@Override
	@CacheEvict(value = { CacheConstant.SYS_USERS_CACHE }, allEntries = true)
	@Transactional(rollbackFor=Exception.class)
	public Result<?> changePassword(SysUser sysUser) {
		String salt = ConvertUtils.randomGen(8);
		sysUser.setSalt(salt);
		String password = sysUser.getPassword();
		String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), password, salt);
		sysUser.setPassword(passwordEncode);
		this.userMapper.updateById(sysUser);
		this.userMapper.updatePasswordTime(sysUser.getUsername());
		return Result.ok("密码修改成功!");
	}

	@Override
	@CacheEvict(value = { CacheConstant.SYS_USERS_CACHE }, allEntries = true)
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteUser(String userId) {
		// 1.删除用户
		this.removeById(userId);
		// 2.删除用户部门关联关系
		LambdaQueryWrapper<SysUserDepart> query = new LambdaQueryWrapper<SysUserDepart>();
		query.eq(SysUserDepart::getUserId, userId);
		sysUserDepartMapper.delete(query);
		// 3.删除用户角色关联关系
		return false;
	}

	@Override
	@CacheEvict(value = { CacheConstant.SYS_USERS_CACHE }, allEntries = true)
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteBatchUsers(String userIds) {
		// 1.删除用户
		this.removeByIds(Arrays.asList(userIds.split(",")));
		// 2.删除用户部门关系
		LambdaQueryWrapper<SysUserDepart> query = new LambdaQueryWrapper<SysUserDepart>();
		for (String id : userIds.split(",")) {
			query.eq(SysUserDepart::getUserId, id);
			this.sysUserDepartMapper.delete(query);
		}
		// 3.删除用户角色关系
		return false;
	}

	@Override
	public SysUser getUserByName(String username) {
		return userMapper.getUserByName(username);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void addUserWithRole(SysUser user, String roles) {
		this.save(user);
		// 更新到用户密码更新时间表
		SysUserCheck sysUserCheck = new SysUserCheck();
		sysUserCheck.setLastUpdate(new Date());
		sysUserCheck.setDelFlag(DelFlagConstant.NORMAL);
		sysUserCheck.setSendStatus(0);
		sysUserCheck.setUsername(user.getUsername());
		sysUserCheckMapper.insert(sysUserCheck);
		// 如果没有用户赋予角色的权限，就不允许修改用户角色
		if (SecurityUtils.getSubject().isPermitted("user:role")) {
			if (ConvertUtils.isNotEmpty(roles)) {
				String[] arr = roles.split(",");
				for (String roleId : arr) {
					SysUserRole userRole = new SysUserRole(user.getId(), roleId);
					sysUserRoleMapper.insert(userRole);
				}
			}
		}
	}

	@Override
	@CacheEvict(value = { CacheConstant.SYS_USERS_CACHE }, allEntries = true)
	@Transactional(rollbackFor=Exception.class)
	public void editUserWithRole(SysUser user, String roles) {
		this.updateById(user);
		// 如果没有用户赋予角色的权限，就不允许修改用户角色
		if (SecurityUtils.getSubject().isPermitted("user:role")) {
			// 先删后加
			sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
			if (ConvertUtils.isNotEmpty(roles)) {
				String[] arr = roles.split(",");
				for (String roleId : arr) {
					SysUserRole userRole = new SysUserRole(user.getId(), roleId);
					sysUserRoleMapper.insert(userRole);
				}
			}
		}
	}

	@Override
	public List<String> getRole(String username) {
		return sysUserRoleMapper.getRoleByUserName(username);
	}

	/**
	 * 通过用户名获取用户角色集合
	 * 
	 * @param username 用户名
	 * @return 角色集合
	 */
	@Override
	public Set<String> getUserRolesSet(String username) {
		// 查询用户拥有的角色集合
		List<String> roles = sysUserRoleMapper.getRoleByUserName(username);
		if (log.isInfoEnabled()) {
			log.info("-------通过数据库读取用户拥有的角色Rules------username： " + username + ",Roles size: "
					+ (roles == null ? 0 : roles.size()));
		}
		return new HashSet<>(roles);
	}

	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param username 用户名
	 * @return 权限集合
	 */
	@Override
	public Set<String> getUserPermissionsSet(String username) {
		Set<String> permissionSet = new HashSet<>();
		List<SysPermission> permissionList = sysPermissionMapper.queryByUser(username);
		for (SysPermission po : permissionList) {
			if (ConvertUtils.isNotEmpty(po.getPerms())) {
				permissionSet.add(po.getPerms());
			}
		}
		return permissionSet;
	}

	@Override
	public SysUserCacheInfo getCacheUser(String username) {
		SysUserCacheInfo info = new SysUserCacheInfo();
		info.setOneDepart(true);

		LoginUser user = sysBaseApi.getUserByName(username);
		if (user != null) {
			info.setSysUserCode(user.getUsername());
			info.setSysUserName(user.getRealname());
			info.setSysOrgCode(user.getOrgCode());
		}

		// 多部门支持in查询
		List<SysDepart> list = sysDepartMapper.queryUserDeparts(user.getId());
		List<String> sysMultiOrgCode = new ArrayList<String>();
		if (list == null || list.size() == 0) {
			// 当前用户无部门
		} else if (list.size() == 1) {
			sysMultiOrgCode.add(list.get(0).getOrgCode());
		} else {
			info.setOneDepart(false);
			for (SysDepart dpt : list) {
				sysMultiOrgCode.add(dpt.getOrgCode());
			}
		}
		info.setSysMultiOrgCode(sysMultiOrgCode);

		return info;
	}

	// 根据部门Id查询
	@Override
	public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username) {
		return userMapper.getUserByDepId(page, departId, username);
	}

	@Override
	public IPage<SysUser> getUserByDepartIdAndQueryWrapper(Page<SysUser> page, String departId,
			QueryWrapper<SysUser> queryWrapper) {
		LambdaQueryWrapper<SysUser> lambdaQueryWrapper = queryWrapper.lambda();

		lambdaQueryWrapper.eq(SysUser::getDelFlag, "0");
		lambdaQueryWrapper.inSql(SysUser::getId,
				"SELECT user_id FROM sys_user_depart WHERE dep_id = '" + departId + "'");

		return userMapper.selectPage(page, lambdaQueryWrapper);
	}

	@Override
	public IPage<SysUserSysDepartModel> queryUserByOrgCode(String orgCode, SysUser userParams, IPage page) {
		List<SysUserSysDepartModel> list = baseMapper.getUserByOrgCode(page, orgCode, userParams);
		Integer total = baseMapper.getUserByOrgCodeTotal(orgCode, userParams);

		IPage<SysUserSysDepartModel> result = new Page<>(page.getCurrent(), page.getSize(), total);
		result.setRecords(list);

		return result;
	}

	// 根据角色Id查询
	@Override
	public IPage<SysUser> getUserByRoleId(Page<SysUser> page, String roleId, String username) {
		return userMapper.getUserByRoleId(page, roleId, username);
	}

	@Override
	public void updateUserDepart(String username, String orgCode) {
		baseMapper.updateUserDepart(username, orgCode);
	}

	@Override
	public SysUser getUserByPhone(String phone) {
		return userMapper.getUserByPhone(phone);
	}

	@Override
	public SysUser getUserByEmail(String email) {
		return userMapper.getUserByEmail(email);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void addUserWithDepart(SysUser user, String selectedParts) {
		// this.save(user); //保存角色的时候已经添加过一次了
		if (ConvertUtils.isNotEmpty(selectedParts)) {
			String[] arr = selectedParts.split(",");
			for (String deaprtId : arr) {
				SysUserDepart userDeaprt = new SysUserDepart(user.getId(), deaprtId);
				sysUserDepartMapper.insert(userDeaprt);
			}
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	@CacheEvict(value = { CacheConstant.SYS_USERS_CACHE }, allEntries = true)
	public void editUserWithDepart(SysUser user, String departs) {
		// 更新角色的时候已经更新了一次了，可以再跟新一次
		this.updateById(user);
		// 先删后加
		sysUserDepartMapper
				.delete(new QueryWrapper<SysUserDepart>().lambda().eq(SysUserDepart::getUserId, user.getId()));
		if (ConvertUtils.isNotEmpty(departs)) {
			String[] arr = departs.split(",");
			for (String departId : arr) {
				SysUserDepart userDepart = new SysUserDepart(user.getId(), departId);
				sysUserDepartMapper.insert(userDepart);
			}
		}
	}

	/**
	 * 校验用户是否有效
	 * 
	 * @param sysUser
	 * @return
	 */
	@Override
	public Result<?> checkUserIsEffective(SysUser sysUser) {
		Result<Object> result = new Result<Object>();
		// 情况1：根据用户信息查询，该用户不存在
		if (sysUser == null) {
			result.error500("该用户不存在，请注册");
			sysBaseApi.addLog("用户登录失败，用户不存在！", CommonConstant.LOG_TYPE_1, null);
			return result;
		}
		// 情况2：根据用户信息查询，该用户已注销
		if (CommonConstant.DEL_FLAG_1.toString().equals(sysUser.getDelFlag())) {
			sysBaseApi.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已注销！", CommonConstant.LOG_TYPE_1, null);
			result.error500("该用户已注销");
			return result;
		}
		// 情况3：根据用户信息查询，该用户已冻结
		if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
			sysBaseApi.addLog("用户登录失败，用户名:" + sysUser.getUsername() + "已冻结！", CommonConstant.LOG_TYPE_1, null);
			result.error500("该用户已冻结");
			return result;
		}
		return result;
	}

	/*
	 * @see
	 * com.zcdy.dsc.modules.system.service.ISysUserService#getUserByNameOrPhone(
	 * java.lang.String)
	 */
	@Override
	public SysUser getUserByNameOrPhone(String username) {
		return userMapper.getUserByNameOrPhone(username);
	}

	@Override
	public Boolean checkUserPasswordTime(String username) {
		return userMapper.checkUserPasswordTime(username, TimeConstant.DAYS);
	}

	@Override
	public String getOverTimeUser() {
		return userMapper.getOverTimeUser(TimeConstant.DAYS);
	}

	@Override
	public void updateSendStatus(String users) {
		String[] split = users.split(",");
		List<String> list = new ArrayList<String>(Arrays.asList(split));
		userMapper.updateSendStatus(list);
	}

	@Override
	public List<UserIdDropdown> getUserIdList(String name) {
		return userMapper.getUserIdList(name);
	}

	@Override
	public List<SysUser> queryExport(String username, Integer sex) {
		return userMapper.queryExport(username, sex);
	}

	@Override
	public IPage<SysUserVo> queryPage(Page<SysUserVo> page, SysUser user) {
		return userMapper.queryPage(page, user);
	}

	@Override
	@CacheEvict(value = { CacheConstant.SYS_USERS_CACHE }, allEntries = true)
	public Result<Object> forgetResetPassword(String password, String confirmPassword, String mobile) {
		SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, mobile));
		if(user==null){
			return Result.error("用户名不存在");
		}
		if(!password.equals(confirmPassword)){
			return Result.error("两次密码输入不一致");
		}
		String encryptPassword = PasswordUtil.encrypt(user.getUsername(), password, user.getSalt());
		this.userMapper.update(new SysUser().setPassword(encryptPassword),
				new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, user.getId()));
		this.userMapper.updatePasswordTime(user.getUsername());
		return Result.ok("重置成功");
	}


}
