package com.zcdy.dsc.modules.operation.work.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.work.entity.WorkShifts;
import com.zcdy.dsc.modules.operation.work.param.ShiftsParam;
import com.zcdy.dsc.modules.operation.work.service.WorkShiftsService;
import com.zcdy.dsc.modules.operation.work.service.WorkService;
import com.zcdy.dsc.modules.operation.work.vo.ShiftsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

 /**
 * 描述: 班次管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
@RestController
@RequestMapping("/work/shifts")
@Api(tags="班次管理")
public class WorkShiftsController extends BaseController<WorkShifts, WorkShiftsService> {
	
	@Autowired
	private WorkShiftsService workShiftsService;
	
	@Autowired
	private WorkService workService;

	
	/**
	 * 分页列表查询
	 *
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "班次管理-分页列表查询")
	@ApiOperation(value="班次管理-分页列表查询", notes="班次管理-分页列表查询")
	@GetMapping(value = "/list")
	@ApiImplicitParams({
	 	@ApiImplicitParam(name="pageNo",value="页码",paramType="query"),
	    @ApiImplicitParam(name="pageSize",value="每页显示条数",paramType="query"),
	    @ApiImplicitParam(name="shiftsName",value="班次名称",paramType="query"),
	 	@ApiImplicitParam(name="shiftsStatus",value="班次状态",paramType="query")
	})
	public Result<IPage<ShiftsVo>> queryPageList(String shiftsName,String shiftsStatus,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Result<IPage<ShiftsVo>> result=new Result<>();
		Page<ShiftsVo> page=new Page<>(pageNo, pageSize);
		IPage<ShiftsVo> shiftsList = workService.queryShiftsList(page, shiftsName, shiftsStatus);
		result.setCode(CommonConstant.SC_OK_200);
		result.setResult(shiftsList);
		return result;
	}
	
	/**
	 * 添加
	 *
	 * @return
	 */
	@AutoLog(value = "班次管理-添加")
	@ApiOperation(value="班次管理-添加", notes="班次管理-添加")
	@PostMapping(value = "/add")
	public Result<Object> add(@RequestBody ShiftsParam shiftsWorkVo) {
		WorkShifts shifts=new WorkShifts();
		shifts.setShiftsStatus(shiftsWorkVo.getShiftsStatus());
		shifts.setShiftsName(shiftsWorkVo.getShiftsName());
		shifts.setShiftsDescribe(shiftsWorkVo.getShiftsDescribe());
		shifts.setStartTime(shiftsWorkVo.getStartTime());
		shifts.setOverTime(shiftsWorkVo.getOverTime());
		shifts.setOffSign(shiftsWorkVo.getOffSign());
		if(!StringUtils.isEmpty(shiftsWorkVo.getWorkHours())){
			shifts.setWorkHours(Integer.parseInt(shiftsWorkVo.getWorkHours()));
		}
		shifts.setRestStartTime(shiftsWorkVo.getRestStartTime());
		shifts.setRestOverTime(shiftsWorkVo.getRestOverTime());
		shifts.setDelFlag(DelFlagConstant.NORMAL);
		workShiftsService.save(shifts);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 编辑
	 * @return
	 */
	@AutoLog(value = "班次管理-编辑")
	@ApiOperation(value="班次管理-编辑", notes="班次管理-编辑")
	@PostMapping(value = "/edit")
	public Result<Object> edit(@RequestBody ShiftsParam shiftsWorkVo) {
		WorkShifts shifts=new WorkShifts();
		shifts.setId(shiftsWorkVo.getId());
		shifts.setShiftsStatus(shiftsWorkVo.getShiftsStatus());
		shifts.setShiftsName(shiftsWorkVo.getShiftsName());
		shifts.setShiftsDescribe(shiftsWorkVo.getShiftsDescribe());
		shifts.setStartTime(shiftsWorkVo.getStartTime());
		shifts.setOverTime(shiftsWorkVo.getOverTime());
		shifts.setOffSign(shiftsWorkVo.getOffSign());
		if(!StringUtils.isEmpty(shiftsWorkVo.getWorkHours())){
			shifts.setWorkHours(Integer.parseInt(shiftsWorkVo.getWorkHours()));
		}
		shifts.setRestStartTime(shiftsWorkVo.getRestStartTime());
		shifts.setRestOverTime(shiftsWorkVo.getRestOverTime());
		workShiftsService.updateById(shifts);
		return Result.ok("编辑成功!");
	}
	
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "班次管理-通过id查询")
	@ApiOperation(value="班次管理-通过id查询", notes="班次管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Object> queryById(@RequestParam(name="id",required=true) String id) {
		WorkShifts shifts = workShiftsService.getById(id);
		return Result.ok(shifts);
	}
	
	/**
	 * 描述: 查询班次详情
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月10日 下午4:54:12
	 * 版本号: V1.0
	 */
	@AutoLog
	@GetMapping("/queryDetailById")
	@ApiOperation(value="班次详情",notes="班次详情")
	public Result<ShiftsVo> queryDetailById(@RequestParam(required=true,name="id")String id){
		Result<ShiftsVo> result=new Result<>();
		ShiftsVo shiftsVo = workService.queryDetailById(id);
		result.setResult(shiftsVo);
		return result.success();
	}
	
	/**
	 * 描述: 更改班次状态
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月10日 下午6:23:06
	 * 版本号: V1.0
	 */
	@AutoLog
	@GetMapping("/editStatus")
	@ApiOperation(value="更改班次状态",notes="更改班次状态")
	public Result<Object> editStatus(String id){
		workService.editShiftStatus(id);
		return Result.ok();
	}
	
	/**
	 * 描述: 导出班次信息
	 * @author：  songguang.jiao
	 * 创建时间： 2020年2月13日 下午5:33:28 
	 * 版本号: V1.0
	 */
	@GetMapping("/export")
	@ApiOperation(value="导出班次信息",notes="导出班次信息")
	public ModelAndView export(String shiftsName,String shiftsStatus){
		List<ShiftsVo> list= workService.exportShiftsData(shiftsName,shiftsStatus);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime=sdf.format(new Date());
		String secondTitle="导出人:" + sysUser.getRealname()+"  导出时间："+nowTime;
		mv.addObject(NormalExcelConstants.FILE_NAME, "班次信息管理");
		mv.addObject(NormalExcelConstants.CLASS, ShiftsVo.class);
		mv.addObject(NormalExcelConstants.PARAMS,
				new ExportParams("班次信息管理" + "报表", secondTitle, "班次信息管理"));
		mv.addObject(NormalExcelConstants.DATA_LIST, list);
		return mv;
	}
}
