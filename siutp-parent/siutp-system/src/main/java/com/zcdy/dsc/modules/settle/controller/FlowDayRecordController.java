package com.zcdy.dsc.modules.settle.controller;

import com.zcdy.dsc.common.api.vo.Result;
import com.zcdy.dsc.common.aspect.annotation.AutoLog;
import com.zcdy.dsc.modules.settle.constant.FlowModuleConstant;
import com.zcdy.dsc.modules.settle.entity.FlowDayRecordEntity;
import com.zcdy.dsc.modules.settle.param.FlowRecordPageParam;
import com.zcdy.dsc.modules.settle.service.FlowDayRecordService;
import com.zcdy.dsc.modules.settle.vo.FlowDayRecordBarChart;
import com.zcdy.dsc.modules.settle.vo.FlowDayRecordBarChart.FlowBarChartItem;
import com.zcdy.dsc.modules.settle.vo.FlowDayRecordVo;
import com.zcdy.dsc.modules.system.service.ISysCategoryService;
import com.zcdy.dsc.modules.system.vo.SysCateDropdown;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.*;

/**
 * @Description: 每日用水量记录
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-04-30 15:09:53
 * @Version: V1.0
 */
@Api(tags = "每日用水量记录")
@RestController
@RequestMapping("/settle/flowDayRecord")
public class FlowDayRecordController{
    @Resource
    private FlowDayRecordService flowDayRecordService;

    @Resource
    private ISysCategoryService categoryService;
    /**
     *  时间单位周期 d
     */
    private static final String TIME_PERID="d";

    /**
     * 每日用水量记录
     * @return
     */
    @AutoLog(value = "每日用水量记录")
    @ApiOperation(value = "每日用水量记录", notes = "每日用水量记录")
    @GetMapping
    public Result<List<FlowDayRecordVo>> queryPageList(FlowRecordPageParam param) {
        Result<List<FlowDayRecordVo>> result = new Result<List<FlowDayRecordVo>>();
        // 判断是按照月查询还是日查询
        String localDateLeft = param.getLeft().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
        String localDateRight = param.getRight().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
        List<FlowDayRecordEntity> list = null;
        if (TIME_PERID.equals(param.getTimeUnit())) {
            list = flowDayRecordService.dayList(localDateLeft, localDateRight);
        } else {
            list = flowDayRecordService.monthList(localDateLeft, localDateRight);
        }
        Map<String, FlowDayRecordVo> existMap = new TreeMap<String, FlowDayRecordVo>();
        for (FlowDayRecordEntity record : list) {
            FlowDayRecordVo flowDayRecordVo = existMap.get(record.getCountDate());
            if (flowDayRecordVo == null) {
                flowDayRecordVo = new FlowDayRecordVo();
                flowDayRecordVo.setCountDate(record.getCountDate());
                existMap.put(record.getCountDate(), flowDayRecordVo);
            }
            switch (record.getCountCode()) {
                case FlowModuleConstant.FLOW_HD:
                    flowDayRecordVo.setFlowHd(record.getCountValue());
                    break;
                case FlowModuleConstant.FLOW_XZQ:
                    flowDayRecordVo.setFlowXzq(record.getCountValue());
                    break;
                case FlowModuleConstant.FLOW_XZRL:
                    flowDayRecordVo.setFlowXzrl(record.getCountValue());
                    break;
                case FlowModuleConstant.FLOW_XZLH:
                    flowDayRecordVo.setFlowXzlh(record.getCountValue());
                    break;
                case FlowModuleConstant.FLOW_FZG:
                    flowDayRecordVo.setFlowFzg(record.getCountValue());
                    break;
                case FlowModuleConstant.FLOW_HDBR:
                    flowDayRecordVo.setFlowHdbr(record.getCountValue());
                    break;
                default:
                    break;
            }
        }
        result.setResult(new ArrayList<>(existMap.values()));
        return result.success("success");
    }

    /**
     * 描述: 柱状图展示 @author: songguang.jiao 创建时间: 2020年5月6日 下午4:29:06 版本: V1.0
     */
    @ApiOperation(value = "柱状图")
    @GetMapping("/chart")
    public Result<FlowDayRecordBarChart> chart(FlowRecordPageParam param) {
        Result<FlowDayRecordBarChart> result = new Result<FlowDayRecordBarChart>();
        FlowDayRecordBarChart flowDayRecordBarChart = new FlowDayRecordBarChart();
        // 判断是按照月查询还是日查询
        String localDateLeft = param.getLeft().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
        String localDateRight = param.getRight().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
        List<FlowDayRecordEntity> list = null;
        if (TIME_PERID.equals(param.getTimeUnit())) {
            list = flowDayRecordService.dayList(localDateLeft, localDateRight);
        } else {
            list = flowDayRecordService.monthList(localDateLeft, localDateRight);
        }

        // x轴时间组合
        Map<String, String> existTime = new TreeMap<String, String>();
        for (FlowDayRecordEntity recordEntity : list) {
            existTime.put(recordEntity.getCountDate(), recordEntity.getCountDate());
        }
        flowDayRecordBarChart.setMainTitle("用水量统计");
        flowDayRecordBarChart.setYTitle("用水量(m³)");
        ArrayList<String> timeList = new ArrayList<>(existTime.keySet());
        flowDayRecordBarChart.setXCategories(timeList);

        // y轴数据组装
        List<SysCateDropdown> keyValue = categoryService.queryKeyValueByParentCode("A18");
        Map<String, String> sysCateMap = new HashMap<>(keyValue.size());
        keyValue.forEach(item -> {
            sysCateMap.put(item.getCode(), item.getTitle());
        });
        Map<String, FlowBarChartItem> existData = new TreeMap<String, FlowBarChartItem>();
        for (String string : timeList) {
            for (FlowDayRecordEntity recordEntity : list) {
                FlowBarChartItem flowBarChartItem = existData.get(recordEntity.getCountCode());
                if (flowBarChartItem == null) {
                    flowBarChartItem = new FlowBarChartItem();
                    List<BigDecimal> data = new ArrayList<>();
                    flowBarChartItem.setData(data);
                    flowBarChartItem.setName(sysCateMap.get(recordEntity.getCountCode()));
                    existData.put(recordEntity.getCountCode(), flowBarChartItem);
                }
                // 比较时间是同一天就赋值
                List<BigDecimal> data = flowBarChartItem.getData();
                if (string.equals(recordEntity.getCountDate())) {
                    data.add(new BigDecimal(recordEntity.getCountValue()));
                    flowBarChartItem.setData(data);
                }
            }
        }
        flowDayRecordBarChart.setSeries(new ArrayList<>(existData.values()));
        result.setResult(flowDayRecordBarChart);
        return result.success("success");
    }

}