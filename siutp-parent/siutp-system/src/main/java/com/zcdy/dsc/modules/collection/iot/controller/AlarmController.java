package com.zcdy.dsc.modules.collection.iot.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.collection.iot.entity.Alarm;
import com.zcdy.dsc.modules.collection.iot.entity.AlarmRule;
import com.zcdy.dsc.modules.collection.iot.service.AlarmRuleService;
import com.zcdy.dsc.modules.collection.iot.service.AlarmService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

 /**
 * 描述: 设备报警规则
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02 15:38:09
 * 版本号: V1.0
 */
@Api(tags="设备报警规则")
@RestController
@RequestMapping("/equipment/alarm")
public class AlarmController extends BaseController<Alarm, AlarmService> {
	@Autowired
	private AlarmService alarmService;
	 @Autowired
	 private AlarmRuleService alarmRuleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param alarm
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "设备报警规则-分页列表查询")
	@ApiOperation(value="设备报警规则-分页列表查询", notes="设备报警规则-分页列表查询")
	@GetMapping
	public Result<IPage<Alarm>> queryPageList(Alarm alarm,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Page<Alarm> page = new Page<Alarm>(pageNo, pageSize);
		List<Alarm> list= new ArrayList<Alarm>();
		//根据传入的设备id查询该设备上所有的报警规则
		IPage<Alarm> pageList = alarmService.queryPageList(page, alarm);

		list=pageList.getRecords();
		List<Alarm> list1= new ArrayList<Alarm>();
		//遍历所有规则，拼接规则内容
		for (Alarm alarm1:list){
			String andOr=alarm1.getAndOr();
			//根据规则id查询所有变量信息
			List<AlarmRule> alarmRuleList=alarmRuleService.getAlarmRuleByAlarmId(alarm1.getId());
			String 	alarmRuleValue="";
			//根据规则字段拼接规则内容
			if(alarmRuleList!=null&&alarmRuleList.size()>0){
				for (AlarmRule alarmRule:alarmRuleList){
					alarmRuleValue=alarmRuleValue+alarmRule.getVariableName()+alarmRule.getAlarmRuleName()+alarmRule.getAlarmValue()+andOr;
				}
				alarmRuleValue=alarmRuleValue.substring(0,alarmRuleValue.length()-2);
			}

			alarm1.setAlarmRule(alarmRuleValue);
			list1.add(alarm1);
		}
		pageList.setRecords(list1);
		Result<IPage<Alarm>>result=new Result<>();
		result.setCode(CommonConstant.SC_OK_200);
		result.setResult((IPage<Alarm>) pageList);
		return result;
	}
	 /**
	  * 停用启用规则
	  * @param alarmStatusCode
	  * @return
	  */
	 @AutoLog(value = "设备报警规则-停用启用规则")
	 @ApiOperation(value="设备报警规则-停用启用规则", notes="设备报警规则-停用启用规则")
	 @ApiImplicitParams({
			 @ApiImplicitParam(name = "id",value="规则的id"),
			 @ApiImplicitParam(name = "alarmStatusCode",value="启停code 1为启用，0为停用")
 			}
	 )
	 @GetMapping(value = "/startOrStop")
	 public Result<?> startOrStop(String id ,String alarmStatusCode) {
		 alarmService.startOrStop(id,alarmStatusCode);
		 return Result.ok("修改成功！");
	 }
	/**
	 * 添加
	 * @param alarm
	 * @return
	 */
	@AutoLog(value = "设备报警规则-添加")
	@ApiOperation(value="设备报警规则-添加", notes="设备报警规则-添加")

	@PostMapping(value = "one")
	public Result<?> add(@RequestBody Alarm alarm) {
		//报警规则必须传设备id，
		if(alarm.getEquipmentId()==null&&"".equals(alarm.getEquipmentId())){
			return Result.error("设备id不能为空");
		}else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowDate = sdf.format(new Date());
			if (!StringUtils.isEmpty(alarm.getId())) {
				//对规则表进行修改
				alarmService.updateByAlarmId(alarm);
				//对规则绑定变量表进行删除
				alarmRuleService.deleteByAlarmId(alarm.getId());
			} else {
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				alarm.setId(uuid);
				alarm.setCreateTime(nowDate);
				//对规则表进行新增
				alarmService.saveAlarm(alarm);
			}
			List<AlarmRule> listAlarmRule = alarm.getAlarmRuleList();
			//对规则绑定变量表进行新增
			for (AlarmRule alarmRule : listAlarmRule) {
				String uuid1 = UUID.randomUUID().toString().replaceAll("-", "");
				alarmRule.setId(uuid1);
				alarmRule.setAlarmId(alarm.getId());
				alarmRule.setCreateTime(nowDate);
				alarmRuleService.saveAlarmRule(alarmRule);
			}
			return Result.ok("保存成功！");
		}
	}

	 /**
	  * 编辑规则内容
	  * @param alarm
	  * @return
	  */
	 @AutoLog(value = "设备报警规则-编辑规则内容")
	 @ApiOperation(value="设备报警规则-编辑规则内容", notes="设备报警规则-编辑规则内容")
	 @PostMapping(value = "/updateValue")
	 public Result<?> updateValue(@RequestBody Alarm alarm) {

		 if(alarm.getEquipmentId()==null&&"".equals(alarm.getEquipmentId())){
			 return Result.error("设备id不能为空");
		 }else {
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 String nowDate = sdf.format(new Date());
			 if (!StringUtils.isEmpty(alarm.getId())) {
				 alarm.setCreateTime(nowDate);
				 //对规则表进行修改
				 alarmService.updateByAlarmId(alarm);
				 //对规则绑定变量表进行删除
			 }else{
				 return Result.error("规则id不能为空");
			 }
		 }
		 return Result.ok("保存成功！");
	 }

	 /**
	  * 编辑规则策略
	  * @param alarm
	  * @return
	  */
	 @AutoLog(value = "设备报警规则-编辑规则策略")
	 @ApiOperation(value="设备报警规则-编辑规则策略", notes="设备报警规则-编辑规则策略")
	 @PostMapping(value = "/updateAlarm")
	 public Result<?> updateAlarm(@RequestBody Alarm alarm) {
		this.startOrStop(alarm.getId(),"0");
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String nowDate = sdf.format(new Date());
			 String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			 alarm.setId(uuid);
			 alarm.setCreateTime(nowDate);
			 //对规则表进行新增
			 alarmService.saveAlarm(alarm);
		 List<AlarmRule> listAlarmRule = alarm.getAlarmRuleList();
		 //对规则绑定变量表进行新增
		 for (AlarmRule alarmRule : listAlarmRule) {
			 String uuid1 = UUID.randomUUID().toString().replaceAll("-", "");
			 alarmRule.setId(uuid1);
			 alarmRule.setAlarmId(alarm.getId());
			 alarmRule.setCreateTime(nowDate);
			 alarmRuleService.saveAlarmRule(alarmRule);
		 }
		 return Result.ok("保存成功！");
	 }
	
	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设备报警规则-通过id删除")
	@ApiOperation(value="设备报警规则-通过id删除", notes="设备报警规则-通过id删除")
	@DeleteMapping(value = "{id}")
	public Result<?> delete(@PathVariable("id") String id) {
		alarmService.removeById(id);
		//对规则绑定变量表进行删除
		alarmRuleService.deleteByAlarmId(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "设备报警规则-批量删除")
	@ApiOperation(value="设备报警规则-批量删除", notes="设备报警规则-批量删除")
	@DeleteMapping(value = "/many")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.alarmService.removeByIds(Arrays.asList(ids.split(",")));
		String[] alarmIds=ids.split(",");
		for (String id:alarmIds){
			//对规则绑定变量表进行删除
			alarmRuleService.deleteByAlarmId(id);
		}
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设备报警规则-通过id查询规则详细信息")
	@ApiOperation(value="设备报警规则-通过id查询规则详细信息", notes="设备报警规则-通过id查询规则详细信息")
	@GetMapping(value = "/{id}")
	public Result<Alarm> queryById(@PathVariable("id") String id) {
		Alarm alarm = alarmService.getAlarmById(id);

		if(alarm!=null){
			//根据报警id查询具体规则集合
			List<AlarmRule> alarmRuleList=alarmRuleService.getAlarmRuleByAlarmId(id);
			alarm.setAlarmRuleList(alarmRuleList);

		}else{
			alarm=new Alarm();
		}

		Result<Alarm>result=new Result<>();
		result.setCode(CommonConstant.SC_OK_200);
		result.setResult(alarm);
		return result;
//
	}

  /**
   * 导出excel
   * @param request
   * @param alarm
   */
  @RequestMapping(value = "/export")
  public ModelAndView exportXls(HttpServletRequest request, Alarm alarm) {
      return super.exportXls(request, alarm, Alarm.class, "设备报警规则");
  }

  /**
   * 通过excel导入数据
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/import", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      return super.importExcel(request, response, Alarm.class);
  }
}