package com.zcdy.dsc.modules.datacenter.statistic.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.common.framework.swagger.ApiVersion;
import com.zcdy.dsc.common.framework.swagger.VersionConstant;
import com.zcdy.dsc.constant.UnitConstant;
import com.zcdy.dsc.constant.WorkingStatus;
import com.zcdy.dsc.modules.collection.gis.vo.MeterAndFlowCountVo;
import com.zcdy.dsc.modules.configcentre.constant.SystemParamConstant;
import com.zcdy.dsc.modules.configcentre.entity.SystemConfig;
import com.zcdy.dsc.modules.configcentre.service.SystemParamService;
import com.zcdy.dsc.modules.operation.alarm.service.BusinessService;
import com.zcdy.dsc.modules.operation.alarm.vo.PolicyOneDayVo;
import com.zcdy.dsc.modules.settle.constant.FlowModuleConstant;
import com.zcdy.dsc.modules.settle.logic.FlowCountCurrentTimeLogic;
import com.zcdy.dsc.modules.settle.logic.FlowCountYesterDayLogic;
import com.zcdy.dsc.modules.settle.service.IContractService;
import com.zcdy.dsc.modules.settle.vo.FlowDayRecordBarChart;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 领导看板
 *
 * @author songguang.jiao
 * @date 2020/05/29 10:39:53
 */
@Api(tags = "APP领导看板")
@RestController
@RequestMapping("/leader/board")
public class AppLeaderBoardController {

    @Resource
    private FlowCountCurrentTimeLogic flowCountCurrentTimeLogic;

    @Resource
    private BusinessService businessService;

    @Resource
    private IContractService contractService;

    @Resource
    private SystemParamService systemParamService;

    @Resource
    private FlowCountYesterDayLogic yesterDayLogic;

    /**
     * 今日流量统计
     *
     * @return
     * @author songguang.jiao
     * @date 2020/05/29 10:44:00
     */
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @ApiOperation(value = "今日流量统计")
    @GetMapping("/flowToday")
    public Result<FlowDayRecordBarChart> flowCountToday() {
        Result<FlowDayRecordBarChart> result = new Result<>();
        FlowDayRecordBarChart flowDayRecordBarChart = new FlowDayRecordBarChart();
        // x轴时间组合
        flowDayRecordBarChart.setMainTitle("用水量统计");
        flowDayRecordBarChart.setYTitle("用水量(m³)");
        List<String> timeList = Arrays.asList(LocalDate.now().toString());
        flowDayRecordBarChart.setXCategories(timeList);

        //y轴数据组合
        List<FlowDayRecordBarChart.FlowBarChartItem> returnData = new ArrayList<>();
        List<MeterAndFlowCountVo> todayData = flowCountCurrentTimeLogic.getTodayData();

        //手机端需要展示(政办公+华电+风子沟)合计
        BigDecimal usedCount = BigDecimal.ZERO;
        for (MeterAndFlowCountVo vo : todayData) {
            BigDecimal bigDecimal = new BigDecimal(StringUtils.removeEnd(vo.getFlowCount(), UnitConstant.FLOWUNIT));
            if (FlowModuleConstant.FLOW_XZQ.equals(vo.getFlowCode()) || FlowModuleConstant.FLOW_HDBR.equals(vo.getFlowCode())
                    || FlowModuleConstant.FLOW_FZG.equals(vo.getFlowCode())) {
                usedCount = usedCount.add(bigDecimal);
            }
            FlowDayRecordBarChart.FlowBarChartItem flowBarChartItem = new FlowDayRecordBarChart.FlowBarChartItem();
            flowBarChartItem.setName(vo.getFlowName());
            flowBarChartItem.setData(Arrays.asList(bigDecimal));
            returnData.add(flowBarChartItem);
        }
        FlowDayRecordBarChart.FlowBarChartItem flowUserdCount = new FlowDayRecordBarChart.FlowBarChartItem();
        flowUserdCount.setName(FlowModuleConstant.FLOW_USERD_NAME);
        flowUserdCount.setData(Arrays.asList(usedCount));
        returnData.add(flowUserdCount);

        flowDayRecordBarChart.setSeries(returnData);
        result.setResult(flowDayRecordBarChart);
        return result.success();
    }

    /**
     * 昨日流量计统计
     *
     * @return
     */
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @AutoLog("昨日流量计统计")
    @ApiOperation("昨日流量计统计")
    @GetMapping("/flowYesterday")
    public Result<FlowDayRecordBarChart> flowCountYesterday() {
        Result<FlowDayRecordBarChart> result = new Result<>();
        FlowDayRecordBarChart flowDayRecordBarChart = new FlowDayRecordBarChart();
        // x轴时间组合
        flowDayRecordBarChart.setMainTitle("用水量统计");
        flowDayRecordBarChart.setYTitle("用水量(m³)");
        List<String> timeList = Arrays.asList(LocalDate.now().minusDays(1).toString());
        flowDayRecordBarChart.setXCategories(timeList);

        //y轴数据组合  用水合计初始化
        List<FlowDayRecordBarChart.FlowBarChartItem> returnData = new ArrayList<>();
        BigDecimal usedCount = BigDecimal.ZERO;

        // 河东(供)
        FlowDayRecordBarChart.FlowBarChartItem flowHeDong = new FlowDayRecordBarChart.FlowBarChartItem();
        flowHeDong.setName(FlowModuleConstant.FLOW_HD_NAME);
        flowHeDong.setData(Arrays.asList(formatValue(yesterDayLogic.getFlowHd())));
        returnData.add(flowHeDong);

        // 行政区 7标+2标流量
        FlowDayRecordBarChart.FlowBarChartItem flowXzq = new FlowDayRecordBarChart.FlowBarChartItem();
        flowXzq.setName(FlowModuleConstant.FLOW_XZQ_NAME);
        BigDecimal bigDecimalXzq = formatValue(yesterDayLogic.getFlowXzq());
        usedCount = usedCount.add(bigDecimalXzq);
        flowXzq.setData(Arrays.asList(bigDecimalXzq));
        returnData.add(flowXzq);

        // 行政区入楼
        FlowDayRecordBarChart.FlowBarChartItem flowXzrl = new FlowDayRecordBarChart.FlowBarChartItem();
        flowXzrl.setName(FlowModuleConstant.FLOW_XZRL_NAME);
        flowXzrl.setData(Arrays.asList(formatValue(yesterDayLogic.getFlowXzrl())));
        returnData.add(flowXzrl);

        // 行政区绿化 =行政区-行政区入楼
        FlowDayRecordBarChart.FlowBarChartItem flowXzlh = new FlowDayRecordBarChart.FlowBarChartItem();
        flowXzlh.setName(FlowModuleConstant.FLOW_XZLH_NAME);
        flowXzlh.setData(Arrays.asList(formatValue(yesterDayLogic.getFlowXzlh())));
        returnData.add(flowXzlh);

        // 镜河补水
        FlowDayRecordBarChart.FlowBarChartItem flowFzg = new FlowDayRecordBarChart.FlowBarChartItem();
        flowFzg.setName(FlowModuleConstant.FLOW_FZG_NAME);
        BigDecimal bigDecimalFzg = formatValue(yesterDayLogic.getFlowFzg());
        usedCount = usedCount.add(bigDecimalFzg);
        flowFzg.setData(Arrays.asList(bigDecimalFzg));
        returnData.add(flowFzg);

        // 华电北燃
        FlowDayRecordBarChart.FlowBarChartItem flowHdbr = new FlowDayRecordBarChart.FlowBarChartItem();
        flowHdbr.setName(FlowModuleConstant.FLOW_HDBR_NAME);
        BigDecimal bigDecimalHdbr = formatValue(yesterDayLogic.getFlowHdbr());
        usedCount = usedCount.add(bigDecimalHdbr);
        flowHdbr.setData(Arrays.asList(bigDecimalHdbr));
        returnData.add(flowHdbr);

        //手机端需要展示(政办公+华电+风子沟)合计
        FlowDayRecordBarChart.FlowBarChartItem flowUserdCount = new FlowDayRecordBarChart.FlowBarChartItem();
        flowUserdCount.setName(FlowModuleConstant.FLOW_USERD_NAME);
        flowUserdCount.setData(Arrays.asList(usedCount));
        returnData.add(flowUserdCount);

        flowDayRecordBarChart.setSeries(returnData);
        result.setResult(flowDayRecordBarChart);
        return result.success();

    }

    /**
     * 处理昨日数据格式为--的数据
     *
     * @param flow
     * @return
     */
    private BigDecimal formatValue(String flow) {
        //默认值
        String endStr="--";
        if (!flow.endsWith(endStr)) {
            return new BigDecimal(flow);
        }
        return BigDecimal.ZERO;
    }

    /**
     * 今日各个级别告警统计
     *
     * @return
     * @author songguang.jiao
     * @date 2020/05/29 11:04:33
     */
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @ApiOperation(value = "今日各个级别告警统计")
    @GetMapping("/warnInfo")
    public Result<List<PolicyOneDayVo>> warnInfo() {
        Result<List<PolicyOneDayVo>> result = new Result<>();
        List<PolicyOneDayVo> data = businessService.getWarnLevelInfoToday();
        result.setResult(data);
        return result.success();
    }

    /**
     * 告警的合同数量
     *
     * @return
     * @author songguang.jiao
     * @date 2020/05/29 14:35:29
     */
    @ApiVersion(group = VersionConstant.VERSION_APP)
    @ApiOperation(value = "告警的合同数量")
    @GetMapping("/alarm/contract")
    public Result<Integer> countAlarmContract() {
        //查询告警时间,根据时间差计算是否报警
        SystemConfig systemConfig = systemParamService.getConfigByKey(SystemParamConstant.CONTRACT_REMIND_REMAINING_DAYS);
        if (systemConfig != null && WorkingStatus.WORKING.equals(systemConfig.getConfigValue())) {
            return Result.ok(contractService.countAlarmContracts(systemConfig.getConfigValue()));
        }
        return Result.ok(0);
    }

}
