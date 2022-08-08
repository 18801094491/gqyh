package com.zcdy.dsc.modules.operation.work.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.work.param.WorkInspectionPlanPageParam;
import com.zcdy.dsc.modules.operation.work.vo.WorkInspectionPlanVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkPlanDropdown;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @description: 巡检计划
 * @author: songguang.jiao
 * @date:   2020-07-06
 * @version: V1.0
 */
public interface WorkInspectionPlanMapper extends BaseMapper<WorkInspectionPlan> {

    /**
     * 分页查询
     * @param page 分页参数
     * @param param 查询参数
     * @return
     */
    IPage<WorkInspectionPlanVo> slectPageData(Page<WorkInspectionPlanVo> page,@Param("param") WorkInspectionPlanPageParam param);

    /**
     * 更改数据状态
     * @param id 主键id
     */
    @Update("UPDATE work_inspection_plan t SET t.plan_status =(CASE t.plan_status when 1 then 0 ELSE 1 end) where t.id=#{id}")
    void editStatus(String id);

    /**
     * 下拉选
     * @return
     */
    @Select(" SELECT t.id, t.plan_name planName FROM work_inspection_plan t where t.del_flag='0' ")
    List<WorkPlanDropdown> dropdown();

    /**
     * 通过id查询计划详细
     * @param planId 计划id
     * @return
     */
    WorkInspectionPlanVo selectByPlanId(@Param("planId") String planId);
}
