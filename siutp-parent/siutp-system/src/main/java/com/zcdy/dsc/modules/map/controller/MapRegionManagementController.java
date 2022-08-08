package com.zcdy.dsc.modules.map.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.api.R;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.common.system.query.QueryGenerator;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.map.entity.MapRegionManagement;
import com.zcdy.dsc.modules.map.entity.MapRiverInformation;
import com.zcdy.dsc.modules.map.param.MapRegionParam;
import com.zcdy.dsc.modules.map.service.MapRegionManagementService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.rdp.vo.RegionVo;
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
 * @Description: 地图-区域管理表
 * @Author: 在信汇通
 * @Date: 2020-12-02 15:29:14
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "地图-区域管理表")
@RestController
@RequestMapping("/map/region")
public class MapRegionManagementController extends BaseController<MapRegionManagement, MapRegionManagementService> {
    @Autowired
    private MapRegionManagementService mapRegionManagementService;

    /**
     * 分页列表查询
     *
     * @param mapRegionParam
     * @return
     */
    @AutoLog(value = "地图-区域管理表-分页列表查询")
    @ApiOperation(value = "地图-区域管理表-分页列表查询", notes = "地图-区域管理表-分页列表查询")
    @GetMapping(value = "/list")
    public ResultData<IPage<MapRegionManagement>> queryPageList(MapRegionParam mapRegionParam) {
        ResultData<IPage<MapRegionManagement>> resultData = new ResultData<IPage<MapRegionManagement>>();
        try {
            Page<MapRegionManagement> page = new Page<MapRegionManagement>(mapRegionParam.getPageNo(), mapRegionParam.getPageSize());
            IPage<MapRegionManagement> pageList = mapRegionManagementService.queryPageList(page, mapRegionParam);
            resultData.setData(pageList);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
    }

    /**
     * 全部列表数据
     *
     * @return
     */
    @AutoLog(value = "地图-区域管理表-全部列表查询")
    @ApiOperation(value = "地图-区域管理表-全部列表查询", notes = "地图-区域管理表-全部列表查询")
    @GetMapping(value = "/all")
    public ResultData<List<MapRegionManagement>> regionAllList() {
        ResultData<List<MapRegionManagement>> resultData = new ResultData<List<MapRegionManagement>>();
        try {
            List<MapRegionManagement> allList = mapRegionManagementService.list();
            resultData.setData(allList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
    }

    /**
     * 添加
     *
     * @param mapRegionManagement
     * @return
     */
    @AutoLog(value = "地图-区域管理表-添加")
    @ApiOperation(value = "地图-区域管理表-添加", notes = "地图-区域管理表-添加")
    @PostMapping(value = "/add")
    public ResultData<?> add(@RequestBody MapRegionManagement mapRegionManagement) {
        ResultData<MapRegionManagement> resultData = new ResultData<MapRegionManagement>();
        try {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            mapRegionManagement.setId(uuid);
            mapRegionManagement.setCreateTime(new Date());
            mapRegionManagementService.save(mapRegionManagement);
            resultData.success("保存成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
    }

    /**
     * 编辑
     *
     * @param mapRegionManagement
     * @return
     */
    @AutoLog(value = "地图-区域管理表-编辑")
    @ApiOperation(value = "地图-区域管理表-编辑", notes = "地图-区域管理表-编辑")
    @PostMapping(value = "/edit")
    public ResultData<MapRegionManagement> edit(@RequestBody MapRegionManagement mapRegionManagement) {
        ResultData<MapRegionManagement> resultData = new ResultData<MapRegionManagement>();
        try {
            if (mapRegionManagement == null || mapRegionManagement.getId() == null) {
                resultData.error500("区域id不能为空");
                return resultData;
            }
            mapRegionManagement.setUpdateTime(new Date());
            boolean ok = mapRegionManagementService.updateById(mapRegionManagement);
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
     *
     * @param id
     * @return
     */
    @AutoLog(value = "地图-区域管理表-通过id删除")
    @ApiOperation(value = "地图-区域管理表-通过id删除", notes = "地图-区域管理表-通过id删除")
    @DeleteMapping(value = "/delete")
    public ResultData<?> delete(@RequestParam("id") String id) {
        ResultData<MapRegionManagement> resultData = new ResultData<MapRegionManagement>();
        if (id == null) {
            return ResultData.error("id不能为空");
        }
        boolean ok = mapRegionManagementService.removeById(id);
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
    @AutoLog(value = "地图-区域管理表-批量删除")
    @ApiOperation(value = "地图-区域管理表-批量删除", notes = "地图-区域管理表-批量删除")
    @DeleteMapping(value = "/deletes")
    public ResultData<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        ResultData<MapRegionManagement> resultData = new ResultData<MapRegionManagement>();
        if (ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        } else {
            mapRegionManagementService.removeByIds(Arrays.asList(ids.split(",")));
            resultData.success("批量删除成功!");
        }
        return resultData;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "地图-区域管理表-通过id查询")
    @ApiOperation(value = "地图-区域管理表-通过id查询", notes = "地图-区域管理表-通过id查询")
    @GetMapping(value = "/query")
    public ResultData<?> queryById(@RequestParam("id") String id) {
        ResultData<MapRegionManagement> resultData = new ResultData<MapRegionManagement>();
        try {
            if (ConvertUtils.isEmpty(id)) {
                resultData.error500("id不能为空");
                return resultData;
            }
            MapRegionManagement mapRegionManagement = mapRegionManagementService.getById(id);
            resultData.setData(mapRegionManagement);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    /**
     * 获取地图区域信息列表
     * @return
     */
    @AutoLog(value = "获取地图区域信息列表")
    @ApiOperation(value = "获取地图区域信息列表", notes = "获取地图区域信息列表")
    @GetMapping(value = "/app/regions")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<List<MapRegionManagement>> getRegionList() {
        ResultData<List<MapRegionManagement>> resultData = new ResultData<>();
        try {
            List<MapRegionManagement> regionList = mapRegionManagementService.list();
            resultData.setData(regionList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

}