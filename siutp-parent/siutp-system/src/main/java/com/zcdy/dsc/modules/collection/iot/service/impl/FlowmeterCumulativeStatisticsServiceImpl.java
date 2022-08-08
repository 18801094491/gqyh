package com.zcdy.dsc.modules.collection.iot.service.impl;

import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.modules.collection.iot.mapper.FlowmeterCumulativeStatisticsMapper;
import com.zcdy.dsc.modules.collection.iot.service.FlowmeterCumulativeStatisticsService;
import com.zcdy.dsc.modules.collection.iot.vo.FlowmeterCumulativeStatisticsParam;
import com.zcdy.dsc.modules.collection.iot.vo.FlowmeterCumulativeStatisticsVo;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointData;
import org.influxdb.dto.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * 水表流量统计
 *
 * @author tangchao
 * @since 2020-4-28 20:57:34
 */
@Service
public class FlowmeterCumulativeStatisticsServiceImpl implements FlowmeterCumulativeStatisticsService {

    @Resource
    private FlowmeterCumulativeStatisticsMapper flowmeterCumulativeStatisticsMapper;

    @Resource
    private InfluxService influxService;

    /**
     * 数据维度转换,
     * 时间   2段累计流量  3段累计流量    外部河东水厂 累计流量
     *
     * @param param 查询条件
     * @return 数据
     * @author tangchao
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> ioDataByCycleAndIntervalDataFormat(FlowmeterCumulativeStatisticsParam param) {
        List<Map<String, Object>> maps = this.ioDataByCycleAndInterval(param);

        Map<String, Object> table = new LinkedHashMap<>();
        List<Map<String, String>> data = new ArrayList<>();
        List<Map<String, String>> header = new ArrayList<>();
        table.put("data", data);
        table.put("header", header);
        table.put("title", "流量计统计");
        List<Map<String, Object>> result = new ArrayList<>();
        result.add(table);
        header.add(getHead("序号", "serialNo", "10"));
        header.add(getHead("统计时间", "statisticsTime", "41"));

        if (maps.size() == 0) {
            return null;
        }
        List<FlowmeterCumulativeStatisticsVo> data1 = (List<FlowmeterCumulativeStatisticsVo>) maps.get(0).get("data");
        if(data1 != null){
            for (FlowmeterCumulativeStatisticsVo flowmeterCumulativeStatisticsVo : data1) {
                Map<String, String> map = new LinkedHashMap<>();
                map.put("serialNo", flowmeterCumulativeStatisticsVo.getSerialNo());
                map.put("statisticsTime", flowmeterCumulativeStatisticsVo.getStatisticsTime());
                data.add(map);
            }
        }
        for (Map<String, Object> map : maps) {
            String columnDataIndex = (String) map.get("columnDataIndex");
            header.add(getHead((String) map.get("columnName"), columnDataIndex, "20"));
            //每个设备数据
            List<FlowmeterCumulativeStatisticsVo> dataList = (List<FlowmeterCumulativeStatisticsVo>) map.get("data");
            for (int i = 0; i < dataList.size(); i++) {
                FlowmeterCumulativeStatisticsVo f = dataList.get(i);
                String totalFlow = f.getTotalFlow();
                if (i >= data.size()) {
                    data.add(new LinkedHashMap<>());
                }
                Map<String, String> map1 = data.get(i);
                map1.put(columnDataIndex, totalFlow);
                map1.put("serialNo", f.getSerialNo());
                map1.put("statisticsTime", f.getStatisticsTime());
            }
        }
        return result;
    }

    /**
     * 生成列属性
     *
     * @param title     列名称
     * @param dataIndex 列字段名
     * @param width     宽度
     * @return 列
     */
    private Map<String, String> getHead(String title, String dataIndex, String width) {
        Map<String, String> header = new HashMap<>(3);
        header.put("title", title);
        header.put("dataIndex", dataIndex);
        header.put("width", width);
        return header;
    }

    /**
     * 结束时间 endTime = beginTime + (cycle * cycleUnit)
     * 1.获取统计数据
     * 2.获取间隔数据
     * 每次递增 (interval * intervalUnit) 并且小于等于 结束时间
     *
     * @return 表格结果
     * @author tangchao
     * beginDate 开始时间
     * cycle 周期
     * interval 间隔
     * cycleUnit 周期单位: 周期单位, YEARS-年 MONTHS-月 DAYS-日 HOURS-小时 MINUTES-分
     * intervalUnit 间隔单位:  MONTHS-月 DAYS-日 HOURS-小时 MINUTES-分
     * @since 2020年4月28日12:58:58
     */
    @Override
    public List<Map<String, Object>> ioDataByCycleAndInterval(FlowmeterCumulativeStatisticsParam param) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        List<Map<String, Object>> r = new ArrayList<>();
        String endTime = param.getEndTime();
        //获取设配变量信息
        List<FlowmeterCumulativeStatisticsVo> variableByEquipmentId =
                flowmeterCumulativeStatisticsMapper.getVariableByEquipmentId(param.getEquipmentIds());
        //循环每个设备
        for (FlowmeterCumulativeStatisticsVo f : variableByEquipmentId) {
            List<FlowmeterCumulativeStatisticsVo> result = new ArrayList<>();
            Map<String, Object> map = new HashMap<>(4);
            //统计数据
            FlowmeterCumulativeStatisticsVo statisticsByCycle = new FlowmeterCumulativeStatisticsVo(f);
            statisticsByCycle.setStatisticsTime(param.getBeginTime() + " ~ " + endTime);
            statisticsByCycle.setSerialNo("合计");
            List<FlowmeterCumulativeStatisticsVo> detail = new ArrayList<>();
            //序号
            int index = 1;
            //周期截止日期
            String midBegin = param.getEndTime();
            LocalDateTime midTimeObj = LocalDateTime.parse(midBegin, DateTimeFormatter.ofPattern(pattern))
                    .plus(-param.getInterval(), ChronoUnit.valueOf(param.getIntervalUnit()));
            //周期开始日期
            String midTime = midTimeObj.format(DateTimeFormatter.ofPattern(pattern));
            //获取时间间隔数据
            while (midTimeObj.compareTo(LocalDateTime.parse(param.getBeginTime(), DateTimeFormatter.ofPattern(pattern))) >= 0) {
                FlowmeterCumulativeStatisticsVo influxDbSpreadByTime = getStatisticsByInterval(f, midBegin, midTime, param);
                influxDbSpreadByTime.setStatisticsTime(midBegin);
                influxDbSpreadByTime.setSerialNo(String.valueOf(index));
                // 数据不存在则过滤
                if (!influxDbSpreadByTime.isNull()) {
                    detail.add(influxDbSpreadByTime);
                    index++;
                }
                midBegin = midTime;
                midTimeObj = LocalDateTime.parse(midTime, DateTimeFormatter.ofPattern(pattern))
                        .plus(-param.getInterval(), ChronoUnit.valueOf(param.getIntervalUnit()));
                //时间 - 一次间隔
                midTime = midTimeObj.format(DateTimeFormatter.ofPattern(pattern));

            }
            //计算统计值
            if (detail.size() > 0) {
                BigDecimal sumPositive = new BigDecimal("0");
                BigDecimal sumTotalFlow = new BigDecimal("0");
                BigDecimal sumNegativeTotalFlow = new BigDecimal("0");
                BigDecimal sumInstantaneousFlow = new BigDecimal("0.000");
                BigDecimal sumFlowRate = new BigDecimal("0.000");
                for (FlowmeterCumulativeStatisticsVo fv : detail) {
                    sumPositive = sumPositive.add(new BigDecimal(fv.getPositiveTotalFlow() == null ? "0" : fv.getPositiveTotalFlow()));
                    sumTotalFlow = sumTotalFlow.add(new BigDecimal(fv.getTotalFlow() == null ? "0" : fv.getTotalFlow()));
                    sumNegativeTotalFlow = sumNegativeTotalFlow.add(new BigDecimal(fv.getNegativeTotalFlow() == null ? "0" : fv.getNegativeTotalFlow()));
                    sumInstantaneousFlow = sumInstantaneousFlow.add(new BigDecimal(fv.getInstantaneousFlow() == null ? "0" : fv.getInstantaneousFlow()));
                    sumFlowRate = sumFlowRate.add(new BigDecimal(fv.getFlowRate() == null ? "0" : fv.getFlowRate()));
                }
                statisticsByCycle.setPositiveTotalFlow(sumPositive.toString());
                statisticsByCycle.setTotalFlow(sumTotalFlow.toString());
                statisticsByCycle.setNegativeTotalFlow(sumNegativeTotalFlow.toString());
                statisticsByCycle.setInstantaneousFlow(sumInstantaneousFlow.divide(new BigDecimal(detail.size()), RoundingMode.HALF_UP).toString());
                statisticsByCycle.setFlowRate(sumFlowRate.divide(new BigDecimal(detail.size()), RoundingMode.HALF_UP).toString());
                result.add(statisticsByCycle);
            }
            result.addAll(detail);
            map.put("data", result);
            map.put("title", f.getEquipmentName());
            map.put("columnDataIndex", f.getVariableName()[0]);
            map.put("columnName", f.getColumnName());
            r.add(map);
        }
        return r;
    }

    /**
     * 每个周期值:
     * 暂时没用
     * 累计流量 = 周期差值 = Data(endTime) - Data(startTime)
     * 正累计流量 = 周期差值 = Data(endTime) - Data(startTime)
     * 负累计流量 = 周期差值 = Data(endTime) - Data(startTime)
     * 流量计瞬时流量 = 周期内流速总和/周期内数据总条数 = ∑(Data(time)) time∈(周期) / Count()
     * 流量计流速 = 周期内流速总和/周期内数据总条数 = ∑(Data(time)) time∈(周期) / Count()
     *
     * @param param 参数
     * @return 数据
     * @author tangchao
     */
    public FlowmeterCumulativeStatisticsVo getStatisticsByInterval(FlowmeterCumulativeStatisticsVo fcsv,
                                                                   String startTime, String endTime, FlowmeterCumulativeStatisticsParam param) {
        StringBuilder sqlBuilder = new StringBuilder();
        String measurement = getStatisticsByIntervalSql(sqlBuilder, fcsv, startTime, endTime, param);
        List<PointData> pointDataList = this.influxService.query(new Query(sqlBuilder.toString()), PointData.class, measurement);
        return new FlowmeterCumulativeStatisticsVo(fcsv, pointDataList);
    }

    public String getStatisticsByIntervalSql(StringBuilder sqlBuilder, FlowmeterCumulativeStatisticsVo fcsv,
                                             String startTime, String endTime, FlowmeterCumulativeStatisticsParam param) {

        String measurement;
        if (ChronoUnit.MONTHS.name().equals(param.getIntervalUnit()) && param.getInterval() >= 1) {
            sqlBuilder.append(" select (last(avg_value) - first(avg_value)) as var_value from iot_point_data_day where");
            measurement = "iot_point_data_day";
        } else if (ChronoUnit.DAYS.name().equals(param.getIntervalUnit()) && param.getInterval() >= 1) {
            sqlBuilder.append(" select (last(avg_value) - first(avg_value)) as var_value from iot_point_data_hour where");
            measurement = "iot_point_data_hour";
        } else if (ChronoUnit.HOURS.name().equals(param.getIntervalUnit()) && param.getInterval() >= 24) {
            sqlBuilder.append(" select (last(avg_value) - first(avg_value)) as var_value from iot_point_data_half_hour where");
            measurement = "iot_point_data_half_hour";
        } else if (ChronoUnit.HOURS.name().equals(param.getIntervalUnit()) && param.getInterval() >= 1) {
            sqlBuilder.append(" select (last(avg_value) - first(avg_value)) as var_value from iot_point_data_15m where");
            measurement = "iot_point_data_15m";
        } else {
            sqlBuilder.append(" select (last(var_value) - first(var_value)) as var_value from iot_point_data where");
            measurement = "iot_point_data";
        }

        sqlBuilder.append(" time <= '").append(startTime).append("'");
        sqlBuilder.append(" and time >= '").append(endTime).append("'");
        String[] variableName = fcsv.getVariableName();
        sqlBuilder.append(" and (var_name ='");
        for (int i = 0; i < variableName.length; i++) {
            if (i != 0) {
                sqlBuilder.append("' or var_name='");
            }
            sqlBuilder.append(variableName[i]);
        }
        sqlBuilder.append("')");
        sqlBuilder.append(" group by var_name");
        sqlBuilder.append(" order by time desc");
        return measurement;
    }

}
