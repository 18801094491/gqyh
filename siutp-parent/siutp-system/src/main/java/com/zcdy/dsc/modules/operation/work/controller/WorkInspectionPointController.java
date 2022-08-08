package com.zcdy.dsc.modules.operation.work.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.work.constant.WorkPointCategoryConstant;
import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionPoint;
import com.zcdy.dsc.modules.operation.work.param.WorkInspectionPointPageParam;
import com.zcdy.dsc.modules.operation.work.service.WorkInspectionPointService;
import com.zcdy.dsc.modules.operation.work.vo.WorkInspectionPointVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkPointDropdown;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @description: 巡检点
 * @author: songguang.jiao
 * @date: 2020-07-01 15:45:29
 */
@Slf4j
@Validated
@Api(tags = "工单巡检点")
@RestController
@RequestMapping("/work/workInspectionPoint")
public class WorkInspectionPointController extends BaseController<WorkInspectionPoint, WorkInspectionPointService> {
    @Resource
    private WorkInspectionPointService workInspectionPointService;

    /**
     * 分页列表查询
     *
     * @param param
     * @return
     */
    @AutoLog(value = "巡检点-分页列表查询")
    @ApiOperation(value = "巡检点-分页列表查询", notes = "巡检点-分页列表查询")
    @GetMapping
    public Result<IPage<WorkInspectionPointVo>> queryPageList(WorkInspectionPointPageParam param) {
        Result<IPage<WorkInspectionPointVo>> result = new Result<>();
        Page<WorkInspectionPointVo> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<WorkInspectionPointVo> data = workInspectionPointService.pageData(page, param);
        result.setResult(data);
        return result.success();
    }

    /**
     * 添加或修改
     * 分类字典  /sys/dict/getDictValue/inspection_type
     *
     * @param workInspectionPoint
     * @return
     */
    @AutoLog(value = "巡检点-添加或修改")
    @ApiOperation(value = "巡检点-添加或修改", notes = "巡检点-添加或修改")
    @PostMapping(value = "one")
    public Result<Object> add(@RequestBody @Valid WorkInspectionPoint workInspectionPoint) {
        //如果不是点位的话,dataId跟设备id都不能为空
        if (!WorkPointCategoryConstant.POINT.equals(workInspectionPoint.getCategory())) {
            if (StringUtils.isEmpty(workInspectionPoint.getDataId()) || StringUtils.isEmpty(workInspectionPoint.getEquipmentId())) {
                return Result.error("如果不是新增点位，设备id和dataId不能为空");
            }
        }
        if (!StringUtils.isEmpty(workInspectionPoint.getId())) {
            workInspectionPointService.updateById(workInspectionPoint);
        } else {
            workInspectionPointService.save(workInspectionPoint);
        }
        return Result.ok("保存成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "巡检点-通过id删除")
    @ApiOperation(value = "巡检点-通过id删除", notes = "巡检点-通过id删除")
    @DeleteMapping(value = "{id}")
    public Result<Object> delete(@PathVariable("id") String id) {
        workInspectionPointService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 根据类别显示
     *
     * @return
     */
    @AutoLog("根据类别显示下拉选")
    @ApiOperation(value = "根据类别显示下拉选",notes = "巡检点类别下拉选接口~/sys/dict/getDictValue/inspection_type")
    @GetMapping("/categoryDropdown")
    public Result<List<WorkPointDropdown>> categoryDropdown(@RequestParam(value = "category",required = false) @NotBlank(message = "类别不能为空") String category,
                                                            @RequestParam(value = "name", required = false) String name) {
        return Result.ok(workInspectionPointService.categoryDropdown(category, name));
    }

    /**
     * 巡检点下拉选列表
     *
     * @param name
     * @return
     */
    @AutoLog("巡检点下拉选")
    @ApiOperation(value = "所有巡检点下拉选",notes = "新增巡检路线时使用-可以通过巡检点名称模糊搜索")
    @GetMapping(value = "/pointDropdown")
    public Result<List<WorkPointDropdown>> pointDropdown(String name) {
        return Result.ok(workInspectionPointService.pointDropdown(name));
    }


}