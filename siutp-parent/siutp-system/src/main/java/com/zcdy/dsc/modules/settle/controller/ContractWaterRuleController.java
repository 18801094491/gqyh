package com.zcdy.dsc.modules.settle.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.common.util.RegexUtils;
import com.zcdy.dsc.modules.settle.constant.ContactRuleTypeConstant;
import com.zcdy.dsc.modules.settle.entity.Contract;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRule;
import com.zcdy.dsc.modules.settle.entity.ContractWaterRuleItem;
import com.zcdy.dsc.modules.settle.param.ContractWaterRulePageParam;
import com.zcdy.dsc.modules.settle.service.ContractWaterRuleItemService;
import com.zcdy.dsc.modules.settle.service.ContractWaterRuleService;
import com.zcdy.dsc.modules.settle.service.IContractService;
import com.zcdy.dsc.modules.settle.vo.ContractWaterRuleItemVo;
import com.zcdy.dsc.modules.settle.vo.ContractWaterRuleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

 /**
 * @Description: 合同水价规则
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-22 18:13:17
 * @Version: V1.0
 */
@Api(tags="合同水价规则")
@RestController
@RequestMapping("/settle/contractWaterRule")
public class ContractWaterRuleController extends BaseController<ContractWaterRule, ContractWaterRuleService> {
	@Resource
	private ContractWaterRuleService contractWaterRuleService;
	
	@Resource
	private ContractWaterRuleItemService contractWaterRuleItemService;
	
	@Resource
	private IContractService contractService;
	
	/**
	 * 分页列表查询
	 *
	 * @param contractWaterRule
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "合同水价规则-分页列表查询")
	@ApiOperation(value="合同水价规则-分页列表查询", notes="合同水价规则-分页列表查询")
	@GetMapping
	public Result<IPage<ContractWaterRuleVo>> queryPageList(@Valid ContractWaterRulePageParam param) {
		Result<IPage<ContractWaterRuleVo>> result=new Result<>();
		Page<ContractWaterRuleVo> page=new Page<>(param.getPageNo(), param.getPageSize());
		IPage<ContractWaterRuleVo> data= contractWaterRuleService.queryPageData(page,param);
		result.setResult(data);
		return result.success("success");
	}
	
	/**
	 * 添加或修改
	 * @param contractWaterRule
	 * @return
	 */
	@AutoLog(value = "合同水价规则-添加或修改")
	@ApiOperation(value="合同水价规则-添加或修改", notes="合同水价规则-添加或修改")
	@PostMapping(value = "one")
	public Result<Object> one(@RequestBody @Valid ContractWaterRuleItemVo contractWaterRule) {
		//查出合同信息
		Contract contract = contractService.getOne(Wrappers.lambdaQuery(new Contract()).eq(Contract::getId, contractWaterRule.getContractId()));
		if(contract==null){
			return Result.error("没有对应合同信息");
		}
		//如果是阶梯价格的话,判断非空
		if(contractWaterRule.getRuleType().equals(ContactRuleTypeConstant.STEP_RULE)){
			if(StringUtils.isEmpty(contractWaterRule.getBenchPrice())|| StringUtils.isEmpty(contractWaterRule.getSetUp())
					|| contractWaterRule.getSetTime()==null){
				return Result.error("阶梯价格中基准水价、阶梯增幅、阶梯时间都不能为空");
			}
			if(!RegexUtils.checkPositiveInteger(contractWaterRule.getSetTime())){
				return Result.error("阶梯时间只能为正整数");
			}
			if(!RegexUtils.checkDecimals(contractWaterRule.getBenchPrice()) ||!RegexUtils.checkDecimals(contractWaterRule.getSetUp())){
				return Result.error("阶梯价格中基准水价、阶梯增幅都必须位数字");
			}
			
		}else{
			if(contractWaterRule.getItems().size()<=0){
				return Result.error("手动录入价格中至少输入1行数据");
			}
			
			List<ContractWaterRuleItem> items = contractWaterRule.getItems();
			//对比时间是否按照相邻日期传递
			LocalDate templocalDate=LocalDate.now();
			for (int i = 0; i < items.size(); i++) {
				ContractWaterRuleItem item = items.get(i);
				
				//起始时间跟结束时间比较
				if(item.getEndTime().compareTo(item.getStartTime())<0){
					return Result.error("结束时间必须大于等于起始时间");
				}
				
				//判断第一次起始时间是否等于合同生效日期
				if(i==0){
					if(!DateUtil.convertDateToLocalDate(item.getStartTime()).isEqual(DateUtil.convertDateToLocalDate(contract.getStartDate()))){
						return Result.error("起始时间必须等于合同生效日期");
					}
					templocalDate=DateUtil.convertDateToLocalDate(item.getEndTime()).toLocalDate();
				}
				
				//区间结束时间+1需要等于下一区间开始时间
				if(i>0){
					if(!templocalDate.plusDays(1).isEqual(DateUtil.convertDateToLocalDate(item.getStartTime()).toLocalDate())){
						return Result.error("区间结束时间+1需要等于下一区间开始时间");
					}
					//最后一次判断结束日期是否等于合同终止日期
					if(i==items.size()-1){
						if(!DateUtil.convertDateToLocalDate(item.getEndTime()).isEqual(DateUtil.convertDateToLocalDate(contract.getEndDate()))){
							return Result.error("结束时间必须等于合同终止日期");
						}
					}
					templocalDate=DateUtil.convertDateToLocalDate(item.getEndTime()).toLocalDate();
				}
				
				
				
			}	
		}
		 contractWaterRuleService.one(contractWaterRule,contract);
		return Result.ok("保存成功");
	}
	
	/**
	 * 描述: 详情查看
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月23日 上午11:53:33
	 * 版本: V1.0
	 */
	@GetMapping("/detail")
	@ApiOperation(value="详情查看",notes="详情查看")
	@ApiImplicitParams(
			@ApiImplicitParam(name = "ruleId",value="规则id",paramType="query")
			)
	public Result<ContractWaterRuleItemVo> detail(String ruleId){
		Result<ContractWaterRuleItemVo>  result=new Result<>();
		if(StringUtils.isEmpty(ruleId)){
			return result.error500("规则id不能为空");
		}
		ContractWaterRuleItemVo vo =new ContractWaterRuleItemVo();
		LambdaQueryWrapper<ContractWaterRule> queryWrapper = Wrappers.lambdaQuery(new ContractWaterRule()).eq(ContractWaterRule::getId, ruleId);
		ContractWaterRule waterRule = contractWaterRuleService.getOne(queryWrapper);
		if(waterRule==null){
			return result.error500("没有对应规则");
		}
		BeanUtil.copyProperties(waterRule, vo);
		if(vo.getRuleType().equals(ContactRuleTypeConstant.HAND_RULE)){
			LambdaQueryWrapper<ContractWaterRuleItem> wrapper = Wrappers.lambdaQuery(new ContractWaterRuleItem()).eq(ContractWaterRuleItem::getRuleId, ruleId);
			List<ContractWaterRuleItem> list = contractWaterRuleItemService.list(wrapper);
			vo.setItems(list);
		}
		result.setResult(vo);
		return result.success("success");
	}

}