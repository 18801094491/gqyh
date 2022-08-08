package com.zcdy.dsc.modules.collection.iot.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.zcdy.dsc.modules.system.service.ISysCategoryService;
import com.zcdy.dsc.modules.system.service.ISysDictService;
import com.zcdy.dsc.modules.system.vo.DictDropdown;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;
import org.apache.shiro.SecurityUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.modules.collection.iot.entity.IOServerTag;
import com.zcdy.dsc.modules.collection.iot.entity.IotVariableInfo;
import com.zcdy.dsc.modules.collection.iot.service.IIotVariableInfoService;
import com.zcdy.dsc.modules.collection.iot.vo.IotVariableInfoVo;
import com.zcdy.dsc.modules.operation.equipment.entity.OptEquipmentDownLoad;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述: 采集变量信息
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-03
 * 版本号: V1.0
 */
@Api(tags="采集变量信息")
@RestController
@RequestMapping("/iot/varinfo")
public class IotVariableInfoController extends BaseController<IotVariableInfo, IIotVariableInfoService> {
	@Autowired
	private IIotVariableInfoService iotVariableInfoService;
	@Autowired
	private ISysDictService sysDictService;
	@Autowired
	private ISysCategoryService sysCategoryService;
	/**
	 * 分页列表查询
	 *
	 * @param iotVariableInfo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "采集变量信息-分页列表查询")
	@ApiOperation(value="采集变量信息-分页列表查询", notes="采集变量信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<IotVariableInfoVo>> queryPageList(IotVariableInfoVo iotVariableInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		//查出來信息
		IPage<IotVariableInfoVo> page = new Page<IotVariableInfoVo>(pageNo, pageSize);
		IPage<IotVariableInfoVo> pageList = iotVariableInfoService.getPageData(page, iotVariableInfo.getVariableName(), iotVariableInfo.getVariableTitle(),iotVariableInfo.getVariableType());
        Result<IPage<IotVariableInfoVo>> result = new Result<>();
        result.setResult(pageList);
        result.setCode(200);
        result.setSuccess(true);
        return result;
	}
	
	/**
	 * 添加
	 *
	 * @param iotVariableInfo
	 * @return
	 */
	@AutoLog(value = "采集变量信息-添加")
	@ApiOperation(value="采集变量信息-添加", notes="采集变量信息-添加")
	@PostMapping(value = "/add")
	public Result<Object> add(@RequestBody IotVariableInfoVo iotVariableInfo) {
		List<IotVariableInfoVo> list=iotVariableInfoService.getIotVariableInfoVoByName(iotVariableInfo.getId(),iotVariableInfo.getVariableName());
        if(list!=null&&list.size()>0){
			return Result.error("变量名称重复！");
		}else{
			IotVariableInfo iotVariableInfoNew = new IotVariableInfo();
			BeanUtils.copyProperties(iotVariableInfo,iotVariableInfoNew);
			iotVariableInfoNew.setWorkingStatus(iotVariableInfo.getWoStValue());
			iotVariableInfoNew.setReadType(iotVariableInfo.getReadType());
			iotVariableInfoService.save(iotVariableInfoNew);
			return Result.ok("添加成功！");
		}


	}
	
	/**
	 * 编辑
	 *
	 * @param iotVariableInfo
	 * @return
	 */
	@AutoLog(value = "采集变量信息-编辑")
	@ApiOperation(value="采集变量信息-编辑", notes="采集变量信息-编辑")
	@PutMapping(value = "/edit")
	public Result<Object> edit(@RequestBody IotVariableInfoVo iotVariableInfo) {
		List<IotVariableInfoVo> list=iotVariableInfoService.getIotVariableInfoVoByName(iotVariableInfo.getId(),iotVariableInfo.getVariableName());
		if(list!=null&&list.size()>0){
			return Result.error("变量名称重复！");
		}else {
			IotVariableInfo iotVariableInfoNew = new IotVariableInfo();
			BeanUtils.copyProperties(iotVariableInfo, iotVariableInfoNew);
			iotVariableInfoNew.setWorkingStatus(iotVariableInfo.getWoStValue());
			iotVariableInfoNew.setReadType(iotVariableInfo.getReadType());
			iotVariableInfoService.updateById(iotVariableInfoNew);
			return Result.ok("编辑成功!");
		}
	}


	/**
	 * 停用启用
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "采集变量信息-启用停用")
	@ApiOperation(value="采集变量信息-启用停用", notes="采集变量信息-启用停用")
	@ApiImplicitParams({
			@ApiImplicitParam(name="workStatus",value="停用启用code",paramType="query"),
			@ApiImplicitParam(name="id",value="变量的id",paramType="query")
	}
	)
	@GetMapping(value = "/editWorkingStatus")
	public Result<Object> editWorkingStatus(String workStatus,String id) {
		iotVariableInfoService.editWorkingStatus(workStatus,id);
			return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采集变量信息-通过id删除")
	@ApiOperation(value="采集变量信息-通过id删除", notes="采集变量信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<Object> delete(@RequestParam(name="id",required=true) String id) {
        IotVariableInfo variableInfo = new IotVariableInfo();
        variableInfo.setId(id);
        variableInfo.setDelFlag(1);
        iotVariableInfoService.updateById(variableInfo);
		return Result.ok("删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采集变量信息-通过id查询")
	@ApiOperation(value="采集变量信息-通过id查询", notes="采集变量信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		IotVariableInfo iotVariableInfo = iotVariableInfoService.getById(id);
		return Result.ok(iotVariableInfo);
	}
	
	/**
	 * @author：Roberto
	 * @create:2020年4月7日 上午11:07:01
	 * 描述:<p>同步IOServer变量</p>
	 */
	@AutoLog(value = "同步IOServer变量")
	@ApiOperation(value="同步IOServer变量", notes="从IOServer上同步变量")
	@GetMapping(value = "/sync")
	public Result<String> syncVar(){
		JSONObject json = this.iotVariableInfoService.syncIoServerTags();
		
		Result<String> result = null;
		//处理同步接口传回的数据
		if(null==json){
			result = Result.fail("同步变量失败");
		}else{
			boolean isSuccess = json.getBoolean("success");
			if(isSuccess){
				JSONArray array = json.getJSONArray("result");
				//同步的变量信息
				List<IOServerTag> data = JSON.parseArray(array.toJSONString(), IOServerTag.class);
				//存储策略，只新增不修改不删除
				LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
				this.iotVariableInfoService.syncDbTags(data, user.getUsername());
				result = Result.success("success", "开始后台同步(可能需要几分钟时间)，请留意系统通知……");
			}else{
				String message = json.getString("message");
				result = Result.fail(message);
			}
		}
		return result;
	}

  /**
   * 导出excel
   *
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(String variableName,String variableTitle,String variableType) {
	  List<IotVariableInfoVo> pageList = iotVariableInfoService.getExportXls(variableName, variableTitle, variableType);
	  LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
	  ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
	  SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  String nowTime=sdf.format(new Date());
	  String secondTitle="导出人:" + sysUser.getRealname()+"  导出时间："+nowTime;
	  mv.addObject(NormalExcelConstants.FILE_NAME, "采集变量信息");
	  mv.addObject(NormalExcelConstants.CLASS, IotVariableInfoVo.class);
	  mv.addObject(NormalExcelConstants.PARAMS,
			  new ExportParams("采集变量信息" + "报表", secondTitle, "采集变量信息"));
	  mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
	  return mv;
  }
	/**
	 * 通过excel导入数据
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value="导入excel", notes="导入excel")
	@RequestMapping(value = "/import", method = RequestMethod.POST)

	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		List<IotVariableInfo> listOldIotVariableInfo=new ArrayList<IotVariableInfo>();
		//获取所需要的字典表的map集合
		List<DictDropdown> readTypeList = sysDictService.getDictValue("read_type");
		List<DictDropdown> dataTypeList = sysDictService.getDictValue("data_type");
		List<DictDropdown> workingStatusList = sysDictService.getDictValue("working_status");
		List<SysCateDropdown> shuJuTypeValue = this.sysCategoryService.queryKeyValue("A16");
		Map<String, String> readTypeMap = readTypeList.stream().collect(Collectors.toMap(DictDropdown::getTitle, DictDropdown::getCode, (key1, key2) -> key2));
		Map<String, String> dataTypeMap = dataTypeList.stream().collect(Collectors.toMap(DictDropdown::getTitle, DictDropdown::getCode, (key1, key2) -> key2));
		Map<String, String> workingStatusMap = workingStatusList.stream().collect(Collectors.toMap(DictDropdown::getTitle, DictDropdown::getCode, (key1, key2) -> key2));
		Map<String, String> shuJuTypeMap = shuJuTypeValue.stream().collect(Collectors.toMap(SysCateDropdown::getTitle, SysCateDropdown::getCode, (key1, key2) -> key2));

		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			//最好不要设置为true，否则导入一次excel服务器会重启一次
			params.setNeedSave(false);

			List<IotVariableInfo> listIotVariableInfo = ExcelImportUtil.importExcel(file.getInputStream(), IotVariableInfo.class, params);
			for (IotVariableInfo iotVariableInfo:listIotVariableInfo){
				List<IotVariableInfoVo> list=iotVariableInfoService.getIotVariableInfoVoByName(iotVariableInfo.getId(),iotVariableInfo.getVariableName());
				if(list!=null&&list.size()>0){
					listOldIotVariableInfo.add(iotVariableInfo);
				}else{
					//把下拉选里的数据填上
					String dataType=dataTypeMap.get(iotVariableInfo.getDataType());
					String readType=readTypeMap.get(iotVariableInfo.getReadType());
					String variableType=shuJuTypeMap.get(iotVariableInfo.getVariableType());
					String workingStatus=workingStatusMap.get(iotVariableInfo.getWorkingStatus());
					iotVariableInfo.setDataType(dataType);
					iotVariableInfo.setReadType(readType);
					iotVariableInfo.setVariableType(variableType);
					iotVariableInfo.setWorkingStatus(workingStatus);
					listOldIotVariableInfo.add(iotVariableInfo);
					iotVariableInfoService.save(iotVariableInfo);
				}
			}
		}
		Result<List<IotVariableInfo>> result = new Result<>();
		result.setResult(listOldIotVariableInfo);
		result.setCode(200);
		result.setSuccess(true);
		return result;

	}


	@GetMapping("/downloadModel")
	@ApiOperation(value="下载导入模板",notes="下载导入模板")
	public ModelAndView loadModel(){
		List<OptEquipmentDownLoad> list=new ArrayList<>();
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "采集变量导入模板");
		mv.addObject(NormalExcelConstants.CLASS, IotVariableInfo.class);
		mv.addObject(NormalExcelConstants.PARAMS,
				new ExportParams("采集变量导入模板", "备注：导入功能只能用来新增不能进行修改，如果变量名称已经存在于系统中，则不作处理" , "采集变量导入模板"));
		mv.addObject(NormalExcelConstants.DATA_LIST, list);
		return mv;
	}


}
