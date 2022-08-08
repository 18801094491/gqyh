package com.zcdy.dsc.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.system.entity.SysUser;
import com.zcdy.dsc.modules.system.model.SysUserSysDepartModel;
import com.zcdy.dsc.modules.system.vo.SysUserVo;
import com.zcdy.dsc.modules.system.vo.UserIdDropdown;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *@author : songguang.jiao
 
 * @since 2018-12-20
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	/**
	  * 通过用户账号查询用户信息
	 * @param username
	 * @return
	 */
	public SysUser getUserByName(@Param("username") String username);

	/**
	 * 根据部门Id查询用户信息
	 * @param page 分页参数
	 * @param departId 部门id
	 * @param username 用户名
	 * @return
	 */
	IPage<SysUser> getUserByDepId(Page page, @Param("departId") String departId, @Param("username") String username);

	/**
	 * 根据角色Id查询用户信息
	 * @param page 分页参数
	 * @param roleId 角色id
	 * @param username 用户名
	 * @return
	 */
	IPage<SysUser> getUserByRoleId(Page page, @Param("roleId") String roleId, @Param("username") String username);
	
	/**
	 * 根据用户名设置部门ID
	 * @param username
	 * @param orgCode
	 */
	void updateUserDepart(@Param("username") String username,@Param("orgCode") String orgCode);
	
	/**
	 * 根据手机号查询用户信息
	 * @param phone
	 * @return
	 */
	public SysUser getUserByPhone(@Param("phone") String phone);
	
	
	/**
	 * 根据邮箱查询用户信息
	 * @param email
	 * @return
	 */
	public SysUser getUserByEmail(@Param("email")String email);

	/**
	 * 根据 orgCode 查询用户，包括子部门下的用户
	 *
	 * @param page 分页对象, xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
	 * @param orgCode
	 * @param userParams 用户查询条件，可为空
	 * @return
	 */
	List<SysUserSysDepartModel> getUserByOrgCode(IPage page, @Param("orgCode") String orgCode, @Param("userParams") SysUser userParams);


    /**
     * 查询 getUserByOrgCode 的Total
     *
     * @param orgCode
     * @param userParams 用户查询条件，可为空
     * @return
     */
    Integer getUserByOrgCodeTotal(@Param("orgCode") String orgCode, @Param("userParams") SysUser userParams);

	/**
	 * 通过名称或者电话查找用户
	 * @param username
	 * @return
	 */
	public SysUser getUserByNameOrPhone(String username);

	/**
	 * 校验用户密码使用时长是否超过30天
	 * @param username 用户名称
	 * @param days 天数
	 * @return
	 */
	@Select({
		" SELECT count( id ) FROM sys_user_check WHERE username =#{username} and TimeStampDiff(day,last_update,CURRENT_DATE)>#{days}"
	})
	public Boolean checkUserPasswordTime(@Param("username")String username,@Param("days")Integer days);

	/**
	 * 更新用户密码更新时间和通知状态
	 * @param username 用户名称
	 */
	@Update({
		"update sys_user_check set last_update=CURRENT_DATE,send_status=0 where username =#{username}"
	})
	public void updatePasswordTime(@Param("username")String username);


	/**
	 * 查询所有超时未更换密码用户
	 * @param days 天数
	 * @return
	 */
	@Select({
		"SELECT GROUP_CONCAT(username) from sys_user_check where send_status='0' and TimeStampDiff(day,last_update,CURRENT_DATE)>#{days} ",
	})
	String getOverTimeUser(@Param("days")Integer days);

	/**
	 * 批量更新用户发送状态,list不能为空
	 * @param list 入参列表
	 */
	@Update({
		" <script>",
		" <if test='list!=null'>",
		" UPDATE sys_user_check set send_status='1'  where username in",
		" <foreach collection='list' item='item' index='index' open='(' separator=',' close=')'>",
		" 	#{item}",
		" </foreach>",
		" </if>",
		" </script>"
	})
	public void updateSendStatus(@Param("list")List<String> list);

	/**
	 * 查询用户下拉列表
	 * @param name 用户名称
	 * @return
	 */
	@Select({
		"<script> ",
		" SELECT",
		" t.id,",
		" CONCAT( t.realname, '[', IFNULL( t.work_no, '' ), ']' ) name",
		" FROM",
		" sys_user t",
		" where t.del_flag='0'",
		" <if test='name!=null and name!=\"\"'>",
		"  and t.realname like concat('%',#{name},'%')",
		" </if>",
		" ORDER BY t.username",
		"</script> ",
	})
	public List<UserIdDropdown> getUserIdList(@Param("name") String name);

	/**
	 * 查询导出数据
	 * @param username 用户名
	 * @param sex 性别
	 * @return
	 */
	@Select({
		" <script>",
		" SELECT",
		" t1.id,",
		" t1.username,",
		" t1.realname,",
		" t1.password,",
		" t1.salt,",
		" t1.avatar,",
		" t1.birthday,",
		" t1.sex,",
		" t1.email,",
		" t1.phone,",
		" t1.org_code orgCode,",
		" t1.status,",
		" t1.del_flag delFlag,",
		" t1.work_no workNo,",
		" t2.name  post,",
		" t1.telephone,",
		" t1.create_by createBy,",
		" t1.create_time createTime,",
		" t1.update_by updateBy,",
		" t1.update_time updateTime,",
		" t1.activiti_sync activitiSync",
		" FROM",
		" sys_user t1",
		" LEFT JOIN sys_position t2 on t2.code=t1.post",
		" WHERE",
		" del_flag =0",
		" <if test='username!=null and username!=\"\"'>",
		"  and t1.username like concat('%',#{username},'%')",
		" </if>",
		" <if test='sex!=null and sex!=\"\"'>",
		"  and t1.sex =#{sex}",
		" </if>",
		" </script>",
	})
	public List<SysUser> queryExport(@Param("username") String username,@Param("sex") Integer sex);

	/**
	 * 分页查询用户信息
	 * @param page 分页参数
	 * @param user 用户参数
	 * @return
	 */
	@Select({
			" <script>",
			"select su.id," +
					"su.username," +
					"su.realname," +
					"su.password," +
					"su.salt," +
					"su.avatar," +
					"su.birthday," +
					"su.sex," +
					"su.email," +
					"su.phone," +
					"su.org_code as orgCode," +
					"su.status," +
					"su.del_flag as delFlag," +
					"su.activiti_sync as activitiSync," +
					"su.work_no as workNo," +
					"su.post," +
					"su.telephone," +
					"su.create_by as createBy," +
					"su.create_time as createTime," +
					"su.update_by as updateBy," +
					"su.app_role as appRole," +
					"su.update_time as updateTime,sp.name as postDict   from sys_user su ",
			"left join sys_position sp on su.post=sp.code  ",
			" WHERE",
			" del_flag =0",
			" <if test='user.username!=null and user.username!=\"\"'>",
			"  and su.username like concat('%',#{user.username},'%')",
			" </if>",
			" <if test='user.realname!=null and user.realname!=\"\"'>",
			"  and su.realname like concat('%',#{user.realname},'%')",
			" </if>",
			" <if test='user.sex!=null and user.sex!=\"\"'>",
			"  and su.sex =#{user.sex}",
			" </if>",
			" <if test='user.email!=null and user.email!=\"\"'>",
			"  and su.email like concat('%',#{user.email},'%')",
			" </if>",
			" <if test='user.phone!=null and user.phone!=\"\"'>",
			"  and su.phone =#{user.phone}",
			" </if>",
			" <if test='user.status!=null and user.status!=\"\"'>",
			"  and su.status =#{user.status}",
			" </if>",
			" </script>",
	})
	IPage<SysUserVo> queryPage(Page<SysUserVo> page, @Param("user") SysUser user);
}
