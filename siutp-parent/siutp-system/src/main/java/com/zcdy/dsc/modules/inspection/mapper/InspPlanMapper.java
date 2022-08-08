package com.zcdy.dsc.modules.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.inspection.entity.InspPlan;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Description: 巡检计划
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface InspPlanMapper extends BaseMapper<InspPlan> {

    @Select({"<script>" +
            "select p.id, p.name, p.code, \n" +
                "p.area_id, a.name areaName,\n" +
                "p.route_id, r.name routeName,\n" +
                "p.team_id, t.team_name teamName,\n" +
                "p.start_date, p.over_date,\n" +
                "p.frequency_type, p.frequency_desc, p.status,\n" +
                "(select di.item_text\n" +
                "from sys_dict d \n" +
                    "left join sys_dict_item di on d.id = di.dict_id\n" +
                "where d.dict_code = #{query.queryTypeCode} and p.frequency_type = di.item_value) typeDes, \n" +
                "(select di.item_text\n" +
                "from sys_dict d \n" +
                    "left join sys_dict_item di on d.id = di.dict_id\n" +
                "where d.dict_code = #{query.queryStatusCode} and p.status = di.item_value) statusDes\n" +
            "from work_list_inspection_plan p\n" +
                "left join work_list_inspection_area a on a.id = p.area_id\n" +
                "left join work_list_inspection_route r on r.id = p.route_id\n" +
                "left join work_team t on t.id = p.team_id\n" +
            "where p.del_flag = #{query.delFlag}" +
            "<if test='query.name != null and query.name != \"\"'>",
            "and p.name like concat('%',#{query.name},'%') ",
            "</if>" +
            "<if test='query.code != null and query.code != \"\"'>",
            "and p.code like concat('%',#{query.code},'%') ",
            "</if>" +
            "<if test='query.areaId != null and query.areaId != \"\"'>",
            "and p.area_id  = #{query.areaId} ",
            "</if>" +
            "<if test='query.routeId != null and query.routeId != \"\"'>",
            "and p.route_id  = #{query.routeId} ",
            "</if>" +
            "order by p.create_time desc" +
            "</script>"})
    IPage<InspPlan> selectPageDate(IPage<InspPlan> page, InspPlan query);

    @Select({"select p.id, p.name, p.code, \n" +
                "p.area_id, a.name areaName,\n" +
                "p.route_id, r.name routeName,\n" +
                "p.team_id, t.team_name teamName,\n" +
                "p.start_date, p.over_date, p.team_leader_id, \n" +
                "p.frequency_type, p.frequency_desc, p.status,\n" +
                "(select di.item_text \n" +
                    "from sys_dict d \n" +
                        "left join sys_dict_item di on d.id = di.dict_id\n" +
                    "where d.dict_code = #{queryTypeCode} and p.frequency_type = di.item_value) typeDes\n" +
            "from work_list_inspection_plan p\n" +
                "left join work_list_inspection_area a on a.id = p.area_id\n" +
                "left join work_list_inspection_route r on r.id = p.route_id\n" +
                "left join work_team t on t.id = p.team_id\n" +
            "where p.del_flag = #{delFlag} and p.id = #{id}"})
    InspPlan getOneById(InspPlan plan);

    /**
     * 获取巡检计划和与其对应的巡检任务列表
     * @return
     */
    List<InspPlan> getInspPlanAndPointAndWorkListAnd(InspPlan plan);
}
