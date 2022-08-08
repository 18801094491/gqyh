package com.zcdy.dsc.modules.operation.work.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.work.entity.WorkProblemReport;
import com.zcdy.dsc.modules.operation.work.param.ProblemReportPageParam;
import com.zcdy.dsc.modules.operation.work.vo.ProblemReportVo;

/**
 * @Description: 问题上报管理
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-06-04
 * @Version: V1.0
 */
public interface WorkProblemReportService extends IService<WorkProblemReport> {

    /**
     * 保存上报问题
     * @param workProblemReport
     */
    void saveProblem(WorkProblemReport workProblemReport);

    /**
     * 问题上报管理-分页列表查询
     * @param page
     * @param param
     * @return
     */
    IPage<ProblemReportVo> selectPageData(Page<ProblemReportVo> page, ProblemReportPageParam param);

}
