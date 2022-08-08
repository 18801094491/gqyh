package com.zcdy.dsc.modules.operation.equipment.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.entity.OptSupplier;
import com.zcdy.dsc.modules.operation.equipment.service.IOptSupplierService;
import com.zcdy.dsc.modules.operation.equipment.vo.SupplierListVo;
import com.zcdy.dsc.modules.operation.equipment.vo.SupplierParamVo;
import com.zcdy.dsc.modules.operation.equipment.vo.SupplierVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

 /**
 * 描述: 供应商管理
 * @author： bthy
 * 创建时间：   2019-12-16
 * 版本号: V1.0
 */
@Api(tags="供应商管理")
@RestController
@RequestMapping("/equipment/optSupplier")
public class OptSupplierController extends BaseController<OptSupplier, IOptSupplierService> {
	@Autowired
	private IOptSupplierService optSupplierService;
	
	/**
	 * 分页列表查询
	 *
	 * @param optSupplier
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "供应商管理-分页列表查询")
	@ApiOperation(value="供应商管理-分页列表查询", notes="供应商管理-分页列表查询")
	@GetMapping(value = "/list")
	@ApiImplicitParams({
	 	@ApiImplicitParam(name="pageNo",value="页码",paramType="query"),
	    @ApiImplicitParam(name="pageSize",value="每页显示条数",paramType="query"),
	    @ApiImplicitParam(name="supplierSn",value="供应商编码",paramType="query"),
	 	@ApiImplicitParam(name="supplierName",value="供应商名称",paramType="query"),
	    @ApiImplicitParam(name="startTime",value="查询开始日期",paramType="query"),
	    @ApiImplicitParam(name="endTime",value="查询截至日期",paramType="query")
})
	public Result<IPage<SupplierVo>> queryPageList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String supplierSn,String supplierName,
								   String startTime,String endTime) {
		Page<SupplierVo> page = new Page<SupplierVo>(pageNo, pageSize);
		Result<IPage<SupplierVo>> result = new Result<IPage<SupplierVo>>();
		IPage<SupplierVo> list=optSupplierService.getList(page, supplierSn, supplierName, startTime, endTime);
		result.setResult(list);
		result.success("查询成功");
		return result;
	}
	
	/**
	 * 添加
	 *
	 * @param optSupplier
	 * @return
	 */
	@AutoLog(value = "供应商管理-添加")
	@ApiOperation(value="供应商管理-添加", notes="供应商管理-添加")
	@PostMapping(value = "/add")
	public Result<Object> add(@RequestBody SupplierParamVo supplierParamVo) {
		Boolean exist= optSupplierService.checkSnExist(null,supplierParamVo.getSupplierSn());
		if(exist){
			return Result.error("供应商编号已经存在");
		}
		OptSupplier optSupplier=new OptSupplier();
		BeanUtils.copyProperties(supplierParamVo, optSupplier);
		optSupplier.setDelFlag(DelFlagConstant.NORMAL);
		optSupplierService.save(optSupplier);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param optSupplier
	 * @return
	 */
	@AutoLog(value = "供应商管理-编辑")
	@ApiOperation(value="供应商管理-编辑", notes="供应商管理-编辑")
	@PostMapping(value = "/edit")
	public Result<Object> edit(@RequestBody SupplierParamVo supplierParamVo) {
		Boolean exist= optSupplierService.checkSnExist(supplierParamVo.getId(),supplierParamVo.getSupplierSn());
		if(exist){
			return Result.error("供应商编号已经存在");
		}
		OptSupplier optSupplier=new OptSupplier();
		BeanUtils.copyProperties(supplierParamVo, optSupplier);
		optSupplierService.updateById(optSupplier);
		return Result.ok("编辑成功!");
	}
	
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "供应商管理-通过id查询")
	@ApiOperation(value="供应商管理-通过id查询", notes="供应商管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<OptSupplier> queryById(@RequestParam(name="id",required=true) String id) {
		Result<OptSupplier> result = new Result<OptSupplier>();
		OptSupplier optSupplier= optSupplierService.getById(id);
		result.success("查询成功");
		result.setResult(optSupplier);
		return result;
	}

  /**
	 * 修改供应商状态
	 *
	 * @param optSupplier
	 * @return
	 */
	@AutoLog(value = "供应商状态修改-启停用")
	@ApiOperation(value="供应商状态修改-启停用", notes="供应商状态修改-启停用")
	@GetMapping(value = "/startupAndShutdown")
	@ApiImplicitParams({
		@ApiImplicitParam(name="supplierStatus",value="启停用状态 0-停用,1-启用")
	})
	public Result<Object> startupAndShutdown(@RequestParam(name="id",required=true) String id) {
		optSupplierService.startupAndShutdown(id);
		return Result.ok("修改成功!");
	}
	
	/**
	 * 查询供应商下拉列表
	 * @return
	 */
	@AutoLog(value="查询供应商下拉列表")
	@ApiOperation(value="查询供应商下拉列表",notes="查询供应商下拉列表")
	@GetMapping(value="/queryNameList")
	public Result<List<SupplierListVo>> queryNameList(String name){
		Result<List<SupplierListVo>> result=new Result<List<SupplierListVo>>();
		List<SupplierListVo> queryNameList = optSupplierService.queryNameList(name);
		result.success("success");
		result.setResult(queryNameList);
		return result;
	}

}
