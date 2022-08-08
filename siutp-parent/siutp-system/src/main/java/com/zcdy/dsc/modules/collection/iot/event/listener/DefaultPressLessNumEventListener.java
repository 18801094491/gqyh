package com.zcdy.dsc.modules.collection.iot.event.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.modules.collection.iot.constant.VariNameConstant;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.event.PressLessNumEvent;
import com.zcdy.dsc.modules.collection.iot.service.IIotVariableInfoService;
import com.zcdy.dsc.modules.collection.iot.service.IotEquipmentService;
import com.zcdy.dsc.modules.datacenter.statistic.service.SmsUsersService;
import com.zcdy.dsc.modules.message.constant.EventConstant;
import com.zcdy.dsc.modules.message.util.SendSmsUtil;
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
 * PressLessNumEvent 监听器
 * 
 * @author Roberto
 * @date 2020/07/02
 */
@Component
public class DefaultPressLessNumEventListener implements ApplicationListener<PressLessNumEvent> {

    private final String iotId_7P = "02bf0fb366ba11eab600685d43cf9012";

    @Resource
    private SmsUsersService smsUsersService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IotEquipmentService iotEquipmentService;

    @Resource
    private IIotVariableInfoService iotVariableInfoService;

    @Resource
    private SendSmsUtil sendSmsUtil;

    @Override
    public void onApplicationEvent(PressLessNumEvent event) {

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

        String P2 = "--", P7 = "--";
        String alarmValue = "--";

        VariableInfo variableInfo7 = variMap.get(VariNameConstant.YL_7_01);
        P7 = event.getWarnValue() + variableInfo7.getUnited();
        alarmValue = event.getWarnValue() + variableInfo7.getUnited();
        
        String press2 = stringRedisTemplate.opsForValue()
            .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.YL_2_01));
        if (!StringUtils.isEmpty(press2)) {
            VariableInfo variableInfo = variMap.get(VariNameConstant.YL_2_01);
            ValueEntity valueEntity = JSON.parseObject(press2, ValueEntity.class);
            P2 = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(), RoundingMode.HALF_UP)
                + variableInfo.getUnited();
        }
        
        String LL2 = "--";
        // 从redis获取2标流量
        String flow2 = stringRedisTemplate.opsForValue()
            .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.LL_2));
        if (!StringUtils.isEmpty(flow2)) {
            VariableInfo variableInfo = variMap.get(VariNameConstant.LL_2);
            ValueEntity valueEntity = JSON.parseObject(flow2, ValueEntity.class);
            LL2 = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(), RoundingMode.HALF_UP)
                + variableInfo.getUnited();
        }
        // 获取7标流量
        String LL7 = "--";
        String flow7 = stringRedisTemplate.opsForValue()
            .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.LL_7));
        if (!StringUtils.isEmpty(flow7)) {
            VariableInfo variableInfo = variMap.get(VariNameConstant.LL_7);
            ValueEntity valueEntity = JSON.parseObject(flow7, ValueEntity.class);
            LL7 = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(), RoundingMode.HALF_UP)
                + variableInfo.getUnited();
        }

        // 获取河东水厂流量
        String LLHD = "--";
        String hdFlow = stringRedisTemplate.opsForValue()
            .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.HDSC));
        if (!StringUtils.isEmpty(hdFlow)) {
            VariableInfo variableInfo = variMap.get(VariNameConstant.HDSC);
            ValueEntity valueEntity = JSON.parseObject(hdFlow, ValueEntity.class);
            LLHD = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(), RoundingMode.HALF_UP)
                + variableInfo.getUnited();
        }

        // 通过采集设备id获取设备标段
        String section = iotEquipmentService.queryEquipmentById(iotId_7P);
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 创建短信模板
        JSONObject templateJson = new JSONObject();
        templateJson.put("time", sFormat.format(event.getDate()));
        templateJson.put("section", section);
        templateJson.put("alarmValue", alarmValue);
        templateJson.put("LL7", LL7);
        templateJson.put("LL2", LL2);
        templateJson.put("LLHD", LLHD);
        templateJson.put("P2", P2);
        templateJson.put("P7", P7);

        sendSmsUtil.sendSms(EventConstant.ALARM_CREATE, templateJson);
    }
}
