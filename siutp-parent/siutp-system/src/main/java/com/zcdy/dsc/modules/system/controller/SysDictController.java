package com.zcdy.dsc.modules.system.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.common.system.query.QueryGenerator;
import com.zcdy.dsc.common.system.vo.DictModel;
import com.zcdy.dsc.common.system.vo.LoginUser;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.common.util.SqlInjectionUtil;
import com.zcdy.dsc.common.util.StringUtil;
import com.zcdy.dsc.modules.system.constant.CacheConstant;
import com.zcdy.dsc.modules.system.entity.SysDict;
import com.zcdy.dsc.modules.system.entity.SysDictItem;
import com.zcdy.dsc.modules.system.model.SysDictTree;
import com.zcdy.dsc.modules.system.model.TreeSelectModel;
import com.zcdy.dsc.modules.system.service.ISysDictItemService;
import com.zcdy.dsc.modules.system.service.ISysDictService;
import com.zcdy.dsc.modules.system.vo.DictDropdown;
import com.zcdy.dsc.modules.system.vo.SysDictPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author： Roberto
 * 创建时间：2020年3月11日 下午6:39:06
 * 描述: <p>字典表 前端控制器</p>
 */
@RestController
@RequestMapping("/sys/dict")
@Slf4j
@Api(tags = "数据字典列表")
public class SysDictController {

	@Autowired
	private ISysDictService sysDictService;

	@Autowired
	private ISysDictItemService sysDictItemService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<SysDict>> queryPageList(SysDict sysDict,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
		Result<IPage<SysDict>> result = new Result<IPage<SysDict>>();
		QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, req.getParameterMap());
		queryWrapper.lambda().orderByDesc(SysDict::getCreateTime);
		Page<SysDict> page = new Page<SysDict>(pageNo, pageSize);
		IPage<SysDict> pageList = sysDictService.page(page, queryWrapper);
		log.debug("查询当前页："+pageList.getCurrent());
		log.debug("查询当前页数量："+pageList.getSize());
		log.debug("查询结果数量："+pageList.getRecords().size());
		log.debug("数据总数："+pageList.getTotal());
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * @功能：获取树形字典数据
	 * @param sysDict
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/treeList", method = RequestMethod.GET)
	public Result<List<SysDictTree>> treeList(SysDict sysDict,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
		Result<List<SysDictTree>> result = new Result<>();
		LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
		// 构造查询条件
		String dictName = sysDict.getDictName();
		if(ConvertUtils.isNotEmpty(dictName)) {
			query.like(true, SysDict::getDictName, dictName);
		}
		query.orderByDesc(true, SysDict::getCreateTime);
		List<SysDict> list = sysDictService.list(query);
		List<SysDictTree> treeList = new ArrayList<>();
		for (SysDict node : list) {
			treeList.add(new SysDictTree(node));
		}
		result.setSuccess(true);
		result.setResult(treeList);
		return result;
	}

	
	/**
	 * 描述: 通过字典编号加载数据字典
	 * @author：  songguang.jiao
	 * 创建时间：  2020年1月16日 下午6:59:21
	 * 版本号: V1.0
	 */
	@ApiOperation(value="通过字典编号加载数据字典")
	@GetMapping(value="/getDictValue/{code}")
	public Result<List<DictDropdown>> getDictValue(@PathVariable String code) {
		Result<List<DictDropdown>> result=new Result<>();
		List<DictDropdown> value = sysDictService.getDictValue(code);
		result.setResult(value);
		result.success("查询成功");
		return result;
	}

	/**
	 * 通过字典编号加载数据字典
	 * @param code
	 * @return
	 */
	@ApiOperation(value="通过字典编号加载数据字典")
	@GetMapping(value="/app/getDictValue/{code}")
	@ApiVersion(group = VersionConstant.VERSION_APP)
	public Result<List<DictDropdown>> getAppDictValue(@PathVariable String code) {
		Result<List<DictDropdown>> result = new Result<>();
		List<DictDropdown> dictValueList = sysDictService.getAppDictValue(code);
		result.setResult(dictValueList);
		result.success("查询成功");
		return result;
	}
	
	
	
	/**
	 * 获取字典数据
	 * @param dictCode 字典code
	 * @param dictCode 表名,文本字段,code字段  | 举例：sys_user,realname,id
	 * @return
	 */
	@RequestMapping(value = "/getDictItems/{dictCode}", method = RequestMethod.GET)
	public Result<List<DictModel>> getDictItems(@PathVariable String dictCode) {
		log.info(" dictCode : "+ dictCode);
		Result<List<DictModel>> result = new Result<List<DictModel>>();
		List<DictModel> ls = null;
		try {
			if(dictCode.indexOf(",")!=-1) {
				//关联表字典（举例：sys_user,realname,id）
				String[] params = dictCode.split(",");
				
				if(params.length<3) {
					result.error500("字典Code格式不正确！");
					return result;
				}
				//SQL注入校验（只限制非法串改数据库）
				final String[] sqlInjCheck = {params[0],params[1],params[2]};
				SqlInjectionUtil.filterContent(sqlInjCheck);
				
				if(params.length==4) {
					//SQL注入校验（查询条件SQL 特殊check，此方法仅供此处使用）
					SqlInjectionUtil.specialFilterContent(params[3]);
					ls = sysDictService.queryTableDictItemsByCodeAndFilter(params[0],params[1],params[2],params[3]);
				}else if (params.length==3) {
					ls = sysDictService.queryTableDictItemsByCode(params[0],params[1],params[2]);
				}else{
					result.error500("字典Code格式不正确！");
					return result;
				}
			}else {
				//字典表
				 ls = sysDictService.queryDictItemsByCode(dictCode);
			}

			 result.setSuccess(true);
			 result.setResult(ls);
			 log.info(result.toString());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
			return result;
		}

		return result;
	}

	/**
	 * 获取字典数据
	 * @param dictCode
	 * @return
	 */
	@RequestMapping(value = "/getDictText/{dictCode}/{key}", method = RequestMethod.GET)
	public Result<String> getDictItems(@PathVariable("dictCode") String dictCode, @PathVariable("key") String key) {
		log.info(" dictCode : "+ dictCode);
		Result<String> result = new Result<String>();
		String text = null;
		try {
			text = sysDictService.queryDictTextByKey(dictCode, key);
			 result.setSuccess(true);
			 result.setResult(text);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
			return result;
		}
		return result;
	}

	/**
	 * @功能：新增
	 * @param sysDict
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<SysDict> add(@RequestBody SysDict sysDict) {
		Result<SysDict> result = new Result<SysDict>();
		try {
			sysDict.setCreateTime(new Date());
			sysDict.setDelFlag(CommonConstant.DEL_FLAG_0);
			sysDictService.save(sysDict);
			result.success("保存成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * @功能：编辑
	 * @param sysDict
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public Result<SysDict> edit(@RequestBody SysDict sysDict) {
		Result<SysDict> result = new Result<SysDict>();
		SysDict sysdict = sysDictService.getById(sysDict.getId());
		if(sysdict==null) {
			result.error500("未找到对应实体");
		}else {
			sysDict.setUpdateTime(new Date());
			boolean ok = sysDictService.updateById(sysDict);
			if(ok) {
				result.success("编辑成功!");
			}
		}
		return result;
	}

	/**
	 * @功能：删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@CacheEvict(value=CacheConstant.SYS_DICT_CACHE, allEntries=true)
	public Result<SysDict> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysDict> result = new Result<SysDict>();
		boolean ok = sysDictService.removeById(id);
		if(ok) {
			result.success("删除成功!");
		}else{
			result.error500("删除失败!");
		}
		return result;
	}

	/**
	 * @功能：批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	@CacheEvict(value= CacheConstant.SYS_DICT_CACHE, allEntries=true)
	public Result<SysDict> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysDict> result = new Result<SysDict>();
		if(ConvertUtils.isEmpty(ids)) {
			result.error500("参数不识别！");
		}else {
			sysDictService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(SysDict sysDict,HttpServletRequest request) {
		// Step.1 组装查询条件
		QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, request.getParameterMap());
		queryWrapper.lambda().orderByDesc(SysDict::getCreateTime);
		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<SysDictPage> pageList = new ArrayList<SysDictPage>();

		List<SysDict> sysDictList = sysDictService.list(queryWrapper);
		for (SysDict dictMain : sysDictList) {
			SysDictPage vo = new SysDictPage();
			BeanUtils.copyProperties(dictMain, vo);
			// 查询机票
			List<SysDictItem> sysDictItemList = sysDictItemService.selectItemsByMainId(dictMain.getId());
			vo.setSysDictItemList(sysDictItemList);
			pageList.add(vo);
		}
		LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime=sdf.format(new Date());
		String secondTitle="导出人:" + user.getRealname()+"  导出时间："+nowTime;
		// 导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "数据字典");
		// 注解对象Class
		mv.addObject(NormalExcelConstants.CLASS, SysDictPage.class);
		// 自定义表格参数

		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("数据字典列表", secondTitle, "数据字典"));
		// 导出数据列表
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		return mv;
	}

	/**
	 * 通过excel导入数据
	 *
	 * @param request
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();
			String fileName = file.getOriginalFilename();
			String fileType = fileName.substring(fileName.indexOf("."));
			if (!(fileType.endsWith(".xls") || fileType.endsWith(".xlsx"))) {
				return Result.error("请上传 xls、xlsx 格式表格文件");
			}
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(2);
			//最好不要设置为true，否则导入一次excel服务器会重启一次
			params.setNeedSave(false);
			try {
				List<SysDictPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SysDictPage.class, params);
				if (list.size() > 0) {
					for (SysDictPage page : list) {
						if ((StringUtil.isNull(page.getDictName()) || "null".equals(page.getDictName())) ||
								(StringUtil.isNull(page.getDictCode()) || "null".equals(page.getDictCode()))) {
							return Result.error("表格数据不完整，请检查完善后重新导入");
						}
						SysDict po = new SysDict();
						BeanUtils.copyProperties(page, po);
						if(page.getDelFlag()==null){
							po.setDelFlag(1);
						}
						sysDictService.saveMain(po, page.getSysDictItemList());
					}
					return Result.ok("文件导入成功！");
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return Result.error("文件导入失败");
			} finally {
				try {
					file.getInputStream().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return Result.error("文件导入失败！");
	}
	
	/**
	 * 大数据量的字典表 走异步加载  即前端输入内容过滤数据 （
	 * @param dictCode
	 * @return
	 */
	@RequestMapping(value = "/loadDict/{dictCode}", method = RequestMethod.GET)
	public Result<List<DictModel>> loadDict(@PathVariable String dictCode,@RequestParam(name="keyword") String keyword) {
		log.info(" 加载字典表数据,加载关键字: "+ keyword);
		Result<List<DictModel>> result = new Result<List<DictModel>>();
		List<DictModel> ls = null;
		try {
			if(dictCode.indexOf(",")!=-1) {
				String[] params = dictCode.split(",");
				if(params.length!=3) {
					result.error500("字典Code格式不正确！");
					return result;
				}
				ls = sysDictService.queryTableDictItems(params[0],params[1],params[2],keyword);
				result.setSuccess(true);
				result.setResult(ls);
				log.info(result.toString());
			}else {
				result.error500("字典Code格式不正确！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
			return result;
		}

		return result;
	}
	
	/**
	 * 根据字典code加载字典text 返回
	 */
	@RequestMapping(value = "/loadDictItem/{dictCode}", method = RequestMethod.GET)
	public Result<List<String>> loadDictItem(@PathVariable String dictCode,@RequestParam(name="key") String key) {
		Result<List<String>> result = new Result<>();
		try {
			if(dictCode.indexOf(",")!=-1) {
				String[] params = dictCode.split(",");
				if(params.length!=3) {
					result.error500("字典Code格式不正确！");
					return result;
				}

				List<String> texts = sysDictService.queryTableDictByKeys(params[0], params[1], params[2], key.split(","));

				result.setSuccess(true);
				result.setResult(texts);
				log.info(result.toString());
			}else {
				result.error500("字典Code格式不正确！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
			return result;
		}

		return result;
	}
	
	/**
	 * 根据表名——显示字段-存储字段 pid 加载树形数据
	 */
	@RequiresAuthentication
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/loadTreeData", method = RequestMethod.GET)
	public Result<List<TreeSelectModel>> loadDict(@RequestParam(name="pid") String pid,@RequestParam(name="pidField") String pidField,
			@RequestParam(name="tableName") String tbname,
			@RequestParam(name="text") String text,
			@RequestParam(name="code") String code,
			@RequestParam(name="hasChildField") String hasChildField,
			@RequestParam(name="condition") String condition) {
		Result<List<TreeSelectModel>> result = new Result<List<TreeSelectModel>>();
		Map<String, String> query = null;
		if(ConvertUtils.isNotEmpty(condition)) {
		    //存在注入风险
		    try {
                SqlInjectionUtil.filterContent(condition);
            } catch (Exception e) {
                return Result.fail("请不要使用非法字符");
            }
			query = JSON.parseObject(condition, Map.class);
		}

		List<TreeSelectModel> ls = sysDictService.queryTreeList(query,tbname, text, code, pidField, pid,hasChildField);
		result.setSuccess(true);
		result.setResult(ls);
		return result;
	}


	/**
	 * 查询被删除的列表
	 * @return
	 */
	@RequestMapping(value = "/deleteList", method = RequestMethod.GET)
	public Result<List<SysDict>> deleteList() {
		Result<List<SysDict>> result = new Result<List<SysDict>>();
		List<SysDict> list = this.sysDictService.queryDeleteList();
		result.setSuccess(true);
		result.setResult(list);
		return result;
	}

	/**
	 * 物理删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deletePhysic/{id}", method = RequestMethod.DELETE)
	public Result<?> deletePhysic(@PathVariable String id) {
		try {
			sysDictService.deleteOneDictPhysically(id);
			return Result.ok("删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("删除失败!");
		}
	}

	/**
	 * 取回
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/back/{id}", method = RequestMethod.PUT)
	public Result<?> back(@PathVariable String id) {
		try {
			sysDictService.updateDictDelFlag(0,id);
			return Result.ok("操作成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("操作失败!");
		}
	}
	

}
