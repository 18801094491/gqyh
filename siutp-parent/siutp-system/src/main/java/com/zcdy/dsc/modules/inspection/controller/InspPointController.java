package com.zcdy.dsc.modules.inspection.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.query.QueryGenerator;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.system.vo.DictModel;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.inspection.constant.InspConstant;
import com.zcdy.dsc.modules.inspection.entity.InspPoint;
import com.zcdy.dsc.modules.inspection.param.EquipmentQueryParam;
import com.zcdy.dsc.modules.inspection.param.InspPointParam;
import com.zcdy.dsc.modules.inspection.service.InspPointService;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import com.zcdy.dsc.modules.system.service.ISysDictService;
import com.zcdy.dsc.modules.system.vo.DictDropdown;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 巡检点
 * @Author: 在信汇通
 * @Date:   2021-01-15 16:26:44
 * @Version: V1.0
 */
@Slf4j
@Api(tags="巡检点")
@RestController
@RequestMapping("/inspection/inspPoint")
public class InspPointController extends BaseController<InspPoint, InspPointService> {
    @Autowired
	private InspPointService inspPointService;

    @Autowired
    private ISysDictService sysDictService;

    /**
     * 获取设备列表
     * @param param
     * @return
     */
    @ApiOperation(value = "获取设备列表", notes = "获取设备列表")
    @GetMapping(value = "/equipments")
    public Result<List<OptEquipmentModel>> queryEquipmentList(EquipmentQueryParam param) {
        Result<List<OptEquipmentModel>> result = new Result<List<OptEquipmentModel>>();
        Page<OptEquipmentModel> page = new Page<OptEquipmentModel>(param.getPageNo(), param.getPageSize());
//        IPage<OptEquipmentModel> list = inspPointService.getEquipmentList(page, param);
        List<OptEquipmentModel> list = inspPointService.getEquipmentList(param);
        result.setResult(list);
        result.success("查询成功");
        return result;
    }
    /**
     * 巡检点类型来自数据字典，必须先在数据字典中创建相关信息
     * @return
     */
    @ApiOperation(value="获取巡检点类型")
    @GetMapping(value="/types")
	public Result<List<DictDropdown>> getInspPointTypeList() {
        String code = InspConstant.INSP_POINT_TYPE_CODE;
        Result<List<DictDropdown>> result=new Result<>();
        List<DictModel> dmlist = sysDictService.queryDictItemsByCode(code);
        List<DictDropdown> value = dmlist.stream().map(dictModel -> {
            DictDropdown dd = new DictDropdown();
            dd.setCode(dictModel.getValue());
            dd.setTitle(dictModel.getText());
            return dd;
        }).collect(Collectors.toList());
        result.setResult(value);
        result.success("查询成功");
        return result;
    }
	
	/**
	 * 分页列表查询
	 *
	 * @param inspPointParam
	 * @param req
	 * @return
	 */
	@AutoLog(value = "巡检点-分页列表查询")
	@ApiOperation(value="巡检点-分页列表查询", notes="巡检点-分页列表查询")
	@GetMapping(value = "/list")
	public ResultData<?> queryPageList(InspPointParam inspPointParam,
								   HttpServletRequest req) {
		InspPoint searchParam = new InspPoint(inspPointParam);
        searchParam.setQueryTypeDictCode(InspConstant.INSP_POINT_TYPE_CODE);
        searchParam.setQueryImportDictCode(InspConstant.INSP_POINT_IMPORTANT_CODE);
		searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
		Page<InspPoint> page = new Page<InspPoint>(inspPointParam.getPageNo(), inspPointParam.getPageSize());
		IPage<InspPoint> pageList = inspPointService.selectPageDate(page, searchParam);
		return ResultData.ok(pageList);
	}
	
	/**
	 * 添加
	 * @param inspPoint
	 * @return
	 */
	@AutoLog(value = "巡检点-添加")
	@ApiOperation(value="巡检点-添加", notes="巡检点-添加")
	@PostMapping(value = "/add")
	public ResultData<?> add(@RequestBody InspPoint inspPoint, HttpServletRequest req) {
	    ResultData<InspPoint> resultData = new ResultData<InspPoint>();
	    try {
            String username = JwtUtil.getUserNameByToken(req);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            inspPoint.setId(uuid);
            inspPoint.setCreateTime(new Date());
            inspPoint.setCreateBy(username);
            boolean ok = inspPointService.save(inspPoint);
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
	 * @param inspPoint
	 * @return
	 */
	@AutoLog(value = "巡检点-编辑")
	@ApiOperation(value="巡检点-编辑", notes="巡检点-编辑")
	@PostMapping(value = "/edit")
	public ResultData<?> edit(@RequestBody InspPoint inspPoint, HttpServletRequest req) {
        ResultData<InspPoint> resultData = new ResultData<InspPoint>();
        try {
            if (inspPoint == null || inspPoint.getId() == null) {
                resultData.error500("id不能为空");
                return resultData;
            }
            String username = JwtUtil.getUserNameByToken(req);
            inspPoint.setUpdateTime(new Date());
            inspPoint.setUpdateBy(username);
            boolean ok = inspPointService.updateById(inspPoint);
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
     * 通过区域id查询巡检点
     * @param inspPointParam
     * @return
     */
    @AutoLog(value = "通过区域id查询巡检点")
    @ApiOperation(value="通过区域id查询巡检点", notes="通过区域id查询巡检点")
    @GetMapping(value = "/list4area")
    public ResultData<?> list4area(InspPointParam inspPointParam, HttpServletRequest req) {
        if (inspPointParam.getAreaId() == null) {
            return ResultData.error("区域id不能为空");
        }
        InspPoint inspPoint = new InspPoint(inspPointParam);
        String name = inspPoint.getName();
        String code = inspPoint.getCode();
        if(name != null && !"".equals(name))
        {
            inspPoint.setName("*" + name + "*");
        }
        if(code != null && !"".equals(code))
        {
            inspPoint.setCode("*" + code + "*");
        }
        inspPoint.setDelFlag(DelFlagConstant.NORMAL);//删除标记
        QueryWrapper<InspPoint> queryWrapper = QueryGenerator.initQueryWrapper(inspPoint, req.getParameterMap());
        queryWrapper.lambda().orderByAsc(InspPoint::getName);//默认名称排序
        List<InspPoint> list = inspPointService.list(queryWrapper);
        return ResultData.ok(list);
    }

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "巡检点-通过id删除")
	@ApiOperation(value="巡检点-通过id删除", notes="巡检点-通过id删除")
	@DeleteMapping(value = "/delete")
	public ResultData<?> deleteById(@RequestParam("id") String id, HttpServletRequest req) {
        ResultData<InspPoint> resultData = new ResultData<InspPoint>();
        if (id == null) {
            return ResultData.error("id不能为空");
        }
        InspPoint inspPoint = new InspPoint();
        inspPoint.setId(id);
        inspPoint.setDelFlag(DelFlagConstant.DELETED);//删除标记
        String username = JwtUtil.getUserNameByToken(req);
        inspPoint.setUpdateTime(new Date());
        inspPoint.setUpdateBy(username);
        boolean ok = inspPointService.updateById(inspPoint);//逻辑删除
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
	@AutoLog(value = "巡检点-批量删除")
	@ApiOperation(value="巡检点-批量删除", notes="巡检点-批量删除")
	@DeleteMapping(value = "/deletes")
	public ResultData<?> deleteBatch(@RequestParam(name="ids",required=true) String ids, HttpServletRequest req) {
        ResultData<InspPoint> resultData = new ResultData<InspPoint>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        }else {
            List<String> list = Arrays.asList(ids.split(","));
            List<InspPoint> entityList = new ArrayList<>();
            for(String s : list)
            {
                InspPoint inspPoint = new InspPoint();
                inspPoint.setId(s);
                inspPoint.setDelFlag(DelFlagConstant.DELETED);//删除标记
                inspPoint.setUpdateTime(new Date());
                String username = JwtUtil.getUserNameByToken(req);
                inspPoint.setUpdateBy(username);
                entityList.add(inspPoint);
            }
            boolean ok = inspPointService.updateBatchById(entityList);//批量逻辑删除

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
	@AutoLog(value = "巡检点-通过id查询")
	@ApiOperation(value="巡检点-通过id查询", notes="巡检点-通过id查询")
	@GetMapping(value = "/query")
	public ResultData<?> queryById(@RequestParam("id") String id) {
        ResultData<InspPoint> resultData = new ResultData<InspPoint>();
        try {
            if (ConvertUtils.isEmpty(id)) {
                resultData.error500("id不能为空");
                return resultData;
            }
            InspPoint inspPoint = inspPointService.getById(id);
            resultData.setData(inspPoint);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
	}
}