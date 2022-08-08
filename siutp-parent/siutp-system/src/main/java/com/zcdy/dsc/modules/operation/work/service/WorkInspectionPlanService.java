package com.zcdy.dsc.modules.operation.work.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionPlan;
import com.zcdy.dsc.modules.operation.work.param.WorkInspectionPlanPageParam;
import com.zcdy.dsc.modules.operation.work.param.WorkInspectionPlanParam;
import com.zcdy.dsc.modules.operation.work.vo.WorkInspectionPlanVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkPlanDropdown;

import java.util.List;

/**
 * @description: 巡检计划
 * @author: songgguang.jiao
 * @date:   2020-07-06
 */
public interface WorkInspectionPlanService extends IService<WorkInspectionPlan> {

    /**
     * 新增，修改巡检计划,同时生成巡检表，巡检人表，工单表
     * @param workInspectionPlanParam 计划内容
     */
    void saveOne(WorkInspectionPlanParam workInspectionPlanParam);

    /**
     * 巡检计划-分页列表查询
     * @param page 分页参数
     * @param param 入参
     * @return
     */
    IPage<WorkInspectionPlanVo> pageData(Page<WorkInspectionPlanVo> page, WorkInspectionPlanPageParam param);

    /**
     * 更改数据状态
     * @param id
     */
    void editStatus(String id);

    /**
     * 计划下拉列表
     * @return
     */
    List<WorkPlanDropdown> dropdown();
}
