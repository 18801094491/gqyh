package com.zcdy.dsc.modules.settle.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.modules.settle.entity.Contract;
import com.zcdy.dsc.modules.settle.entity.ContractWater;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRule;
import com.zcdy.dsc.modules.settle.service.ContractWaterService;
import com.zcdy.dsc.modules.settle.service.IContractService;
import com.zcdy.dsc.modules.settle.vo.ContractWaterVo;
import com.zcdy.dsc.modules.settle.vo.CustomerWaterInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 合同绑定水表信息
 * @author tangchao
 * @date 2020-04-22 18:14:14
 * @version V1.0
 */
@Api(tags = "合同绑定水表信息")
@RestController
@RequestMapping("/settle/contractWater")
public class ContractWaterController extends BaseController<ContractWater, ContractWaterService> {
    @Resource
    private ContractWaterService contractWaterService;


    private final IContractService contractService;


    @Autowired
    public ContractWaterController(IContractService contractService){
        this.contractService = contractService;
    }

    /**
     * 分页列表查询
     *
     * @param pageNo 页数
     * @param pageSize 页大小
     * @return 返回合同绑定水表信息
     */
    @AutoLog(value = "合同绑定水表信息-分页列表查询")
    @ApiOperation(value = "合同绑定水表信息-分页列表查询", notes = "合同绑定水表信息-分页列表查询")
    @GetMapping
    public Result<?> queryPageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam(name = "contractId") String contractId
    ) {
        Page<ContractWaterVo> page = new Page<>(pageNo, pageSize);
        IPage<ContractWaterVo> pageList = contractWaterService.selecContractWatertPage(page, contractId);
        return Result.ok(pageList);
    }

    /**
     * 获取客户绑定水表单选框
     * @return 处理结果
     */
    @AutoLog(value = "合同绑定水表信息-获取客户绑定水表单选框")
    @ApiOperation(value = "合同绑定水表信息-获取客户绑定水表单选框", notes = "合同绑定水表信息-获取客户绑定水表单选框")
    @GetMapping("/queryCustomerBindedWater")
    public Result<?> queryCustomerBindedWater(@RequestParam(value = "customerId") String customerId) {
        Result<List<CustomerWaterInfoVo>> result = new Result<>();
        List<CustomerWaterInfoVo> list = this.contractWaterService.queryCustomerBindedWater(customerId);
        result.setResult(list);
        result.success("查询成功");
        return result;
    }

    /**
     * 获取规则
     * @return 处理结果
     */
    @AutoLog(value = "合同绑定水表信息-获取规则单选框下拉列表")
    @ApiOperation(value = "合同绑定水表信息-获取规则单选框下拉列表", notes = "合同绑定水表信息-获取规则单选框下拉列表")
    @GetMapping("/queryContractRule")
    public Result<?> queryContractRule(@RequestParam(value = "contractId") String contractId) {
        Result<List<ContractWaterRule>> result = new Result<>();
        List<ContractWaterRule> list = this.contractWaterService.queryContractRule(contractId);
        result.setResult(list);
        result.success("查询成功");
        return result;
    }

    /**
     * 合同绑定水表信息 绑定
     *
     * @param contractWater 水表参数
     * @return {
     * "contractId":"",
     * "ruleId": "",
     * "equipmentId", ""
     * }
     */
    @AutoLog(value = "合同绑定水表信息-绑定")
    @PostMapping(value = "/bind")
    @ApiOperation(value = "合同绑定水表信息-绑定", notes = "合同绑定水表信息-绑定")
    public Result<?> bind(@RequestBody ContractWater contractWater) {
        String contractId = contractWater.getContractId();
        Contract contract = this.contractService.getById(contractId);
      //关系实际失效日期为合同失效日期
        contractWater.setInvalidDate(contract.getEndDate());
        this.contractWaterService.bind(contractWater);
        return Result.ok("绑定成功！");
    }


    @AutoLog(value = "合同绑定水表信息-解绑")
    @GetMapping(value = "/unbind")
    @ApiOperation(value = "合同绑定水表信息-解绑", notes = "合同绑定水表信息-解绑")
    public Result<?> unbind(@RequestParam(name = "id") String id) {

        ContractWater contractWater = this.contractWaterService.getById(id);
        if(contractWater == null){
            return Result.error("合同水表绑定不存在!");
        }
        contractWater.setInvalidDate(DateUtil.convertLocalDateToDate(LocalDateTime.now()));
        this.contractWaterService.unbind(contractWater);

        return Result.ok("解绑成功！");
    }

    /**
     * 通过id删除
     *
     * @param id 合同水表绑定表 id
     * @return 处理结果
     */
    @AutoLog(value = "合同绑定水表信息-通过id删除")
    @ApiOperation(value = "合同绑定水表信息-通过id删除", notes = "合同绑定水表信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        contractWaterService.removeById(id);
        return Result.ok("删除成功!");
    }
}