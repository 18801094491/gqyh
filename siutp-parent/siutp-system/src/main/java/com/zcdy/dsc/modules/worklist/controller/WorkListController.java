package com.zcdy.dsc.modules.worklist.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import com.zcdy.dsc.modules.system.service.ISysDictService;
import com.zcdy.dsc.modules.system.vo.DictDropdown;
import com.zcdy.dsc.modules.worklist.constant.WorkListConstant;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import com.zcdy.dsc.modules.worklist.param.WorkListParam;
import com.zcdy.dsc.modules.worklist.service.WorkListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

 /**
 * @Description: 工单信息
 * @Author: 在信汇通
 * @Date:   2021-01-15 16:28:36
 * @Version: V1.0
 */
@Slf4j
@Api(tags="工单信息")
@RestController
@RequestMapping("/worklist/workList")
public class WorkListController extends BaseController<WorkList, WorkListService> {
	@Autowired
	private WorkListService workListService;

	@Autowired
    private ISysDictService sysDictService;

    /**
     * 工单状态来自数据字典，必须先在数据字典中创建相关信息
     * @return
     */
    @ApiOperation(value="获取工单状态")
    @GetMapping(value="/status")
    public Result<List<DictDropdown>> getStatusList() {
        String code = WorkListConstant.WORK_LIST_STATUS_CODE;
        Result<List<DictDropdown>> result=new Result<>();
        List<DictDropdown> value = sysDictService.getDictValue(code);
        result.setResult(value);
        result.success("查询成功");
        return result;
    }

    @AutoLog(value = "获取已完成工单数量")
    @ApiOperation(value="获取已完成工单数量", notes="获取已完成工单数量")
    @GetMapping(value = "/getCompleteNum")
    public ResultData<?> getCompleteNum()
    {
        return getWorkListNumByStatus(WorkListConstant.WORK_LIST_STATUS_COMPLETE);
    }

    @AutoLog(value = "获取待处理工单数量")
    @ApiOperation(value="获取待处理工单数量", notes="获取待处理工单数量")
    @GetMapping(value = "/getAllotNum")
    public ResultData<?> getAllotNum()
    {
        return getWorkListNumByStatus(WorkListConstant.WORK_LIST_STATUS_ALLOT);
    }

    /**
     * 根据状态获取工单数量
     * @param status
     * @return
     */
    private ResultData<?> getWorkListNumByStatus(String status)
    {
        Integer num = workListService.getWorkListNumByStatus(status);
        return ResultData.ok(num);
    }

    @AutoLog(value = "大屏工单列表")
    @ApiOperation(value="大屏工单列表", notes="大屏工单列表")
    @GetMapping(value = "/rdp/list")
    public ResultData<?> getWorkList7DayList()
    {
        WorkList workList = new WorkList();
        workList.setDelFlag(DelFlagConstant.NORMAL);
        workList.setQueryStatusCode(WorkListConstant.WORK_LIST_STATUS_CODE);
        workList.setQueryTypeCode(WorkListConstant.WORK_LIST_TYPE_CODE);
        Date today = new Date();//今天
        Date startDate = DateUtil.addDay(today, -6);//6天前
        String todayStr = DateUtil.date2String(today, DateUtil.dateFormatStr);//取日期
        String startStr = DateUtil.date2String(startDate, DateUtil.dateFormatStr);//取日期
        today = DateUtil.string2Date(todayStr + " 23:59:59", DateUtil.dateTimeFormatStr);//加上时间
        startDate = DateUtil.string2Date(startStr + " 00:00:00", DateUtil.dateTimeFormatStr);//加上时间
        workList.setStartDate(startDate);
        workList.setOverDate(today);
        List<WorkList> workLists = workListService.getWorkList7DayList(workList);
        return ResultData.ok(workLists);
    }

    @AutoLog(value = "问题工单-分页列表查询")
	@ApiOperation(value="问题工单-分页列表查询", notes="问题工单-分页列表查询")
	@GetMapping(value = "/list/matter")
	public ResultData<?> queryPageListMatter(WorkListParam workListParam,
								   HttpServletRequest req) {
		WorkList searchParam = new WorkList(workListParam);
        searchParam.setType(WorkListConstant.WORK_LIST_TYPE_MATTER);//问题工单类型
		return getPageList(searchParam, req, workListParam.getPageNo(), workListParam.getPageSize());
	}

    @AutoLog(value = "维养工单-分页列表查询")
    @ApiOperation(value="维养工单-分页列表查询", notes="维养工单-分页列表查询")
    @GetMapping(value = "/list/keep")
    public ResultData<?> queryPageListKeep(WorkListParam workListParam,
                                       HttpServletRequest req) {
        WorkList searchParam = new WorkList(workListParam);
        searchParam.setType(WorkListConstant.WORK_LIST_TYPE_KEEP);//问题工单类型
        return getPageList(searchParam, req, workListParam.getPageNo(), workListParam.getPageSize());
    }

    @AutoLog(value = "维养工单-获取维养设备")
    @ApiOperation(value="维养工单-获取维养设备", notes="维养工单-获取维养设备")
    @GetMapping(value = "/list/keep-equ")
    public ResultData<?> queryKeepEquList(WorkListParam workListParam) {
        WorkList workList = new WorkList(workListParam);
        List<OptEquipmentModel> equList = workListService.getEquList(workList);
        return ResultData.ok(equList);
    }

    @AutoLog(value = "巡检工单-分页列表查询")
    @ApiOperation(value="巡检工单-分页列表查询", notes="巡检工单-分页列表查询")
    @GetMapping(value = "/list/insp")
    public ResultData<?> queryPageListInsp(WorkListParam workListParam,
                                            HttpServletRequest req) {
        WorkList searchParam = new WorkList(workListParam);
        searchParam.setType(WorkListConstant.WORK_LIST_TYPE_INSP);//问题工单类型
        return getPageList(searchParam, req, workListParam.getPageNo(), workListParam.getPageSize());
    }

    /**
     * 分页查询共用
     * @param workList
     * @param req
     * @param pageNo
     * @param pageSize
     * @return
     */
	private ResultData<?> getPageList(WorkList workList, HttpServletRequest req, Integer pageNo, Integer pageSize) {
        workList.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
        workList.setQueryStatusCode(WorkListConstant.WORK_LIST_STATUS_CODE);//数据字典中的工单状态code
        Page<WorkList> page = new Page<WorkList>(pageNo, pageSize);
        IPage<WorkList> pageList = workListService.selectPageDate(page, workList);
        return ResultData.ok(pageList);
    }

    @AutoLog(value = "问题工单-添加")
    @ApiOperation(value="问题工单-添加", notes="问题工单-添加")
    @PostMapping(value = "/add/matter")
    public ResultData<?> addMatter(@RequestBody WorkList workList, HttpServletRequest req) {
        workList.setType(WorkListConstant.WORK_LIST_TYPE_MATTER);
        return add(workList, req);
    }

    @AutoLog(value = "维养工单-添加")
    @ApiOperation(value="维养工单-添加", notes="维养工单-添加")
    @PostMapping(value = "/add/keep")
    public ResultData<?> addKeep(@RequestBody WorkList workList, HttpServletRequest req) {
        ResultData<WorkList> resultData = new ResultData<WorkList>();
        try{
            workList.setType(WorkListConstant.WORK_LIST_TYPE_KEEP);
            String username = JwtUtil.getUserNameByToken(req);
            workList.setCreateBy(username);
            return workListService.addKeepWorkList(workList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
     }
        return resultData;
	}
	
	/**
	 * 问题工单-添加
	 * @param workList
	 * @return
	 */
	private ResultData<?> add(WorkList workList, HttpServletRequest req) {
	    ResultData<WorkList> resultData = new ResultData<WorkList>();
	    try {
            String username = JwtUtil.getUserNameByToken(req);
            workList.setCreateBy(username);
            return workListService.addWorkList(workList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
	}

	/**
	 * 编辑
	 * @param workList
	 * @return
	 */
	@AutoLog(value = "问题工单-编辑")
	@ApiOperation(value="问题工单-编辑", notes="问题工单-编辑")
	@PostMapping(value = "/edit/matter")
	public ResultData<?> editMatter(@RequestBody WorkList workList, HttpServletRequest req) {
        ResultData<WorkList> resultData = new ResultData<WorkList>();
        try {
            if (workList == null || workList.getId() == null) {
                resultData.error500("id不能为空");
                return resultData;
            }
            String username = JwtUtil.getUserNameByToken(req);
            workList.setUpdateTime(new Date());
            workList.setUpdateBy(username);
            return workListService.editMatter(workList);
        } catch (
                Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
	}

    @AutoLog(value = "维养工单-编辑")
    @ApiOperation(value="维养工单-编辑", notes="维养工单-编辑")
    @PostMapping(value = "/edit/keep")
    public ResultData<?> editKeep(@RequestBody WorkList workList, HttpServletRequest req) {
        ResultData<WorkList> resultData = new ResultData<WorkList>();
        try {
            if (workList == null || workList.getId() == null) {
                resultData.error500("id不能为空");
                return resultData;
            }
            String username = JwtUtil.getUserNameByToken(req);
            workList.setUpdateTime(new Date());
            workList.setUpdateBy(username);
            return workListService.editKeep(workList);
        } catch (
                Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
    }

    @AutoLog(value = "巡检工单-编辑")
    @ApiOperation(value="巡检工单-编辑", notes="巡检工单-编辑")
    @PostMapping(value = "/edit/insp")
    public ResultData<?> editInsp(@RequestBody WorkList workList, HttpServletRequest req) {
        ResultData<WorkList> resultData = new ResultData<WorkList>();
        try {
            if (workList == null || workList.getId() == null) {
                resultData.error500("id不能为空");
                return resultData;
            }
            String username = JwtUtil.getUserNameByToken(req);
            workList.setUpdateTime(new Date());
            workList.setUpdateBy(username);
            return workListService.editInsp(workList);
        } catch (
                Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
    }

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "问题工单-通过id删除")
	@ApiOperation(value="问题工单-通过id删除", notes="问题工单-通过id删除")
	@DeleteMapping(value = "/delete/matter")
	public ResultData<?> deleteMatterById(@RequestParam("id") String id, HttpServletRequest req) {
        ResultData<WorkList> resultData = new ResultData<WorkList>();
        if (id == null) {
            return ResultData.error("id不能为空");
        }
        WorkList workList = new WorkList();
        workList.setId(id);
        workList.setDelFlag(DelFlagConstant.DELETED);//删除标记
        String username = JwtUtil.getUserNameByToken(req);
        workList.setUpdateTime(new Date());
        workList.setUpdateBy(username);
        return workListService.deleteMatterById(workList);//逻辑删除
	}

    @AutoLog(value = "维养工单-通过id删除")
    @ApiOperation(value="维养工单-通过id删除", notes="维养工单-通过id删除")
    @DeleteMapping(value = "/delete/keep")
    public ResultData<?> deleteKeepById(@RequestParam("id") String id, HttpServletRequest req) {
        ResultData<WorkList> resultData = new ResultData<WorkList>();
        if (id == null) {
            return ResultData.error("id不能为空");
        }
        WorkList workList = new WorkList();
        workList.setId(id);
        workList.setDelFlag(DelFlagConstant.DELETED);//删除标记
        String username = JwtUtil.getUserNameByToken(req);
        workList.setUpdateTime(new Date());
        workList.setUpdateBy(username);
        return workListService.deleteKeepById(workList);//逻辑删除
    }

    @AutoLog(value = "巡检工单-通过id删除")
    @ApiOperation(value="巡检工单-通过id删除", notes="巡检工单-通过id删除")
    @DeleteMapping(value = "/delete/insp")
    public ResultData<?> deleteInspById(@RequestParam("id") String id, HttpServletRequest req) {
        ResultData<WorkList> resultData = new ResultData<WorkList>();
        if (id == null) {
            return ResultData.error("id不能为空");
        }
        WorkList workList = new WorkList();
        workList.setId(id);
        workList.setDelFlag(DelFlagConstant.DELETED);//删除标记
        String username = JwtUtil.getUserNameByToken(req);
        workList.setUpdateTime(new Date());
        workList.setUpdateBy(username);
        return workListService.deleteInspById(workList);//逻辑删除
    }
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "问题工单-批量删除")
	@ApiOperation(value="问题工单-批量删除", notes="工单-批量删除")
	@DeleteMapping(value = "/deletes/matter")
	public ResultData<?> deleteBatchMatter(@RequestParam(name="ids",required=true) String ids, HttpServletRequest req) {
        ResultData<WorkList> resultData = new ResultData<WorkList>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        }else {
            String username = JwtUtil.getUserNameByToken(req);
            return workListService.deleteBatchMatter(ids, username);
        }
        return resultData;
	}

    @AutoLog(value = "维养工单-批量删除")
    @ApiOperation(value="维养工单-批量删除", notes="维养工单-批量删除")
    @DeleteMapping(value = "/deletes/keep")
    public ResultData<?> deleteBatchKeep(@RequestParam(name="ids",required=true) String ids, HttpServletRequest req) {
        ResultData<WorkList> resultData = new ResultData<WorkList>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        }else {
            String username = JwtUtil.getUserNameByToken(req);
            return workListService.deleteBatchKeep(ids, username);
        }
        return resultData;
    }

    @AutoLog(value = "维养工单-批量删除")
    @ApiOperation(value="维养工单-批量删除", notes="维养工单-批量删除")
    @DeleteMapping(value = "/deletes/insp")
    public ResultData<?> deleteBatchInsp(@RequestParam(name="ids",required=true) String ids, HttpServletRequest req) {
        ResultData<WorkList> resultData = new ResultData<WorkList>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        }else {
            String username = JwtUtil.getUserNameByToken(req);
            return workListService.deleteBatchInsp(ids, username);
        }
        return resultData;
    }
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "工单-通过id查询")
	@ApiOperation(value="工单-通过id查询", notes="工单-通过id查询")
	@GetMapping(value = "/query")
	public ResultData<?> queryById(@RequestParam("id") String id) {
        ResultData<WorkList> resultData = new ResultData<WorkList>();
        try {
            if (ConvertUtils.isEmpty(id)) {
                resultData.error500("id不能为空");
                return resultData;
            }
            WorkList workList = new WorkList();
            workList.setId(id);
            workList.setDelFlag(DelFlagConstant.NORMAL);
            workList.setQueryStatusCode(WorkListConstant.WORK_LIST_STATUS_CODE);
            workList.setQueryTypeCode(WorkListConstant.WORK_LIST_TYPE_CODE);
            workList.setQueryMatterTypeCode(WorkListConstant.WORK_LIST_MATTER_TYPE);
            workList.setQueryMatterStatusCode(WorkListConstant.WORK_LIST_MATTER_STATUS);
            workList.setQueryMatterLevelCode(WorkListConstant.WORK_LIST_MATTER__MATTER_LEVEL_CODE);
            workList.setQueryMatterMatterTypeCode(WorkListConstant.WORK_LIST_MATTER__MATTER_TYPE_CODE);
            workList = workListService.getOneById(workList);
            resultData.setData(workList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
	}

	@AutoLog(value = "根据工单id获取对应班组成员所在位置")
    @ApiOperation(value="根据工单id获取对应班组成员所在位置", notes="根据工单id获取对应班组成员所在位置")
    @GetMapping(value = "/getLocation")
	public ResultData<?> getWorkListLocation(@RequestParam("id") String id)
    {
        if (ConvertUtils.isEmpty(id)) {
            return ResultData.error("id不能为空");
        }
        return workListService.getWorkListLocationById(id);
    }
}