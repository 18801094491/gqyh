package com.zcdy.dsc.modules.operation.work.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionRoute;
import com.zcdy.dsc.modules.operation.work.param.WorkInspectionRoutePageParam;
import com.zcdy.dsc.modules.operation.work.service.WorkInspectionRouteService;
import com.zcdy.dsc.modules.operation.work.vo.WorkPointDropdown;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @description: 巡检路线
 * @author: songguang.jiao
 * @date: 2020-07-01 15:45:13
 */
@Slf4j
@Validated
@Api(tags = "工单巡检路线")
@RestController
@RequestMapping("/work/route")
public class WorkInspectionRouteController extends BaseController<WorkInspectionRoute, WorkInspectionRouteService> {
    @Resource
    private WorkInspectionRouteService workInspectionRouteService;

    /**
     * 分页列表查询
     * @param param 入参
     * @return
     */
    @AutoLog(value = "巡检路线-分页列表查询")
    @ApiOperation(value = "巡检路线-分页列表查询", notes = "巡检路线-分页列表查询")
    @GetMapping
    public Result<IPage<WorkInspectionRoute>> queryPageList(WorkInspectionRoutePageParam param) {
        Page<WorkInspectionRoute> page = new Page<WorkInspectionRoute>(param.getPageNo(), param.getPageSize());
        LambdaQueryWrapper<WorkInspectionRoute> queryWrapper = Wrappers.<WorkInspectionRoute>lambdaQuery();
        if (StringUtils.isNotEmpty(param.getName())) {
            queryWrapper.like(WorkInspectionRoute::getName, param.getName());
        }
        IPage<WorkInspectionRoute> pageList = workInspectionRouteService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加或修改
     * @param workInspectionRoute 入参
     * @return
     */
    @AutoLog(value = "巡检路线-添加或修改")
    @ApiOperation(value = "巡检路线-添加或修改", notes = "巡检路线-添加或修改")
    @PostMapping(value = "one")
    public Result<Object> add(@RequestBody @Validated WorkInspectionRoute workInspectionRoute) {
        workInspectionRouteService.saveOne(workInspectionRoute);
        return Result.ok("保存成功！");
    }

    /**
     * 通过id删除
     *
     * @param id 路线id
     * @return
     */
    @AutoLog(value = "巡检路线-通过id删除")
    @ApiOperation(value = "巡检路线-通过id删除", notes = "巡检路线-通过id删除")
    @DeleteMapping(value = "{id}")
    public Result<Object> delete(@PathVariable("id") String id) {
        workInspectionRouteService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 巡检线路--巡检点列表
     *
     * @param routeId 路线id
     * @return
     */
    @AutoLog("巡检线路--巡检点列表")
    @ApiOperation(value = "巡检线路--巡检点列表",notes = "巡检路线回显巡检点使用")
    @GetMapping("pointList")
    public Result<List<WorkPointDropdown>> pointList(@RequestParam(value = "routeId",required = false) @NotBlank(message = "路线id不能为空") String routeId) {
        return Result.ok(workInspectionRouteService.pointList(routeId));
    }

    /**
     * 巡检路线下拉选
     */
    @AutoLog(value = "巡检路线下拉选")
    @ApiOperation(value = "巡检路线下拉选",notes = "巡检计划使用")
    @GetMapping("/dropdown")
    public Result<List<WorkPointDropdown>> routeDropdown() {
        return Result.ok(workInspectionRouteService.dropdown());
    }


}