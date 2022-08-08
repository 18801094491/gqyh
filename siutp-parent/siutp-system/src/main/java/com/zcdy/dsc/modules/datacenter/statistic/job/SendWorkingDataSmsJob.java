package com.zcdy.dsc.modules.datacenter.statistic.job;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSONObject;
import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.modules.collection.iot.constant.VariNameConstant;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointData;
import com.zcdy.dsc.modules.datacenter.statistic.service.ModuleVarService;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentVariableVO;
import com.zcdy.dsc.modules.message.constant.EventConstant;
import com.zcdy.dsc.modules.message.util.SendSmsUtil;

/**
 * 描述: 两小时推送同时发生两条短信，一条以7标压力为基准，一条以河东水厂流量为基准
 * 
 * 22点至次日8点：8点发短信 8点至10点：10点发短信 10点至12点：12点发短信 以此类推20点至22点：22点发送短信
 * 
 * 
 * 时间：（15:30:30）7标压力峰值：0.268 同时间参考值： 7标瞬时流量：69.8m³/h 2标瞬时压力：0.240MPa 2标瞬时流量：-89.7m³/h 河东瞬时流量：584.9m³/h
 ** 时间：（15:30:30）7标压力谷值：0.268 同时间参考值： 7标瞬时流量：69.8m³/h 2标瞬时压力：0.240MPa 2标瞬时值流量：-89.7m³/h 河东瞬时流量：584.9m³/h
 * 
 * @author: songguang.jiao 创建时间: 2020年5月8日 上午11:16:32
 */
public class SendWorkingDataSmsJob implements Job {

    @Resource
    private InfluxService influxService;

    @Resource
    private ModuleVarService moduleVarService;

    /**
     * 模块id
     */
    private static String moduleId = "818fa6ac749611ea9fded05099cd3eff";

    @Resource
    private SendSmsUtil sendSmsUtil;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 河东为基准,7标压力为基准
        JSONObject workDataJsonHd = new JSONObject();
        JSONObject workDataJson7P = new JSONObject();

        // 发送日期
        DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        workDataJsonHd.put("statisticDate", now.format(dateformatter));
        workDataJson7P.put("statisticDate", now.format(dateformatter));

        // 根据当前时间判断是否时早上8点时间,暂时定义为7-9点之间(防止系统时间差异),默认间隔为2小时
        int interval = 2;
        int hourStart = 7, hourEnd = 9;
        if (now.getHour() >= hourStart && now.getHour() < hourEnd) {
            interval = 10;
        }
        // 查询区间开始时间-结束时间
        LocalDateTime startLocalTime = now.minusHours(interval);
        DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("H:00");
        workDataJsonHd.put("satisticCycle", startLocalTime.format(timeformatter) + "至" + now.format(timeformatter));
        workDataJson7P.put("satisticCycle", startLocalTime.format(timeformatter) + "至" + now.format(timeformatter));
        long startTime = startLocalTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() * 1000000L;
        long endTime = now.toInstant(ZoneOffset.of("+8")).toEpochMilli() * 1000000L;

        // 查询基础数据,获取精度单位
        List<EquipmentVariableVO> datas = this.moduleVarService.queryEquipAndVarByModuleId(moduleId, null);
        Map<String, EquipmentVariableVO> equipMap = new HashMap<String, EquipmentVariableVO>(datas.size());
        for (EquipmentVariableVO vo : datas) {
            equipMap.put(vo.getVariableName(), vo);
        }

        this.queryMaxOrMin(startTime, endTime, workDataJsonHd, workDataJson7P, equipMap, "min");
        this.queryMaxOrMin(startTime, endTime, workDataJsonHd, workDataJson7P, equipMap, "max");

        sendSmsUtil.sendSms(EventConstant.AUTO_RUNNING_DATA_HD, workDataJsonHd);
        sendSmsUtil.sendSms(EventConstant.AUTO_RUNNING_DATA_7P, workDataJson7P);
    }

    /**
     * 描述: 查询最大值,最小值
     * 
     * @author: songguang.jiao 创建时间: 2020年5月8日 下午7:16:57
     */
    private void queryMaxOrMin(long startTime, long endTime, JSONObject workDataJsonHd, JSONObject workDataJson7P,
        Map<String, EquipmentVariableVO> equipMap, String minOrMax) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select ").append(minOrMax).append("(var_value) as var_value from iot_point_data where");
        sqlBuilder.append("(var_name='" + VariNameConstant.YL_7_01 + "' or var_name='" + VariNameConstant.HDSC + "')");
        sqlBuilder.append(" and time>=" + startTime + " and time<=" + endTime + " group by var_name fill(none)");
        List<PointData> datas = this.influxService.query(sqlBuilder.toString(), PointData.class);
        if (datas.size() < 1) {
            String msg = "--";
            workDataJson7P.put("minOccurTime", msg);
            workDataJson7P.put("minValue7P", msg);
            workDataJson7P.put("minValue2P", msg);
            workDataJson7P.put("minValue2L", msg);
            workDataJson7P.put("minValue7L", msg);
            workDataJson7P.put("minValueHDL", msg);
            workDataJson7P.put("maxOccurTime", msg);
            workDataJson7P.put("maxValue7P", msg);
            workDataJson7P.put("maxValue2P", msg);
            workDataJson7P.put("maxValue2L", msg);
            workDataJson7P.put("maxValue7L", msg);
            workDataJson7P.put("maxValueHDL", msg);

            workDataJsonHd.put("minOccurTime", msg);
            workDataJsonHd.put("minValue7P", msg);
            workDataJsonHd.put("minValue2P", msg);
            workDataJsonHd.put("minValue2L", msg);
            workDataJsonHd.put("minValue7L", msg);
            workDataJsonHd.put("minValueHDL", msg);
            workDataJsonHd.put("maxOccurTime", msg);
            workDataJsonHd.put("maxValue7P", msg);
            workDataJsonHd.put("maxValue2P", msg);
            workDataJsonHd.put("maxValue2L", msg);
            workDataJsonHd.put("maxValue7L", msg);
            workDataJsonHd.put("maxValueHDL", msg);
        }
        for (PointData data : datas) {
            String occurTime = data.getTime().atZone(ZoneId.systemDefault()).toLocalDateTime().format(formatter);
            EquipmentVariableVO variableVO = equipMap.get(data.getVarName());
            BigDecimal occurValue =
                new BigDecimal(data.getVarValue()).setScale(variableVO.getScale(), RoundingMode.HALF_UP);
            long occurTimeLong = data.getTime().toEpochMilli() * 1000000L;
            if ("min".equals(minOrMax)) {
                if (data.getVarName().equals(VariNameConstant.YL_7_01)) {
                    workDataJson7P.put("minOccurTime", occurTime);
                    this.queryReferenceData(startTime, occurTimeLong, workDataJson7P, equipMap, minOrMax);
                    workDataJson7P.put("minValue7P", occurValue);
                }
                if (data.getVarName().equals(VariNameConstant.HDSC)) {
                    workDataJsonHd.put("minOccurTime", occurTime);
                    this.queryReferenceData(startTime, occurTimeLong, workDataJsonHd, equipMap, minOrMax);
                    workDataJsonHd.put("minValueHDL", occurValue);
                }
            } else {
                if (data.getVarName().equals(VariNameConstant.YL_7_01)) {
                    workDataJson7P.put("maxOccurTime", occurTime);
                    this.queryReferenceData(startTime, occurTimeLong, workDataJson7P, equipMap, minOrMax);
                    workDataJson7P.put("maxValue7P", occurValue);
                }
                if (data.getVarName().equals(VariNameConstant.HDSC)) {
                    workDataJsonHd.put("maxOccurTime", occurTime);
                    this.queryReferenceData(startTime, occurTimeLong, workDataJsonHd, equipMap, minOrMax);
                    workDataJsonHd.put("maxValueHDL", occurValue);
                }
            }
        }

    }

    /**
     * 描述: 查询相关数据,并组装数据
     * 
     * @author: songguang.jiao 创建时间: 2020年5月8日 下午7:17:20 版本: V1.0
     */
    private void queryReferenceData(long startTime, long occurTime, JSONObject workDataJson,
        Map<String, EquipmentVariableVO> equipMap, String minOrMax) {
        StringBuilder builder = new StringBuilder();
        builder.append("select var_value from iot_point_data where ");
        builder.append("(var_name='" + VariNameConstant.YL_7_01 + "' or var_name='" + VariNameConstant.HDSC
            + "' or var_name='" + VariNameConstant.YL_2_01 + "' or var_name='" + VariNameConstant.LL_2
            + "' or var_name='" + VariNameConstant.LL_7 + "')");
        builder.append(
            " and time>=" + startTime + " and time<=" + occurTime + " group by var_name order by time desc limit 1");
        List<PointData> referebceData = this.influxService.query(builder.toString(), PointData.class);
        for (PointData data : referebceData) {
            EquipmentVariableVO variableVO = equipMap.get(data.getVarName());
            BigDecimal value = new BigDecimal(data.getVarValue()).setScale(variableVO.getScale(), RoundingMode.HALF_UP);
            // 获取需要的采集数据,并放置到消息模板中
            if ("min".equals(minOrMax)) {
                switch (data.getVarName()) {
                    case VariNameConstant.LL_2: {
                        workDataJson.put("minValue2L", value);
                        break;
                    }
                    case VariNameConstant.LL_7: {
                        workDataJson.put("minValue7L", value);
                        break;
                    }
                    case VariNameConstant.YL_2_01: {
                        workDataJson.put("minValue2P", value);
                        break;
                    }
                    case VariNameConstant.YL_7_01: {
                        workDataJson.put("minValue7P", value);
                        break;
                    }
                    case VariNameConstant.HDSC: {
                        workDataJson.put("minValueHDL", value);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            } else {
                switch (data.getVarName()) {
                    case VariNameConstant.LL_2: {
                        workDataJson.put("maxValue2L", value);
                        break;
                    }
                    case VariNameConstant.LL_7: {
                        workDataJson.put("maxValue7L", value);
                        break;
                    }
                    case VariNameConstant.YL_2_01: {
                        workDataJson.put("maxValue2P", value);
                        break;
                    }
                    case VariNameConstant.YL_7_01: {
                        workDataJson.put("maxValue7P", value);
                        break;
                    }
                    case VariNameConstant.HDSC: {
                        workDataJson.put("maxValueHDL", value);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        }
    }
}
