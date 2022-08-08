package com.zcdy.dsc.modules.settle.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.vo.DictModel;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.settle.entity.SettleUseralot;
import com.zcdy.dsc.modules.settle.service.SettleUseralotService;
import com.zcdy.dsc.modules.settle.vo.SettleUseralotVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 描述: 用户报装信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-11 10:13:58
 * 版本号: V1.0
 */
@Api(tags="用户报装信息")
@RestController
@RequestMapping("/settle/settleUseralot")
public class SettleUseralotController extends BaseController<SettleUseralot, SettleUseralotService> {
	@Autowired
	private SettleUseralotService settleUseralotService;
	
	/**
	 * 分页列表查询
	 *
	 * @param customerName
	 * @param pageNo
	 * @param pageSize
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@AutoLog(value = "用户报装信息-分页列表查询")
	@ApiOperation(value="用户报装信息-分页列表查询", notes="用户报装信息-分页列表查询")
	@GetMapping
	@ApiImplicitParams({
			@ApiImplicitParam(name="pageNo",value="页码",paramType="query"),
			@ApiImplicitParam(name="pageSize",value="每页显示条数",paramType="query"),
			@ApiImplicitParam(name="customerName",value="客户名称",paramType="query"),
			@ApiImplicitParam(name="startTime",value="开始时间",paramType="query"),
			@ApiImplicitParam(name="endTime",value="结束时间",paramType="query")
	})
	public Result<IPage<SettleUseralotVo>> queryPageList(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String customerName,String startTime ,String endTime) {
        Page<SettleUseralotVo> page = new Page<SettleUseralotVo>(pageNo, pageSize);
        Result<IPage<SettleUseralotVo>> result = new Result<IPage<SettleUseralotVo>>();
        IPage<SettleUseralotVo> pageList = settleUseralotService.getUseralotInfo(page,customerName,startTime,endTime);
        result.setResult(pageList);
        result.setSuccess(true);
        result.setCode(200);
        return result;
	}
	
	/**
	 * 添加或修改
	 * @param settleUseralot
	 * @return
	 */
	@AutoLog(value = "用户报装信息-添加或修改")
	@ApiOperation(value="用户报装信息-添加或修改", notes="用户报装信息-添加或修改")
	@PostMapping(value = "one")
	public Result<Object> add(@RequestBody SettleUseralotVo settleUseralot) {
        SettleUseralot useralot = new SettleUseralot();
        BeanUtils.copyProperties(settleUseralot,useralot);
        if(!StringUtils.isEmpty(useralot.getId())){
			settleUseralotService.updateById(useralot);
		}else{
        	//初始化未安装
			useralot.setInstallState("0");
			useralot.setDelFlag(DelFlagConstant.NORMAL);
			settleUseralotService.save(useralot);
		}
		return Result.ok("保存成功！");
	}

	/**
	 * @return
	 */
	@AutoLog(value = "查看安装状态")
	@ApiOperation(value="查看安装状态", notes="查看安装状态")
	@GetMapping(value = "/queryByStatus")
	@ApiImplicitParams({
			@ApiImplicitParam(name="code",value="传入 install_state",paramType="query")
	})
	public Result<List<DictModel>> queryByStatus(String code) {
		List<DictModel> dictModels = settleUseralotService.queryByStatus(code);
		Result<List<DictModel>> result = new Result<List<DictModel>>();
		result.setResult(dictModels);
		result.setSuccess(true);
		result.setCode(200);
		return result;
	}


  /**
      * 导出excel
      */
     @RequestMapping(value = "/exportXls")
     public ModelAndView exportXls(String customerName,String startTime,String endTime) {
		 List<SettleUseralotVo> pageList = settleUseralotService.getExportXls(customerName,startTime,endTime);
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String nowTime=sdf.format(new Date());
		 String secondTitle="导出人:" + sysUser.getRealname()+"  导出时间："+nowTime;
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, "用户报装信息管理");
		 mv.addObject(NormalExcelConstants.CLASS, SettleUseralotVo.class);
		 mv.addObject(NormalExcelConstants.PARAMS,
				 new ExportParams("用户报装信息管理" + "报表", secondTitle, "用户报装信息"));
		 mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		 return mv;
	 }


}