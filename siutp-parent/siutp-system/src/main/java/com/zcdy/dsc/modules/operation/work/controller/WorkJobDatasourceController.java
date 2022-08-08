package com.zcdy.dsc.modules.operation.work.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobDatasource;
import com.zcdy.dsc.modules.operation.work.param.WorkJobDatasourceParam;
import com.zcdy.dsc.modules.operation.work.service.WorkJobDatasourceItemService;
import com.zcdy.dsc.modules.operation.work.service.WorkJobDatasourceService;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobDatasourceTree;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 工单数据项
 * @author: 智能无人硬核心项目组
 * @date: 2020-06-24 11:06:33
 * @version: V1.0
 */
@Slf4j
@Api(tags = "工单数据项")
@RestController
@RequestMapping("/work/workJobDatasource")
public class WorkJobDatasourceController extends BaseController<WorkJobDatasource, WorkJobDatasourceService> {
    @Resource
    private WorkJobDatasourceService workJobDatasourceService;
    @Resource
    private WorkJobDatasourceItemService workJobDatasourceItemService;


    /**
     * 树形工单数据项查询
     * @param tplId 模板id
     * @return
     */
    @AutoLog(value = "树形工单数据项查询")
    @ApiOperation(value = "树形工单数据项查询", notes = "树形工单数据项查询")
    @GetMapping("/{tplId}")
    public Result<List<WorkJobDatasourceTree>> queryPageData(@PathVariable("tplId") String tplId) {
        Result<List<WorkJobDatasourceTree>> result = new Result<>();
        List<WorkJobDatasourceTree> treeList = workJobDatasourceService.tplDataTree(tplId);
        result.setResult(treeList);
        return result.success();
    }


    /**
     * 删除子选项
     *
     * @param itemId 子项id
     * @return
     */
    @AutoLog("删除子选项")
    @ApiOperation(value = "删除子选项", notes = "在详情页删除单个子选项使用")
    @DeleteMapping(value = "/item/{itemId}")
    public Result<Object> deleteItem(@PathVariable("itemId") String itemId) {
        workJobDatasourceItemService.removeById(itemId);
        return Result.ok();
    }


    /**
     * 添加或修改
     *
     * @param datasourceParam 入参
     * @return
     */
    @AutoLog(value = "工单数据项-添加或修改")
    @ApiOperation(value = "工单数据项-添加或修改", notes = "工单数据项-添加或修改")
    @PostMapping(value = "one")
    public Result<Object> add(@RequestBody @Validated WorkJobDatasourceParam datasourceParam) {
        workJobDatasourceService.saveData(datasourceParam);
        return Result.ok("保存成功！");
    }

    /**
     * 通过id删除
     *
     * @param id 主键id
     * @return
     */
    @AutoLog(value = "工单数据项-通过id删除")
    @ApiOperation(value = "工单数据项-通过id删除", notes = "工单数据项-通过id删除,树形列表调用")
    @DeleteMapping(value = "/{id}")
    public Result<Object> delete(@PathVariable("id") String id) {
        workJobDatasourceService.delete(id);
        return Result.ok("删除成功!");
    }

}