package com.zcdy.dsc.modules.settle.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.modules.settle.param.CustometInfoParam;
import com.zcdy.dsc.modules.settle.service.SettleBatchService;
import com.zcdy.dsc.modules.settle.vo.CustomerInfoData;
import com.zcdy.dsc.modules.settle.vo.CustomerInfoData.CustomerCount;
import com.zcdy.dsc.modules.settle.vo.CustomerInfoData.CustomerMonthInfo;
import com.zcdy.dsc.modules.settle.vo.SettlementStatisticsVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 客户信息查询
 * 
 * @author songguang.jiao
 * @date 2020/05/11 17:51:16
 */
@Api(tags = "客户信息查询")
@RestController
@RequestMapping("/settle/customerInfo")
public class CustomerInfoController {

	@Resource
	private SettleBatchService settleBatchService;

	/**
	 * 客户信息查询
	 * 
	 * @return
	 */
	@ApiOperation(value = "客户信息查询", notes = "客户信息查询")
	@GetMapping("/")
	public Result<CustomerInfoData> data(CustometInfoParam param) {
		Result<CustomerInfoData> result = new Result<>();
		CustomerInfoData customerInfoData = new CustomerInfoData();
		List<CustomerMonthInfo> customerMonthInfos = settleBatchService
				.queryMonthInfo(param);
		List<CustomerCount> customerCounts = settleBatchService
				.customerCount(param);
		customerInfoData.setCustomerCounts(customerCounts);
		customerInfoData.setCustomerMonthInfos(customerMonthInfos);
		result.setResult(customerInfoData);
		return result.success("success");
	}
	
	/**
	 * 客户信息历史查询
	 * @param param
	 * @return
	 */
	@ApiOperation(value="客户信息历史查询",notes="客户信息历史查询")
	@GetMapping("/historyData")
	public Result<IPage<SettlementStatisticsVo>> historyData(CustometInfoParam param){
	    Result<IPage<SettlementStatisticsVo>> result=new Result<>();
	    Page<SettlementStatisticsVo> page=new Page<>(param.getPageNo(), param.getPageSize());
	    IPage<SettlementStatisticsVo> data= settleBatchService.queryHistory(page,param);
	    result.setResult(data);
	    return result.success("success");
	}
	
	/**
	 * 导出
	 * @param param
	 * @return
	 */
	@ApiOperation(value="导出")
	@GetMapping("/export")
	public ModelAndView export(CustometInfoParam param){
	    String title="客户信息";
	    List<SettlementStatisticsVo> data=settleBatchService.exportHistory(param);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime=sdf.format(new Date());
        String secondTitle="导出人:" + sysUser.getRealname()+"  导出时间："+nowTime;
        mv.addObject(NormalExcelConstants.FILE_NAME, title);
        mv.addObject(NormalExcelConstants.CLASS, SettlementStatisticsVo.class);
        mv.addObject(NormalExcelConstants.PARAMS,
                new ExportParams(title + "报表", secondTitle, title));
        mv.addObject(NormalExcelConstants.DATA_LIST, data);
        return mv;
	    
	}
}
