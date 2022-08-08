package com.zcdy.dsc.modules.collection.gis.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.query.QueryGenerator;
import com.zcdy.dsc.modules.collection.gis.entity.GisEquipment;
import com.zcdy.dsc.modules.collection.gis.service.IGisEquipmentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * 描述: 模型设备
 * @author： 智能无人硬核心项目组
 * 创建时间：   2019-12-17
 * 版本号: V1.0
 */
@Api(tags="模型设备")
@RestController
@RequestMapping("/equipment/gisEquipment")
public class GisEquipmentController extends BaseController<GisEquipment, IGisEquipmentService> {
	@Autowired
	private IGisEquipmentService gisEquipmentService;
	
	/**
	 * 分页列表查询
	 *
	 * @param gisEquipment
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "模型设备-分页列表查询")
	@ApiOperation(value="模型设备-分页列表查询", notes="模型设备-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(GisEquipment gisEquipment,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<GisEquipment> queryWrapper = QueryGenerator.initQueryWrapper(gisEquipment, req.getParameterMap());
		Page<GisEquipment> page = new Page<GisEquipment>(pageNo, pageSize);
		IPage<GisEquipment> pageList = gisEquipmentService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param gisEquipment
	 * @return
	 */
	@AutoLog(value = "模型设备-添加")
	@ApiOperation(value="模型设备-添加", notes="模型设备-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody GisEquipment gisEquipment) {
		gisEquipmentService.save(gisEquipment);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param gisEquipment
	 * @return
	 */
	@AutoLog(value = "模型设备-编辑")
	@ApiOperation(value="模型设备-编辑", notes="模型设备-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody GisEquipment gisEquipment) {
		gisEquipmentService.updateById(gisEquipment);
		return Result.ok("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "模型设备-通过id删除")
	@ApiOperation(value="模型设备-通过id删除", notes="模型设备-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		gisEquipmentService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "模型设备-批量删除")
	@ApiOperation(value="模型设备-批量删除", notes="模型设备-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.gisEquipmentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "模型设备-通过id查询")
	@ApiOperation(value="模型设备-通过id查询", notes="模型设备-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		GisEquipment gisEquipment = gisEquipmentService.getById(id);
		return Result.ok(gisEquipment);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param gisEquipment
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, GisEquipment gisEquipment) {
      return super.exportXls(request, gisEquipment, GisEquipment.class, "模型设备");
  }

  /**
   * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      return super.importExcel(request, response, GisEquipment.class);
  }

}
