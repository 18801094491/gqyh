package com.zcdy.dsc.modules.operation.work.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobTemplate;
import com.zcdy.dsc.modules.operation.work.param.WorkJobTemplatePageParam;
import com.zcdy.dsc.modules.operation.work.service.WorkJobTemplateService;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobTemplateVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkTplDropdown;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 工单模板
 * @author: 智能无人硬核心项目组
 * @date: 2020-06-24 11:05:58
 * @version: V1.0
 */
@Slf4j
@Api(tags = "工单模板")
@RestController
@RequestMapping("/work/template")
public class WorkJobTemplateController extends BaseController<WorkJobTemplate, WorkJobTemplateService> {
    @Resource
    private WorkJobTemplateService workJobTemplateService;

    /**
     * 分页列表查询
     *
     * @return
     */
    @AutoLog(value = "工单模板-分页列表查询")
    @ApiOperation(value = "工单模板-分页列表查询", notes = "工单模板-分页列表查询")
    @GetMapping
    public Result<IPage<WorkJobTemplateVo>> queryPageList(WorkJobTemplatePageParam param) {
        Result<IPage<WorkJobTemplateVo>> result = new Result<IPage<WorkJobTemplateVo>>();
        Page<WorkJobTemplateVo> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<WorkJobTemplateVo> data = workJobTemplateService.queryPageData(page, param);
        result.setResult(data);
        return result.success();
    }

    /**
     * 添加或修改
     *
     * @param workJobTemplate
     * @return
     */
    @AutoLog(value = "工单模板-添加或修改")
    @ApiOperation(value = "工单模板-添加或修改", notes = "工单模板-添加或修改")
    @PostMapping(value = "one")
    public Result<Object> add(@RequestBody @Validated WorkJobTemplate workJobTemplate) {
        LambdaQueryWrapper<WorkJobTemplate> queryWrapper = Wrappers.<WorkJobTemplate>lambdaQuery().eq(WorkJobTemplate::getTplName, workJobTemplate.getTplName());
        if (!StringUtils.isEmpty(workJobTemplate.getId())) {
            queryWrapper = queryWrapper.ne(WorkJobTemplate::getId, workJobTemplate.getId());
        }
        List<WorkJobTemplate> list = workJobTemplateService.list(queryWrapper);
        if (list.size() > 0) {
            return Result.error("模板名称不能重复");
        }
        if (!StringUtils.isEmpty(workJobTemplate.getId())) {
            workJobTemplateService.updateById(workJobTemplate);
        } else {
            workJobTemplateService.save(workJobTemplate);
        }
        return Result.ok("保存成功！");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "工单模板-通过id删除")
    @ApiOperation(value = "工单模板-通过id删除", notes = "工单模板-通过id删除")
    @DeleteMapping(value = "{id}")
    public Result<Object> delete(@PathVariable("id") String id) {
        workJobTemplateService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 数据模板下拉选
     *
     * @return
     */
    @AutoLog("数据模板下拉选")
    @ApiOperation(value = "模板下拉选",notes = "巡检点下拉选可以使用")
    @GetMapping("/dropdown")
    public Result<List<WorkTplDropdown>> tplDropdown() {
        return Result.ok(workJobTemplateService.dropdown());
    }

}