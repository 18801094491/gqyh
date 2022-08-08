package com.zcdy.dsc.modules.datacenter.statistic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.datacenter.statistic.entity.SmsUsers;
import com.zcdy.dsc.modules.datacenter.statistic.mapper.SmsUsersMapper;
import com.zcdy.dsc.modules.datacenter.statistic.service.SmsUsersService;
import com.zcdy.dsc.modules.system.entity.SysUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述: 短信发送用户
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-02
 * 版本号: V1.0
 */
@Service
public class SmsUsersServiceImpl extends ServiceImpl<SmsUsersMapper, SmsUsers> implements SmsUsersService {

	@Resource
	private SmsUsersMapper smsUsersMapper;
	
	@Override
	public List<SysUser> queryUsersInfo(String moduleId) {
		return smsUsersMapper.queryUsersInfo(moduleId);
	}

}
