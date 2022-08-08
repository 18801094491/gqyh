package com.zcdy.dsc.modules.operation.work.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.common.util.UUIDGenerator;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.work.constant.TeamMermberTypeConstant;
import com.zcdy.dsc.modules.operation.work.entity.*;
import com.zcdy.dsc.modules.operation.work.mapper.WorkThirdTeamMapper;
import com.zcdy.dsc.modules.operation.work.mapper.WorkDao;
import com.zcdy.dsc.modules.operation.work.mapper.WorkShiftTeamMapper;
import com.zcdy.dsc.modules.operation.work.mapper.WorkTeamMapper;
import com.zcdy.dsc.modules.operation.work.param.WorkTeamParam;
import com.zcdy.dsc.modules.operation.work.service.WorkService;
import com.zcdy.dsc.modules.operation.work.vo.*;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 描述: workServcice实现类
 * @author：  songguang.jiao
 * 创建时间： 2020年2月12日 上午11:11:09
 * 版本号: V1.0
 */
@Service
public class WorkServiceImpl implements WorkService {

	@Autowired
	private WorkDao workDao;
	
	@Autowired
	private WorkThirdTeamMapper teamMapper;
	
	@Autowired
	private WorkTeamMapper workTeamMapper;
	
	@Autowired
	private WorkShiftTeamMapper workShiftTeamMapper;
	
	@Override
	public IPage<ShiftsVo> queryShiftsList(IPage<ShiftsVo> page, String shiftsName, String shiftsStatus) {
		return workDao.queryShiftsList(page, shiftsName, shiftsStatus);
	}

	@Override
	public ShiftsVo  queryDetailById(String id) {
		return workDao.queryDetailById(id);
	}

	@Override
	public void editShiftStatus(String id) {
		workDao.editShiftStatus(id);
	}

	@Override
	public synchronized void saveThirdTeam(WorkThirdTeam workThirdTeam) {
		workThirdTeam.setTeamSn(this.getThirdTeamSn());
		teamMapper.insert(workThirdTeam);
	}
	/**
	 * 描述: 获取最后一个第三方团队编号
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月16日 上午10:09:24
	 * 版本号: V1.0
	 */
	private String getThirdTeamSn(){
		String teamSn = "DSFT";
		String teamSnNum = workDao.getLastThirdTeamSn();
		if(StringUtils.isEmpty(teamSnNum)){
			teamSn=teamSn+"000001";
		}else{
			String temp = teamSnNum.substring(4);
			String number = StringUtils.leftPad((Integer.parseInt(temp)+1)+"", 6,'0');
			teamSn = teamSn + number;
		}
		return teamSn;
	}

	@Override
	public IPage<ThirdTeamVo> queryThirdPageData(Page<ThirdTeamVo> page, String teamName, String teamSn) {
		return workDao.queryThirdPageData(page, teamName, teamSn);
	}

	@Override
	public IPage<WorkTeamVo> queryTeamPageData(Page<WorkTeamVo> page, String teamName, String teamStatus) {
		return workDao.queryTeamPageData(page, teamName, teamStatus);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updateTeam(WorkTeam team, WorkTeamParam workTeamVo) {
		workTeamMapper.updateById(team);
		//判断是否存在关联，不存在的话，删除原来班组班次关联表,重新插入
		 if((workDao.checkShiftTeamExist(team.getId(),workTeamVo.getShiftsId()))==0){
			 workDao.deleteShiftTeam(workTeamVo.getId());
			 WorkShiftTeam workShiftTeam=new WorkShiftTeam();
			 workShiftTeam.setTeamId(team.getId());
			 workShiftTeam.setShiftsId(workTeamVo.getShiftsId());
			 workShiftTeamMapper.insert(workShiftTeam);
		 }
		//批量删除现有组员,重新插入
		workDao.deleteTeamMember(workTeamVo.getId());
		this.insertTeamMembers(team,workTeamVo);
	}

	/**
	 * 分别插入组长跟组员
	 * @param team
	 * @param workTeamVo
	 */
	private void insertTeamMembers(WorkTeam team, WorkTeamParam workTeamVo){
		List<WorkTeamMember> list=new ArrayList<WorkTeamMember>();
		Date date=new Date();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(!StringUtils.isEmpty(workTeamVo.getUserIds())){
			String[] split = workTeamVo.getUserIds().split(",");
			for (int i = 0; i < split.length; i++) {
				WorkTeamMember member=new WorkTeamMember();
				member.setCreateBy(sysUser.getUsername());
				member.setCreateTime(date);
				member.setDelFlag(DelFlagConstant.NORMAL);
				member.setId(UUIDGenerator.generate());
				member.setMemberType(TeamMermberTypeConstant.MEMBER);
				member.setTeamId(team.getId());
				member.setUserId(split[i]);
				list.add(member);
			}
		}
		if(!StringUtils.isEmpty(workTeamVo.getUserManageIds())){
			String[] split = workTeamVo.getUserManageIds().split(",");
			for (int i = 0; i < split.length; i++) {
				WorkTeamMember member=new WorkTeamMember();
				member.setCreateBy(sysUser.getUsername());
				member.setCreateTime(date);
				member.setDelFlag(DelFlagConstant.NORMAL);
				member.setId(UUIDGenerator.generate());
				member.setMemberType(TeamMermberTypeConstant.HEADMAN);
				member.setTeamId(team.getId());
				member.setUserId(split[i]);
				list.add(member);
			}
		}
		if(list!=null && list.size()>0){
			workDao.insertTeamMember(list);
		}
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void saveTeam(WorkTeam team, WorkTeamParam workTeamVo) {
		workTeamMapper.insert(team);
		//插入关联表
		WorkShiftTeam workShiftTeam=new WorkShiftTeam();
		workShiftTeam.setTeamId(team.getId());
		workShiftTeam.setShiftsId(workTeamVo.getShiftsId());
		workShiftTeamMapper.insert(workShiftTeam);
		this.insertTeamMembers(team,workTeamVo);
	}

	@Override
	public List<WorkTeamVo> exportTeamData(String teamName, String teamStatus) {
		return workDao.exportTeamData(teamName,teamStatus);
	}

	@Override
	public void editTeamStatus(String id) {
		workDao.editTeamStatus(id);
	}

	@Override
	public List<ShiftsDropdown> queryShiftsName() {
		return workDao.queryShiftsName();
	}

	@Override
	public void addWorkTeamDuty(List<WorkTeamDuty> list) {
		workDao.addWorkTeamDuty(list);
	}

	@Override
	public List<WorkTeamDutyVo> queryWorkDutyData(String dutyDay) {
		String lastMonth = DateUtil.getLastMonth(dutyDay);
		String nextMonth = DateUtil.getNextMonth(dutyDay);
		return workDao.queryWorkDutyDataMonth(lastMonth,nextMonth);
	}

	@Override
	public List<TeamDropdown> queryTeamNameList() {
		return workDao.queryTeamNameList();
	}

	@Override
	public ShiftsDropdown queryShiftsByTeamId(String teamId) {
		return workDao.queryShiftsByTeamId(teamId);
	}

	@Override
	public List<WorkTeamDutyOneDayVo> queryWorkDutyOneDay(String dutyDay) {
		return workDao.queryWorkDutyOneDay(dutyDay);
	}

	@Override
	public List<WorkMyTeamDutyVo> queryMyWorkDutyData( String dutyDay) {
		//获取到当前登录者用户信息
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String id = sysUser.getId();
		String lastMonth = DateUtil.getLastMonth(dutyDay);
		String nextMonth = DateUtil.getNextMonth(dutyDay);
		return workDao.queryMyWorkDutyDataMonth(lastMonth,nextMonth,id);
	}

	@Override
	public List<WorkDutyAllExport> queryWorkDutyExportData(String dutyDay) {
		return workDao.queryWorkDutyExportData(dutyDay);
	}

	@Override
	public List<ShiftsVo> exportShiftsData(String shiftsName, String shiftsStatus) {
		return workDao.exportShiftsData(shiftsName, shiftsStatus);
	}

	@Override
	public List<WorkTeamTreeEntity> treeList() {
		return workDao.treeList();
	}

}
