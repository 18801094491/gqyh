package com.zcdy.dsc.modules.message.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.util.UUIDGenerator;
import com.zcdy.dsc.constant.WorkingStatus;
import com.zcdy.dsc.modules.datacenter.statistic.entity.SmsUsers;
import com.zcdy.dsc.modules.datacenter.statistic.mapper.SmsUsersMapper;
import com.zcdy.dsc.modules.message.entity.SmsTemplateConfig;
import com.zcdy.dsc.modules.message.mapper.SmsTemplateConfigMapper;
import com.zcdy.dsc.modules.message.param.SmsTemplateConfigPageParam;
import com.zcdy.dsc.modules.message.param.SmsTemplateConfigParam;
import com.zcdy.dsc.modules.message.service.SmsTemplateConfigService;
import com.zcdy.dsc.modules.message.vo.SmsTemplateConfigVo;
import com.zcdy.dsc.modules.message.vo.SmsTemplateDropdown;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;

import cn.hutool.core.bean.BeanUtil;

/**
 * @Description: 短信模板配置
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-13
 * @Version: V1.0
 */
@Service
public class SmsTemplateConfigServiceImpl extends ServiceImpl<SmsTemplateConfigMapper, SmsTemplateConfig> implements SmsTemplateConfigService {

	@Resource
	private SmsTemplateConfigMapper smsTemplateConfigMapper;
	
	@Resource
	private SmsUsersMapper smsUsersMapper;
	
	@Override
	public IPage<SmsTemplateConfigVo> queryPageData(Page<SmsTemplateConfigVo> page,SmsTemplateConfigPageParam param) {
		return smsTemplateConfigMapper.selectPageData(page, param);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void editStatus(String id) {
		UpdateWrapper<SmsTemplateConfig> updateWrapper=new UpdateWrapper<>();
		updateWrapper.lambda().eq(SmsTemplateConfig::getId, id);
		//查询当前状态然后变更
		QueryWrapper<SmsTemplateConfig> queryWrapper=new QueryWrapper<>();
		queryWrapper.lambda().eq(SmsTemplateConfig::getId, id).select(SmsTemplateConfig::getTemplateStatus);
		SmsTemplateConfig one = smsTemplateConfigMapper.selectOne(queryWrapper);
		if(WorkingStatus.WORKING.equals(one.getTemplateStatus())){
			one.setTemplateStatus(WorkingStatus.STOP);
		}else{
			one.setTemplateStatus(WorkingStatus.WORKING);
		}
		smsTemplateConfigMapper.update(one, updateWrapper);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void one(SmsTemplateConfigParam smsTemplateConfigParam) {
		SmsTemplateConfig smsTemplateConfig=new SmsTemplateConfig();
		BeanUtil.copyProperties(smsTemplateConfigParam, smsTemplateConfig);
		
		if(!StringUtils.isEmpty(smsTemplateConfig.getId())){
			//修改时模板id跟模块id都不能为空,否则不能存入模块用户表
			smsTemplateConfigMapper.updateById(smsTemplateConfig);
		}else{
			smsTemplateConfig.setDelFlag(DelFlagConstant.NORMAL);
			smsTemplateConfig.setTemplateStatus(WorkingStatus.WORKING);
			//TODO 暂时随机生成1个模块id,后续有模块管理功能后再接入id
			smsTemplateConfig.setModuleId(UUIDGenerator.generate());
			smsTemplateConfigMapper.insert(smsTemplateConfig);
		}
		//插入收件人
		String userIdsStr=smsTemplateConfigParam.getUsersId();
		if(!StringUtils.isEmpty(userIdsStr)){
			//删除原有关联人,然后插入
			QueryWrapper<SmsUsers> queryWrapper=new QueryWrapper<>();
			queryWrapper.lambda().eq(SmsUsers::getModuleId, smsTemplateConfig.getModuleId());
			smsUsersMapper.delete(queryWrapper);
			String[] userIds = StringUtils.split(userIdsStr,",");
			List<SmsUsers> userList=new ArrayList<SmsUsers>();
			for (String userId : userIds) {
				SmsUsers smsUsers=new SmsUsers();
				smsUsers.setModuleId(smsTemplateConfig.getModuleId());
				smsUsers.setUserId(userId);
				smsUsers.setId(UUIDGenerator.generate());
				userList.add(smsUsers);
			}
			if(userList.size()>0 &&!userList.isEmpty()){
				smsUsersMapper.insertBatchUsers(userList);
			}
		}
	}

	@Override
	public List<SmsTemplateConfigVo> export(SmsTemplateConfigPageParam param) {
		return smsTemplateConfigMapper.export(param);
	}

	@Override
	public List<SmsTemplateDropdown> queryDropdown(String templateName) {
		return smsTemplateConfigMapper.queryDropdown(templateName);
	}

}
