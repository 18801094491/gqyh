package com.zcdy.dsc.modules.map.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.common.system.query.QueryGenerator;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.map.entity.MapRegionManagement;
import com.zcdy.dsc.modules.map.entity.MapRiverInformation;
import com.zcdy.dsc.modules.map.param.MapRiverParam;
import com.zcdy.dsc.modules.map.service.MapRiverInformationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.rdp.vo.RiverVo;
import lombok.extern.slf4j.Slf4j;
import com.zcdy.dsc.common.system.base.controller.BaseController;

import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 地图-河流信息表
 * @Author: 在信汇通
 * @Date:   2020-12-10 16:26:42
 * @Version: V1.0
 */
@Slf4j
@Api(tags="地图-河流信息表")
@RestController
@RequestMapping("/map/river")
public class MapRiverInformationController extends BaseController<MapRiverInformation, MapRiverInformationService> {
	@Autowired
	private MapRiverInformationService mapRiverInformationService;
	
	/**
	 * 分页列表查询
	 *
	 * @param mapRiverParam
	 * @return
	 */
	@AutoLog(value = "地图-河流信息表-分页列表查询")
	@ApiOperation(value="地图-河流信息表-分页列表查询", notes="地图-河流信息表-分页列表查询")
	@GetMapping(value = "/list")
	public ResultData<IPage<MapRiverInformation>> queryPageList(MapRiverParam mapRiverParam) {
		ResultData<IPage<MapRiverInformation>> resultData = new ResultData<IPage<MapRiverInformation>>();
		try {
			Page<MapRiverInformation> page = new Page<MapRiverInformation>(mapRiverParam.getPageNo(), mapRiverParam.getPageSize());
			IPage<MapRiverInformation> pageList = mapRiverInformationService.queryPageData(page, mapRiverParam);
			resultData.setData(pageList);
		}catch (Exception e){
			log.error(e.getMessage(), e);
			resultData.error500("操作失败");
		}
		return resultData;
	}
	/**
	 * 列表查询
	 *
	 * @return
	 */
	@AutoLog(value = "地图-河流信息表-列表查询")
	@ApiOperation(value="地图-河流信息表-列表查询", notes="地图-河流信息表-列表查询")
	@GetMapping(value = "/all")
	public ResultData<?> queryAllList() {
		ResultData<List<MapRiverInformation>> resultData = new ResultData<List<MapRiverInformation>>();
		try {
			List<MapRiverInformation> allList = mapRiverInformationService.list();
			resultData.setData(allList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultData.error500("操作失败");
		}
		return resultData;
	}

	/**
	 * 添加
	 * @param mapRiverInformation
	 * @return
	 */
	@AutoLog(value = "地图-河流信息表-添加")
	@ApiOperation(value="地图-河流信息表-添加", notes="地图-河流信息表-添加")
	@PostMapping(value = "/add")
	public ResultData<?> add(@RequestBody MapRiverInformation mapRiverInformation) {
	 ResultData<MapRiverInformation> resultData = new ResultData<MapRiverInformation>();
        try {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            mapRiverInformation.setId(uuid);
            mapRiverInformation.setCreateTime(new Date());
            mapRiverInformationService.save(mapRiverInformation);
            resultData.success("保存成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
	}

	/**
	 * 编辑
	 * @param mapRiverInformation
	 * @return
	 */
	@AutoLog(value = "地图-河流信息表-编辑")
	@ApiOperation(value="地图-河流信息表-编辑", notes="地图-河流信息表-编辑")
	@PostMapping(value = "/edit")
	public ResultData<?> edit(@RequestBody MapRiverInformation mapRiverInformation) {
        ResultData<MapRiverInformation> resultData = new ResultData<MapRiverInformation>();
        try {
            if (mapRiverInformation == null || mapRiverInformation.getId() == null) {
                resultData.error500("id不能为空");
                return resultData;
            }
            mapRiverInformation.setUpdateTime(new Date());
            boolean ok = mapRiverInformationService.updateById(mapRiverInformation);
            if (ok) {
                resultData.success("编辑成功！");
            } else {
                resultData.error500("操作失败");
            }
        } catch (
                Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "地图-河流信息表-通过id删除")
	@ApiOperation(value="地图-河流信息表-通过id删除", notes="地图-河流信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	public ResultData<?> deleteById(@RequestParam("id") String id) {
        ResultData<MapRiverInformation> resultData = new ResultData<MapRiverInformation>();
        if (id == null) {
            return ResultData.error("id不能为空");
        }
        boolean ok = mapRiverInformationService.removeById(id);
        if (ok) {
            resultData.success("删除成功!");
        } else {
            resultData.error500("删除失败!");
        }
        return resultData;
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "地图-河流信息表-批量删除")
	@ApiOperation(value="地图-河流信息表-批量删除", notes="地图-河流信息表-批量删除")
	@DeleteMapping(value = "/deletes")
	public ResultData<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        ResultData<MapRiverInformation> resultData = new ResultData<MapRiverInformation>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        }else {
            mapRiverInformationService.removeByIds(Arrays.asList(ids.split(",")));
            resultData.success("批量删除成功!");
        }
        return resultData;
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "地图-河流信息表-通过id查询")
	@ApiOperation(value="地图-河流信息表-通过id查询", notes="地图-河流信息表-通过id查询")
	@GetMapping(value = "/query")
	public ResultData<?> queryById(@RequestParam("id") String id) {
        ResultData<MapRiverInformation> resultData = new ResultData<MapRiverInformation>();
        try {
            if (ConvertUtils.isEmpty(id)) {
                resultData.error500("id不能为空");
                return resultData;
            }
            MapRiverInformation mapRiverInformation = mapRiverInformationService.getById(id);
            resultData.setData(mapRiverInformation);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
	}

	 /**
	  * 获取地图河流信息列表
	  * @return
	  */
	 @AutoLog(value = "获取地图河流信息列表")
	 @ApiOperation(value = "获取地图河流信息列表", notes = "获取地图河流信息列表")
	 @GetMapping(value = "/app/rivers")
	 @ApiVersion(group = VersionConstant.VERSION_APP)
	 public ResultData<List<MapRiverInformation>> getRiverList() {
		 ResultData<List<MapRiverInformation>> resultData = new ResultData<>();
		 try {
			 List<MapRiverInformation> riverList = mapRiverInformationService.list();
			 resultData.setData(riverList);
		 } catch (Exception e) {
			 log.error(e.getMessage(), e);
			 resultData.error500("查询失败");
		 }
		 return resultData;
	 }
}