package com.zcdy.dsc.modules.worklist.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.worklist.constant.WorkListConstant;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import com.zcdy.dsc.modules.worklist.param.MyWorkListParam;
import com.zcdy.dsc.modules.worklist.service.MyWorkListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 我的工单
 * @Author: 在信汇通
 * @Date:   2021-01-15 16:28:36
 * @Version: V1.0
 */
@Slf4j
@Api(tags="我的工单")
@RestController
@RequestMapping("/worklist/myWorkList")
public class MyWorkListController  extends BaseController<WorkList, MyWorkListService> {

    @Autowired
    private MyWorkListService myWorkListService;

    @AutoLog(value = "我的工单-分页列表查询")
    @ApiOperation(value="我的工单-分页列表查询", notes="我的工单-分页列表查询")
    @GetMapping(value = "/list")
    public ResultData<?> queryPageListInsp(MyWorkListParam workListParam,
                                           HttpServletRequest req) {
        WorkList searchParam = new WorkList(workListParam);
        searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
        String username = JwtUtil.getUserNameByToken(req);
        searchParam.setTeamMemberUsername(username);
        searchParam.setQueryStatusCode(WorkListConstant.WORK_LIST_STATUS_CODE);
        searchParam.setQueryTypeCode(WorkListConstant.WORK_LIST_TYPE_CODE);
        Page<WorkList> page = new Page<WorkList>(workListParam.getPageNo(), workListParam.getPageSize());
        IPage<WorkList> pageList = myWorkListService.selectPageDate(page, searchParam);
        return ResultData.ok(pageList);
    }

    @AutoLog(value = "我的工单-我的工单数量")
    @ApiOperation(value="我的工单-我的工单数量", notes="我的工单-我的工单数量")
    @GetMapping(value = "/mylistnum")
    public ResultData<?> getMylistNum(HttpServletRequest req) {
        ResultData<?> resultData = queryPageListInsp(new MyWorkListParam(), req);
        IPage<WorkList> data = (IPage<WorkList>) resultData.getData();
        return ResultData.ok(data.getTotal());
    }

    @AutoLog(value = "工单任务-标记完成")
    @ApiOperation(value="工单任务-标记完成", notes="工单任务-标记完成")
    @GetMapping(value = "/complete")
    public ResultData<?> complete(@RequestParam(name="ids",required=true) String ids, HttpServletRequest req) {
        ResultData<WorkList> resultData = new ResultData<WorkList>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
            return resultData;
        }else {
            String username = JwtUtil.getUserNameByToken(req);
            return myWorkListService.complete(ids, username);
        }

    }
}
