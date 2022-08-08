package com.zcdy.dsc.modules.collection.iot.event.listener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zcdy.dsc.common.framework.kafka.entity.ValueEntity;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.modules.collection.iot.constant.VariNameConstant;
import com.zcdy.dsc.modules.collection.iot.constant.WarnClassificationConstant;
import com.zcdy.dsc.modules.collection.iot.entity.VariableInfo;
import com.zcdy.dsc.modules.collection.iot.event.AlarmAutoCloseEvent;
import com.zcdy.dsc.modules.collection.iot.service.IIotVariableInfoService;
import com.zcdy.dsc.modules.collection.iot.service.IotEquipmentService;
import com.zcdy.dsc.modules.configcentre.constant.SystemParamConstant;
import com.zcdy.dsc.modules.configcentre.entity.SystemConfig;
import com.zcdy.dsc.modules.configcentre.service.SystemParamService;
import com.zcdy.dsc.modules.datacenter.statistic.service.SmsUsersService;
import com.zcdy.dsc.modules.message.constant.EventConstant;
import com.zcdy.dsc.modules.message.service.SmsEventService;
import com.zcdy.dsc.modules.message.util.SendSmsUtil;
import com.zcdy.dsc.modules.operation.alarm.entity.BusinessWarn;

/**
 * @author: Roberto 创建时间:2020年4月10日 上午9:52:31 描述:
 *          <p>
 *          事件自动恢复监听器
 *          </p>
 */
@Component
public class DefaultAlarmAutoEventListener implements ApplicationListener<AlarmAutoCloseEvent> {

    private final String iotId_7P = "02bf0fb366ba11eab600685d43cf9012";
    private final String iotId_2P = "02bf0e1066ba11eab600685d43cf9012";

    @Resource
    private SystemParamService systemParamService;

    @Resource
    private SmsUsersService smsUsersService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IIotVariableInfoService iotVariableInfoService;

    @Resource
    private IotEquipmentService iotEquipmentService;

    @Resource
    private SmsEventService smsEventService;

    @Resource
    private SendSmsUtil sendSmsUtil;

    private final boolean PRESS_2TH_ENABLED = false;

    /*
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(AlarmAutoCloseEvent event) {
        // 获取是否出发的开关
        SystemConfig config = this.systemParamService.getConfigByKey(SystemParamConstant.EVENT_ALARM_AUTOCLOSE_SMS);
        boolean isOpen = false;
        if (null == config) {
            isOpen = true;
        } else {
            isOpen = "1".equals(config.getConfigValue());
        }
        if (!isOpen) {
            return;
        }
        // 获取到当前的告警事件信息
        BusinessWarn warn = event.getEvent();

        // 非数据告警不处理
        if (null == warn.getWarnClassification()
            || !WarnClassificationConstant.DATAALARM.equals(warn.getWarnClassification())) {
            return;
        }

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

        // 恢复线数据
        /*
        String minResumeLine = "0.260";
        String valStr = CacheSystemParamBean.get(SystemParamConstant.PRESS_7_RESUME_POINT_VALUE);
        if (!StringUtils.isEmpty(valStr)) {
            minResumeLine = valStr;
        }
        String maxResumeLine = "0.370";
        String minLine = "0.100";
        BigDecimal minResume = new BigDecimal(minResumeLine);
        BigDecimal maxResume = new BigDecimal(maxResumeLine);
        BigDecimal min = new BigDecimal(minLine);
        */
        
        // 把iotid拿到，判断是否是2、7标流量
        String pvalue = "--", p2 = "--", p7 = "--";
        if (iotId_7P.equals(warn.getIotId())) {
            // 获取7标压力
            Set<String> keys7 = new HashSet<>(2);
            String p7RedisKey = String.format(RedisKeyConstantV2.MODEL_PRESS_NORMAL, VariNameConstant.YL_7_01);
            keys7.add(p7RedisKey);
            String press7 = this.stringRedisTemplate.opsForValue().get(p7RedisKey);
            
            p7RedisKey = String.format(RedisKeyConstantV2.MODEL_PRESS_WARN, VariNameConstant.YL_7_01);
            keys7.add(p7RedisKey);
            // 移除正常和异常的数据点
            String warnBottomSmsKey = String.format(RedisKeyConstantV2.ALARM_EVENT_SMS_FLAG, warn.getId());
            keys7.add(warnBottomSmsKey);
            this.stringRedisTemplate.delete(keys7);
            keys7 = null;
            
            if (null == press7) {
                press7 = stringRedisTemplate.opsForValue()
                    .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.YL_7_01));
            }

            if (!StringUtils.isEmpty(press7)) {
                VariableInfo variableInfo = variMap.get(VariNameConstant.YL_7_01);
                ValueEntity valueEntity = JSON.parseObject(press7, ValueEntity.class);
                BigDecimal pressValue = new BigDecimal(valueEntity.getValue());

                // 获取告警值
                /*
                press7 = this.stringRedisTemplate.opsForValue().get(p7RedisKey);
                if (null == press7) {
                    // 没有获取到告警值，此次任务无法判断是从什么值恢复，所以抛弃
                    return;
                } else {
                    // 判断告警值和恢复值得关系，从而做出相应的处理
                    valueEntity = JSON.parseObject(press7, ValueEntity.class);
                    BigDecimal pressWarnValue = new BigDecimal(valueEntity.getValue());
                    // 移除正常和异常的数据点
                    this.stringRedisTemplate.delete(keys7);
                    if (pressWarnValue.compareTo(min) <= 0) {
                        // 小于超下限告警
                        // 不触发短信通知
                    } else if (pressWarnValue.compareTo(minResume) < 0) {
                        // 实际上告警值会小于下告警值，由于一定会小于下恢复值所以用这个值
                        // 此时就是从下线恢复，没什么好说的发短信就好
                    } else if (pressWarnValue.compareTo(maxResume) > 0) {
                        // 同下线恢复
                    }
                }
                */
                pressValue = pressValue.setScale(variableInfo.getScale(), RoundingMode.HALF_UP);
                // 如果从告警到回复，且恢复值在0.255之上，发送短信，否则抛弃
                pvalue = pressValue.toString() + variableInfo.getUnited();
            }

            // 获取2标压力
            String press2 = stringRedisTemplate.opsForValue()
                .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.YL_2_01));
            if (!StringUtils.isEmpty(press2)) {
                VariableInfo variableInfo = variMap.get(VariNameConstant.YL_2_01);
                ValueEntity valueEntity = JSON.parseObject(press2, ValueEntity.class);
                p2 = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(), RoundingMode.HALF_UP)
                    + variableInfo.getUnited();
            }

            p7 = pvalue;
        } else if (iotId_2P.equals(warn.getIotId())) {
            if (!PRESS_2TH_ENABLED) {
                return;
            }
            String p2RedisKey = String.format(RedisKeyConstantV2.MODEL_PRESS_NORMAL, VariNameConstant.YL_2_01);
            String press2 = this.stringRedisTemplate.opsForValue().get(p2RedisKey);
            if (null == press2) {
                // 获取2标压力
                press2 = stringRedisTemplate.opsForValue()
                    .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.YL_2_01));
            }
            if (!StringUtils.isEmpty(press2)) {
                VariableInfo variableInfo = variMap.get(VariNameConstant.YL_2_01);
                ValueEntity valueEntity = JSON.parseObject(press2, ValueEntity.class);
                BigDecimal pressValue =
                    new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(), RoundingMode.HALF_UP);
                // 如果从告警到回复，且恢复值在0.255之上，发送短信，否则抛弃
                pvalue = pressValue.toString() + variableInfo.getUnited();
            }

            // 获取7标压力
            String press7 = stringRedisTemplate.opsForValue()
                .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.YL_7_01));
            if (!StringUtils.isEmpty(press7)) {
                VariableInfo variableInfo = variMap.get(VariNameConstant.YL_7_01);
                ValueEntity valueEntity = JSON.parseObject(press7, ValueEntity.class);
                p7 = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(), RoundingMode.HALF_UP)
                    .toString() + variableInfo.getUnited();
            }

            p2 = pvalue;
        } else {
            return;
        }

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
                .toString() + variableInfo.getUnited();

        }

        // 获取河东水厂流量
        String llhd = "--";
        String hdFlow = stringRedisTemplate.opsForValue()
            .get(String.format(RedisKeyConstantV2.REDISDATAKEY, VariNameConstant.HDSC));
        if (!StringUtils.isEmpty(hdFlow)) {
            VariableInfo variableInfo = variMap.get(VariNameConstant.HDSC);
            ValueEntity valueEntity = JSON.parseObject(hdFlow, ValueEntity.class);
            llhd = new BigDecimal(valueEntity.getValue()).setScale(variableInfo.getScale(), RoundingMode.HALF_UP)
                .toString() + variableInfo.getUnited();
        }

        String section = iotEquipmentService.queryEquipmentById(warn.getIotId());
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 创建短信模板
        JSONObject templateJson = new JSONObject();
        templateJson.put("time", sFormat.format(warn.getWarnTime()));
        templateJson.put("section", section);
        templateJson.put("pvalue", pvalue);
        templateJson.put("LL7", ll7);
        templateJson.put("LL2", ll2);
        templateJson.put("LLHD", llhd);
        templateJson.put("P2", p2);
        templateJson.put("P7", p7);
        sendSmsUtil.sendSms(EventConstant.EVENT_RESUME_SMS, templateJson);
    }
}
