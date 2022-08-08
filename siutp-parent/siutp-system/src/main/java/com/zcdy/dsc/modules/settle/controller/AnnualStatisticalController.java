package com.zcdy.dsc.modules.settle.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.modules.settle.entity.SettleCustomer;
import com.zcdy.dsc.modules.settle.service.AnnualStatisticalService;
import com.zcdy.dsc.modules.settle.service.ISettleCustomerService;
import com.zcdy.dsc.modules.settle.vo.AnnualStatisticalQueryParmVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 年度统计
 *
 * @author tangchao
 * @date 2020/5/25
 */
@Api(tags = "结算管理")
@RestController
@RequestMapping("/settle/statistical")
public class AnnualStatisticalController {
    @Resource
    private AnnualStatisticalService annualStatisticalService;

    @Resource
    private ISettleCustomerService settleCustomerService;

    @GetMapping
    @ApiOperation(value = "年度统计-首页", tags = "年度统计-首页")
    public Result<List<Map<String, String>>> index(AnnualStatisticalQueryParmVo param) {
        List<Map<String, String>> index = annualStatisticalService.index(param);
        return Result.ok(index);
    }

    @GetMapping("/customers")
    @ApiOperation(value = "年度统计-客户下拉查询", tags = "年度统计-客户下拉查询")
    public List<SettleCustomer> customerSelecter(String customerType) {
        QueryWrapper<SettleCustomer> query = new QueryWrapper<>();
        if(StringUtils.isNotBlank(customerType)){
            query.lambda().eq(SettleCustomer::getCustomerType, customerType);
        }
        query.lambda().eq(SettleCustomer::getDelFlag, "0");
        return settleCustomerService.list(query);
    }

}
