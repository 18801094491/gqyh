package com.zcdy.dsc.modules.settle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.modules.settle.service.ManualFlowService;
import com.zcdy.dsc.modules.settle.service.SettlementStatisticsService;
import com.zcdy.dsc.modules.settle.vo.ManualContractInfoVo;
import com.zcdy.dsc.modules.settle.vo.ManualFlowFormVo;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsQueryParam;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tangchao
 * @date 2020/5/11
 */
@Api(tags = "结算管理")
@RestController()
@RequestMapping("/settle/manual")
public class ManualFlowController {
    @Resource
    private SettlementStatisticsService settlementStatisticsService;

    @Resource
    private ManualFlowService manualFlowService;

    /**
     * 查询
     * 1.未结算过的手工录入
     * @param param 查询参数
     * @return 数据
     */
    @ApiOperation(value = "非远程水表手动录入-分页列表展示", notes = "非远程水表手动录入-分页列表展示")
    @GetMapping
    public Result<IPage<SettlementStatisticsVo>> queryPageData(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                               SettlementStatisticsQueryParam param
    ) {
        Result<IPage<SettlementStatisticsVo>> result = new Result<>();
        Page<SettlementStatisticsVo> page = new Page<>(pageNo, pageSize);
        IPage<SettlementStatisticsVo> data = this.settlementStatisticsService.pageSettle(page, param);
        result.setResult(data);
        return result;
    }

    /**
     * 新增
     * @param manualFlowAddVo 保存数据
     * @return 结果
     */
    @ApiOperation(value = "非远程水表手动录入-新增", notes = "非远程水表手动录入-新增")
    @PostMapping("/add")
    public Result<?> add(@RequestBody ManualFlowFormVo manualFlowAddVo) {
        this.manualFlowService.add(manualFlowAddVo);
        return Result.ok("保存成功");
    }

    /**
     * 修改数据
     * @param manualFlowAddVo 修改数据
     * @return 结果
     */
    @ApiOperation(value = "非远程水表手动录入-修改", notes = "非远程水表手动录入-修改")
    @PostMapping("/edit/{id}")
    public Result<?> edit(@PathVariable("id") String id, @RequestBody ManualFlowFormVo manualFlowAddVo) {
        if(StringUtils.isNotBlank(id)){
            manualFlowAddVo.setId(id);
        }
        this.manualFlowService.edit(manualFlowAddVo);

        return Result.ok("保存成功");
    }
    @ApiOperation(value = "非远程水表手动录入-合同选择", notes = "非远程水表手动录入-合同选择")
    @GetMapping("/contracts")
    public Result<List<ManualContractInfoVo>> listContractByOptDate(String equipmentId, String date){
        List<ManualContractInfoVo> manualContractInfoVos = this.manualFlowService.listContractByOptDate(equipmentId, date);
        return Result.ok(manualContractInfoVos);
    }

}
