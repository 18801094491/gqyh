package com.zcdy.dsc.modules.collection.iot.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.zcdy.dsc.modules.collection.iot.entity.AlarmRule;
import com.zcdy.dsc.modules.collection.iot.service.AlarmRuleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * 描述: 设备报警规则变量表
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02 15:39:20
 * 版本号: V1.0
 */
@Api(tags="设备报警规则变量表")
@RestController
@RequestMapping("/equipment/alarmRule")
public class AlarmRuleController extends BaseController<AlarmRule, AlarmRuleService> {
	@Autowired
	private AlarmRuleService alarmRuleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param alarmRule
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "设备报警规则变量表-分页列表查询")
	@ApiOperation(value="设备报警规则变量表-分页列表查询", notes="设备报警规则变量表-分页列表查询")
	@GetMapping
	public Result<?> queryPageList(AlarmRule alarmRule,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AlarmRule> queryWrapper = QueryGenerator.initQueryWrapper(alarmRule, req.getParameterMap());
		Page<AlarmRule> page = new Page<AlarmRule>(pageNo, pageSize);
		IPage<AlarmRule> pageList = alarmRuleService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加或修改
	 * @param alarmRule
	 * @return
	 */
	@AutoLog(value = "设备报警规则变量表-添加或修改")
	@ApiOperation(value="设备报警规则变量表-添加或修改", notes="设备报警规则变量表-添加或修改")
	@PostMapping(value = "one")
	public Result<?> add(@RequestBody AlarmRule alarmRule) {
		if(!StringUtils.isEmpty(alarmRule.getId())){
			alarmRuleService.updateById(alarmRule);
		}else{
			alarmRuleService.save(alarmRule);
		}
		return Result.ok("保存成功！");
	}
	
	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设备报警规则变量表-通过id删除")
	@ApiOperation(value="设备报警规则变量表-通过id删除", notes="设备报警规则变量表-通过id删除")
	@DeleteMapping(value = "{id}")
	public Result<?> delete(@PathVariable("id") String id) {
		alarmRuleService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "设备报警规则变量表-批量删除")
	@ApiOperation(value="设备报警规则变量表-批量删除", notes="设备报警规则变量表-批量删除")
	@DeleteMapping(value = "/many")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.alarmRuleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设备报警规则变量表-通过id查询")
	@ApiOperation(value="设备报警规则变量表-通过id查询", notes="设备报警规则变量表-通过id查询")
	@GetMapping(value = "/{id}")
	public Result<?> queryById(@PathVariable("id") String id) {
		AlarmRule alarmRule = alarmRuleService.getById(id);
		return Result.ok(alarmRule);
	}

  /**
   * 导出excel
   * @param request
   * @param alarmRule
   */
  @RequestMapping(value = "/export")
  public ModelAndView exportXls(HttpServletRequest request, AlarmRule alarmRule) {
      return super.exportXls(request, alarmRule, AlarmRule.class, "设备报警规则变量表");
  }

  /**
   * 通过excel导入数据
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/import", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      return super.importExcel(request, response, AlarmRule.class);
  }
}