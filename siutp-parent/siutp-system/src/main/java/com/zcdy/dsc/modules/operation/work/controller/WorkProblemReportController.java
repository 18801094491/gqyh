package com.zcdy.dsc.modules.operation.work.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.alarm.constant.WarnStatusConstant;
import com.zcdy.dsc.modules.operation.work.entity.WorkProblemReport;
import com.zcdy.dsc.modules.operation.work.param.ProblemReportPageParam;
import com.zcdy.dsc.modules.operation.work.service.WorkProblemReportService;
import com.zcdy.dsc.modules.operation.work.vo.ProblemReportVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description: 问题上报管理
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-06-04 16:37:04
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "问题上报管理")
@RestController
@RequestMapping("/work/workProblemReport")
public class WorkProblemReportController extends BaseController<WorkProblemReport, WorkProblemReportService> {
    @Resource
    private WorkProblemReportService workProblemReportService;

    /**
     * 分页列表查询
     *
     * @return
     */
    @AutoLog(value = "问题上报管理-分页列表查询")
    @ApiOperation(value = "问题上报管理-分页列表查询", notes = "问题上报管理-分页列表查询")
    @GetMapping
    public Result<IPage<ProblemReportVo>> queryPageList(ProblemReportPageParam param) {
        Result<IPage<ProblemReportVo>> result = new Result<>();
        Page<ProblemReportVo> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<ProblemReportVo> data = workProblemReportService.selectPageData(page, param);
        result.setResult(data);
        return result.success();
    }

    /**
     * 添加或修改
     *
     * @param workProblemReport
     * @return
     */
    @AutoLog(value = "问题上报管理-添加或修改")
    @ApiOperation(value = "问题上报管理-添加或修改", notes = "问题上报管理-添加或修改")
    @PostMapping(value = "one")
    public Result<Object> add(@RequestBody WorkProblemReport workProblemReport) {
        workProblemReportService.saveProblem(workProblemReport);
        return Result.ok("保存成功！");
    }


    /**
     * 关闭问题
     * @param id
     * @return
     */
    @AutoLog("关闭问题")
    @ApiOperation("关闭问题")
    @GetMapping("/close/{id}")
    public Result<Object> close(@PathVariable("id") String id){
        workProblemReportService.updateById(new WorkProblemReport().setId(id).setProblemStatus(WarnStatusConstant.CLOSED));
        return Result.ok("问题已关闭");
    }

}