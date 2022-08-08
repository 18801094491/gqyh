package com.zcdy.dsc.modules.operation.work.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.work.entity.WorkTeamDuty;
import com.zcdy.dsc.modules.operation.work.entity.WorkTeamMember;
import com.zcdy.dsc.modules.operation.work.entity.WorkTeamTreeEntity;
import com.zcdy.dsc.modules.operation.work.vo.*;
import com.zcdy.dsc.modules.operation.work.vo.WorkTeamDutyVo.ShiftsTeamInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 描述: 工作dao层
 * @author：  songguang.jiao
 * 创建时间：  2020年1月10日 下午3:25:46
 * 版本号: V1.0
 */
public interface WorkDao {


	/**
	 * 分页查询班次列表
	 * @param page
	 * @param shiftsName
	 * @param shiftsStatus
	 * @return
	 */
	@Select({
			" <script>",
			" select",
			" t.id,",
			" t.shifts_name shiftsName,",
			" t.shifts_describe shiftsDescribe,",
			" t.start_time startTime,",
			" t.over_time overTime,",
			" t.work_hours workHours,",
			" t.rest_start_time restStartTime,",
			" t.rest_over_time restOverTime,",
			" t.shifts_status shiftsStatus ",
			" FROM",
			" work_shifts t ",
			" WHERE",
			" t.del_flag = '0' ",
			" <if test='shiftsName!=null and shiftsName!=\"\"'>",
			"  and t.shifts_name like concat('%',#{shiftsName},'%')",
			" </if>",
			" <if test='shiftsStatus!=null and shiftsStatus!=\"\"'>",
			" and t.shifts_status=#{shiftsStatus} ",
			" </if>",
			" ORDER BY",
			" t.create_time DESC",
			" </script>",
	})
	public IPage<ShiftsVo> queryShiftsList(IPage<ShiftsVo> page,@Param("shiftsName")String shiftsName,@Param("shiftsStatus")String shiftsStatus);


	/**
	 * 查询班次详情
	 * @param id
	 * @return
	 */
	@Select({
			" select",
			" t.id,",
			" t.shifts_name shiftsName,",
			" t.shifts_describe shiftsDescribe,",
			" t.start_time startTime,",
			" t.over_time overTime,",
			" t.work_hours workHours,",
			" t.rest_start_time restStartTime,",
			" t.rest_over_time restOverTime,",
			" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = t.shifts_status ) shiftsStatus, ",
			" (case t.off_sign when 1 then '是' else '否' end) offSign",
			" FROM",
			" work_shifts t ",
			" WHERE",
			" t.id = #{id} ",
	})
	public ShiftsVo queryDetailById(String id);

	/**
	 * 更改班次状态
	 * @param id
	 */
	@Update({
			" update work_shifts set shifts_status=(case shifts_status when 0 then 1 when 1 then 0 else shifts_status end) where id=#{id}"
	})
	public void editShiftStatus(@Param("id")String id);


	/**
	 * 获取最后一个团队sn
	 * @return
	 */
	@Select({
			" SELECT max(t.team_sn) from work_third_team t",
	})
	public String getLastThirdTeamSn();

	/**
	 * 页查询第三方团队
	 * @param page
	 * @param teamName
	 * @param teamSn
	 * @return
	 */
	@Select({
			" <script>",
			" SELECT",
			" t.id,",
			" t.team_sn teamSn,",
			" t.team_name teamName,",
			" t.agree_start agreeStart,",
			" t.agree_end agreeEnd,",
			" t.contact_name contactName,",
			" t.contact_phone contactPhone,",
			" cat.NAME contactPosition,",
			" t.contact_position contactPositionCode,",
			" t.team_rating teamRatingCode,",
			" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'team_rate' ) AND item_value = t.team_rating ) teamRating, ",
			" t.file_url fileUrl,",
			" t.file_name fileName",
			" FROM",
			" work_third_team t",
			" LEFT JOIN sys_category cat ON cat. CODE = t.contact_position",
			" where t.del_flag='0'",
			" <if test='teamName!=null and teamName!=\"\"'>",
			" and t.team_name like concat('%',#{teamName},'%')",
			" </if>",
			" <if test='teamSn!=null and teamSn!=\"\"'>",
			" and t.team_sn like concat('%',#{teamSn},'%')",
			" </if>",
			" order by t.create_time desc",
			" </script>",
	})
	public IPage<ThirdTeamVo> queryThirdPageData(Page<ThirdTeamVo> page,@Param("teamName") String teamName,@Param("teamSn") String teamSn);

	/**
	 * 班组信息表-分页列表查询
	 * @param page
	 * @param teamName
	 * @param teamStatus
	 * @return
	 */
	@Select({
			" <script>",
			" SELECT ",
			" t.id id,",
			" t.team_name teamName,",
			" t.team_describe teamDescribe,",
			" t3.id shiftsId,",
			" t3.shifts_name shiftsName,",
			" t.start_time startTime,",
			" t.over_time overTime,",
			" ( SELECT GROUP_CONCAT( user_id ) FROM work_team_member WHERE t.id = team_id AND member_type = '0' ) userIds,",
			" ( SELECT GROUP_CONCAT( tu.realname ) FROM work_team_member tm LEFT JOIN sys_user tu ON tm.user_id = tu.id WHERE tm.member_type = '0' AND t.id = tm.team_id ) userNames,",
			" ( SELECT GROUP_CONCAT( user_id ) FROM work_team_member WHERE t.id = team_id AND member_type = '1' ) userManageIds,",
			" ( SELECT GROUP_CONCAT( tu.realname ) FROM work_team_member tm LEFT JOIN sys_user tu ON tm.user_id = tu.id WHERE tm.member_type = '1' AND t.id = tm.team_id ) userManageNames,",
			" t.team_status teamStatusCode,",
			" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = t.team_status ) teamStatusName ",
			" FROM",
			" work_team t",
			" LEFT JOIN work_shifts_team t2 ON t2.team_id = t.id",
			" LEFT JOIN work_shifts t3 ON t3.id = t2.shifts_id",
			" where t.del_flag='0'",
			" <if test='teamName!=null and teamName!=\"\"'>",
			" and t.team_name like concat('%',#{teamName},'%')",
			" </if>",
			" <if test='teamStatus!=null and teamStatus!=\"\"'>",
			" and t.team_status=#{teamStatus}",
			" </if>",
			" order by t.create_time",
			" </script>",
	})
	public IPage<WorkTeamVo> queryTeamPageData(Page<WorkTeamVo> page,@Param("teamName") String teamName,@Param("teamStatus") String teamStatus);


	/**
	 * 批量插入组员组长
	 * @param list
	 */
	@Insert({
			" <script>",
			" INSERT INTO `work_team_member` ( `id`, `team_id`, `user_id`, `member_type`, `create_by`, `create_time`, `del_flag` )",
			" VALUES",
			" <foreach collection='list' item='item' index='index' separator=','>",
			" (",
			" #{item.id},",
			" #{item.teamId},",
			" #{item.userId},",
			" #{item.memberType},",
			" #{item.createBy},",
			" #{item.createTime},",
			" #{item.delFlag}",
			" )",
			" </foreach>",
			" </script>",
	})
	public void insertTeamMember(@Param("list")List<WorkTeamMember> list);


	/**
	 * 批量删除现有组员
	 * @param teamId
	 */
	@Delete({" delete  from work_team_member where team_id=#{teamId}"})
	public void deleteTeamMember(@Param("teamId")String teamId);

	/**
	 * 导出班组成员
	 * @param teamName
	 * @param teamStatus
	 * @return
	 */
	@Select({
			" <script>",
			" SELECT ",
			" t.team_name teamName,",
			" t.team_describe teamDescribe,",
			" t3.shifts_name shiftsName,",
			" t.start_time startTime,",
			" t.over_time overTime,",
			" ( SELECT GROUP_CONCAT( tu.realname ) FROM work_team_member tm LEFT JOIN sys_user tu ON tm.user_id = tu.id WHERE tm.member_type = '0' AND t.id = tm.team_id ) userNames,",
			" ( SELECT GROUP_CONCAT( tu.realname ) FROM work_team_member tm LEFT JOIN sys_user tu ON tm.user_id = tu.id WHERE tm.member_type = '1' AND t.id = tm.team_id ) userManageNames,",
			" ( SELECT item_text FROM sys_dict_item WHERE dict_id = ( SELECT id FROM sys_dict WHERE dict_code = 'working_status' ) AND item_value = t.team_status ) teamStatusName ",
			" FROM",
			" work_team t",
			" LEFT JOIN work_shifts_team t2 ON t2.team_id = t.id",
			" LEFT JOIN work_shifts t3 ON t3.id = t2.shifts_id",
			" <if test='teamName!=null and teamName!=\"\"'>",
			" and t.team_name like concat('%',#{teamName},'%')",
			" </if>",
			" <if test='teamStatus!=null and teamStatus!=\"\"'>",
			" and t.team_status=#{teamStatus}",
			" </if>",
			" order by t.create_time",
			" </script>",
	})
	public List<WorkTeamVo> exportTeamData(@Param("teamName")String teamName,@Param("teamStatus") String teamStatus);

	/**
	 * 班组状态-启停用
	 * @param id
	 */
	@Update({
			" update work_team set team_status=(case team_status when 0 then 1 when 1 then 0 else team_status end) where id=#{id}"
	})
	public void editTeamStatus(@Param("id")String id);

	/**
	 * 删除班组班次关联表
	 * @param teamId
	 */
	@Delete({" delete  from work_shifts_team where team_id=#{teamId}"})
	public void deleteShiftTeam(String teamId);

	/**
	 * 判断班组班次是否存在关联关系
	 * @param teamId
	 * @param shiftsId
	 * @return
	 */
	@Select({"select count(id) from work_shifts_team where team_id=#{teamId} and shifts_id=#{shiftsId}"})
	public Integer checkShiftTeamExist(@Param("teamId")String teamId,@Param("shiftsId") String shiftsId);

	/**
	 * 班次下拉列表(id-value)
	 * @return
	 */
	@Select({
			"SELECT t.id id,t.shifts_name shiftsName from work_shifts t where t.del_flag='0' and t.shifts_status='1'"
	})
	public List<ShiftsDropdown> queryShiftsName();

	/**
	 * 批量插入排版表
	 * @param list
	 */
	@Insert({
			" <script>",
			" INSERT INTO `work_team_duty` ( `id`, `shifts_id`, `team_id`, `duty_day`, `start_time`, `over_time`, `create_by`, `create_time`, `del_flag` )",
			" VALUES",
			" <foreach collection='list' item='item' index='index' separator=','>",
			" (",
			" #{item.id},",
			" #{item.shiftsId},",
			" #{item.teamId},",
			" #{item.dutyDay},",
			" #{item.startTime},",
			" #{item.overTime},",
			" #{item.createBy},",
			" #{item.createTime},",
			" #{item.delFlag}",
			" )",
			" </foreach>",
			" </script>",
	})
	public void addWorkTeamDuty(@Param("list")List<WorkTeamDuty> list);

	/**
	 * 班组排班列表 按照月查询,查询3个月数据,方便前台展示
	 * @param lastMonth
	 * @param nextMonth
	 * @return
	 */
	@Results(id="workDutyDataMonth",
			value={
					@Result(property="dutyDay",column="duty_day"),
					@Result(property="list",javaType=List.class,column="duty_day",many=@Many(select="getDutyDaylist"))
			})
	@Select({
			"  SELECT duty_day FROM work_team_duty where DATE_FORMAT(duty_day,'%Y-%m')>=#{lastMonth} and DATE_FORMAT(duty_day,'%Y-%m') <=#{nextMonth} GROUP BY duty_day",
	})
	public List<WorkTeamDutyVo> queryWorkDutyDataMonth(@Param("lastMonth")String lastMonth,@Param("nextMonth")String nextMonth);


	/**
	 * 每日排班集合
	 * @param dutyDay
	 * @return
	 */
	@Select({
			" SELECT",
			" t2.shifts_name,",
			" t1.team_name",
			" FROM",
			" work_team_duty t",
			" LEFT JOIN work_team t1 ON t1.id = t.team_id",
			" LEFT JOIN work_shifts t2 ON t2.id = t.shifts_id",
			" where t.duty_day=#{dutyDay}",
	})
	public List<ShiftsTeamInfo> getDutyDaylist(@Param("dutyDay")String dutyDay);


	/**
	 * 查询班组下拉列表
	 * @return
	 */
	@Select("SELECT t.id id,t.team_name teamName from work_team t where t.del_flag='0' and t.team_status='1'")
	public List<TeamDropdown> queryTeamNameList();

	/**
	 * 通过班组id查询班次id
	 * @param teamId
	 * @return
	 */
	@Select(" SELECT id,shifts_name shiftsName from work_shifts  where id= (SELECT shifts_id  from work_shifts_team where team_id=#{teamId})")
	public ShiftsDropdown queryShiftsByTeamId(@Param("teamId") String teamId);

	/**
	 * 当日详细排班情况
	 * @param dutyDay
	 * @return
	 */
	@Select({
			" SELECT",
			" t.id,",
			" ( SELECT team_name FROM work_team WHERE id = t.team_id ) teamName,",
			" ( SELECT shifts_name FROM work_shifts WHERE id = t.shifts_id ) shiftsName ",
			" FROM",
			" work_team_duty t",
			" where DATE_FORMAT(duty_day,'%Y-%m-%d') =#{dutyDay}"
	})
	public List<WorkTeamDutyOneDayVo> queryWorkDutyOneDay(String dutyDay);


	/**
	 * 当前用户的排班列表 按照月查询
	 * @param lastMonth
	 * @param nextMonth
	 * @param id
	 * @return
	 */
	@Select({
			" <script>",
			"SELECT ",
			" ta.duty_day dutyDay,",
			" t.team_name teamName, s.shifts_name shiftsName, ",
			" ta.start_time  startTime,",
			" ta.over_time  overTime ",
			" FROM ",
			" work_team_duty ta ",
			" left join work_team t on ta.team_id = t.id ",
			" left join work_shifts s on ta.shifts_id = s.id ",
			" WHERE",
			" ta.team_id in (",
			"SELECT ",
			" a.team_id teamId",
			" FROM",
			"work_team_member a ",
			"WHERE ",
			"a.user_id =#{id}) ",
			"AND DATE_FORMAT(duty_day,'%Y-%m')&gt;=#{lastMonth}" ,
			" AND DATE_FORMAT(duty_day,'%Y-%m')&lt;=#{nextMonth}",
			" order BY ",
			"ta.duty_day  ",
			" </script>",
	})
	List<WorkMyTeamDutyVo> queryMyWorkDutyDataMonth(@Param("lastMonth")String lastMonth,@Param("nextMonth")String nextMonth, @Param("id") String id);

	/**
	 * 导出全部排班数据
	 * @param dutyDay
	 * @return
	 */
	@Select({
			" SELECT",
			" t5.duty_day dutyDay,",
			" t4.shifts_name shiftsName,",
			" t4.team_name teamName",
			" FROM",
			" ( SELECT duty_day FROM work_team_duty where DATE_FORMAT(duty_day,'%Y-%m')=#{dutyDay} GROUP BY duty_day ORDER BY duty_day desc) t5",
			" LEFT JOIN ( SELECT t.duty_day, t.id, t2.shifts_name, t1.team_name FROM work_team_duty t LEFT JOIN work_team t1 ON t1.id = t.team_id ",
			" LEFT JOIN work_shifts t2 ON t2.id = t.shifts_id ) t4 ON t4.duty_day = t5.duty_day",
	})
	public List<WorkDutyAllExport> queryWorkDutyExportData(@Param("dutyDay")String dutyDay);

	/**
	 * 导出所有班次信息
	 * @param shiftsName
	 * @param shiftsStatus
	 * @return
	 */
	@Select({
			" <script>",
			" select",
			" t.shifts_name shiftsName,",
			" t.shifts_describe shiftsDescribe,",
			" t.start_time startTime,",
			" t.over_time overTime,",
			" t.work_hours workHours,",
			" t.rest_start_time restStartTime,",
			" t.rest_over_time restOverTime",
			" FROM",
			" work_shifts t ",
			" WHERE",
			" t.del_flag = '0' ",
			" <if test='shiftsName!=null and shiftsName!=\"\"'>",
			"  and t.shifts_name like concat('%',#{shiftsName},'%')",
			" </if>",
			" <if test='shiftsStatus!=null and shiftsStatus!=\"\"'>",
			" and t.shifts_status=#{shiftsStatus} ",
			" </if>",
			" ORDER BY",
			" t.create_time DESC",
			" </script>",
	})
	public List<ShiftsVo> exportShiftsData(@Param("shiftsName")String shiftsName,@Param("shiftsStatus") String shiftsStatus);

	/**
	 *  班组人员关系 人员去重查询
	 * @return
	 */
	@Select({
			" SELECT",
			" DISTINCT(t2.user_id) userId,",
			" t.id teamId,",
			" t.team_name teamName,",
			" t3.realname userName",
			" FROM",
			" work_team t",
			" LEFT JOIN work_team_member t2 ON t2.team_id = t.id",
			" LEFT JOIN sys_user t3 ON t3.id = t2.user_id",
			" where t.del_flag='0' and t.team_status='1'",
	})
    List<WorkTeamTreeEntity> treeList();
}
