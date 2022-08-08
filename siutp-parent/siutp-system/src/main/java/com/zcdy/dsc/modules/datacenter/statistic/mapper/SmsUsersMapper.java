package com.zcdy.dsc.modules.datacenter.statistic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.datacenter.statistic.entity.SmsUsers;
import com.zcdy.dsc.modules.system.entity.SysUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 描述: 短信发送用户
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-02
 * 版本号: V1.0
 */
public interface SmsUsersMapper extends BaseMapper<SmsUsers> {

	/**
	 * 描述: 通过模块查询对应用户手机号和用户登录名
	 * @author：  songguang.jiao
	 * 创建时间：  2020年4月2日 下午4:43:40
	 * 版本号: V1.0
	 */
	@Select({
		"SELECT ",
		" t2.id,",
		 "t2.username,",
		" t2.phone",
		" FROM",
		" sms_users t1 ",
		" LEFT JOIN sys_user t2 on t2.id=t1.user_id",
		" WHERE",
		" module_id = #{moduleId}",
	})
	List<SysUser> queryUsersInfo(@Param("moduleId") String moduleId);
	
	/**
	 * 描述: 批量插入用户
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月13日 下午4:14:03
	 * 版本: V1.0
	 */
	@Insert({
		" <script>",
		" INSERT INTO `sms_users` (`id`, `user_id`, `module_id`)",
		" VALUES",
		" <foreach collection='list' item='item' index='index' separator=','>",
		" (",
		" #{item.id},",
		" #{item.userId},",
		" #{item.moduleId}",
		" )",
		" </foreach>",
		" </script>",
	})
	void insertBatchUsers(@Param("list") List<SmsUsers> list);

}
