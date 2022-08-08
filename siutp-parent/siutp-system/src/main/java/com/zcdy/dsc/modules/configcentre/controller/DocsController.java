package com.zcdy.dsc.modules.configcentre.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.configcentre.entity.Docs;
import com.zcdy.dsc.modules.configcentre.service.DocsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

 /**
 * 描述: 文档管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-08 16:28:21
 * 版本号: V1.0
 */
@Api(tags="文档管理接口组")
@RestController
@RequestMapping("/oa/docs")
public class DocsController extends BaseController<Docs, DocsService> {

	@Resource
	private DocsService docsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@AutoLog(value = "文档管理-分页列表查询")
	@ApiOperation(value="文档管理-分页列表查询", notes="文档管理-分页列表查询")
	@GetMapping
	public Result<?> queryPageList(String docName,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Page<Docs> page = new Page<Docs>(pageNo, pageSize);
		IPage<Docs> pageList = docsService.pageData(page, docName);
		return Result.ok(pageList);
	}
	
	/**
	 * 添加或修改
	 * @param docs
	 * @return
	 */
	@AutoLog(value = "文档管理-添加或修改")
	@ApiOperation(value="文档管理-添加或修改", notes="文档管理-添加或修改")
	@PostMapping(value = "one")
	public Result<?> add(@RequestBody Docs docs) {
		if(!StringUtils.isEmpty(docs.getId())){
			if(StringUtils.isEmpty(docs.getDocUrl())){
				docs.setDocUrl(null);
			}
			docsService.updateById(docs);
		}else{
			docs.setStatus(true);
			docs.setDelFlag(StatusConstant.WORKING +"");
			docsService.save(docs);
		}
		return Result.ok("保存成功！");
	}


	 /**
	  *  启停用
	  * @return
	  */
	 @ApiOperation(value = "文档管理-启停用 true为启用，false为停用", notes = "角色表-启停用")
	 @GetMapping(value = "/startOrStop")
	 public Result<?> startOrStop(String id,Boolean status) {
		 docsService.startOrStop(id,status);
		 return Result.ok("修改成功！");
	 }

	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "文档管理-通过id查询")
	@ApiOperation(value="文档管理-通过id查询", notes="文档管理-通过id查询")
	@GetMapping(value = "/{id}")
	public Result<Docs> queryById(@PathVariable("id") String id) {
		Docs docs = docsService.getById(id);
		return Result.success(docs, "操作成功");
	}
}