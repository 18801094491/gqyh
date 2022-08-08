package com.zcdy.dsc.modules.message.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.modules.datacenter.statistic.service.SmsUsersService;
import com.zcdy.dsc.modules.message.entity.SmsTemplateConfig;
import com.zcdy.dsc.modules.message.param.SmsTemplateConfigPageParam;
import com.zcdy.dsc.modules.message.param.SmsTemplateConfigParam;
import com.zcdy.dsc.modules.message.service.SmsTemplateConfigService;
import com.zcdy.dsc.modules.message.vo.SmsTemplateConfigVo;
import com.zcdy.dsc.modules.message.vo.SmsTemplateDropdown;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 短信模板配置
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-13 09:15:17
 * @Version: V1.0
 */
@Api(tags="短信模板配置")
@RestController
@RequestMapping("/message/smsTemplateConfig")
public class SmsTemplateConfigController extends BaseController<SmsTemplateConfig, SmsTemplateConfigService> {
	@Resource
	private SmsTemplateConfigService smsTemplateConfigService;
	
	@Resource
	private SmsUsersService smsUsersService;
	
	/**
	 * 分页列表查询
	 *
	 * @param smsTemplateConfig
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "短信模板配置-分页列表查询")
	@ApiOperation(value="短信模板配置-分页列表查询", notes="短信模板配置-分页列表查询")
	@GetMapping
	public Result<IPage<SmsTemplateConfigVo>> queryPageList(SmsTemplateConfigPageParam param) {
		Result<IPage<SmsTemplateConfigVo>> result=new Result<>();
		Page<SmsTemplateConfigVo> page = new Page<SmsTemplateConfigVo>(param.getPageNo(), param.getPageSize());
		IPage<SmsTemplateConfigVo> pageList= smsTemplateConfigService.queryPageData(page,param);
		result.setResult(pageList);
		return result.success("success");
	}
	
	/**
	 * 添加或修改
	 * @param smsTemplateConfig
	 * @return
	 */
	@AutoLog(value = "短信模板配置-添加或修改")
	@ApiOperation(value="短信模板配置-添加或修改", notes="短信模板配置-添加或修改")
	@PostMapping(value = "one")
	public Result<Object> add(@RequestBody SmsTemplateConfigParam smsTemplateConfig) {
		smsTemplateConfigService.one(smsTemplateConfig);
		return Result.ok("保存成功！");
	}
	
	/**
	 * 描述: 更改启停用状态
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月13日 上午11:17:31
	 * 版本: V1.0
	 */
	@ApiOperation(value="更改启停用状态",notes="更改启停用状态")
	@GetMapping("/editStatus")
	public Result<Object> editStatus(String id) {
		smsTemplateConfigService.editStatus(id);
		return Result.ok("更新成功");
	}
	
	/**
	 * 描述: 导出
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月13日 下午3:39:04
	 * 版本: V1.0
	 */
	@ApiOperation(value="导出",notes="导出")
	@GetMapping("/export")
	public ModelAndView export(SmsTemplateConfigPageParam param){
		//子标题
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String secondTitle="导出人:" + sysUser.getRealname()+"  导出时间："+nowTime;
		String sheetName="短信模板配置";
		List<SmsTemplateConfigVo> exportList = smsTemplateConfigService.export(param);
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, sheetName);
		mv.addObject(NormalExcelConstants.CLASS, SmsTemplateConfigVo.class);
		mv.addObject(NormalExcelConstants.PARAMS,
				new ExportParams(sheetName + "报表",secondTitle, sheetName));
		mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
		return mv;
	}
	
	/**
	 * 描述: 短信模板下拉选
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月14日 上午9:55:30
	 * 版本: V1.0
	 */
	@ApiOperation(value="短信模板下拉选",notes="短信模板下拉选")
	@GetMapping("/dropdown")
	public Result<List<SmsTemplateDropdown>> queryDropdown(String templateName){
		Result<List<SmsTemplateDropdown>>  result=new Result<>();
		List<SmsTemplateDropdown> list=smsTemplateConfigService.queryDropdown(templateName);
		result.setResult(list);
		result.success("succes");
		return result;
	}
}