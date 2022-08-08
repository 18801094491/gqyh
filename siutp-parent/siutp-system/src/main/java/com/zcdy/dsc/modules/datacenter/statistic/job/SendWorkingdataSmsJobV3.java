package com.zcdy.dsc.modules.datacenter.statistic.job;

import com.alibaba.fastjson.JSONObject;
import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.modules.collection.iot.constant.VariNameConstant;
import com.zcdy.dsc.modules.datacenter.statistic.entity.PointData;
import com.zcdy.dsc.modules.datacenter.statistic.service.ModuleVarService;
import com.zcdy.dsc.modules.datacenter.statistic.vo.EquipmentVariableVO;
import com.zcdy.dsc.modules.message.constant.EventConstant;
import com.zcdy.dsc.modules.message.util.SendSmsUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: 两小时推送同时发生两条短信
 * 22点至次日8点：8点发短信 8点至10点：10点发短信 10点至12点：12点发短信 以此类推20点至22点：22点发送短信
 *
 * @author: zengli.wang 创建时间: 2020年9月3日 上午10:30
 */

public class SendWorkingdataSmsJobV3 implements Job {


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
        JSONObject workDataJson = new JSONObject();

        // 发送日期
        DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        workDataJson.put("sDate", now.format(dateformatter));

        // 根据当前时间判断是否时早上8点时间,暂时定义为7-9点之间(防止系统时间差异),默认间隔为2小时
        int interval = 2;
        int hourStart = 7, hourEnd = 9;
        if (now.getHour() >= hourStart && now.getHour() < hourEnd) {
            interval = 10;
        }
        // 查询区间开始时间-结束时间
        LocalDateTime startLocalTime = now.minusHours(interval);
        DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("H:00");
        workDataJson.put("sCycle", startLocalTime.format(timeformatter) + "至" + now.format(timeformatter));
        long startTime = startLocalTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() * 1000000L;
        long endTime = now.toInstant(ZoneOffset.of("+8")).toEpochMilli() * 1000000L;
        // 查询基础数据,获取精度单位
        List<EquipmentVariableVO> datas = this.moduleVarService.queryEquipAndVarByModuleId(moduleId, null);
        Map<String, EquipmentVariableVO> equipMap = new HashMap<String, EquipmentVariableVO>(datas.size());
        for (EquipmentVariableVO vo : datas) {
            equipMap.put(vo.getVariableName(), vo);
        }
        //设置默认值
        String msg = "--";
        workDataJson.put("maxOTimeHD", msg);
        workDataJson.put("maxVHDL", msg);
        workDataJson.put("minOTimeHD", msg);
        workDataJson.put("minVHDL", msg);
        workDataJson.put("maxV7P", msg);
        workDataJson.put("minV7P", msg);
        workDataJson.put("maxV7L", msg);
        workDataJson.put("minV7L", msg);
        workDataJson.put("maxV2P", msg);
        workDataJson.put("minV2P", msg);
        workDataJson.put("maxV2L", msg);
        workDataJson.put("minV2L", msg);
        workDataJson.put("maxOTime7", msg);
        workDataJson.put("maxV7P1", msg);
        workDataJson.put("minOTime7", msg);
        workDataJson.put("minV7P1", msg);
        workDataJson.put("maxVHDL1", msg);
        workDataJson.put("minVHDL1", msg);
        workDataJson.put("maxV7L1", msg);
        workDataJson.put("minV7L1", msg);
        workDataJson.put("maxV2P1", msg);
        workDataJson.put("minV2P1", msg);
        workDataJson.put("maxV2L1", msg);
        workDataJson.put("minV2L1", msg);
        //2020.9.3新增
        //河东压力峰值
        workDataJson.put("maxVHDP", msg);
        //河东压力谷值
        workDataJson.put("minVHDP", msg);
        //何工流量 压力 （7标压力分析 峰值）
        workDataJson.put("maxVHDP1", msg);
        //河东流量 压力 （7标压力分析 谷值）
        workDataJson.put("minVHDP1", msg);

        this.queryMaxOrMin(startTime, endTime, workDataJson, equipMap, "min");
        this.queryMaxOrMin(startTime, endTime, workDataJson, equipMap, "max");
        this.sendSmsUtil.sendSms(EventConstant.AUTO_RUNNING_IOT_DATA_20200904, workDataJson);
    }


    /**
     * 描述: 查询最大值,最小值
     *
     * @author: songguang.jiao 创建时间: 2020年5月8日 下午7:16:57
     */
    private void queryMaxOrMin(long startTime, long endTime, JSONObject workDataJson,
                               Map<String, EquipmentVariableVO> equipMap, String minOrMax) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select ").append(minOrMax).append("(var_value) as var_value from iot_point_data where");
        sqlBuilder.append("(var_name='" + VariNameConstant.YL_7_01 + "' or var_name='" + VariNameConstant.HDYL + "')");
        sqlBuilder.append(" and time>=" + startTime + " and time<=" + endTime + " group by var_name fill(none)");
        List<PointData> datas = this.influxService.query(sqlBuilder.toString(), PointData.class);


        for (PointData data : datas) {
            String occurTime = data.getTime().atZone(ZoneId.systemDefault()).toLocalDateTime().format(formatter);
            EquipmentVariableVO variableVO = equipMap.get(data.getVarName());
            //判定数据
            if(variableVO.getScale()!=null && data.getVarValue()!=null) {
                BigDecimal occurValue =
                        new BigDecimal(data.getVarValue()).setScale(variableVO.getScale(), RoundingMode.HALF_UP);
                long occurTimeLong = data.getTime().toEpochMilli() * 1000000L;
                if ("min".equals(minOrMax)) {
                    if (data.getVarName().equals(VariNameConstant.YL_7_01)) {
                        workDataJson.put("minOTime7", occurTime);
                        this.queryReferenceData7P(startTime, occurTimeLong, workDataJson, equipMap, minOrMax);
                        workDataJson.put("minV7P1", occurValue);
                    }
                    //新增河东流量压力最小值
                    if (data.getVarName().equals(VariNameConstant.HDYL)) {
                        workDataJson.put("minOTimeHD", occurTime);
                        this.queryReferenceDataHd(startTime, occurTimeLong, workDataJson, equipMap, minOrMax);
                        workDataJson.put("minVHDP", occurValue);
                    }
                } else {
                    if (data.getVarName().equals(VariNameConstant.YL_7_01)) {
                        workDataJson.put("maxOTime7", occurTime);
                        this.queryReferenceData7P(startTime, occurTimeLong, workDataJson, equipMap, minOrMax);
                        workDataJson.put("maxV7P1", occurValue);
                    }

                    //新增河东流量压力最大值
                    if (data.getVarName().equals(VariNameConstant.HDYL)) {
                        workDataJson.put("maxOTimeHD", occurTime);
                        this.queryReferenceDataHd(startTime, occurTimeLong, workDataJson, equipMap, minOrMax);
                        workDataJson.put("maxVHDP", occurValue);
                    }
                }
            }
        }

    }

    /**
     * 描述: 查询7标段压力相关数据,并组装数据
     *
     * @author: songguang.jiao 创建时间: 2020年5月8日 下午7:17:20 版本: V1.0
     */
    private void queryReferenceData7P(long startTime, long occurTime, JSONObject workDataJson,
                                      Map<String, EquipmentVariableVO> equipMap, String minOrMax) {
        StringBuilder builder = new StringBuilder();
        builder.append("select var_value from iot_point_data where ");
        builder.append("(var_name='" + VariNameConstant.YL_7_01 + "' or var_name='" + VariNameConstant.HDSC
                + "' or var_name='" + VariNameConstant.YL_2_01 + "' or var_name='" + VariNameConstant.LL_2
                + "' or var_name='" + VariNameConstant.LL_7 +"' or var_name='"+VariNameConstant.HDYL
                +"')");
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
                        workDataJson.put("minV2L1", value);
                        break;
                    }
                    case VariNameConstant.LL_7: {
                        workDataJson.put("minV7L1", value);
                        break;
                    }
                    case VariNameConstant.YL_2_01: {
                        workDataJson.put("minV2P1", value);
                        break;
                    }
                    case VariNameConstant.YL_7_01: {
                        workDataJson.put("minV7P1", value);
                        break;
                    }
                    case VariNameConstant.HDSC: {
                        workDataJson.put("minVHDL1", value);
                        break;
                    }
                    case VariNameConstant.HDYL: {
                        workDataJson.put("minVHDP1", value);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            } else {
                switch (data.getVarName()) {
                    case VariNameConstant.LL_2: {
                        workDataJson.put("maxV2L1", value);
                        break;
                    }
                    case VariNameConstant.LL_7: {
                        workDataJson.put("maxV7L1", value);
                        break;
                    }
                    case VariNameConstant.YL_2_01: {
                        workDataJson.put("maxV2P1", value);
                        break;
                    }
                    case VariNameConstant.YL_7_01: {
                        workDataJson.put("maxV7P1", value);
                        break;
                    }
                    case VariNameConstant.HDSC: {
                        workDataJson.put("maxVHDL1", value);
                        break;
                    }
                    case VariNameConstant.HDYL: {
                        workDataJson.put("maxVHDP1", value);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        }
    } /**
     * 描述: 查询河东流量相关数据,并组装数据
     *
     * @author: songguang.jiao 创建时间: 2020年5月8日 下午7:17:20 版本: V1.0
     */
    private void queryReferenceDataHd(long startTime, long occurTime, JSONObject workDataJson,
                                      Map<String, EquipmentVariableVO> equipMap, String minOrMax) {
        StringBuilder builder = new StringBuilder();
        builder.append("select var_value from iot_point_data where ");
        builder.append("(var_name='" + VariNameConstant.YL_7_01 + "' or var_name='" + VariNameConstant.HDSC
                + "' or var_name='" + VariNameConstant.YL_2_01 + "' or var_name='" + VariNameConstant.LL_2
                + "' or var_name='" + VariNameConstant.LL_7 + "' or var_name='"+VariNameConstant.HDYL
                +"')");
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
                        workDataJson.put("minV2L", value);
                        break;
                    }
                    case VariNameConstant.LL_7: {
                        workDataJson.put("minV7L", value);
                        break;
                    }
                    case VariNameConstant.YL_2_01: {
                        workDataJson.put("minV2P", value);
                        break;
                    }
                    case VariNameConstant.YL_7_01: {
                        workDataJson.put("minV7P", value);
                        break;
                    }
                    case VariNameConstant.HDSC: {
                        workDataJson.put("minVHDL", value);
                        break;
                    }
                    case VariNameConstant.HDYL:{
                        workDataJson.put("minVHDP", value);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            } else {
                switch (data.getVarName()) {
                    case VariNameConstant.LL_2: {
                        workDataJson.put("maxV2L", value);
                        break;
                    }
                    case VariNameConstant.LL_7: {
                        workDataJson.put("maxV7L", value);
                        break;
                    }
                    case VariNameConstant.YL_2_01: {
                        workDataJson.put("maxV2P", value);
                        break;
                    }
                    case VariNameConstant.YL_7_01: {
                        workDataJson.put("maxV7P", value);
                        break;
                    }
                    case VariNameConstant.HDSC: {
                        workDataJson.put("maxVHDL", value);
                        break;
                    }
                    case VariNameConstant.HDYL:{
                        workDataJson.put("maxVHDP", value);
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
