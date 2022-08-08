package com.zcdy.dsc.modules.worklist.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.common.framework.influxdb.entity.WorkListLocationInflux;
import com.zcdy.dsc.common.framework.redis.RedisService;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.common.system.vo.DictModel;
import com.zcdy.dsc.common.util.ConvertUtils;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.system.service.ISysDictService;
import com.zcdy.dsc.modules.system.vo.DictDropdown;
import com.zcdy.dsc.modules.worklist.constant.WorkListConstant;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import com.zcdy.dsc.modules.worklist.entity.WorkListLocation;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatter;
import com.zcdy.dsc.modules.worklist.param.MyWorkListParam;
import com.zcdy.dsc.modules.worklist.service.MyWorkListService;
import com.zcdy.dsc.modules.worklist.service.WorkListService;
import com.zcdy.dsc.modules.worklist.utils.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 我的工单App
 * @Author: 在信汇通
 * @Date:   2021-03-02
 * @Version: V1.0
 */
@Slf4j
@Api(tags="我的工单")
@RestController
@RequestMapping("/worklist/myWorkList/app")
public class MyWorkListAppController extends BaseController<WorkList, MyWorkListService> {

    @Autowired
    private MyWorkListService myWorkListService;

    @Autowired
    private WorkListService workListService;

    @Autowired
    private ISysDictService sysDictService;

    @Autowired
    private RedisService redisService;

    @Resource
    private InfluxService influxService;

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
    @ApiOperation(value="获取日期查询条件")
    @GetMapping(value="/dateSearch")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public Result<List<DictDropdown>> getTypeList() {
        String code = WorkListConstant.WORK_LIST_APP_DATE_SEARCH_LIST;
        Result<List<DictDropdown>> result=new Result<>();
        result.setResult(getDictIteamsByCode(code));
        result.success("查询成功");
        return result;
    }

    @AutoLog(value = "我的工单-我的工单数量")
    @ApiOperation(value="我的工单-我的工单数量", notes="我的工单-我的工单数量")
    @GetMapping(value = "/mylistnum")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<?> getMylistNum(HttpServletRequest req) {
        MyWorkListParam myWorkListParam = new MyWorkListParam();
        myWorkListParam.setSearchStatus(WorkListConstant.MY_WORK_LIST_STATUS_DOING);
        ResultData<?> resultData = queryPageListInsp(myWorkListParam, req);
        IPage<WorkList> data = (IPage<WorkList>) resultData.getData();
        return ResultData.ok(data.getTotal());
    }

    @AutoLog(value = "我的工单-分页列表查询")
    @ApiOperation(value="我的工单-分页列表查询", notes="我的工单-分页列表查询")
    @GetMapping(value = "/list")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<?> queryPageListInsp(MyWorkListParam workListParam,
                                           HttpServletRequest req) {
        WorkList searchParam = new WorkList(workListParam);
        searchParam.setDelFlag(DelFlagConstant.NORMAL);//删除标记=0
        String username = JwtUtil.getUserNameByToken(req);
        searchParam.setTeamMemberUsername(username);
        searchParam.setQueryStatusCode(WorkListConstant.WORK_LIST_STATUS_CODE);
        searchParam.setQueryTypeCode(WorkListConstant.WORK_LIST_TYPE_CODE);
        if(workListParam.getSearchDate() != null)//处理查询日期
        {
            String searchDate = workListParam.getSearchDate();
            Date today = new Date();
            if(WorkListConstant.WORK_LIST_APP_DATE_SEARCH_LIST_WEEK.equals(searchDate))
            {
                //本周
                Date monday = DateUtil.getDayOfWeek(today, 1);
                Date sunday = DateUtil.addDay(monday, 6);
                searchParam.setStartDate(monday);//周一
                searchParam.setOverDate(sunday);//周日
            }
            else if(WorkListConstant.WORK_LIST_APP_DATE_SEARCH_LIST_MONTH.equals(searchDate))
            {
                //本月
                Date firstDay = DateUtil.getDayOfMonth(today, 1);//月初第一天
                Date lastDay = DateUtil.getLastDayOfMonth(today);//月底最后一天
                searchParam.setStartDate(firstDay);
                searchParam.setOverDate(lastDay);
            }
            else if(WorkListConstant.WORK_LIST_APP_DATE_SEARCH_LIST_YEAR.equals(searchDate))
            {
                //今年
                Date firstDay = DateUtil.getFirstDayOfYear(today);//月初第一天
                Date lastDay = DateUtil.getLastDayOfYear(today);//月底最后一天
                searchParam.setStartDate(firstDay);
                searchParam.setOverDate(lastDay);
            }
            else if(WorkListConstant.WORK_LIST_APP_DATE_SEARCH_LIST_ALL.equals(searchDate))
            {
                //全部
                searchParam.setStartDate(null);
                searchParam.setOverDate(null);
            }
            else
            {
                return ResultData.fail("日期参数错误");
            }
        }
        Page<WorkList> page = new Page<WorkList>(workListParam.getPageNo(), workListParam.getPageSize());
        IPage<WorkList> pageList = myWorkListService.selectPageDate(page, searchParam);
        return ResultData.ok(pageList);
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @AutoLog(value = "工单-通过id查询")
    @ApiOperation(value="工单-通过id查询", notes="工单-通过id查询")
    @GetMapping(value = "/query")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<?> queryById(@RequestParam("id") String id) {
        ResultData<WorkList> resultData = new ResultData<WorkList>();
        try {
            if (ConvertUtils.isEmpty(id)) {
                resultData.error500("id不能为空");
                return resultData;
            }
            WorkList workList = new WorkList();
            workList.setId(id);
            workList.setDelFlag(DelFlagConstant.NORMAL);
            workList.setQueryStatusCode(WorkListConstant.WORK_LIST_STATUS_CODE);
            workList.setQueryTypeCode(WorkListConstant.WORK_LIST_TYPE_CODE);
            workList.setQueryMatterTypeCode(WorkListConstant.WORK_LIST_MATTER_TYPE);
            workList.setQueryMatterStatusCode(WorkListConstant.WORK_LIST_MATTER_STATUS);
            workList.setQueryMatterLevelCode(WorkListConstant.WORK_LIST_MATTER__MATTER_LEVEL_CODE);
            workList.setQueryMatterMatterTypeCode(WorkListConstant.WORK_LIST_MATTER__MATTER_TYPE_CODE);
            workList = workListService.getOneById(workList);
            resultData.setData(workList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultData.error500("查询失败");
        }
        return resultData;
    }

    @AutoLog(value = "工单任务-标记完成")
    @ApiOperation(value="工单任务-标记完成", notes="工单任务-标记完成")
    @PostMapping(value = "/completeOne")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<?> completeOne(@RequestBody WorkListMatter matter, HttpServletRequest req) {
        String username = JwtUtil.getUserNameByToken(req);
        //将腾讯地图坐标转为百度地图坐标
        Map<String, Double> map = MapUtil.TencentMap2baiduMap(matter.getSolveLongitudeTencent(), matter.getSolveLatitudeTencent());
        matter.setSolveLongitude(String.valueOf(map.get("x")));
        matter.setSolveLatitude(String.valueOf(map.get("y")));
        return myWorkListService.completeOne(matter, username);
    }

    @AutoLog(value = "app提交自己的位置坐标")
    @ApiOperation(value="app提交自己的位置坐标", notes="app提交自己的位置坐标")
    @PostMapping(value = "/subLocation")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<?> subLocation(@RequestBody WorkListLocation workListLocation, HttpServletRequest req)
    {
        if(workListLocation == null)
        {
            return ResultData.fail("位置信息不能为空");
        }
        if(workListLocation.getLatitude() == null || workListLocation.getLongitude() == null)
        {
            return ResultData.fail("位置信息不完整");
        }
        String username = JwtUtil.getUserNameByToken(req);
        String locationKey = RedisKeyConstantV2.WORKLIST_APP_LOCATION;
        locationKey = String.format(locationKey, username);
        workListLocation.setDateTime(new Date());
        //将腾讯地图坐标转为百度地图坐标
        Map<String, Double> map = MapUtil.TencentMap2baiduMap(workListLocation.getLongitude(), workListLocation.getLatitude());
        workListLocation.setLongitude(String.valueOf(map.get("x")));
        workListLocation.setLatitude(String.valueOf(map.get("y")));
        //存入redis，redis只保留最新的
        redisService.set(locationKey, JSON.toJSONString(workListLocation));

        //存入influxdb
        WorkListLocationInflux workListLocationInflux = WorkListLocationInflux.LOCATION_DATA.clone();
        workListLocationInflux.setUserName(username);
        workListLocationInflux.setTime(System.currentTimeMillis());
        workListLocationInflux.setLon(workListLocation.getLongitude());
        workListLocationInflux.setLat(workListLocation.getLatitude());
        List<WorkListLocationInflux> list = new ArrayList<>();
        list.add(workListLocationInflux);
        influxService.writeBatchWithTime(list, WorkListLocationInflux.class);
        return ResultData.ok();
    }
}
