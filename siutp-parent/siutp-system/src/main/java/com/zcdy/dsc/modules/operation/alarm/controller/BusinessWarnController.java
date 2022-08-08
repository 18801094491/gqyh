package com.zcdy.dsc.modules.operation.alarm.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.aspect.log.LogEnum.Operation;
import com.zcdy.dsc.common.aspect.log.LogEnum.Type;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.common.framework.redis.RedisOperation;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.common.system.base.controller.BaseController;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.constant.ModuleConstant;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.operation.alarm.constant.WarnLevelConstant;
import com.zcdy.dsc.modules.operation.alarm.constant.WarnStatusConstant;
import com.zcdy.dsc.modules.operation.alarm.entity.BusinessWarn;
import com.zcdy.dsc.modules.operation.alarm.entity.WeekData;
import com.zcdy.dsc.modules.operation.alarm.param.BusinessWarnParam;
import com.zcdy.dsc.modules.operation.alarm.service.BusinessService;
import com.zcdy.dsc.modules.operation.alarm.service.BusinessWarnService;
import com.zcdy.dsc.modules.operation.alarm.vo.*;
import com.zcdy.dsc.modules.operation.param.AlarmParam;
import com.zcdy.dsc.modules.operation.work.vo.TeamDropdown;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: 告警信息表 @author： 智能无人硬核心项目组 创建时间： 2020-02-18 10:19:55 版本号: V1.0
 */
@Api(tags = "告警/报警信息处理接口集")
@RestController
@RequestMapping("/business/warn")
public class BusinessWarnController extends BaseController<BusinessWarn, BusinessWarnService> {

    @Resource
    private BusinessService businessService;
    
    @Resource
    private RedisOperation<String> stringRedisOperation;

    /**
     * 分页列表查询
     * 
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "告警信息表-分页列表查询")
    @ApiOperation(value = "告警信息表-分页列表查询", notes = "告警信息表-分页列表查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNo", value = "页码", paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示条数", paramType = "query"),
        @ApiImplicitParam(name = "warnName", value = "告警名称", paramType = "query"),
        @ApiImplicitParam(name = "warnLevel", value = "告警等级", paramType = "query"),
        @ApiImplicitParam(name = "warnType", value = "告警类型", paramType = "query"),
        @ApiImplicitParam(name = "equipmentType", value = "设备类型", paramType = "query"),
        @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query"),
        @ApiImplicitParam(name = "warnStatus", value = "告警状态", paramType = "query")})
    @GetMapping
    public Result<IPage<BusinessWarnVo>> queryPageList(String warnName, String warnLevel, String warnType,
        String warnStatus, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String equipmentType, String startTime,
        String endTime) {
        Result<IPage<BusinessWarnVo>> result = new Result<>();
        Page<BusinessWarnVo> page = new Page<>(pageNo, pageSize);
        IPage<BusinessWarnVo> data = businessService.queryWarnData(page, warnName, warnLevel, warnType, warnStatus,
            equipmentType, startTime, endTime);
        List<BusinessWarnVo> records = data.getRecords();
        if (records != null && records.size() > 0) {
            records.forEach(item -> {
                if (!StringUtils.isEmpty(item.getDuration())) {
                    item.setDuration(DateUtil.convertHourMinuSec(Long.parseLong(item.getDuration())));
                }
            });
        }
        result.setResult(data);
        return result.success();
    }

    /**
     * 描述: 处理报警信息 @author： songguang.jiao 创建时间： 2020年2月18日 下午2:26:41 版本号: V1.0
     */
    @ApiOperation(value = "处理报警信息", notes = "处理报警信息")
    @PostMapping("/deal")
    @RequiresPermissions("business:operate")
    public Result<Object> dealData(@RequestBody @Valid BusinessDeal business) {
        businessService.dealData(business);
        return Result.ok("处理完毕");
    }

    /**
     * 描述: 关闭告警信息 @author： songguang.jiao 创建时间： 2020年2月24日 上午11:10:53 版本号: V1.0
     */
    @ApiOperation(value = "关闭报警信息", notes = "关闭报警信息")
    @PostMapping("/close")
    @RequiresPermissions("business:operate")
    public Result<Object> close(@RequestBody @Valid BusinessDeal business) {
        businessService.closeData(business);
        return Result.ok("关闭成功");
    }

    /**
     * 确认报警信息 -->告警派工
     * @param id
     * @return
     */
    @ApiOperation(value = "告警派工", notes = "告警派工")
    @GetMapping("/confirm")
    @RequiresPermissions("business:operate")
    public Result<Object> confirm(String id) {
        if (StringUtils.isEmpty(id)) {
            return Result.error("id不能为空");
        }
        businessService.confirmData(id, WarnStatusConstant.UNDEAL);
        return Result.ok("确认成功");
    }

    /**
     * 描述: 首页告警信息列表
     * @author： songguang.jiao
     * 创建时间： 2020年2月28日 下午2:10:55
     * 版本号: V1.0
     */
    @ApiOperation(value = "地图页面展示未处理和初始化报警数据", notes = "地图页面展示未处理报警数据")
    @GetMapping("/undealData")
    public Result<List<BusinessWarnVo>> queryUndealData() {
        Result<List<BusinessWarnVo>> result = new Result<>();
        List<BusinessWarnVo> undealDataList = businessService.queryUndealDataNew(WarnStatusConstant.UNDEAL, WarnStatusConstant.INIT);
        result.setResult(undealDataList);
        result.success("查询成功");
        return result;
    }

    /**
     * 获取告警数量
     * @author： songguang.jiao
     * 创建时间： 2020年2月28日 下午2:10:55
     * 版本号: V1.0
     */
    @ApiOperation(value = "获取告警数量", notes = "获取告警数量")
    @GetMapping("/warnNum")
    public Result<List<BusinessWarnVo>> queryWarnNum() {
        Result<List<BusinessWarnVo>> result = new Result<>();
        List<BusinessWarnVo> undealDataList = businessService.queryWarnNum();
        result.setResult(undealDataList);
        result.success("查询成功");
        return result;
    }

    /**
     * 分页查询告警事件列表
     * @param param
     * @return
     */
    @AutoLog(value = "分页查询告警事件列表")
    @ApiOperation(value = "分页查询告警事件列表", notes = "分页查询告警事件列表")
    @GetMapping("/app/businessWarns")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<IPage<BusinessWarnVo>> businessWarns(BusinessWarnParam param) {
        ResultData<IPage<BusinessWarnVo>> resultData = null;
        try {
            resultData = new ResultData<>();
            Page<BusinessWarnVo> page = new Page<>(param.getPageNo(), param.getPageSize());
//            param.setWarnStatus(WarnStatusConstant.UNDEAL);
            IPage<BusinessWarnVo> pageData = businessService.getAppBusinessList(page, param);
            resultData.setData(pageData);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage(), e);
        }
        return resultData;
    }

    /**
     * 查询全部告警事件列表
     * @return
     */
    @AutoLog(value = "查询全部告警事件列表")
    @ApiOperation(value = "查询全部告警事件列表", notes = "查询全部告警事件列表")
    @GetMapping("/app/undealDatas")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<List<BusinessWarnVo>> undealDatas() {
        ResultData<List<BusinessWarnVo>> resultData = null;
        try {
            resultData = new ResultData<>();
            BusinessWarnParam param = new BusinessWarnParam();
            param.setWarnLevel(WarnLevelConstant.ORDINARY);
            param.setWarnStatus(WarnStatusConstant.UNDEAL);
            List<BusinessWarnVo> businessList = businessService.getAppBusinessList(param);
            resultData.setData(businessList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage(), e);
        }
        return resultData;
    }

    /**
     * 查询全部告警事件列表
     * @return
     */
    @AutoLog(value = "查询告警事件列表详情")
    @ApiOperation(value = "查询告警事件列表详情", notes = "查询告警事件列表详情")
    @GetMapping("/app/undealDatas/{id}")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    public ResultData<BusinessWarnVo> businessDetail(@PathVariable("id") String id) {
        ResultData<BusinessWarnVo> resultData = null;
        try {
            resultData = new ResultData<>();
            if (StringUtils.isEmpty(id)) {
                return resultData.error500("告警事件id为空");
            }
            BusinessWarnVo businessDetail = businessService.getAppBusinessDetail(id);
            resultData.setData(businessDetail);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage(), e);
        }
        return resultData;
    }

    /**
     * 描述: 近7天的报警数据情况
     * @author： songguang.jiao
     * 创建时间： 2020年2月28日
     * 下午2:28:25
     * 版本号: V1.0
     */
    @GetMapping("/weekData")
    @ApiOperation(value = "近7天的报警数据情况", notes = "近7天的报警数据情况")
    public Result<WeekBusinissData> weekData() {
        Result<WeekBusinissData> result = new Result<WeekBusinissData>();
        WeekBusinissData weekBusinissData = new WeekBusinissData();

        List<String> sevenDays = new ArrayList<>();
        List<WeekData> weekDatas = businessService.getSevenDayData();
        weekBusinissData.setTime(sevenDays);
        // 拼接数据为柱状图数据
        List<Map<String, Object>> dayDatas = new ArrayList<Map<String, Object>>();
        Map<String, Object> sumMap = new HashMap<>(weekDatas.size());
        sumMap.put("name", "总报警");
        Map<String, Object> dealMap = new HashMap<>(weekDatas.size());
        dealMap.put("name", "已处理");
        Map<String, Object> unDealMap = new HashMap<>(weekDatas.size());
        unDealMap.put("name", "未处理");
        if (weekDatas.size() > 0 && !weekDatas.isEmpty()) {
            for (WeekData weekData : weekDatas) {
                String replace = StringUtils.replace(weekData.getDate(), "-", " ");
                sevenDays.add(replace);
                sumMap.put(replace, weekData.getSumNum());
                dealMap.put(replace, weekData.getDealNum());
                unDealMap.put(replace, weekData.getUnDealNum());
            }
        }
        dayDatas.add(sumMap);
        dayDatas.add(dealMap);
        dayDatas.add(unDealMap);
        // 封装数据格式到WeekBusinissData中
        weekBusinissData.setDayDatas(dayDatas);
        result.setResult(weekBusinissData);
        result.success("查询成功");
        return result;
    }

    /**
     * 描述: 通过月份或者年份查询报警总数量 @author： songguang.jiao 创建时间： 2020年2月28日 下午5:53:56 版本号: V1.0
     */
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "1-月份,2-年份(默认月)", paramType = "query")})
    @GetMapping("/monthYear")
    @ApiOperation(value = "通过月份或者年份查询报警总数量", notes = "通过月份或者年份查询报警总数量")
    public Result<List<MonthYearData>> qeuryByMonthYear(@RequestParam(name = "type", defaultValue = "1") String type) {
        Result<List<MonthYearData>> result = new Result<List<MonthYearData>>();
        List<MonthYearData> datas = businessService.queryMonthYearData(type);
        result.success("查询成功");
        result.setResult(datas);
        return result;
    }

    /**
     * 描述: 通知策略新增或者修改 @author： songguang.jiao 创建时间： 2020年3月4日 上午10:23:21 版本号: V1.0
     */
    @ApiOperation(value = "通知策略新增或者修改", notes = "通知策略新增或者修改")
    @PostMapping("/one")
    @RequiresPermissions("business:config")
    public Result<Object> addPolicy(@RequestBody @Valid PolicyParamVo vo) {
        boolean exist = businessService.checkPolicyExist(vo);
        if (exist) {
            return Result.error("策略等级不能重复!");
        }
        if (StringUtils.isEmpty(vo.getId())) {
            businessService.addPolicy(vo);
        } else {
            businessService.editPolicy(vo);
        }
        return Result.ok();
    }

    /**
     * 描述: 角色下拉列表 @author： songguang.jiao 创建时间： 2020年3月4日 下午3:14:07 版本号: V1.0
     */
    @ApiOperation(value = "角色下拉列表", notes = "角色下拉列表")
    @GetMapping("/roleUsers")
    public Result<List<RoleNameVo>> getRoleUsers(String roleName) {
        Result<List<RoleNameVo>> result = new Result<>();
        List<RoleNameVo> list = businessService.getRoleUsers(roleName);
        result.setResult(list);
        result.success("successs");
        return result;
    }

    /**
     * 描述: 班组下列表 @author： songguang.jiao 创建时间： 2020年3月4日 下午3:14:07 版本号: V1.0
     */
    @ApiOperation(value = "班组下列表", notes = "班组下列表")
    @GetMapping("/workUsers")
    public Result<List<TeamDropdown>> getWorkUsers(String name) {
        Result<List<TeamDropdown>> result = new Result<>();
        List<TeamDropdown> list = businessService.getWorkUsers(name);
        result.setResult(list);
        result.success("successs");
        return result;
    }

    /**
     * 描述: 分页查询通知策略 @author： songguang.jiao 创建时间： 2020年3月5日 下午1:12:15 版本号: V1.0
     */
    @AutoLog(value = "分页查询通知策略")
    @ApiOperation(value = "分页查询通知策略", notes = "分页查询通知策略")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNo", value = "页码", paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页显示条数", paramType = "query"),
        @ApiImplicitParam(name = "name", value = "策略名称", paramType = "query")})
    @GetMapping("/pilicyList")
    public Result<IPage<PolicyListVo>> queryPolicyPageData(
        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, String name) {
        Result<IPage<PolicyListVo>> result = new Result<>();
        Page<PolicyListVo> page = new Page<>(pageNo, pageSize);
        IPage<PolicyListVo> data = businessService.queryPolicyPageData(page, name);
        result.setResult(data);
        result.success("success");
        return result;
    }

    /**
     * 描述: 通知策略详情 @author： songguang.jiao 创建时间： 2020年3月4日 下午3:14:07 版本号: V1.0
     */
    @ApiOperation(value = "通知策略详情", notes = "通知策略详情")
    @GetMapping("/getDetail")
    public Result<PolicyDetailVo> getDetail(String id) {
        Result<PolicyDetailVo> result = new Result<PolicyDetailVo>();
        PolicyDetailVo policeVo = businessService.getDetail(id);
        result.setResult(policeVo);
        result.success("success");
        return result;
    }

    /**
     * 描述: 删除 @author： songguang.jiao 创建时间： 2020年3月4日 下午3:14:07 版本号: V1.0
     */
    @ApiOperation(value = "删除通知策略", notes = "删除通知策略")
    @GetMapping("/delete")
    public Result<Object> deleteById(String id) {
        businessService.delete(id);
        return Result.ok("删除成功");
    }

    /**
     * 描述: 更改启停用状态 @author： songguang.jiao 创建时间： 2020年3月4日 下午3:14:07 版本号: V1.0
     */
    @ApiOperation(value = "更改启停用状态", notes = "更改启停用状态")
    @GetMapping("/changeStatus")
    public Result<Object> changeStatus(String id) {
        businessService.changeStatus(id);
        return Result.ok("更改成功");
    }

    /**
     * 描述: 消息模板下拉选 @author： songguang.jiao 创建时间： 2020年3月6日 下午3:41:45 版本号: V1.0
     */
    @ApiOperation(value = "消息模板下拉选", notes = "消息模板下拉选")
    @GetMapping("/smstemplate")
    public Result<List<SmsTemplateVo>> queryTemplate(String name) {
        Result<List<SmsTemplateVo>> result = new Result<List<SmsTemplateVo>>();
        List<SmsTemplateVo> list = businessService.queryTemplate(name);
        result.setResult(list);
        result.success("success");
        return result;
    }

    /**
     * 描述: 查询当天报警数据(饼状图,未处理,已处理,已关闭) @author： songguang.jiao 创建时间： 2020年3月6日 下午3:41:45 版本号: V1.0
     */
    @ApiOperation(value = "查询当天报警数据(饼状图,未处理,已处理,已关闭)", notes = "查询当天报警数据(饼状图,未处理,已处理,已关闭)")
    @GetMapping("/oneData")
    public Result<List<PolicyOneDayVo>> oneDayData() {
        Result<List<PolicyOneDayVo>> result = new Result<List<PolicyOneDayVo>>();
        List<PolicyOneDayVo> list = businessService.oneDayData();
        result.setResult(list);
        result.success("success");
        return result;
    }

    /**
     * 描述: 地图界面查询所有图标
     * 
     * @return @author： songguang.jiao 创建时间： 2020年3月9日 上午10:03:30 版本号: V1.0
     */
    @ApiOperation(value = " 地图界面查询所有图标", notes = " 地图界面查询所有图标")
    @GetMapping("/queryAllIcon")
    public Result<List<IconListVo>> queryAllIcon() {
        Result<List<IconListVo>> result = new Result<List<IconListVo>>();
        List<IconListVo> data = businessService.queryAllIcon();
        List<IconListVo> returnData = new ArrayList<>();
        // 处理数据格式
        for (IconListVo iconListVo : data) {
            if (iconListVo != null) {
                iconListVo.setName(StringUtils.replace(iconListVo.getName(), "打开", "-开"));
                iconListVo.setName(StringUtils.replace(iconListVo.getName(), "关闭", "-关"));
                iconListVo.setName(StringUtils.replace(iconListVo.getName(), "正常", ""));
                iconListVo.setUrl(baseStoragePath + iconListVo.getUrl());
                returnData.add(iconListVo);
            }
        }
        result.setResult(returnData);
        return result.success("success");
    }

    /**
     * 获取报警信息分页数据接口
     * 
     * @param param
     * @return 结果集
     */
    @ApiOperation(notes = "获取报警信息分页数据接口", value = "获取报警信息分页数据接口")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @GetMapping("/app/items")
    public Result<Page<AlarmResultVO>> getAlarmFullData(AlarmParam param) {
        Page<AlarmResultVO> pageinfo = new Page<>();
        int currentPage = 0;
        int pageSize = 0;
        if (null != param.getPageSize()) {
            pageSize = param.getPageSize().intValue();
        } else {
            pageSize = this.DEFAULT_PAGESIZE;
        }
        if (null != param.getPageNo()) {
            currentPage = param.getPageNo().intValue();
        } else {
            currentPage = 1;
        }
        pageinfo.setCurrent(currentPage);
        pageinfo.setSize(pageSize);
//        if (!StringUtils.isEmpty(param.getEquipType())) {
        pageinfo = this.businessService.queryAlarmFullDataPage(pageinfo, param.getParam(),param.getEquipType());
//        } else {
//            // 处理没有参数的情况
//            int total = this.businessService.getCount();
//            long startIndex = 0;
//            startIndex = (currentPage-1)*pageSize;
//            pageinfo.setTotal(total);
//            List<AlarmResultVO> datas = this.businessService.queryAlarmFullDataList(startIndex, pageSize);
//            pageinfo.setRecords(datas);
//        }
        return Result.success(pageinfo, "Operation successfully");
    }
    
    /**
     * 获取告警信息详细数据接口
     * @param bid
     * @return 详细数据
     */
    @ApiOperation(notes = "获取报警信息详细数据接口", value = "获取报警信息详细数据接口")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @GetMapping("/app/item/{bid}")
    public Result<AlarmResultDetailVO> getAlarmFullDataDetail(@PathVariable String bid) {
        AlarmResultDetailVO detail = this.businessService.getAlarmDataDetail(bid);
        if(!StringUtils.isEmpty(detail.getOptPicture())){
            detail.setOptPicture(this.baseStoragePath.concat(detail.getOptPicture()));
        }
        return Result.success(detail, "Operation successfully");
    }

    /**
     * 获取告警信息详细数据接口
     * @param iotId
     * @return
     */
    @AutoLog(group=ModuleConstant.OPERATION, type = Type.OPERATION, operation=Operation.QUERY, value="app获取告警详情数据接口")
    @ApiOperation(notes = "获取设备的可展示运行数据", value = "获取设备的可展示运行数据")
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @GetMapping("/app/item/iot/{iotId}")
    public Result<List<WorkingDataVO>> getIotRunData(@PathVariable String iotId) {
        List<VariableInfo> vars = this.businessService.getVarsByIotId(iotId);
        int size = vars.size();
        if(size<=0){
            return Result.success(null, "success");
        }
        List<WorkingDataVO> datas = new ArrayList<>(size);
        VarType varType = null;
        String maxValue = "";
        String minValue = "";
        String lValue = "";
        String rValue = "";
        String messageStr = null;
        ValueEntity value = null;
        BigDecimal bg = null;
        int scale = 0;
        for(VariableInfo item : vars){
            try {
                varType = VarType.valueOf(item.getVariableType());
                switch (varType) {
                    case A16A01:
                        minValue = "0";
                        maxValue = "30";
                        lValue = "10";
                        rValue = "25";
                        break;
                    case A16A02:
                        minValue = "0";
                        maxValue = "1";
                        lValue = "0.24";
                        rValue = "0.38";
                        break;
                    case A16A03:
                        minValue = "0";
                        maxValue = "10";
                        lValue = "2";
                        rValue = "5";
                        break;
                    case A16A04:
                        minValue = "0";
                        maxValue = "14";
                        lValue = "6";
                        rValue = "9";
                        break;
                    case A16A05:
                        minValue = "0";
                        maxValue = "1";
                        lValue = "0.2";
                        rValue = "0.5";
                        break;
                    case A16A20:
                        if("e95beb5a431f313c54ef78406d2be6d9".equals(iotId)){
                            minValue = "-200";
                            maxValue = "2000";
                            lValue = "-50";
                            rValue = "1500";
                        }else{
                            minValue = "-200";
                            maxValue = "200";
                            lValue = "-50";
                            rValue = "100";
                        }
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                //无图表的变量类型
                continue;
            }
            WorkingDataVO dataVO = new WorkingDataVO();
            messageStr = stringRedisOperation.get(String.format(RedisKeyConstantV2.REDISDATAKEY, item.getVarName()));
            if (!StringUtils.isEmpty(messageStr)){
                value = JSON.parseObject(messageStr, ValueEntity.class);
                if(!StringUtils.isEmpty(item.getUnited())){
                    dataVO.setTitle(item.getVarTitle()+"["+item.getUnited()+"]");
                }else{
                    dataVO.setTitle(item.getVarTitle());
                }
                if(null==value.getValue()){
                    dataVO.setRealValue("--");
                }else{
                    bg = new BigDecimal(value.getValue());
                    //使用小数位控制
                    scale = null==item.getScale()?2:item.getScale().intValue();
                    if(scale>=0){
                        bg = bg.setScale(scale, RoundingMode.HALF_UP);
                    }
                    dataVO.setRealValue(bg.toEngineeringString());
                }
            }else{
                dataVO.setRealValue("--");
            }
            dataVO.setMinValue(minValue);
            dataVO.setMaxValue(maxValue);
            dataVO.setLeftValue(lValue);
            dataVO.setRightValue(rValue);
            datas.add(dataVO);
        }
        return Result.success(datas, "success");
    }
    
    private enum VarType {
        /**
         * 流量计流速
         */
        //A16A22,
        /**
         * 流量计瞬时流量
         */
        A16A20,
        /**
         * 瞬时流量
         */
//        A16A08,
        /**
         * 电池电压
         */
//        A16A07,
        /**
         * 余氯
         */
        A16A05,
        /**
         * PH
         */
        A16A04,
        /**
         * 浊度
         */
        A16A03,
        /**
         * 压力
         */
        A16A02,
        /**
         * 温度值
         */
        A16A01
    }
}