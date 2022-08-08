package com.zcdy.dsc.modules.worklist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: 工单信息
 * @Author: 在信汇通
 * @Date: 2021-01-15
 * @Version: V1.0
 */
public interface WorkListMapper extends BaseMapper<WorkList> {

    /**
     * 分页查询
     *
     * @param page
     * @param query
     * @return
     */
    @Select({"<script>" +
            "select l.id, l.name, l.code, t.team_name teamName, l.type, " +
            "u.realname leaderName, l.start_date, l.over_date,\n" +
            "(select di.item_text \n" +
            "from sys_dict d \n" +
            "left join sys_dict_item di on d.id = di.dict_id \n" +
            "where d.dict_code = #{query.queryStatusCode} and l.status = di.item_value) statusDes\n" +
            "from work_list l\n" +
            "left join work_team t on t.id = l.team_id\n" +
            "left join sys_user u on u.id = l.team_leader_id\n" +
            "where l.del_flag = #{query.delFlag} and l.type = #{query.type} " +
            "<if test='query.name != null and query.name != \"\"'>" +
            "and l.name like concat('%',#{query.name},'%')  " +
            "</if>" +
            "<if test='query.startDate != null'>" +
            "and l.start_date &gt;= #{query.startDate} " +
            "</if>" +
            "<if test='query.overDate != null'>" +
            "and l.over_date &lt;= #{query.overDate} " +
            "</if>" +
            "<if test='query.status != null and query.status != \"\"'>" +
            "and l.status = #{query.status} " +
            "</if>" +
            "order by l.create_time desc, name desc" +
            "</script>"})
    IPage<WorkList> selectPageDate(IPage<WorkList> page, WorkList query);

    @Select({"<script>" +
            "select eq.id, eq.equipment_sn, eq.equipment_name, \n" +
            "(select name from sys_category where code = eq.equipment_mode) equipmentmodename,\n" +
            "cat1.name equipmenttype, cat2.name equipmentcategory,\n" +
            "eq.equipment_location equipmentLocation, \n" +
            "t2.next_plan_date planDate, gm.latitude, gm.longitude\n" +
            "from opt_equipment eq\n" +
            "left join opt_upkeep_plan t2 on eq.id = t2.opt_id\n" +
            "left join gis_equipment ge on ge.equipment_id = eq.id \n" +
            "left join gis_model gm on gm.id = ge.model_id\n" +
            "left join sys_category cat1 on cat1.code=eq.equipment_type\n" +
            "left join sys_category cat2 on cat2.code=eq.equipment_category\n" +
            "where eq.del_flag = '0' \n" +
            "<if test='startDate != null'>" +
            "and t2.next_plan_date &gt;= #{startDate} " +
            "</if>" +
            "<if test='overDate != null'>" +
            "and t2.next_plan_date &lt;= #{overDate} " +
            "</if>" +
            "<if test='name != null and name != \"\"'>" +
            "and eq.equipment_name like concat('%',#{name},'%')  " +
            "</if>" +
            "</script>"})
    List<OptEquipmentModel> getEquList(WorkList query);

    WorkList getOneById(WorkList query);

    List<WorkList> getTimeoutAndNoCompleteWorkList(WorkList query);

    @Select({"select l.id, l.status, l.start_date, l.over_date, " +
            "GROUP_CONCAT(distinct u.username, \"|\",u.realname) teamMemberUsername\n" +
            "from work_list l\n" +
            "left join work_team_member m on m.team_id = l.team_id\n" +
            "left join sys_user u on u.id = m.user_id\n" +
            "where l.id = #{id}\n" +
            "group by id"})
    WorkList getWorkListLocationById(String id);

    @Select("select count(1) " +
            "from work_list " +
            "where del_flag = " + StatusConstant.WORKING + " " +
            "and status = #{status}" +
            "and start_date >= #{startDate} " +
            "and over_date <= #{overDate} ")
    Integer getWorkListNumByStatus(WorkList workList);
//    Integer getWorkListNumByStatus(String status);

    @Select("select l.id, l.name, u.realname leaderName, " +
            "l.start_date startDate, l.over_date overDate, \n" +
            "(select di.item_text \n" +
            "from sys_dict d \n" +
            "left join sys_dict_item di on d.id = di.dict_id \n" +
            "where d.dict_code = #{queryStatusCode} and l.status = di.item_value) statusDes, " +
            "(select di.item_text\n" +
            "from sys_dict d\n" +
            "left join sys_dict_item di on d.id = di.dict_id\n" +
            "where d.dict_code = #{queryTypeCode} and l.type = di.item_value) typeDes \n" +
            "from work_list l \n" +
            "left join work_team t on t.id = l.team_id \n" +
            "left join sys_user u on u.id = l.team_leader_id \n" +
            "where l.del_flag = #{delFlag} " +
            "and l.start_date >= #{startDate} " +
            "and l.start_date <= #{overDate} ")
    List<WorkList> getWorkList7DayList(WorkList workList);

    /**
     * 获取工单和班组成员信息
     *
     * @param workList
     * @return
     */
    List<WorkList> getWorkListAndTeamMember(WorkList workList);

    /**
     * 获取工单信息+任务明细+成员明细
     *
     * @param workList
     * @return
     */
    List<WorkList> getWorkListMatterAndTeamMember(WorkList workList);

}
