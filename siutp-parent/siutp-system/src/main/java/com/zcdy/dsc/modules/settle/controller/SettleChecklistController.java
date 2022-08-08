package com.zcdy.dsc.modules.settle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.settle.entity.SettleChecklist;
import com.zcdy.dsc.modules.settle.entity.SettleChecklistPage;
import com.zcdy.dsc.modules.settle.service.SettleChecklistService;
import com.zcdy.dsc.modules.settle.vo.SettleCheckQueryParam;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsDetailVo;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tangchao
 * @date 2020/5/12
 */
@RestController
@RequestMapping("/settle/checklist")
@Api(tags = "结算管理")
public class SettleChecklistController extends BaseController<SettleChecklist, SettleChecklistService> {

    @Resource
    private SettleChecklistService settleChecklistService;

    @AutoLog(value = "结算统计-分页列表查询")
    @ApiOperation(value = "结算统计-分页列表查询", notes = "结算统计-分页列表查询")
    @GetMapping
    public Result<IPage<SettleChecklistPage>> index(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                          SettleCheckQueryParam settleCheckQueryParam){
        Result<IPage<SettleChecklistPage>> result = new Result<>();
        IPage<SettleChecklistPage> checklistPage = this.settleChecklistService.pageChecklist(new Page<>(pageNo, pageSize), settleCheckQueryParam);
        result.setResult(checklistPage);
        return  result.success("查询成功");
    }
    @AutoLog(value = "结算统计-统计明细")
    @ApiOperation(value = "结算统计-统计明细", notes = "结算统计-统计明细")
    @GetMapping("/detail")
    public Result<SettlementStatisticsDetailVo> getCheckListDetail(String id){
        Result<SettlementStatisticsDetailVo> result = new Result<>();
        SettleChecklist checklist = this.settleChecklistService.getById(id);
        List<SettlementStatisticsVo> list = this.settleChecklistService.getCheckListDetail(id);
        SettlementStatisticsDetailVo settlementStatisticsDetailVo = new SettlementStatisticsDetailVo();
        settlementStatisticsDetailVo.setTotalCost(checklist.getTotalCost().toEngineeringString());
        settlementStatisticsDetailVo.setTotalFlow(checklist.getTotalUsedflow());
        settlementStatisticsDetailVo.setList(list);
        result.setResult(settlementStatisticsDetailVo);
        return result.success("查询成功");
    }
}
