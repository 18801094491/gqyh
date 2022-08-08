package com.zcdy.dsc.modules.collection.gis.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.constant.ShowConstant;
import com.zcdy.dsc.modules.collection.gis.entity.GisModelRes;
import com.zcdy.dsc.modules.collection.gis.service.GisModelResService;
import com.zcdy.dsc.modules.collection.gis.service.GisService;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelResImgVo;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelResParamVo;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelResVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 描述: GIS模型库类型
 * @author： 智能无人硬核心项目组
 * 创建时间： 2020-02-26 21:08:04
 * 版本号: V1.0
 */
@Api(tags = "GIS模型库类型")
@RestController
@RequestMapping("/gis/gisModelRes")
public class GisModelResController extends BaseController<GisModelRes, GisModelResService> {

	@Autowired
	private GisService gisService;

	@Resource
	private GisModelResService gisModelResService;

	/**
	 * 分页列表查询
	 *
	 * @param gisModelRes
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "GIS模型库类型-分页列表查询")
	@ApiOperation(value = "GIS模型库类型-分页列表查询", notes = "GIS模型库类型-分页列表查询")
	@GetMapping
	public Result<IPage<GisModelResVo>> queryPageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String modelTypeCode) {

		Result<IPage<GisModelResVo>> result = new Result<IPage<GisModelResVo>>();
		Page<GisModelResVo> page = new Page<>(pageNo, pageSize);
		IPage<GisModelResVo> data = gisService.queryGisResPageData(page, modelTypeCode);
		List<GisModelResVo> records = data.getRecords();
		if (records.size() > 0 && !records.isEmpty()) {
			for (GisModelResVo gisModelResVo : records) {
				List<GisModelResImgVo> list = gisModelResVo.getList();
				if (list.size() > 0 && !list.isEmpty()) {
					for (GisModelResImgVo gisModelResImgVo : list) {
						if (gisModelResImgVo != null) {
							gisModelResImgVo.setImgUrl(baseStoragePath + gisModelResImgVo.getImgUrl());
						}
					}
				}
			}
		}
		result.setResult(data);
		result.success("查询成功");
		return result;
	}

	/**
	 * 添加或修改
	 * 
	 * @param gisModelRes
	 * @return
	 */
	@AutoLog(value = "GIS模型库类型-添加或修改")
	@ApiOperation(value = "GIS模型库类型-添加或修改", notes = "GIS模型库类型-添加或修改")
	@PostMapping(value = "one")
	public Result<Object> add(@RequestBody GisModelResParamVo paramVo) {
		if (!StringUtils.isEmpty(paramVo.getId())) {
			gisService.edit(paramVo);
		} else {
			return gisService.save(paramVo);
		}
		return Result.ok("保存成功！");
	}

	/**
	 * 描述: 更改是否展示状态
	 * @author: songguang.jiao
	 * 创建时间:  2020年4月14日 上午11:18:48
	 * 版本: V1.0
	 */
	@ApiOperation(value="更改是否展示状态",notes="更改是否展示状态")
	@GetMapping("/eidtShow")
	public Result<Object> eidtShow(String id) {
		if(StringUtils.isEmpty(id)){
			return Result.error("id不能为空");
		}
		UpdateWrapper<GisModelRes> updateWrapper=new UpdateWrapper<>();
		updateWrapper.lambda().eq(GisModelRes::getId, id);
		//查询当前状态
		QueryWrapper<GisModelRes> queryWrapper=new QueryWrapper<>();
		queryWrapper.lambda().eq(GisModelRes::getId, id).select(GisModelRes::getLegendShow);
		GisModelRes one = gisModelResService.getOne(queryWrapper);
		if(ShowConstant.SHOW.equals(one.getLegendShow())){
			one.setLegendShow(ShowConstant.NOT_SHOW);
		}else{
			one.setLegendShow(ShowConstant.SHOW);
		}
		gisModelResService.update(one, updateWrapper);
		return Result.ok("更新成功");
	}
}