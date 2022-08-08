package com.zcdy.dsc.modules.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.inspection.entity.InspRoute;
import com.zcdy.dsc.modules.inspection.entity.InspRoutePoint;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Description: 巡检路线
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface InspRouteMapper extends BaseMapper<InspRoute> {

    @Insert({
            "<script>" +
                "insert into work_list_inspection_route_points\n" +
                "(id, route_id, point_id, seq,\n" +
                "create_by, create_time)\n" +
                "values\n" +
                "<foreach item='item' index='index' collection='list' separator=','>\n" +
                "(#{item.id}, #{item.routeId}, #{item.pointId}, #{item.seq},\n" +
                "#{item.createBy}, #{item.createTime})\n" +
                "</foreach>" +
            "</script>"
    })
    int insertRoutePointList(List<InspRoutePoint> list);

    @Update("update work_list_inspection_route_points\n" +
            "set del_flag = #{delFlag}, update_by = #{updateBy},\n" +
                "update_time = #{updateTime}\n" +
            "where route_id = #{routeId}")
    int delRoutePoint(InspRoutePoint irp);

    @Update("<script>" +
                "update work_list_inspection_route_points\n" +
                "set del_flag = #{delFlag}, update_by = #{updateBy},\n" +
                "update_time = #{updateTime}\n" +
                "where route_id in\n" +
                "<foreach collection='ids' item='item' open='(' close=')' separator=','>\n" +
                    "#{item}\n" +
                "</foreach>" +
            "</script>")
    int delRoutePoints(InspRoutePoint irp);

    @Select({"<script>" +
            "select r.id, r.name, r.code, \n" +
                "r.area_id, a.name areaName, r.description\n" +
            "from work_list_inspection_route r\n" +
                "left join work_list_inspection_area a on a.id = r.area_id\n" +
            "where r.del_flag = #{query.delFlag} " +
            "<if test='query.name != null and query.name != \"\"'>",
                "and r.name like concat('%',#{query.name},'%') ",
            "</if>" +
            "<if test='query.code != null and query.code != \"\"'>",
                "and r.code like concat('%',#{query.code},'%') ",
            "</if>" +
            "<if test='query.areaId != null and query.areaId != \"\"'>",
            "and r.area_id  = #{query.areaId} ",
            "</if>" +
            "order by r.create_time desc" +
            "</script>"})
    IPage<InspRoute> selectPageData(IPage<InspRoute> page, InspRoute query);

    /**
     * 通过id获取路线及其包含巡检点
     * @param id
     * @return
     */
    InspRoute getOneById(String id);
}
