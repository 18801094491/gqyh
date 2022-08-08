package com.zcdy.dsc.modules.inspection.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.system.query.QueryGenerator;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.inspection.entity.InspArea;
import com.zcdy.dsc.modules.inspection.param.InspAreaParam;
import com.zcdy.dsc.modules.inspection.service.InspAreaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import lombok.extern.slf4j.Slf4j;
import com.zcdy.dsc.common.system.base.controller.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 巡检区域
 * @Author: 在信汇通
 * @Date:   2021-01-15 15:30:47
 * @Version: V1.0
 */
@Slf4j
@Api(tags="巡检区域")
@RestController
@RequestMapping("/inspection/inspArea")
public class InspAreaController extends BaseController<InspArea, InspAreaService> {
	@Autowired
	private InspAreaService inspAreaService;
	
	/**
	 * 分页列表查询
	 *
	 * @param inspAreaParam
	 * @param req
	 * @return
	 */
	@AutoLog(value = "巡检区域-分页列表查询")
	@ApiOperation(value="巡检区域-分页列表查询", notes="巡检区域-分页列表查询")
	@GetMapping(value = "/list")
	public ResultData<?> queryPageList(InspAreaParam inspAreaParam,
								   HttpServletRequest req) {
		InspArea searchParam = new InspArea(inspAreaParam);
		searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
        if(searchParam.getName() != null && !"".equals(searchParam.getName()))
        {
            searchParam.setName("*" + searchParam.getName() + "*");//支持模糊查询
        }

		QueryWrapper<InspArea> queryWrapper = QueryGenerator.initQueryWrapper(searchParam, req.getParameterMap());
		queryWrapper.lambda().orderByDesc(InspArea::getCreateTime);//默认创建时间倒序
		Page<InspArea> page = new Page<InspArea>(inspAreaParam.getPageNo(), inspAreaParam.getPageSize());
		IPage<InspArea> pageList = inspAreaService.page(page, queryWrapper);
		return ResultData.ok(pageList);
	}

     /**
      * 获取全部巡检区域，用于各处下拉框
      *
      * @return
      */
     @AutoLog(value = "获取全部巡检区域")
     @ApiOperation(value="获取全部巡检区域", notes="获取全部巡检区域")
     @GetMapping(value = "/all")
     public ResultData<?> listAll(HttpServletRequest req) {
         InspArea searchParam = new InspArea();
         searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
         QueryWrapper<InspArea> queryWrapper = QueryGenerator.initQueryWrapper(searchParam, req.getParameterMap());
         queryWrapper.lambda().orderByAsc(InspArea::getName);//默认创建时间倒序
         List<InspArea> list = inspAreaService.list(queryWrapper);
         return ResultData.ok(list);
     }
	
	/**
	 * 添加
	 * @param inspArea
	 * @return
	 */
	@AutoLog(value = "巡检区域-添加")
	@ApiOperation(value="巡检区域-添加", notes="巡检区域-添加")
	@PostMapping(value = "/add")
	public ResultData<?> add(@RequestBody InspArea inspArea, HttpServletRequest req) {
	    ResultData<InspArea> resultData = new ResultData<InspArea>();
	    try {
            String username = JwtUtil.getUserNameByToken(req);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            inspArea.setId(uuid);
            inspArea.setCreateTime(new Date());
            inspArea.setCreateBy(username);
            boolean ok = inspAreaService.save(inspArea);
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
	 * @param inspArea
	 * @return
	 */
	@AutoLog(value = "巡检区域-编辑")
	@ApiOperation(value="巡检区域-编辑", notes="巡检区域-编辑")
	@PostMapping(value = "/edit")
	public ResultData<?> edit(@RequestBody InspArea inspArea, HttpServletRequest req) {
        ResultData<InspArea> resultData = new ResultData<InspArea>();
        try {
            if (inspArea == null || inspArea.getId() == null) {
                resultData.error500("id不能为空");
                return resultData;
            }
            String username = JwtUtil.getUserNameByToken(req);
            inspArea.setUpdateTime(new Date());
            inspArea.setUpdateBy(username);
            boolean ok = inspAreaService.updateById(inspArea);
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
	@AutoLog(value = "巡检区域-通过id删除")
	@ApiOperation(value="巡检区域-通过id删除", notes="巡检区域-通过id删除")
	@DeleteMapping(value = "/delete")
	public ResultData<?> deleteById(@RequestParam("id") String id, HttpServletRequest req) {
        ResultData<InspArea> resultData = new ResultData<InspArea>();
        if (id == null) {
            return ResultData.error("id不能为空");
        }
        InspArea inspArea = new InspArea();
        inspArea.setId(id);
        inspArea.setDelFlag(DelFlagConstant.DELETED);//删除标记
        String username = JwtUtil.getUserNameByToken(req);
        inspArea.setUpdateTime(new Date());
        inspArea.setUpdateBy(username);
        boolean ok = inspAreaService.updateById(inspArea);//逻辑删除
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
	@AutoLog(value = "巡检区域-批量删除")
	@ApiOperation(value="巡检区域-批量删除", notes="巡检区域-批量删除")
	@DeleteMapping(value = "/deletes")
	public ResultData<?> deleteBatch(@RequestParam(name="ids",required=true) String ids, HttpServletRequest req) {
        ResultData<InspArea> resultData = new ResultData<InspArea>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        }else {
            List<String> list = Arrays.asList(ids.split(","));
            List<InspArea> entityList = new ArrayList<>();
            for(String s : list)
            {
                InspArea inspArea = new InspArea();
                inspArea.setId(s);
                inspArea.setDelFlag(DelFlagConstant.DELETED);//删除标记
                inspArea.setUpdateTime(new Date());
                String username = JwtUtil.getUserNameByToken(req);
                inspArea.setUpdateBy(username);
                entityList.add(inspArea);
            }
            boolean ok = inspAreaService.updateBatchById(entityList);//批量逻辑删除
            //inspAreaService.removeByIds(Arrays.asList(ids.split(",")));//批量物理删除

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
	@AutoLog(value = "巡检区域-通过id查询")
	@ApiOperation(value="巡检区域-通过id查询", notes="巡检区域-通过id查询")
	@GetMapping(value = "/query")
	public ResultData<?> queryById(@RequestParam("id") String id) {
        ResultData<InspArea> resultData = new ResultData<InspArea>();
        try {
            if (ConvertUtils.isEmpty(id)) {
                resultData.error500("id不能为空");
                return resultData;
            }
            InspArea inspArea = inspAreaService.getById(id);
            resultData.setData(inspArea);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
	}
}