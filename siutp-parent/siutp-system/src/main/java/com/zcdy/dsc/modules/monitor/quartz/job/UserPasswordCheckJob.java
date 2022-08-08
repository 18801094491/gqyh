package com.zcdy.dsc.modules.monitor.quartz.job;

import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.zcdy.dsc.common.system.api.ISysBaseApi;
import com.zcdy.dsc.modules.system.service.ISysUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 描述: 每天定时通知超过30天未更新密码的用户
 * @author：  songguang.jiao
 * 创建时间： 2020年2月14日 下午3:31:32 
 * 版本号: V1.0
 */
@Slf4j
public class UserPasswordCheckJob implements Job{
	
	@Autowired
	private ISysUserService sysUserService;
	
	@Autowired
	private ISysBaseApi sysBaseApi;
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		
		String users = sysUserService.getOverTimeUser();
		if(!StringUtils.isEmpty(users)){
			if(log.isDebugEnabled()){
				log.debug("开始通知用户更新密码");
			}
			//调用通知接口,并且更新密码表为已通知
			sysBaseApi.sendSysAnnouncement("admin", users, "关于及时更新密码的通知", "您的密码已使用长达1个月,为了安全起见,请及时登录系统更换新密码");
			sysUserService.updateSendStatus(users);
			if(log.isDebugEnabled()){
				log.debug("通知用户更新密码成功");
			}
		}
	}

}
