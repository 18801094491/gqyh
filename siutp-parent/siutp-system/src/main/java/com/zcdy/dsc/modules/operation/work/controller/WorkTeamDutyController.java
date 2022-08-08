package com.zcdy.dsc.modules.operation.work.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.common.util.UUIDGenerator;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.work.entity.WorkShifts;
import com.zcdy.dsc.modules.operation.work.entity.WorkTeamDuty;
import com.zcdy.dsc.modules.operation.work.param.WorkTeamDutyParam;
import com.zcdy.dsc.modules.operation.work.service.WorkService;
import com.zcdy.dsc.modules.operation.work.service.WorkShiftsService;
import com.zcdy.dsc.modules.operation.work.service.WorkTeamDutyService;
import com.zcdy.dsc.modules.operation.work.vo.*;
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

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述: 班组排班信息
 * @author： 智能无人硬核心项目组
 * 创建时间： 2020-02-12 10:46:20
 * 版本号: V1.0
 */
@Api(tags = "班组排班信息")
@RestController
@RequestMapping("/work/teamDuty")
public class WorkTeamDutyController extends BaseController<WorkTeamDuty, WorkTeamDutyService> {
	@Autowired
	private WorkTeamDutyService workTeamDutyService;

	@Autowired
	private WorkService iWorkService;

	@Autowired
	private WorkShiftsService workShiftsService;

	/**
	 * 描述: 班组排班列表 默认按照月分组 type:1-月,2-年
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月12日 下午12:14:53
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({ @ApiImplicitParam(name = "dutyDay", value = "传入日期字符串(月:yyyy-MM)", paramType = "query") })
	@ApiOperation(value = "班组排班列表-分页列表查询", notes = "班组排班列表-分页列表查询")
	@GetMapping
	public Result<List<WorkTeamDutyVo>> teamDutyData(String dutyDay) {
		Result<List<WorkTeamDutyVo>> result = new Result<List<WorkTeamDutyVo>>();
		List<WorkTeamDutyVo> list = iWorkService.queryWorkDutyData(dutyDay);
		result.setResult(list);
		result.success("查询成功");
		return result;
	}

	/**
	 * 描述: 个人查询自己的排班列表 默认按照月分组 type:1-月,2-年
	 * @author： liyongping
	 * 创建时间： 2020年2月12日 下午12:14:53
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({ @ApiImplicitParam(name = "dutyDay", value = "传入日期字符串(yyyy-MM)", paramType = "query") })
	@ApiOperation(value = "个人班组排班列表-分页列表查询", notes = "个人班组排班列表-分页列表查询")
	@GetMapping("/teamMyDutyData")
	public Result<List<WorkMyTeamDutyVo>> teamMyDutyData(String dutyDay) {
		Result<List<WorkMyTeamDutyVo>> result = new Result<List<WorkMyTeamDutyVo>>();
		List<WorkMyTeamDutyVo> list = iWorkService.queryMyWorkDutyData(dutyDay);
		result.setResult(list);
		result.success("查询成功");
		return result;
	}

	/**
	 * 描述: 通过班组查询班次id跟名称
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月12日 下午12:14:53
	 * 版本号: V1.0
	 */
	@ApiOperation(value = "通过班组id查询班次id跟名称", notes = "通过班组查询班次id跟名称")
	@GetMapping(value = "queryShifts")
	public Result<ShiftsDropdown> queryShiftsData(String teamId) {
		Result<ShiftsDropdown> result = new Result<ShiftsDropdown>();
		ShiftsDropdown shiftsDropdown = iWorkService.queryShiftsByTeamId(teamId);
		result.setResult(shiftsDropdown);
		result.success("查询成功");
		return result;
	}

	/**
	 * 描述: 当日详细排班情况
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月12日 下午8:04:08
	 * 版本号: V1.0
	 */
	@ApiImplicitParams({ @ApiImplicitParam(name = "dutyDay", value = "传入日期字符串(yyyy-MM-dd)", paramType = "query") })
	@AutoLog(value = "当日详细排班情况")
	@ApiOperation(value = "当日详细排班情况", notes = "当日详细排班情况")
	@GetMapping(value = "/oneDay")
	public Result<List<WorkTeamDutyOneDayVo>> queryWorkDutyOneDay(String dutyDay) {
		Result<List<WorkTeamDutyOneDayVo>> result = new Result<List<WorkTeamDutyOneDayVo>>();
		List<WorkTeamDutyOneDayVo> list = iWorkService.queryWorkDutyOneDay(dutyDay);
		result.setResult(list);
		result.success("查询成功");
		return result;
	}

	/**
	 * 描述: 增加班组排班信息
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月12日 下午8:02:53
	 * 版本号: V1.0
	 */
	@AutoLog(value = "增加班组排班信息")
	@ApiOperation(value = "增加班组排班信息", notes = "增加班组排班信息")
	@PostMapping(value = "one")
	public Result<Object> add(@RequestBody @Valid WorkTeamDutyParam workTeamDutyVo) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		// 获取班次信息
		WorkShifts shifts = workShiftsService.getById(workTeamDutyVo.getShfitsId());
		// 设置排班日期 ,设置日期格式
		Date date = new Date();
		Date dutyDay = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 设置开始跟结束日期,并设置时间格式
		Date startTime = null;
		Date overTime = null;
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 计算起始截止中间日期,创建排班入参
		String[] stringDate = workTeamDutyVo.getDateString().split(",");
		List<WorkTeamDuty> list = new ArrayList<WorkTeamDuty>();
		try {
			List<String> dates = DateUtil.findDates(stringDate[0], stringDate[1]);
			for (int i = 0; i < dates.size(); i++) {
				WorkTeamDuty workTeamDuty = new WorkTeamDuty();
				dutyDay = dateFormat.parse(dates.get(i));
				// 设置开始时间跟结束时间，如果为结束时间小于开始时间,则为第二天(24H)
				startTime = timeFormat.parse(dateFormat.format(date) + " " + shifts.getStartTime());
				if (Integer.parseInt(StringUtils.substring(shifts.getOverTime(), 0, 2)) < Integer
						.parseInt(StringUtils.substring(shifts.getStartTime(), 0, 2))) {
					overTime = timeFormat
							.parse(dateFormat.format(DateUtil.getNextDate(date)) + " " + shifts.getOverTime());
				} else {
					overTime = timeFormat.parse(dateFormat.format(date) + " " + shifts.getOverTime());
				}
				workTeamDuty.setId(UUIDGenerator.generate());
				workTeamDuty.setShiftsId(shifts.getId());
				workTeamDuty.setTeamId(workTeamDutyVo.getTeamId());
				workTeamDuty.setDutyDay(dutyDay);
				workTeamDuty.setCreateBy(sysUser.getUsername());
				workTeamDuty.setCreateTime(date);
				workTeamDuty.setStartTime(startTime);
				workTeamDuty.setOverTime(overTime);
				workTeamDuty.setDelFlag(DelFlagConstant.NORMAL);
				list.add(workTeamDuty);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			return Result.error("日期转换错误");
		}

		iWorkService.addWorkTeamDuty(list);
		return Result.ok("保存成功！");
	}

	/**
	 * 描述: 单个删除排班记录
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月13日 下午2:06:56
	 * 版本号: V1.0
	 */
	@AutoLog(value = "班组排班信息-删除")
	@ApiOperation(value = "班组排班信息-批量删除", notes = "班组排班信息-删除")
	@PostMapping(value = "/del")
	public Result<Object> deleteBatch(@RequestBody IdsData ids) {
		this.workTeamDutyService.removeById(ids.getId());
		return Result.ok("删除成功！");
	}

	/**
	 * 描述: 查询班组下拉列表
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月12日 下午2:02:28
	 * 版本号: V1.0
	 */
	@ApiOperation(value = "查询班组下拉列表", notes = "查询班组下拉列表")
	@GetMapping(value = "/teamList")
	public Result<List<TeamDropdown>> queryTeamNameList() {
		Result<List<TeamDropdown>> result = new Result<List<TeamDropdown>>();
		List<TeamDropdown> list = iWorkService.queryTeamNameList();
		result.setResult(list);
		result.success("查询成功");
		return result;
	}

	/**
	 * 描述: 导出全部排班数据
	 * @author： songguang.jiao
	 * 创建时间： 2020年2月13日 下午1:45:02
	 * 版本号: V1.0
	 */
	@GetMapping(value = "/export")
	@ApiOperation(value = "导出全部排班数据", notes = "导出全部排班数据")
	public ModelAndView exportXls(String dutyDay) {
		List<WorkDutyAllExport> workDutyData = iWorkService.queryWorkDutyExportData(dutyDay);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime=sdf.format(new Date());
		String secondTitle="导出人:" + sysUser.getRealname()+"  导出时间："+nowTime;
		mv.addObject(NormalExcelConstants.FILE_NAME, "班组排班信息");
		mv.addObject(NormalExcelConstants.CLASS, WorkDutyAllExport.class);
		mv.addObject(NormalExcelConstants.PARAMS,
				new ExportParams("班组排班信息" + "报表", secondTitle, "班组排班信息"));
		mv.addObject(NormalExcelConstants.DATA_LIST, workDutyData);
		return mv;
	}

	/**
	 * 导出当前用户的excel
	 *
	 * @param dutyDay
	 */
	@RequestMapping(value = "/myexport")
	public ModelAndView myexport(String dutyDay) {
		List<WorkMyTeamDutyVo> pageList = iWorkService.queryMyWorkDutyData(dutyDay);
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime=sdf.format(new Date());
		String secondTitle="导出人:" + sysUser.getRealname()+"  导出时间："+nowTime;
		mv.addObject(NormalExcelConstants.FILE_NAME, "个人排班信息");
		mv.addObject(NormalExcelConstants.CLASS, WorkMyTeamDutyVo.class);
		mv.addObject(NormalExcelConstants.PARAMS,
				new ExportParams("个人排班信息" + "报表", secondTitle, "个人排班信息"));
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		return mv;
	}
}