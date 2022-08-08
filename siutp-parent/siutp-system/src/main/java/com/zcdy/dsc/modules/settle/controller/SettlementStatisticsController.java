package com.zcdy.dsc.modules.settle.controller;


import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.modules.settle.service.SettlementStatisticsService;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsQueryParam;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 结算管理
 *
 * @author tangchao
 * @since 2020-5-7 10:31:50
 */
@RestController
@Api(tags = "结算管理")
@RequestMapping("/settle/statistics")
public class SettlementStatisticsController {

    @Resource
    private SettlementStatisticsService settlementStatisticsService;

    @GetMapping()
    @ApiOperation(value = "结算管理-首页查询", notes = "结算管理-首页查询, 不传参数默认查询全部")
    public Result<List<SettlementStatisticsVo>> index(SettlementStatisticsQueryParam queryParam) {
        Result<List<SettlementStatisticsVo>> result = new Result<>();
        List<SettlementStatisticsVo> settleByCustomerId = this.settlementStatisticsService.getSettleByQueryParam(queryParam);
        result.setResult(settleByCustomerId);
        result.success("查询成功");
        return result;
    }

    @PostMapping("/manualSettle")
    @ApiOperation(value = "结算管理-手工结算", notes = "结算管理-手工结算 字段必须包含 客户id, 合同id, 当前表底, 当前净用水, 当前水价, 当前花费")
    public Result<?> manualSettle(@RequestBody List<SettlementStatisticsVo> settlementList){
        this.settlementStatisticsService.manualSettle(settlementList);
        return Result.ok("结算完成");
    }

}
