package com.zcdy.dsc.modules.collection.iot.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.query.QueryGenerator;
import com.zcdy.dsc.modules.collection.iot.entity.AlarmModel;
import com.zcdy.dsc.modules.collection.iot.service.AlarmModelService;
import com.zcdy.dsc.modules.operation.equipment.util.EquipmentUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * 描述: 报警信息模板
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-03-02 14:21:12
 * 版本号: V1.0
 */
@Api(tags="报警信息模板")
@RestController
@RequestMapping("/equipment/alarmModle")
public class AlarmModelController extends BaseController<AlarmModel, AlarmModelService> {
	@Autowired
	private AlarmModelService alarmModelService;
	
	/**
	 * 分页列表查询
	 *
	 * @param alarmModel
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "报警信息模板-分页列表查询")
	@ApiOperation(value="报警信息模板-分页列表查询", notes="报警信息模板-分页列表查询")
	@GetMapping
	public Result<IPage<AlarmModel>> queryPageList(AlarmModel alarmModel,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<AlarmModel> queryWrapper = QueryGenerator.initQueryWrapper(alarmModel, req.getParameterMap());
		Page<AlarmModel> page = new Page<AlarmModel>(pageNo, pageSize);
		IPage<AlarmModel> pageList = alarmModelService.page(page, queryWrapper);
		Result<IPage<AlarmModel>>result=new Result<>();
		result.setCode(CommonConstant.SC_OK_200);
		result.setResult(pageList);
		return result;
	}
	
	/**
	 * 添加或修改
	 * @param alarmModel
	 * @return
	 */
	@AutoLog(value = "报警信息模板-添加或修改")
	@ApiOperation(value="报警信息模板-添加或修改", notes="报警信息模板-添加或修改")
	@PostMapping(value = "one")
	public Result<?> add(@RequestBody AlarmModel alarmModel) {
		//获取模板信息的内容
		String alarmValue=alarmModel.getAlarmValue();
		EquipmentUtil equipmentUtil=new EquipmentUtil();
		//截取内容中被双大括号包裹起来的内容，这是系统可以提供给用的数据内容
		List<String> values= equipmentUtil.getStrContainData(alarmValue,"{{","}}",true);
		String[] charsValue={"放置位置","设备类型","设备名称","设备编号","变量名称","变量值","日期时间","阈值","标段"};
		//判断用户所要的内容，系统是否能够全部提供
		String res="以下数据信息系统不能提供：";
		StringBuilder sb  = new StringBuilder();
		boolean r=true;
		for(String value:values){
			if(!Arrays.asList(charsValue).contains(value)){
				sb.append(value).append(",");
				r=false;
			}
		}
		res=sb.substring(0,res.length()-2)+"，请修改模板！";

		if(r){
			List<AlarmModel> listAlarmModle=alarmModelService.getAlarmModleByName(alarmModel.getId(),alarmModel.getAlarmTitle());
			if(listAlarmModle!=null&&listAlarmModle.size()>0){
				return Result.error("模板名称重复");
			}else{
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String nowDate=sdf.format(new Date());
				alarmModel.setCreateTime(nowDate);
				if(!StringUtils.isEmpty(alarmModel.getId())){
					alarmModelService.updateById(alarmModel);
				}else{
					alarmModelService.save(alarmModel);
				}
				return Result.ok("保存成功！");
			}

		}else{
			return Result.error(res);
		}
	}
	
	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "报警信息模板-通过id删除")
	@ApiOperation(value="报警信息模板-通过id删除", notes="报警信息模板-通过id删除")
	@DeleteMapping(value = "{id}")
	public Result<?> delete(@PathVariable("id") String id) {
		alarmModelService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "报警信息模板-批量删除")
	@ApiOperation(value="报警信息模板-批量删除", notes="报警信息模板-批量删除")
	@DeleteMapping(value = "/many")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.alarmModelService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "报警信息模板-通过id查询")
	@ApiOperation(value="报警信息模板-通过id查询", notes="报警信息模板-通过id查询")
	@GetMapping(value = "/{id}")
	public Result<?> queryById(@PathVariable("id") String id) {
		AlarmModel alarmModel = alarmModelService.getById(id);
		return Result.ok(alarmModel);
	}

  /**
   * 导出excel
   * @param request
   * @param alarmModel
   */
  @RequestMapping(value = "/export")
  public ModelAndView exportXls(HttpServletRequest request, AlarmModel alarmModel) {
      return super.exportXls(request, alarmModel, AlarmModel.class, "报警信息模板");
  }

  /**
   * 通过excel导入数据
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/import", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      return super.importExcel(request, response, AlarmModel.class);
  }
}