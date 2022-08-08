package com.zcdy.dsc.modules.configcentre.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.modules.configcentre.entity.Docs;
import com.zcdy.dsc.modules.configcentre.service.DocsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * 描述: 文档展示
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-04-08 16:28:21
 * 版本号: V1.0
 */
@Api(tags="文档展示接口组")
@RestController
@RequestMapping("/oa/docs/data")
public class DocViewController extends BaseController<Docs, DocsService> {

	@Resource
	private DocsService docsService;
	
	/**
	 * 获取相关文档列表
	 * @param docs
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "获取文档列表")
	@ApiOperation(value="获取文档列表", notes="获取文档列表:<p>操作手册：A17A01</p>")
	@GetMapping("/{code}")
	public Result<?> getDocs(@PathVariable String code) {
		List<Docs> list = docsService.getDocData(code);
		list.forEach(item->{
			item.setDocUrl(baseStoragePath.concat(item.getDocUrl()));
		});
		return Result.ok(list);
	}
}