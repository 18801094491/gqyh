package com.zcdy.dsc.modules.operation.work.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionPlan;
import com.zcdy.dsc.modules.operation.work.param.WorkInspectionPlanPageParam;
import com.zcdy.dsc.modules.operation.work.param.WorkInspectionPlanParam;
import com.zcdy.dsc.modules.operation.work.service.WorkInspectionPlanService;
import com.zcdy.dsc.modules.operation.work.vo.WorkInspectionPlanVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkPlanDropdown;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 巡检计划
 * @author: songguang.jiao
 * @date: 2020-07-06 16:45:52
 */
@Slf4j
@Api(tags = "工单巡检计划")
@RestController
@RequestMapping("/work/workInspectionPlan")
public class WorkInspectionPlanController extends BaseController<WorkInspectionPlan, WorkInspectionPlanService> {
    @Resource
    private WorkInspectionPlanService workInspectionPlanService;

    /**
     * 巡检计划-分页列表查询
     *
     * @param param 入参
     * @return
     */
    @AutoLog(value = "巡检计划-分页列表查询")
    @ApiOperation(value = "巡检计划-分页列表查询", notes = "巡检计划-分页列表查询")
    @GetMapping
    public Result<IPage<WorkInspectionPlanVo>> queryPageList(WorkInspectionPlanPageParam param) {
        Result<IPage<WorkInspectionPlanVo>> result = new Result<>();
        Page<WorkInspectionPlanVo> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<WorkInspectionPlanVo> data = workInspectionPlanService.pageData(page, param);
        result.setResult(data);
        return result;
    }

    /**
     * 添加或修改
     *
     * @param workInspectionPlanParam 入参
     * @return
     */
    @AutoLog(value = "巡检计划-添加或修改")
    @ApiOperation(value = "巡检计划-添加或修改", notes = "巡检计划-添加或修改")
    @PostMapping(value = "one")
    public Result<Object> add(@RequestBody @Validated WorkInspectionPlanParam workInspectionPlanParam) {
        workInspectionPlanService.saveOne(workInspectionPlanParam);
        return Result.ok("保存成功！");
    }

    /**
     * 通过id删除
     *
     * @param id 主键
     * @return
     */
    @AutoLog(value = "巡检计划-通过id删除")
    @ApiOperation(value = "巡检计划-通过id删除", notes = "巡检计划-通过id删除")
    @DeleteMapping(value = "{id}")
    public Result<Object> delete(@PathVariable("id") String id) {
        workInspectionPlanService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 改计划状态
     * @param id 主键id
     * @return
     */
    @AutoLog("更改计划状态")
    @ApiOperation(value = "更改计划状态")
    @GetMapping("/editStatus/{id}")
    public Result<Object> editStatus(@PathVariable("id") String id){
        workInspectionPlanService.editStatus(id);
        return Result.ok();
    }


    /**
     * 计划下拉列表
     * @return
     */
    @ApiOperation(value = "计划下拉列表",notes = "手动生成工单时使用")
    @AutoLog("计划下拉列表")
    @GetMapping("/dropdown")
    public Result<List<WorkPlanDropdown>> planDropdown(){
       return Result.ok(workInspectionPlanService.dropdown());
    }

}