package com.zcdy.dsc.modules.operation.work.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.operation.work.constant.WorkRecordUserTypeConstant;
import com.zcdy.dsc.modules.operation.work.entity.WorkJob;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobInspectPoint;
import com.zcdy.dsc.modules.operation.work.entity.WorkRecordItemInfoEntity;
import com.zcdy.dsc.modules.operation.work.param.RecordParam;
import com.zcdy.dsc.modules.operation.work.param.WokJobPageParam;
import com.zcdy.dsc.modules.operation.work.param.WorkPointParam;
import com.zcdy.dsc.modules.operation.work.service.*;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobDatasourceTree;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobRecordInfosVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 工单
 * @author: songguang.jiao
 * @date: 2020-07-08 09:13:02
 */
@Slf4j
@Api(tags = "工单")
@RestController
@Validated
@RequestMapping("/work/workJob")
public class WorkJobController extends BaseController<WorkJob, WorkJobService> {
    @Resource
    private WorkJobService workJobService;

    @Resource
    private WorkJobInspectPointService workJobInspectPointService;

    @Resource
    private WorkJobDatasourceService workJobDatasourceService;

    @Resource
    private WorkJobRecordService workJobRecordService;

    @Resource
    private WorkJobRecordItemService workJobRecordItemService;

    /**
     * 工单分页列表
     *
     * @param param 查询参数
     * @return
     */
    @AutoLog(value = "工单分页列表")
    @ApiOperation(value = "工单分页列表", notes = "工单分页列表")
    @GetMapping
    public Result<IPage<WorkJobVo>> queryPageList(WokJobPageParam param) {
        Result<IPage<WorkJobVo>> result = new Result<IPage<WorkJobVo>>();
        Page<WorkJobVo> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<WorkJobVo> data = workJobService.selectPageData(page, param);
        result.setResult(data);
        return result;
    }


    /**
     * 通过工单id删除
     *
     * @param id 主键id
     * @return
     */
    @AutoLog(value = "通过工单id删除")
    @ApiOperation(value = "通过工单id删除", notes = "通过工单id删除")
    @DeleteMapping(value = "{id}")
    public Result<Object> delete(@PathVariable("id") String id) {
        workJobService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 生成派工单
     *
     * @param planId 计划id
     * @return
     */
    @ApiOperation("生成派工单")
    @AutoLog("生成派工单")
    @GetMapping("/add/{planId}")
    public Result<Object> addWorkJob(@PathVariable("planId") String planId) {
        //TODO 手动生成的实际执行日期待定
        workJobService.addWorkJob(planId, LocalDate.now().toString());
        return Result.ok();
    }


    /**
     * 保存附件
     *
     * @param file 附件地址
     * @return
     */
    @AutoLog("保存附件")
    @ApiOperation(value = "保存附件")
    @GetMapping("/saveFile/{file}")
    public Result<Object> saveFile(@RequestParam(value = "file", required = false) @NotBlank(message = "附件不能为空") String file, @RequestParam(value = "id", required = false) @NotBlank(message = "id不能为空") String id) {
        workJobService.update(new WorkJob().setWorkFile(file), Wrappers.<WorkJob>lambdaUpdate().eq(WorkJob::getId, id));
        return Result.ok();
    }

    /**
     * 新增巡检点
     *
     * @param workPointParam 入参
     * @param bindingResult  校验规则
     * @return
     */
    @AutoLog("新增巡检点")
    @ApiOperation("新增巡检点")
    @PostMapping("/add/point")
    public Result<Object> addPoint(@Validated @RequestBody WorkPointParam workPointParam) {
        WorkJobInspectPoint workJobInspectPoint = new WorkJobInspectPoint();
        BeanUtil.copyProperties(workPointParam, workJobInspectPoint);
        workJobInspectPoint.setRecordStatus(StatusConstant.INVALID);
        workJobInspectPointService.save(workJobInspectPoint);
        return Result.ok();
    }


    /**
     * 通过工单id查询巡检点列表
     *
     * @param workJobId 工单id
     * @return
     */
    @AutoLog("通过工单id查询巡检点列表")
    @ApiOperation(value = "通过工单id查询巡检点列表", notes = "点击工单详情时调用")
    @GetMapping("points/{workJobId}")
    public Result<List<WorkJobInspectPoint>> workJobInspectPoints(@PathVariable("workJobId") String workJobId) {
        List<WorkJobInspectPoint> workJobInspectPoints = workJobInspectPointService.list(Wrappers.<WorkJobInspectPoint>lambdaQuery().eq(WorkJobInspectPoint::getWorkJobId, workJobId).select(WorkJobInspectPoint::getId, WorkJobInspectPoint::getName, WorkJobInspectPoint::getRecordStatus));
        return Result.ok(workJobInspectPoints);
    }


    /**
     * 工单填报点击回显
     *
     * @param pointId      工单填报点击回显
     * @param recordStatus 录入状态
     * @return
     */
    @AutoLog("工单填报点击回显")
    @ApiOperation(value = "工单填报点击回显", notes = "工单填报点击回显")
    @GetMapping("/queryInfo")
    public Result<WorkJobRecordInfosVo> queryOne(@RequestParam(value = "pointId", required = false) @NotBlank(message = "巡检点id不能为空") String pointId,
                                                 @RequestParam(value = "recordStatus", required = false) @NotBlank(message = "录入状态不能为空") Short recordStatus) {
        Result<WorkJobRecordInfosVo> result = new Result<>();
        WorkJobRecordInfosVo workJobRecordInfosVo = new WorkJobRecordInfosVo();
        WorkRecordItemInfoEntity recordItemInfoEntity = workJobRecordItemService.getWorkRecordInfoByPointId(pointId);
        if (recordItemInfoEntity != null) {
            //巡检人录入信息
            List<WorkJobDatasourceTree> inspectorData = new ArrayList<>();
            switch (recordStatus) {
                case StatusConstant
                        .INVALID:
                    //返回空模板
                    inspectorData = workJobDatasourceService.tplDataTree(recordItemInfoEntity.getTplId());
                    break;
                case StatusConstant.VALID:
                    inspectorData = workJobRecordService.getRecordItems(pointId, "", WorkRecordUserTypeConstant.DISPATCH);
                    break;
                default:
                    inspectorData = workJobDatasourceService.tplDataTree(recordItemInfoEntity.getTplId());
            }
            workJobRecordInfosVo.setDispatchData(inspectorData);

            //派单人录入情况
            List<WorkJobDatasourceTree> dispatchData = workJobRecordService.getRecordItems("", recordItemInfoEntity.getPlanId(), WorkRecordUserTypeConstant.INSPECTOR);
            workJobRecordInfosVo.setDispatchData(dispatchData);
        }
        result.setResult(workJobRecordInfosVo);
        return result;
    }

    /**
     * 工单填报保存
     *
     * @param recordParam   参数
     * @param bindingResult 参数校验
     * @return
     */
    @AutoLog("工单填报保存")
    @ApiOperation("工单填报保存")
    @PostMapping("save")
    public Result<Object> saveOne(@RequestBody @Validated RecordParam recordParam) {
        recordParam.setUserType(WorkRecordUserTypeConstant.INSPECTOR);
        workJobRecordService.saveRecordItems(recordParam);
        return Result.ok();
    }
}