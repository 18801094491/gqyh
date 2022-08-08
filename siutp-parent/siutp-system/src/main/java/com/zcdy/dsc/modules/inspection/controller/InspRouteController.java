package com.zcdy.dsc.modules.inspection.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.query.QueryGenerator;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.inspection.entity.InspRoute;
import com.zcdy.dsc.modules.inspection.entity.InspRoutePoint;
import com.zcdy.dsc.modules.inspection.param.InspRouteParam;
import com.zcdy.dsc.modules.inspection.service.InspRoutePointService;
import com.zcdy.dsc.modules.inspection.service.InspRouteService;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;

 /**
 * @Description: 巡检路线
 * @Author: 在信汇通
 * @Date:   2021-01-15 16:27:08
 * @Version: V1.0
 */
@Slf4j
@Api(tags="巡检路线")
@RestController
@RequestMapping("/inspection/inspRoute")
public class InspRouteController extends BaseController<InspRoute, InspRouteService> {
	@Autowired
	private InspRouteService inspRouteService;
	@Autowired
	private InspRoutePointService inspRoutePointService;

	/**
	 * 分页列表查询
	 *
	 * @param inspRouteParam
	 * @param req
	 * @return
	 */
	@AutoLog(value = "巡检路线-分页列表查询")
	@ApiOperation(value="巡检路线-分页列表查询", notes="巡检路线-分页列表查询")
	@GetMapping(value = "/list")
	public ResultData<?> queryPageList(InspRouteParam inspRouteParam,
								   HttpServletRequest req) {
		InspRoute searchParam = new InspRoute(inspRouteParam);
		searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
		Page<InspRoute> page = new Page<InspRoute>(inspRouteParam.getPageNo(), inspRouteParam.getPageSize());
		IPage<InspRoute> pageList = inspRouteService.selectPageData(page, searchParam);
		return ResultData.ok(pageList);
	}
	
	/**
	 * 添加
	 * @param inspRoute
	 * @return
	 */
	@AutoLog(value = "巡检路线-添加")
	@ApiOperation(value="巡检路线-添加", notes="巡检路线-添加")
	@PostMapping(value = "/add")
	public ResultData<?> add(@RequestBody InspRoute inspRoute, HttpServletRequest req) {
	    ResultData<InspRoute> resultData = new ResultData<InspRoute>();
	    try {
            String username = JwtUtil.getUserNameByToken(req);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            inspRoute.setId(uuid);
            inspRoute.setCreateTime(new Date());
            inspRoute.setCreateBy(username);
            boolean ok = inspRouteService.saveRoute(inspRoute);
            if(ok){
                resultData.success("保存成功！");
            }
            else{
                resultData.error500("操作失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
	}

    /**
     * 巡检路线所包含的巡检点-删除
     * @param ids
     * @param req
     * @return
     */
    @AutoLog(value = "巡检路线所包含的巡检点-删除")
    @ApiOperation(value="巡检路线所包含的巡检点-删除", notes="巡检路线所包含的巡检点-删除")
    @DeleteMapping(value = "/points/delete")
    public ResultData<?> delPoints(@RequestParam(name="ids",required=true) String ids, HttpServletRequest req){
        ResultData<InspRoute> resultData = new ResultData<InspRoute>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        }else {
            String username = JwtUtil.getUserNameByToken(req);
            boolean ok = inspRoutePointService.deleteRoutePoints(ids, username);
            if(ok){
                resultData.success("批量删除成功!");
            }
            else{
                resultData.error500("操作失败");
            }
        }
        return resultData;
    }

    /**
     * 巡检路线所包含的巡检点-添加
     * @param inspRoute
     * @param req
     * @return
     */
    @AutoLog(value = "巡检路线所包含的巡检点-添加")
    @ApiOperation(value="巡检路线所包含的巡检点-添加", notes="巡检路线所包含的巡检点-添加")
    @PostMapping(value = "/points/add")
    public ResultData<?> addPoints(@RequestBody InspRoute inspRoute, HttpServletRequest req){
        ResultData<InspRoute> resultData = new ResultData<InspRoute>();
        try {
            List<InspRoutePoint> pointList = inspRoute.getPointList();
            if(pointList == null || pointList.size() == 0)
            {
                resultData.error500("巡检点列表不能为空");
                return resultData;
            }
            String username = JwtUtil.getUserNameByToken(req);
            boolean ok = inspRouteService.saveRoutePoints(inspRoute, username);
            if(ok){
                resultData.success("保存成功！");
            }
            else{
                resultData.error500("操作失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("操作失败");
        }
        return resultData;
    }

	/**
	 * 编辑
	 * @param inspRoute
	 * @return
	 */
	@AutoLog(value = "巡检路线-编辑")
	@ApiOperation(value="巡检路线-编辑", notes="巡检路线-编辑")
	@PostMapping(value = "/edit")
	public ResultData<?> edit(@RequestBody InspRoute inspRoute, HttpServletRequest req) {
        ResultData<InspRoute> resultData = new ResultData<InspRoute>();
        try {
            if (inspRoute == null || inspRoute.getId() == null) {
                resultData.error500("id不能为空");
                return resultData;
            }
            String username = JwtUtil.getUserNameByToken(req);
            inspRoute.setUpdateTime(new Date());
            inspRoute.setUpdateBy(username);
            boolean ok = inspRouteService.updateRouteById(inspRoute);
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
      * 通过区域id查询巡检路线
      * @param areaId
      * @return
      */
     @AutoLog(value = "通过区域id查询巡检路线")
     @ApiOperation(value="通过区域id查询巡检路线", notes="通过区域id查询巡检路线")
     @GetMapping(value = "/list4area")
     public ResultData<?> list4area(@RequestParam("areaId") String areaId, HttpServletRequest req) {
         if (areaId == null) {
             return ResultData.error("区域id不能为空");
         }
         InspRoute inspRoute = new InspRoute();
         inspRoute.setAreaId(areaId);
         inspRoute.setDelFlag(DelFlagConstant.NORMAL);//删除标记
         QueryWrapper<InspRoute> queryWrapper = QueryGenerator.initQueryWrapper(inspRoute, req.getParameterMap());
         queryWrapper.lambda().orderByAsc(InspRoute::getName);//默认创建时间倒序
         List<InspRoute> list = inspRouteService.list(queryWrapper);
         return ResultData.ok(list);
     }

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "巡检路线-通过id删除")
	@ApiOperation(value="巡检路线-通过id删除", notes="巡检路线-通过id删除")
	@DeleteMapping(value = "/delete")
	public ResultData<?> deleteById(@RequestParam("id") String id, HttpServletRequest req) {
        ResultData<InspRoute> resultData = new ResultData<InspRoute>();
        if (id == null) {
            return ResultData.error("id不能为空");
        }
        InspRoute inspRoute = new InspRoute();
        inspRoute.setId(id);
        inspRoute.setDelFlag(DelFlagConstant.DELETED);//删除标记
        String username = JwtUtil.getUserNameByToken(req);
        inspRoute.setUpdateTime(new Date());
        inspRoute.setUpdateBy(username);
        boolean ok = inspRouteService.deleteRoute(inspRoute);//逻辑删除
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
	@AutoLog(value = "巡检路线-批量删除")
	@ApiOperation(value="巡检路线-批量删除", notes="巡检路线-批量删除")
	@DeleteMapping(value = "/deletes")
	public ResultData<?> deleteBatch(@RequestParam(name="ids",required=true) String ids, HttpServletRequest req) {
        ResultData<InspRoute> resultData = new ResultData<InspRoute>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        }else {
            String username = JwtUtil.getUserNameByToken(req);
            boolean ok = inspRouteService.deleteRoutes(ids, username);
            if(ok){
                resultData.success("批量删除成功!");
            }
            else{
                resultData.error500("操作失败");
            }
        }
        return resultData;
	}
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "巡检路线-通过id查询")
	@ApiOperation(value="巡检路线-通过id查询", notes="巡检路线-通过id查询")
	@GetMapping(value = "/query")
	public ResultData<?> queryById(@RequestParam("id") String id) {
        ResultData<InspRoute> resultData = new ResultData<InspRoute>();
        try {
            if (ConvertUtils.isEmpty(id)) {
                resultData.error500("id不能为空");
                return resultData;
            }
            InspRoute inspRoute = inspRouteService.getOneById(id);
            resultData.setData(inspRoute);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
	}
}