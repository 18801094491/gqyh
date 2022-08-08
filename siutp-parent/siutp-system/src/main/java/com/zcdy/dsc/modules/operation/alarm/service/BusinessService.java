package com.zcdy.dsc.modules.operation.alarm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.operation.alarm.entity.BusinessWarn;
import com.zcdy.dsc.modules.operation.alarm.entity.VariInfo;
import com.zcdy.dsc.modules.operation.alarm.entity.WeekData;
import com.zcdy.dsc.modules.operation.alarm.param.BusinessWarnParam;
import com.zcdy.dsc.modules.operation.alarm.vo.*;
import com.zcdy.dsc.modules.operation.work.vo.TeamDropdown;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 描述:报警相关自定义接口 
 * @author：  songguang.jiao
 * 创建时间： 2020年2月18日 上午11:05:11 
 * 版本号: V1.0
 */
public interface BusinessService extends IService<BusinessWarn> {

	/**
	 * 分页查询报警数据 排序告警等级跟时间
	 * @param page 分页参数
	 * @param warnName 告警名称
	 * @param warnLevel 告警等级
	 * @param warnType 告警类型
	 * @param warnStatus 状态
	 * @param equipmentType 设备类型
	 * @param startTime 开始时间
	 * @param endTime 截至时间
	 * @return
	 */
	IPage<BusinessWarnVo> queryWarnData(Page<BusinessWarnVo> page, String warnName, String warnLevel, String warnType,
			String warnStatus,String equipmentType,String startTime,String endTime);

	/**
	 * 关闭报警消息
	 * @param business 参数
	 */
	void closeData(BusinessDeal business);

	/**
	 * 批量插入报警新表
	 * @param warnList 告警列表
	 */
	void addWarnInfo(List<BusinessWarn> warnList);

	/**
	 * 描述: 查询所有变量名称
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月20日 下午4:33:29 
	 * 版本号: V1.0
	 */
	Map<String, VariInfo> getAllVarinfo();

	/**
	 * 校验报警信息是否存在
	 * @param warnName 告警名称
	 * @return
	 */
	boolean checkWarnExist(String warnName);

	/**
	 * 通过名称查询未处理的当天报警id
	 * @param warnName 告警名称
	 * @return
	 */
	String getWarnId(String warnName);

	/**
	 *  批量更新报警数据
	 * @param warnList 告警列表
	 */
	void updateWarnInfo(List<BusinessWarn> warnList);

	/**
	 * 确认按钮
	 * @param id 主键id
	 * @param warnStatus 告警状态
	 */
	void confirmData(String id, String warnStatus);

	/**
	 * 查询未处理的数据
	 * @param confirmStatus 确认状态
	 * @param dataStaus 数据状态
	 * @return
	 */
	List<BusinessWarnVo> queryUndealData(String confirmStatus,String dataStaus);

	/**
	 * 获取告警数量
	 * @return
	 */
	List<BusinessWarnVo> queryWarnNum();

	/**
	 * 查询未处理的数据
	 * @param confirmStatus 确认状态
	 * @param dataStaus 数据状态
	 * @return
	 */
	List<BusinessWarnVo> queryUndealDataNew(String confirmStatus,String dataStaus);

	/**
	 * 近7天报警数据
	 * @return
	 */
	List<WeekData> getSevenDayData();

	/**
	 * 按照月份或者年份查询报警数据
	 * @param type  时间类型
	 * @return
	 */
	List<MonthYearData> queryMonthYearData(String type);

	/**
	 * 告警通知策略新增
	 * @param vo 入参
	 */
	void addPolicy(PolicyParamVo vo);

	/**
	 * 查询所有角色用户
	 * @param roleName 角色名称
	 * @return
	 */
	List<RoleNameVo> getRoleUsers(String roleName);

	/**
	 * 告警策略已关联用户
	 * @param policyId 通知id
	 * @return
	 */
	List<String> queryUserIds(String policyId);

	/**
	 * 查询所有班组用户
	 * @param name 用户名称
	 * @return
	 */
	List<TeamDropdown> getWorkUsers(String name);


	/**
	 * 校验策略等级是否重复
	 * @param vo 入参
	 * @return
	 */
	boolean checkPolicyExist(PolicyParamVo vo);

	/**
	 * 修改告警通知
	 * @param vo
	 */
	void editPolicy(PolicyParamVo vo);

	/**
	 * 分页查询告警策略
	 * @param page 分页参数
	 * @param name 名称
	 * @return
	 */
	IPage<PolicyListVo> queryPolicyPageData(Page<PolicyListVo> page, String name);

	/**
	 * 通知策略详情
	 * @param id 主键id
	 * @return
	 */
	PolicyDetailVo getDetail(String id);

	/**
	 * 删除通知策略
	 * @param id 主键id
	 */
	void delete(String id);

	/**
	 * 更改启停用状态
	 * @param id 主键id
	 */
	void changeStatus(String id);

	/**
	 * 查询模板下拉选列表
	 * @param templateName 模板名称
	 * @return
	 */
	List<SmsTemplateVo> queryTemplate(String templateName);

	/**
	 * 根据通知策略发送告警消息给用户
	 * @param businssWarnId 告警id
	 * @return
	 */
	boolean sendWarnMsg(String businssWarnId);

	/**
	 * 查询当天报警数据(饼状图,未处理,已处理,已关闭)
	 * @return
	 */
	List<PolicyOneDayVo> oneDayData();

	/**
	 * 地图界面查询所有图标列表
	 * @return
	 */
	List<IconListVo> queryAllIcon();

	/**
	 * 地图界面查询所有图标列表
	 * @param business 入参
	 */
	void dealData(BusinessDeal business);

	/**
	 * 通过id查询列表
	 * @param id 主键id
	 * @return
	 */
    List<BusinessWarnVo> getBusinessWarnVoById(String id);

	/**
	 * 总记录数
	 * @return
	 */
	int getCount();

	/**
	 * 查询列表
	 * @param startIndex 起始页
	 * @param pageSize 每页大小
	 * @return
	 */
    List<AlarmResultVO> queryAlarmFullDataList(long startIndex, int pageSize);

	/**
	 * 告警列表数据
	 * @param pageinfo 分页参数
	 * @param param 入参
	 * @param equipType 设备类型
	 * @return
	 */
    Page<AlarmResultVO> queryAlarmFullDataPage(Page<AlarmResultVO> pageinfo, String param,String equipType);

	/**
	 * 告警详情
	 * @param bid 主键id
	 * @return
	 */
	AlarmResultDetailVO getAlarmDataDetail(String bid);

	/**
	 * 查询Ioserver变量信息列表
	 * @param iotId 采集id
	 * @return
	 */
	List<VariableInfo> getVarsByIotId(String iotId);

	/**
	 * 今日各个级别告警统计
	 * @return
	 */
	List<PolicyOneDayVo> getWarnLevelInfoToday();

	/**
	 * 分页查询告警事件列表
	 * @param page 分页对象
	 * @param param 列表参数
	 * @return
	 */
	IPage<BusinessWarnVo> getAppBusinessList(Page<BusinessWarnVo> page, BusinessWarnParam param);

	/**
	 * 查询告警事件列表详情
	 * @param id 告警事件id
	 * @return
	 */
	BusinessWarnVo getAppBusinessDetail(String id);

	/**
	 * 查询全部告警事件列表
	 * @param param 列表参数
	 * @return
	 */
	List<BusinessWarnVo> getAppBusinessList(BusinessWarnParam param);

}
