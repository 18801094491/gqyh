package com.zcdy.dsc.modules.operation.work.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.work.entity.WorkTeamTreeEntity;
import com.zcdy.dsc.modules.operation.work.entity.WorkThirdTeam;
import com.zcdy.dsc.modules.operation.work.entity.WorkTeam;
import com.zcdy.dsc.modules.operation.work.entity.WorkTeamDuty;
import com.zcdy.dsc.modules.operation.work.param.WorkTeamParam;
import com.zcdy.dsc.modules.operation.work.vo.*;

import java.util.List;

/**
 * 描述: 班组班次自定义接口
 * @author：  songguang.jiao
 * 创建时间：  2020年1月10日 下午3:23:05
 * 版本号: V1.0
 */
public interface WorkService {

	/**
	 * 分页查询班次列表
	 * @param page
	 * @param shiftsName
	 * @param shiftsStatus
	 * @return
	 */
	public IPage<ShiftsVo> queryShiftsList(IPage<ShiftsVo> page,String shiftsName,String shiftsStatus);

	/**
	 * 查询班次详情
	 * @param id
	 * @return
	 */
	public ShiftsVo queryDetailById(String id);

	/**
	 * 更改班次状态
	 * @param id
	 */
	public void editShiftStatus(String id);

	/**
	 * 新增第三方团队
	 * @param workThirdTeam
	 */
	public void saveThirdTeam(WorkThirdTeam workThirdTeam);

	/**
	 * 分页查询第三方团队
	 * @param page
	 * @param teamName
	 * @param teamSn
	 * @return
	 */
	public IPage<ThirdTeamVo> queryThirdPageData(Page<ThirdTeamVo> page, String teamName, String teamSn);

	/**
	 * 班组信息表-分页列表查询
	 * @param page
	 * @param teamName
	 * @param teamStatus
	 * @return
	 */
	public IPage<WorkTeamVo> queryTeamPageData(Page<WorkTeamVo> page, String teamName, String teamStatus);

	/**
	 * 修改班组
	 * @param team
	 * @param workTeamVo
	 */
	public void updateTeam(WorkTeam team, WorkTeamParam workTeamVo);

	/**
	 * 新增班组
	 * @param team
	 * @param workTeamVo
	 */
	public void saveTeam(WorkTeam team, WorkTeamParam workTeamVo);

	/**
	 * 导出班组信息
	 * @param teamName
	 * @param teamStatus
	 * @return
	 */
	public List<WorkTeamVo> exportTeamData(String teamName, String teamStatus);

	/**
	 * 班组状态-启停用
	 * @param id
	 */
	public void editTeamStatus(String id);

	/**
	 * 班次下拉列表(id-value)
	 * @return
	 */
	public List<ShiftsDropdown> queryShiftsName();

	/**
	 * 批量插入排版表
	 * @param list
	 */
	public void addWorkTeamDuty(List<WorkTeamDuty> list);

	/**
	 * 班组排班列表 默认按照月分组
	 * @param dutyDay
	 * @return
	 */
	public List<WorkTeamDutyVo> queryWorkDutyData(String dutyDay);

	/**
	 *  查询班组下拉列表
	 * @return
	 */
	public List<TeamDropdown> queryTeamNameList();

	/**
	 * 通过班组id查询班次id
	 * @param teamId
	 * @return
	 */
	public ShiftsDropdown queryShiftsByTeamId(String teamId);

	/**
	 * 当日详细排班情况
	 * @param dutyDay
	 * @return
	 */
	public List<WorkTeamDutyOneDayVo> queryWorkDutyOneDay(String dutyDay);

	/**
	 * 查询自己的排班列表
	 * @param dutyDay
	 * @return
	 */
	List<WorkMyTeamDutyVo> queryMyWorkDutyData( String dutyDay);

	/**
	 * 导出全部排班数据
	 * @param dutyDay
	 * @return
	 */
	public List<WorkDutyAllExport> queryWorkDutyExportData(String dutyDay);

	/**
	 * 导出班次信息
	 * @param shiftsName
	 * @param shiftsStatus
	 * @return
	 */
	public List<ShiftsVo> exportShiftsData(String shiftsName, String shiftsStatus);

	/**
	 * 班组人员树
	 * @return
	 */
	List<WorkTeamTreeEntity> treeList();
}
