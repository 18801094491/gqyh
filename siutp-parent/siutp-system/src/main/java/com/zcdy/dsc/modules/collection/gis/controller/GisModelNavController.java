package com.zcdy.dsc.modules.collection.gis.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.constant.ShowConstant;
import com.zcdy.dsc.constant.WorkingStatus;
import com.zcdy.dsc.modules.collection.gis.entity.GisModelNav;
import com.zcdy.dsc.modules.collection.gis.service.GisModelNavService;
import com.zcdy.dsc.modules.collection.gis.vo.GisModelNavVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述: 监控地图导航栏管理
 * @author：  songguang.jiao
 * 创建时间：  2020年4月7日 上午9:58:19
 * 版本号: V1.0
 */
@Api(tags="监控地图导航栏管理")
@RestController
@RequestMapping("/gis/gis")
public class GisModelNavController extends BaseController<GisModelNav, GisModelNavService>{

	@Resource
	private GisModelNavService gisModelNavService;
	
	
	/**
	 * 描述: 监控地图导航栏管理列表
	 * @author：  songguang.jiao
	 * 创建时间：  2020年4月7日 上午10:20:20
	 * 版本号: V1.0
	 */
	@ApiOperation(value="监控地图导航栏管理列表",notes="监控地图导航栏管理列表")
	@GetMapping("/")
	public Result<List<GisModelNavVo>> list(String modelType){
		Result<List<GisModelNavVo>> result=new Result<>();
		List<GisModelNavVo> list = gisModelNavService.queryList(modelType);
		list.forEach(gisModelNav->{
			if(gisModelNav.getModelThumb()!=null){
				gisModelNav.setModelThumb(baseStoragePath+gisModelNav.getModelThumb());
			}
		});
		result.setResult(list);
		return result.success("success");
	}
	
	
	/**
	 * 描述: 新增或者修改监控地图导航栏
	 * @author：  songguang.jiao
	 * 创建时间：  2020年4月7日 上午11:38:25
	 * 版本号: V1.0
	 */
	@ApiOperation(value="新增或修改(图片上传:/sys/file/uploadGisNav)",notes="新增或修改(图片上传:/sys/file/uploadGisNav)")
	@PostMapping("/one")
	public Result<Object>  one(@RequestBody GisModelNavVo gisModelNavVo){
		GisModelNav gisModelNav=new GisModelNav();
		BeanUtil.copyProperties(gisModelNavVo, gisModelNav);
		//查询是否存在
		QueryWrapper<GisModelNav> queryWrapper=new QueryWrapper<>();
		queryWrapper.lambda().eq(GisModelNav::getModelType, gisModelNav.getModelType());
		if(StringUtils.isNotEmpty(gisModelNavVo.getId())){
			queryWrapper.lambda().ne(GisModelNav::getId, gisModelNav.getId());
		}
		GisModelNav one = gisModelNavService.getOne(queryWrapper);
		if(one!=null){
			return Result.error("该类型导航图标已存在");
		}
		if(StringUtils.isEmpty(gisModelNavVo.getId())){
			gisModelNavService.save(gisModelNav);
		}else{
			gisModelNavService.updateById(gisModelNav);
		}
		return Result.ok("保存成功");
	}
	
	/**
	 * 描述: 更改是否展示状态
	 * @author：  songguang.jiao
	 * 创建时间：  2020年4月7日 下午12:05:11
	 * 版本号: V1.0
	 */
	@ApiOperation(value="更改是否展示状态",notes="更改是否展示状态")
	@GetMapping("/editDataShow")
	public Result<Object> editDataShow(String id) {
		if(StringUtils.isEmpty(id)){
			return Result.error("id不能为空");
		}
		QueryWrapper<GisModelNav> queryWrapper=new QueryWrapper<>();
		LambdaQueryWrapper<GisModelNav> lambdaQueryWrapper = queryWrapper.lambda().select(GisModelNav::getDataShow).eq(GisModelNav::getId, id);
		GisModelNav one = gisModelNavService.getOne(lambdaQueryWrapper);
		UpdateWrapper<GisModelNav> updateWrapper=new UpdateWrapper<>();
		updateWrapper.lambda().eq(GisModelNav::getId, id);
		if(ShowConstant.SHOW.endsWith(one.getDataShow())){
			one.setDataShow(ShowConstant.NOT_SHOW);
		}else{
			one.setDataShow(ShowConstant.SHOW);
		}
		gisModelNavService.update(one,updateWrapper);
		return Result.ok("更新成功");
	}
	
	/**
	 * 描述: 更改启停用状态
	 * @author：  songguang.jiao
	 * 创建时间：  2020年4月7日 下午12:05:11
	 * 版本号: V1.0
	 */
	@ApiOperation(value="更改启停用状态",notes="更改启停用状态")
	@GetMapping("/editStatus")
	public Result<Object> editStatus(String id) {
		if(StringUtils.isEmpty(id)){
			return Result.error("id不能为空");
		}
		QueryWrapper<GisModelNav> queryWrapper = new QueryWrapper<>();
		LambdaQueryWrapper<GisModelNav> select = queryWrapper.lambda().select(GisModelNav::getNavStatus).eq(GisModelNav::getId, id);
		GisModelNav one = gisModelNavService.getOne(select);
		UpdateWrapper<GisModelNav> updateWrapper=new UpdateWrapper<>();
		updateWrapper.lambda().eq(GisModelNav::getId, id);
		if(WorkingStatus.WORKING.endsWith(one.getNavStatus())){
			one.setNavStatus(WorkingStatus.STOP);
		}else{
			one.setNavStatus(WorkingStatus.WORKING);
		}
		gisModelNavService.update(one,updateWrapper);
		return Result.ok("更新成功");
	}
	
	
}
