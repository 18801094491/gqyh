package com.zcdy.dsc.modules.operation.equipment.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.vo.DictModel;
import com.zcdy.dsc.modules.operation.equipment.entity.OptApplyVerify;
import com.zcdy.dsc.modules.operation.equipment.entity.OptGoodsApply;
import com.zcdy.dsc.modules.operation.equipment.service.OptApplyVerifyService;
import com.zcdy.dsc.modules.operation.equipment.service.OptGoodsApplyService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptApplyVerifyAddVo;
import com.zcdy.dsc.modules.operation.equipment.vo.OptApplyVerifyVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 描述: 申请审核历史
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-05 11:23:06
 * 版本号: V1.0
 */
@Api(tags="申请审核历史")
@RestController
@RequestMapping("/equipment/optApplyVerify")
public class OptApplyVerifyController extends BaseController<OptApplyVerify, OptApplyVerifyService> {
	@Autowired
	private OptApplyVerifyService optApplyVerifyService;
	@Autowired
	private OptGoodsApplyService optGoodsApplyService;

	/**
	 *  查看审核历史
	 * @param id
	 * @return
	 */
	@AutoLog(value = "查看审核历史--根据申请表id")
	@ApiOperation(value="查看审核历史--根据申请表id", notes="查看审核历史--根据申请表id")
	@GetMapping(value = "/{id}")
	public Result<Object> queryAuditById(@PathVariable("id") String id) {
		LinkedList<OptApplyVerifyVo> optApplyVerify = optApplyVerifyService.getByApplyId(id);
		return Result.ok(optApplyVerify);
	}
	/**
	 *  查看审核结果
	 * @return
	 */
	@AutoLog(value = "查看--审核都有哪几种结果")
	@ApiOperation(value="查看--审核都有哪几种结果", notes="查看--审核都有哪几种结果")
	@GetMapping(value = "/queryAuditStatus")
	public Result<Object> queryAuditStatus() {
		List<DictModel> dictModels = optApplyVerifyService.queryAuditStatus();
		return Result.ok(dictModels);
	}
	/**
	 * 添加或编辑
	 * @param optApplyVerify
	 * @return
	 */
	@AutoLog(value = "申请审核历史-添加")
	@ApiOperation(value="申请审核历史-添加", notes="申请审核历史-添加")
	@PostMapping(value = "one")
	public Result<?> add(@RequestBody OptApplyVerifyAddVo optApplyVerify) {
		OptApplyVerify applyVerify = new OptApplyVerify();
		BeanUtils.copyProperties(optApplyVerify,applyVerify);
		OptGoodsApply apply = new OptGoodsApply();
		apply.setId(optApplyVerify.getApplyId());
		apply.setVerifyStatus(optApplyVerify.getVerifyStatus());
		optGoodsApplyService.updateById(apply);
		optApplyVerifyService.save(applyVerify);
		return Result.ok("保存成功！");
	}
}