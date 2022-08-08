package com.zcdy.dsc.modules.collection.iot.event.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.modules.collection.iot.constant.VariNameConstant;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.event.DataAlarmEvent;
import com.zcdy.dsc.modules.collection.iot.service.IIotVariableInfoService;
import com.zcdy.dsc.modules.collection.iot.service.IotEquipmentService;
import com.zcdy.dsc.modules.datacenter.statistic.service.SmsUsersService;
import com.zcdy.dsc.modules.message.constant.EventConstant;
import com.zcdy.dsc.modules.message.service.SmsEventService;
import com.zcdy.dsc.modules.message.util.SendSmsUtil;
import com.zcdy.dsc.modules.operation.alarm.entity.BusinessWarn;
import com.zcdy.dsc.modules.operation.alarm.service.BusinessWarnSmsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 描述: 监听发送短信模板 2标南端压力报警0.123pa (参考值：0.250至0.330) 报警时间参考数据 7标瞬时压力： 7标瞬时流量： 2标瞬时压力： 2标瞬时流量： 河东瞬时流量： 建议电脑登录智慧平台查看数据核实。
 * 
 * @author： songguang.jiao 创建时间： 2020年4月8日 下午5:03:35 版本号: V1.0
 */
@Component
public class DefaultDataAlarmEventListener implements ApplicationListener<DataAlarmEvent> {

    private final String iotId_7P = "02bf0fb366ba11eab600685d43cf9012";

    private final String iotId_2P = "02bf0e1066ba11eab600685d43cf9012";

    @Resource
    private SmsUsersService smsUsersService;

    @Resource
    private BusinessWarnSmsService businessWarnSmsService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IotEquipmentService iotEquipmentService;

    @Resource
    private IIotVariableInfoService iotVariableInfoService;

    @Resource
    private SmsEventService smsEventService;

    @Resource
    private SendSmsUtil sendSmsUtil;

    private final boolean PRESS_2TH_ENABLED = false;

    @Override
    public void onApplicationEvent(DataAlarmEvent event) {

        // 拿到告警事件信息
        BusinessWarn warn = event.getEvent();

        boolean isDeal = iotId_7P.equals(warn.getIotId());
        if (!isDeal) {
            if (PRESS_2TH_ENABLED) {
                isDeal = iotId_2P.equals(warn.getIotId());
            }
        }else {
            //如果压力小于底线，那么设置小于底线的标识避免清洗数据的过程再次发送短信
            //在采集任务中处理，在这里处理会有延迟
            /*
            String varName = "ZHGL_7_SLL_0010_P";
            String timeKey = String.format(RedisKeyConstantV2.MODEL_VARIABLE_WARN_TIME, varName);
            long listSize = this.stringRedisTemplate.opsForList().size(timeKey);
            if (listSize < 1) {
                // 此时无法判断设备的压力趋势
            } else {
                List<String> tempPressList = this.stringRedisTemplate.opsForList().range(timeKey, 0, 0);
                String tempPress = tempPressList.get(0);
                BigDecimal prevPress = new BigDecimal(tempPress);
                if(prevPress.compareTo(new BigDecimal("0.100"))<=0) {
                    String warnBottomSmsKey = String.format(RedisKeyConstantV2.ALARM_EVENT_SMS_FLAG, warn.getId());
                    if(!this.stringRedisTemplate.hasKey(warnBottomSmsKey)) {
                      //设置已发送短信标识，避免再次发送
                        this.stringRedisTemplate.opsForValue().set(warnBottomSmsKey, "1");
                    }
                }
            }
            */
        }

        // 把iotid拿到，判断是否是2、7标流量
        if (isDeal) {

            // 查询精度,单位
            List<String> varNames = new ArrayList<>(5);
            varNames.add(VariNameConstant.LL_2);
            varNames.add(VariNameConstant.LL_7);
            varNames.add(VariNameConstant.YL_2_01);
            varNames.add(VariNameConstant.YL_7_01);
            varNames.add(VariNameConstant.HDSC);
            List<VariableInfo> variableInfos = this.iotVariableInfoService.getVariinfos(varNames);
            Map<String, VariableInfo> variMap = new HashMap<String, VariableInfo>(variableInfos.size());
            variableInfos.forEach(item -> {
                variMap.put(item.getVarName(), item);
            });
            variableInfos = null;
            String ll2 = "--";
            // 从redis获取2标流量
            String flow2 = stringRedisTemplate.opsForValue()
                .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.LL_2));
            if (!StringUtils.isEmpty(flow2)) {
                VariableInfo variableInfo = variMap.get(VariNameConstant.LL_2);
                ValueEntity valueEntity = JSON.parseObject(flow2, ValueEntity.class);
                ll2 = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(), RoundingMode.HALF_UP)
                    + variableInfo.getUnited();
            }
            // 获取7标流量
            String ll7 = "--";
            String flow7 = stringRedisTemplate.opsForValue()
                .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.LL_7));
            if (!StringUtils.isEmpty(flow7)) {
                VariableInfo variableInfo = variMap.get(VariNameConstant.LL_7);
                ValueEntity valueEntity = JSON.parseObject(flow7, ValueEntity.class);
                ll7 = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(), RoundingMode.HALF_UP)
                    + variableInfo.getUnited();
            }

            // 获取2标压力
            String p2 = "--", p7 = "--";
            String alarmValue = "--";

            if (PRESS_2TH_ENABLED) {
                if (iotId_2P.equals(warn.getIotId())) {
                    String p2RedisKey = String.format(RedisKeyConstantV2.MODEL_PRESS_WARN, VariNameConstant.YL_2_01);
                    String press2 = stringRedisTemplate.opsForValue().get(String.format(p2RedisKey));
                    if (null == press2) {
                        press2 = stringRedisTemplate.opsForValue()
                            .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.YL_2_01));
                    }
                    if (!StringUtils.isEmpty(press2)) {
                        VariableInfo variableInfo = variMap.get(VariNameConstant.YL_2_01);
                        ValueEntity valueEntity = JSON.parseObject(press2, ValueEntity.class);
                        p2 = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(),
                            RoundingMode.HALF_UP) + variableInfo.getUnited();
                        alarmValue = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(),
                            RoundingMode.HALF_UP) + variableInfo.getUnited();
                    }
                    String press7 = stringRedisTemplate.opsForValue()
                        .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.YL_7_01));
                    if (!StringUtils.isEmpty(press7)) {
                        VariableInfo variableInfo = variMap.get(VariNameConstant.YL_7_01);
                        ValueEntity valueEntity = JSON.parseObject(press7, ValueEntity.class);
                        p7 = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(),
                            RoundingMode.HALF_UP) + variableInfo.getUnited();
                    }
                }
            }

            // 获取7标压力
            if (iotId_7P.equals(warn.getIotId())) {
                String p7RedisKey = String.format(RedisKeyConstantV2.MODEL_PRESS_WARN, VariNameConstant.YL_7_01);
                String press7 = stringRedisTemplate.opsForValue().get(p7RedisKey);
                if (null == press7) {
                    press7 = stringRedisTemplate.opsForValue()
                        .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.YL_7_01));
                }
                if (!StringUtils.isEmpty(press7)) {
                    VariableInfo variableInfo = variMap.get(VariNameConstant.YL_7_01);
                    ValueEntity valueEntity = JSON.parseObject(press7, ValueEntity.class);
                    p7 = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(), RoundingMode.HALF_UP)
                        + variableInfo.getUnited();
                    alarmValue =
                        new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(), RoundingMode.HALF_UP)
                            + variableInfo.getUnited();
                }
                String press2 = stringRedisTemplate.opsForValue()
                    .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.YL_2_01));
                if (!StringUtils.isEmpty(press2)) {
                    VariableInfo variableInfo = variMap.get(VariNameConstant.YL_2_01);
                    ValueEntity valueEntity = JSON.parseObject(press2, ValueEntity.class);
                    p2 = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(), RoundingMode.HALF_UP)
                        + variableInfo.getUnited();
                }
            }

            // 获取河东水厂流量
            String llhd = "--";
            String hdFlow = stringRedisTemplate.opsForValue()
                .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.HDSC));
            if (!StringUtils.isEmpty(hdFlow)) {
                VariableInfo variableInfo = variMap.get(VariNameConstant.HDSC);
                ValueEntity valueEntity = JSON.parseObject(hdFlow, ValueEntity.class);
                llhd = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(), RoundingMode.HALF_UP)
                    + variableInfo.getUnited();
            }

            // 通过采集设备id获取设备标段
            String section = iotEquipmentService.queryEquipmentById(warn.getIotId());
            SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 创建短信模板
            JSONObject templateJson = new JSONObject();
            templateJson.put("time", sFormat.format(warn.getWarnTime()));
            templateJson.put("section", section);
            templateJson.put("alarmValue", alarmValue);
            templateJson.put("LL7", ll7);
            templateJson.put("LL2", ll2);
            templateJson.put("LLHD", llhd);
            templateJson.put("P2", p2);
            templateJson.put("P7", p7);

            sendSmsUtil.sendSms(EventConstant.ALARM_CREATE, templateJson);
        }

    }
}
