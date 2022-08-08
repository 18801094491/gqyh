package com.zcdy.dsc.modules.worklist.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.system.vo.DictModel;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.modules.inspection.param.EquipmentQueryParam;
import com.zcdy.dsc.modules.inspection.service.InspPointService;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import com.zcdy.dsc.modules.system.service.ISysDictService;
import com.zcdy.dsc.modules.system.vo.DictDropdown;
import com.zcdy.dsc.modules.worklist.constant.WorkListConstant;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatter;
import com.zcdy.dsc.modules.worklist.param.WorkListMatterParam;
import com.zcdy.dsc.modules.worklist.service.WorkListMatterService;
import com.zcdy.dsc.modules.worklist.utils.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @Description: 工单任务App接口
* @Author: 在信汇通
* @Date:   2021-03-02
* @Version: V1.0
*/
@Slf4j
@Api(tags="工单任务")
@RestController
@RequestMapping("/worklist/workListMatter/app")
public class WorkListMatterAppController extends BaseController<WorkListMatter, WorkListMatterService> {
    @Autowired
    private WorkListMatterService workListMatterService;

    @Autowired
    private ISysDictService sysDictService;

    @Autowired
    private InspPointService inspPointService;

    /**
     * 根据code获取数据字典
     * @param code
     * @return
     */
    private List<DictDropdown> getDictIteamsByCode(String code)
    {
        List<DictModel> dmlist = sysDictService.queryDictItemsByCode(code);
        List<DictDropdown> value = dmlist.stream().map(dictModel -> {
            DictDropdown dd = new DictDropdown();
            dd.setCode(dictModel.getValue());
            dd.setTitle(dictModel.getText());
            return dd;
        }).collect(Collectors.toList());
        return value;
    }

    /**
     * 问题类型来自数据字典，必须先在数据字典中创建相关信息
     * @return
     */
    @ApiOperation(value="获取问题类型")
    @GetMapping(value="/types")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public Result<List<DictDropdown>> getTypeList() {
        String code = WorkListConstant.WORK_LIST_MATTER_TYPE;
        Result<List<DictDropdown>> result=new Result<>();
        result.setResult(getDictIteamsByCode(code));
        result.success("查询成功");
        return result;
    }

    /**
     * 问题状态来自数据字典，必须先在数据字典中创建相关信息
     * @return
     */
    @ApiOperation(value="获取问题状态")
    @GetMapping(value="/status")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public Result<List<DictDropdown>> getStatusList() {
        String code = WorkListConstant.WORK_LIST_MATTER_STATUS;
        Result<List<DictDropdown>> result=new Result<>();
        result.setResult(getDictIteamsByCode(code));
        result.success("查询成功");
        return result;
    }

    /**
     * 问题等级来自数据字典，必须先在数据字典中创建相关信息
     * @return
     */
    @ApiOperation(value="获取问题等级")
    @GetMapping(value="/level")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public Result<List<DictDropdown>> getLevelList() {
        String code = WorkListConstant.WORK_LIST_MATTER__MATTER_LEVEL_CODE;
        Result<List<DictDropdown>> result=new Result<>();
        result.setResult(getDictIteamsByCode(code));
        result.success("查询成功");
        return result;
    }

    /**
     * 问题类型来自数据字典，必须先在数据字典中创建相关信息
     * @return
     */
    @ApiOperation(value="获取工单任务的问题类型")
    @GetMapping(value="/matterType")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public Result<List<DictDropdown>> getMatterTypeList() {
        String code = WorkListConstant.WORK_LIST_MATTER__MATTER_TYPE_CODE;
        Result<List<DictDropdown>> result=new Result<>();
        result.setResult(getDictIteamsByCode(code));
        result.success("查询成功");
        return result;
    }

    @ApiOperation(value = "获取设备列表", notes = "获取设备列表")
    @GetMapping(value = "/equipments")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public Result<List<OptEquipmentModel>> queryEquipmentList(EquipmentQueryParam param) {
        Result<List<OptEquipmentModel>> result = new Result<List<OptEquipmentModel>>();
        Page<OptEquipmentModel> page = new Page<OptEquipmentModel>(param.getPageNo(), param.getPageSize());
        List<OptEquipmentModel> list = inspPointService.getEquipmentList(param);
        result.setResult(list);
        result.success("查询成功");
        return result;
    }

    /**
     * 分页列表查询
     *
     * @param workListMatterParam
     * @param req
     * @return
     */
    @AutoLog(value = "工单任务-分页列表查询")
    @ApiOperation(value="工单任务-分页列表查询", notes="工单任务-分页列表查询")
    @GetMapping(value = "/list")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<?> queryPageList(WorkListMatterParam workListMatterParam,
                                  HttpServletRequest req) {
        WorkListMatter searchParam = new WorkListMatter(workListMatterParam);
        searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
        searchParam.setQueryTypeCode(WorkListConstant.WORK_LIST_MATTER_TYPE);
        searchParam.setQueryStatusCode(WorkListConstant.WORK_LIST_MATTER_STATUS);
        searchParam.setQueryMatterLevelCode(WorkListConstant.WORK_LIST_MATTER__MATTER_LEVEL_CODE);
        searchParam.setQueryMatterTypeCode(WorkListConstant.WORK_LIST_MATTER__MATTER_TYPE_CODE);
        searchParam.setType(WorkListConstant.WORK_LIST_TYPE_MATTER);//只看问题类型
        String username = JwtUtil.getUserNameByToken(req);
        searchParam.setSubId(username);
        Page<WorkListMatter> page = new Page<WorkListMatter>(workListMatterParam.getPageNo(), workListMatterParam.getPageSize());
        IPage<WorkListMatter> pageList = workListMatterService.selectPageDate(page, searchParam);
        return ResultData.ok(pageList);
    }

    /**
     * 添加
     * @param workListMatter
     * @return
     */
    @AutoLog(value = "工单任务-添加")
    @ApiOperation(value="工单任务-添加", notes="工单任务-添加")
    @PostMapping(value = "/add")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<?> add(@RequestBody WorkListMatter workListMatter, HttpServletRequest req) {
        ResultData<WorkListMatter> resultData = new ResultData<WorkListMatter>();
        try {
            String username = JwtUtil.getUserNameByToken(req);
            //将腾讯地图坐标转为百度地图坐标
            Map<String, Double> map = MapUtil.TencentMap2baiduMap(workListMatter.getMatterLongitudeTencent(), workListMatter.getMatterLatitudeTencent());
            workListMatter.setMatterLongitude(String.valueOf(map.get("x")));
            workListMatter.setMatterLatitude(String.valueOf(map.get("y")));
            boolean ok = workListMatterService.addMatterAndFiles(workListMatter, username);
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
     * @param workListMatter
     * @return
     */
    @AutoLog(value = "工单任务-编辑")
    @ApiOperation(value="工单任务-编辑", notes="工单任务-编辑")
    @PostMapping(value = "/edit")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<?> edit(@RequestBody WorkListMatter workListMatter, HttpServletRequest req) {
        ResultData<WorkListMatter> resultData = new ResultData<WorkListMatter>();
        try {
            if (workListMatter == null || workListMatter.getId() == null) {
                resultData.error500("id不能为空");
                return resultData;
            }
            String username = JwtUtil.getUserNameByToken(req);
            boolean ok = workListMatterService.editMatterAndFiles(workListMatter, username);
            if (ok) {
                resultData.success("编辑成功！");
            } else {
                resultData.error500("操作失败");
            }
        } catch (Exception e) {
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
    @AutoLog(value = "工单任务-通过id删除")
    @ApiOperation(value="工单任务-通过id删除", notes="工单任务-通过id删除")
    @DeleteMapping(value = "/delete")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<?> deleteById(@RequestParam("id") String id, HttpServletRequest req) {
        ResultData<WorkListMatter> resultData = new ResultData<WorkListMatter>();
        if (id == null) {
            return ResultData.error("id不能为空");
        }
        String username = JwtUtil.getUserNameByToken(req);
        boolean ok = workListMatterService.delMatterAndFiles(id, username);//逻辑删除
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
    @AutoLog(value = "工单任务-批量删除")
    @ApiOperation(value="工单任务-批量删除", notes="工单任务-批量删除")
    @DeleteMapping(value = "/deletes")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<?> deleteBatch(@RequestParam(name="ids",required=true) String ids, HttpServletRequest req) {
        ResultData<WorkListMatter> resultData = new ResultData<WorkListMatter>();
        if(ConvertUtils.isEmpty(ids)) {
            resultData.error500("参数不识别！");
        }else {
            String username = JwtUtil.getUserNameByToken(req);
            boolean ok = workListMatterService.delBatchMatterAndFiles(ids, username);//批量逻辑删除
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
    @AutoLog(value = "工单任务-通过id查询")
    @ApiOperation(value="工单任务-通过id查询", notes="工单任务-通过id查询")
    @GetMapping(value = "/query")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<?> queryById(@RequestParam("id") String id) {
        ResultData<WorkListMatter> resultData = new ResultData<WorkListMatter>();
        try {
            if (ConvertUtils.isEmpty(id)) {
                resultData.error500("id不能为空");
                return resultData;
            }
            WorkListMatter workListMatter = new WorkListMatter();
            workListMatter.setId(id);
            workListMatter.setQueryTypeCode(WorkListConstant.WORK_LIST_MATTER_TYPE);
            workListMatter.setQueryStatusCode(WorkListConstant.WORK_LIST_MATTER_STATUS);
            workListMatter.setQueryMatterLevelCode(WorkListConstant.WORK_LIST_MATTER__MATTER_LEVEL_CODE);
            workListMatter.setQueryMatterTypeCode(WorkListConstant.WORK_LIST_MATTER__MATTER_TYPE_CODE);
            workListMatter.setDelFlag(DelFlagConstant.NORMAL);
            workListMatter = workListMatterService.getOneById(workListMatter);
            resultData.setData(workListMatter);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }
}