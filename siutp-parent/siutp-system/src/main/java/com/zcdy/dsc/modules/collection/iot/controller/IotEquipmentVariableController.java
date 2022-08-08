package com.zcdy.dsc.modules.collection.iot.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.query.QueryGenerator;
import com.zcdy.dsc.modules.collection.iot.entity.IotEquipmentVariable;
import com.zcdy.dsc.modules.collection.iot.service.IotEquipmentVariableService;
import com.zcdy.dsc.modules.collection.iot.vo.IotEquipmentVariableVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * 描述: 采集设备变量绑定
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-26 10:51:40
 * 版本号: V1.0
 */
@Api(tags="采集设备变量绑定")
@RestController
@RequestMapping("/equipment/iotEquipmentVariable")
public class IotEquipmentVariableController extends BaseController<IotEquipmentVariable, IotEquipmentVariableService> {
	@Autowired
	private IotEquipmentVariableService iotEquipmentVariableService;
	
	/**
	 * 分页列表查询
	 *
	 * @param iotEquipmentVariable
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采集设备变量绑定-分页列表查询")
	@ApiOperation(value="采集设备变量绑定-分页列表查询", notes="采集设备变量绑定-分页列表查询")
	@GetMapping
	public Result<IPage<IotEquipmentVariable>> queryPageList(IotEquipmentVariable iotEquipmentVariable,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<IotEquipmentVariable> queryWrapper = QueryGenerator.initQueryWrapper(iotEquipmentVariable, req.getParameterMap());
		Page<IotEquipmentVariable> page = new Page<IotEquipmentVariable>(pageNo, pageSize);
		IPage<IotEquipmentVariable> pageList = iotEquipmentVariableService.page(page, queryWrapper);
		Result<IPage<IotEquipmentVariable>>result=new Result<>();
		result.setCode(CommonConstant.SC_OK_200);
		result.setResult((IPage<IotEquipmentVariable>) pageList);
		return result;
	}


	 @AutoLog(value = "采集设备变量绑定-变量分页列表查询")
	 @ApiOperation(value="采集设备变量绑定-变量分页列表查询", notes="采集设备变量绑定-变量分页列表查询")
	 @GetMapping(value="/queryPageList")
	 public Result<IPage<IotEquipmentVariableVo>> queryVariableList(String iotId,String variableName,int status,
															  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															  HttpServletRequest req) {
		 Page<IotEquipmentVariableVo> page = new Page<IotEquipmentVariableVo>(pageNo, pageSize);
		 IPage<IotEquipmentVariableVo> pageList = iotEquipmentVariableService.queryVariableList(page, iotId,variableName,status);
		 Result<IPage<IotEquipmentVariableVo>>result=new Result<>();
		 result.setCode(CommonConstant.SC_OK_200);
		 result.setResult( pageList);
		 return result;
	 }



	/**
	 * 添加或修改
	 * @param iotEquipmentVariable
	 * @return
	 */
	@AutoLog(value = "采集设备变量绑定-添加或修改")
	@ApiOperation(value="采集设备变量绑定-添加或修改", notes="采集设备变量绑定-添加或修改")
	@PostMapping(value = "one")
	public Result<?> add(@RequestBody IotEquipmentVariable iotEquipmentVariable) {
		if(!StringUtils.isEmpty(iotEquipmentVariable.getId())){
			iotEquipmentVariableService.updateById(iotEquipmentVariable);
		}else{
			iotEquipmentVariableService.save(iotEquipmentVariable);
		}
		return Result.ok("保存成功！");
	}
	
	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采集设备变量绑定-通过id删除")
	@ApiOperation(value="采集设备变量绑定-通过id删除", notes="采集设备变量绑定-通过id删除")
	@GetMapping(value = "/deleteById")
	public Result<?> delete(String id) {
		iotEquipmentVariableService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "采集设备变量绑定-解绑")
	@ApiOperation(value="采集设备变量绑定-解绑", notes="采集设备变量绑定-解绑")
	@GetMapping(value = "/many")
	public Result<?> deleteBatch( String id) {
		this.iotEquipmentVariableService.removeByIotId(id);
		return Result.ok("解绑成功");
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采集设备变量绑定-通过id查询")
	@ApiOperation(value="采集设备变量绑定-通过id查询", notes="采集设备变量绑定-通过id查询")
	@GetMapping(value = "/{id}")
	public Result<?> queryById(@PathVariable("id") String id) {
		IotEquipmentVariable iotEquipmentVariable = iotEquipmentVariableService.getById(id);
		return Result.ok(iotEquipmentVariable);
	}


}