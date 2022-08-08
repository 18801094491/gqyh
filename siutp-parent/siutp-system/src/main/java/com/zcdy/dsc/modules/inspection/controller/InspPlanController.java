package com.zcdy.dsc.modules.inspection.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.inspection.constant.InspConstant;
import com.zcdy.dsc.modules.inspection.entity.InspPlan;
import com.zcdy.dsc.modules.inspection.param.InspPlanParam;
import com.zcdy.dsc.modules.inspection.param.WorkTeamDateParam;
import com.zcdy.dsc.modules.inspection.service.InspPlanService;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.work.entity.WorkTeam;
import com.zcdy.dsc.modules.system.service.ISysDictService;
import com.zcdy.dsc.modules.system.vo.DictDropdown;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

 /**
 * @Description: 巡检计划
 * @Author: 在信汇通
 * @Date:   2021-01-15 16:26:25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="巡检计划")
@RestController
@RequestMapping("/inspection/inspPlan")
public class InspPlanController extends BaseController<InspPlan, InspPlanService> {
    @Autowired
	private InspPlanService inspPlanService;

	@Autowired
    private ISysDictService sysDictService;

	/**
     * 巡检工单频次类型来自数据字典，必须先在数据字典中创建相关信息
     * @return
     */
	@ApiOperation(value="获取巡检任务频次类型")
    @GetMapping(value="/types")
    public Result<List<DictDropdown>> getInspPointTypeList() {
        String code = InspConstant.WORK_LIST_INSP_PLAN_TYPE_CODE;
        Result<List<DictDropdown>> result=new Result<>();
        List<DictDropdown> value = sysDictService.getDictValue(code);
        result.setResult(value);
        result.success("查询成功");
        return result;
    }
	
	/**
	 * 分页列表查询
	 *
	 * @param inspPlanParam
	 * @param req
	 * @return
	 */
	@AutoLog(value = "巡检计划-分页列表查询")
	@ApiOperation(value="巡检计划-分页列表查询", notes="巡检计划-分页列表查询")
	@GetMapping(value = "/list")
	public ResultData<?> queryPageList(InspPlanParam inspPlanParam,
								   HttpServletRequest req) {
		InspPlan searchParam = new InspPlan(inspPlanParam);
		searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
        searchParam.setQueryTypeCode(InspConstant.WORK_LIST_INSP_PLAN_TYPE_CODE);//数据字典中计划频次类型的code
        searchParam.setQueryStatusCode(InspConstant.INSP_PLAN_STATUS_CODE);//数据字典中计划状态的code
//		QueryWrapper<InspPlan> queryWrapper = QueryGenerator.initQueryWrapper(searchParam, req.getParameterMap());
//		queryWrapper.lambda().orderByDesc(InspPlan::getCreateTime);//默认创建时间倒序
		Page<InspPlan> page = new Page<InspPlan>(inspPlanParam.getPageNo(), inspPlanParam.getPageSize());
		IPage<InspPlan> pageList = inspPlanService.selectPageDate(page, searchParam);
		return ResultData.ok(pageList);
	}

	/**
     * 通过巡检计划有效期获取符合的班组
     * @param workTeamDateParam
     * @return
     */
    @AutoLog(value = "通过巡检计划有效期获取符合的班组")
    @ApiOperation(value="通过巡检计划有效期获取符合的班组", notes="通过巡检计划有效期获取符合的班组")
    @GetMapping(value = "/team/list")
    public ResultData<?> getWorkTeamByData(WorkTeamDateParam workTeamDateParam)
    {
        if(workTeamDateParam == null
                || workTeamDateParam.getOverTime() == null
                || workTeamDateParam.getStartTime() == null)
        {
            return ResultData.error("巡检计划的开始和结束日期都不能为空");
        }
        List<WorkTeam> list = inspPlanService.getWorkTeamAndMemberListByDate(workTeamDateParam);
        return ResultData.ok(list);
    }
	
	/**
	 * 添加
	 * @param inspPlan
	 * @return
	 */
	@AutoLog(value = "巡检计划-添加")
	@ApiOperation(value="巡检计划-添加", notes="巡检计划-添加")
	@PostMapping(value = "/add")
	public ResultData<?> add(@RequestBody InspPlan inspPlan, HttpServletRequest req) {
	    ResultData<InspPlan> resultData = new ResultData<InspPlan>();
	    try {
            String username = JwtUtil.getUserNameByToken(req);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            inspPlan.setId(uuid);
            inspPlan.setCreateTime(new Date());
            inspPlan.setCreateBy(username);
            inspPlan.setStatus(InspConstant.INSP_PLAN_STATUS_ENABLE);//巡检计划状态
            boolean ok = inspPlanService.save(inspPlan);
            if(ok){
                resultData.success("保存成功！");
            }
            else{
                resultData.error500("操作失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
	}

	/**
	 * 编辑
	 * @param inspPlan
	 * @return
	 */
	@AutoLog(value = "巡检计划-编辑")
	@ApiOperation(value="巡检计划-编辑", notes="巡检计划-编辑")
	@PostMapping(value = "/edit")
	public ResultData<?> edit(@RequestBody InspPlan inspPlan, HttpServletRequest req) {
        ResultData<InspPlan> resultData = new ResultData<InspPlan>();
        try {
            if (inspPlan == null || inspPlan.getId() == null) {
                resultData.error500("id不能为空");
                return resultData;
            }
            String username = JwtUtil.getUserNameByToken(req);
            inspPlan.setUpdateTime(new Date());
            inspPlan.setUpdateBy(username);
            boolean ok = inspPlanService.updateById(inspPlan);
            if (ok) {
                resultData.success("编辑成功！");
            } else {
                resultData.error500("操作失败");
            }
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
	@AutoLog(value = "巡检计划-通过id删除")
	@ApiOperation(value="巡检计划-通过id删除", notes="巡检计划-通过id删除")
	@DeleteMapping(value = "/delete")
	public ResultData<?> deleteById(@RequestParam("id") String id, HttpServletRequest req) {
        ResultData<InspPlan> resultData = new ResultData<InspPlan>();
        if (id == null) {
            return ResultData.error("id不能为空");
        }
        InspPlan inspPlan = new InspPlan();
        inspPlan.setId(id);
        inspPlan.setDelFlag(DelFlagConstant.DELETED);//删除标记
        String username = JwtUtil.getUserNameByToken(req);
        inspPlan.setUpdateTime(new Date());
        inspPlan.setUpdateBy(username);
        boolean ok = inspPlanService.updateById(inspPlan);//逻辑删除
        if (ok) {
            resultData.success("删除成功!");
        } else {
            resultData.error500("删除失败!");
        }
        return resultData;
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "巡检计划-批量删除")
	@ApiOperation(value="巡检计划-批量删除", notes="巡检计划-批量删除")
	@DeleteMapping(value = "/deletes")
	public ResultData<?> deleteBatch(@RequestParam(name="ids",required=true) String ids, HttpServletRequest req) {
        ResultData<InspPlan> resultData = new ResultData<InspPlan>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        }else {
            List<String> list = Arrays.asList(ids.split(","));
            List<InspPlan> entityList = new ArrayList<>();
            for(String s : list)
            {
                InspPlan inspPlan = new InspPlan();
                inspPlan.setId(s);
                inspPlan.setDelFlag(DelFlagConstant.DELETED);//删除标记
                inspPlan.setUpdateTime(new Date());
                String username = JwtUtil.getUserNameByToken(req);
                inspPlan.setUpdateBy(username);
                entityList.add(inspPlan);
            }
            boolean ok = inspPlanService.updateBatchById(entityList);//批量逻辑删除

            if(ok){
                resultData.success("批量删除成功!");
            }
            else{
                resultData.error500("操作失败");
            }
        }
        return resultData;
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "巡检计划-通过id查询")
	@ApiOperation(value="巡检计划-通过id查询", notes="巡检计划-通过id查询")
	@GetMapping(value = "/query")
	public ResultData<?> queryById(@RequestParam("id") String id) {
        ResultData<InspPlan> resultData = new ResultData<InspPlan>();
        try {
            if (ConvertUtils.isEmpty(id)) {
                resultData.error500("id不能为空");
                return resultData;
            }
            InspPlan inspPlan = new InspPlan();
            inspPlan.setId(id);
            inspPlan.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
            inspPlan.setQueryTypeCode(InspConstant.WORK_LIST_INSP_PLAN_TYPE_CODE);//数据字典中计划频次类型的code
            inspPlan = inspPlanService.getOneById(inspPlan);
            resultData.setData(inspPlan);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
	}
}