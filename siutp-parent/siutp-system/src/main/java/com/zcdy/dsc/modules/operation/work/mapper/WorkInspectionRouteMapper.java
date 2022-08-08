package com.zcdy.dsc.modules.operation.work.mapper;

import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionRoute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.work.vo.WorkPointDropdown;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description: 巡检路线
 * @author: songguang.jiao
 * @date:   2020-07-01
 * @version: V1.0
 */
public interface WorkInspectionRouteMapper extends BaseMapper<WorkInspectionRoute> {

    /**
     * 巡检线路已选中下拉列表
     * @param routId id
     * @return
     */
    List<WorkPointDropdown> pointList(@Param("routId") String routId);

    /**
     * 巡检路径下拉选
     * @return
     */
    @Select(" select t.id,t.name from work_inspection_route t where t.del_flag='0'")
    List<WorkPointDropdown> dropdown();
}
