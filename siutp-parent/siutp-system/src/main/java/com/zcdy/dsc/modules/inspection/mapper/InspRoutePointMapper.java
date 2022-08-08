package com.zcdy.dsc.modules.inspection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.inspection.entity.InspRoutePoint;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 巡检路线与巡检点关系
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface InspRoutePointMapper extends BaseMapper<InspRoutePoint> {

    @Update({"update work_list_inspection_route_points " +
            "set del_flag = #{delFlag}, update_by = #{updateBy}, update_time = #{updateTime} " +
            "where route_id = #{routeId}"})
    int delRoutePointsByRouteId(InspRoutePoint irp);
}
