package com.zcdy.dsc.modules.settle.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.constant.WorkingStatus;
import com.zcdy.dsc.modules.configcentre.constant.SystemParamConstant;
import com.zcdy.dsc.modules.configcentre.entity.SystemConfig;
import com.zcdy.dsc.modules.configcentre.service.SystemParamService;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.settle.entity.Contract;
import com.zcdy.dsc.modules.settle.service.IContractService;
import com.zcdy.dsc.modules.settle.service.ISettleService;
import com.zcdy.dsc.modules.settle.vo.ContractParam;
import com.zcdy.dsc.modules.settle.vo.ContractVo;
import com.zcdy.dsc.modules.settle.vo.CustomerDropdown;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 描述: 合同管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-13
 * 版本号: V1.0
 */
@Api(tags="合同管理")
@RestController
@RequestMapping("/settle/contract")
public class ContractController extends BaseController<Contract, IContractService> {
	@Autowired
	private IContractService contractService;

	@Autowired
	private ISettleService settleService;

	@Resource
	private SystemParamService systemParamService;

	/**
	 * 分页列表查询
	 * @param pageNo
	 * @param pageSize
	 * @param contractSn
	 * @param contractName
	 * @param customerName
	 * @return
	 */
	@AutoLog(value = "合同管理-分页列表查询")
	@ApiOperation(value="合同管理-分页列表查询", notes="合同管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ContractVo>> queryPageList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												   String contractSn,String contractName,String customerName) {
		Result<IPage<ContractVo>> result=new Result<>();
		Page<ContractVo> page=new Page<>(pageNo, pageSize);
		IPage<ContractVo> contractList = settleService.queryContractList(page, contractSn,contractName,customerName);
		//查询告警时间,根据时间差计算是否报警
		SystemConfig systemConfig = systemParamService.getConfigByKey(SystemParamConstant.CONTRACT_REMIND_REMAINING_DAYS);
		List<ContractVo> records = contractList.getRecords();
		for (ContractVo contractVo : records) {
			//计算倒计时天,倒计时小于0时,就显示零天
			long days=LocalDateTime.now().until(DateUtil.convertDateToLocalDate(contractVo.getEndDate()), ChronoUnit.DAYS);
			if(systemConfig!=null && systemConfig.getConfigStatus().equals(WorkingStatus.WORKING) && days<=Long.parseLong(systemConfig.getConfigValue())){
				contractVo.setAlarmStatus(StatusConstant.ALARM);
			}else{
				contractVo.setAlarmStatus(StatusConstant.NO_ALARM);
			}
			if(days<=0){
				contractVo.setCountdownDays("0");
			}else{
				contractVo.setCountdownDays(String.valueOf(days));
			}
			if(!StringUtils.isEmpty(contractVo.getFileUrl())){
			    contractVo.setFileUrl(baseStoragePath+contractVo.getFileUrl());
			}
		}
		result.setResult(contractList);
		return result.success();
	}


	/**
	 * 添加
	 * @param contractParamVo
	 * @return
	 */
	@AutoLog(value = "合同管理-添加")
	@ApiOperation(value="合同管理-添加", notes="合同管理-添加")
	@PostMapping(value = "/add")
	@RequiresPermissions("contract:add")
	public Result<Object> add(@RequestBody @Valid ContractParam contractParamVo) {
		Contract contract =new Contract();
		Boolean exist= settleService.checkSnExist(contractParamVo.getId(),contractParamVo.getContractSn());
		if(exist){
			return Result.error("合同编号不能重复");
		}
		Integer count= settleService.checkCustomeExsit(contractParamVo.getCustomerId());
		if(count<1){
			return	Result.error("客户信息输入错误，请重新选择");
		}
		BeanUtil.copyProperties(contractParamVo, contract);
		contract.setDelFlag(DelFlagConstant.NORMAL);
		contractService.save(contract);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 * @param contractParamVo
	 * @return
	 */
	@AutoLog(value = "合同管理-编辑")
	@ApiOperation(value="合同管理-编辑", notes="合同管理-编辑")
	@PutMapping(value = "/edit")
	@RequiresPermissions("contract:edit")
	public Result<Object> edit(@RequestBody ContractParam contractParamVo) {
		Boolean exist= settleService.checkSnExist(contractParamVo.getId(),contractParamVo.getContractSn());
		if(exist){
			return Result.error("合同编号不能重复");
		}
		String message= settleService.editContract(contractParamVo);
		return Result.ok(message);
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@RequiresPermissions("contract:delete")
	@AutoLog(value = "合同管理-通过id删除")
	@ApiOperation(value="合同管理-通过id删除", notes="合同管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<Object> delete(@RequestParam(name="id",required=true) @NotBlank(message="合同id不能为空") String id) {
		settleService.deleteContract(id);
		return Result.ok("删除成功!");
	}



	/**
	 * 描述: 合同管理-查询合同详情
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月13日 下午1:44:55
	 * 版本号: V1.0
	 */
	@ApiOperation(value="合同管理-查询合同详情", notes="合同管理-查询合同详情")
	@GetMapping("/queryDetail")
	public Result<ContractVo> queryDetailById(String id){
		Result<ContractVo> result=new Result<>();
		ContractVo contractDetailVo= settleService.queryDetailById(id);
		contractDetailVo.setFileUrl(baseStoragePath+contractDetailVo.getFileUrl());
		result.setResult(contractDetailVo);
		return result.success();
	}

	/**
	 * 描述: 客户名称下拉列表
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月13日 下午2:25:14
	 * 版本号: V1.0
	 */
	@ApiOperation(value="客户名称下拉列表",notes="客户名称下拉列表")
	@GetMapping("/queryCustomerNames")
	public Result<List<CustomerDropdown>> queryCustomerNames(String customerName){
		Result<List<CustomerDropdown>> result=new Result<>();
		List<CustomerDropdown> customerNamesList= settleService.queryCustomerNames(customerName);
		result.setCode(CommonConstant.SC_OK_200);
		result.setResult(customerNamesList);
		return result;
	}

}
