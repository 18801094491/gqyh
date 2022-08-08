package com.zcdy.dsc.modules.operation.equipment.job;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSONObject;
import com.zcdy.dsc.modules.message.constant.EventConstant;
import com.zcdy.dsc.modules.message.util.SendSmsUtil;
import com.zcdy.dsc.modules.settle.logic.FlowCountYesterDayLogic;

/**
 * 描述: 每日早9:00发送昨日流量统计情况
 * 
 * 短信模板SMS_188992113 (2020-04-23 09:00:00)(昨日流量统计情况如下：) 昨日流量统计： 河东供水：12080.0 行政区：3764.0 镜河补水：0.0 华电北燃：3130.0
 * 
 * @author songguang.jiao
 * @date 2020/05/11 10:30:33
 */
public class FlowCoutYesterdayJob implements Job {

    @Resource
    private SendSmsUtil sendSmsUtil;

    @Resource
    private FlowCountYesterDayLogic flowCountYesterDayLogic;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now().plusDays(-1).format(DateTimeFormatter.ofPattern("M月d日")));
        jsonObject.put("flowXZQ", flowCountYesterDayLogic.getFlowXzq());
        jsonObject.put("flowHD", flowCountYesterDayLogic.getFlowHd());
        jsonObject.put("flowHDBR", flowCountYesterDayLogic.getFlowHdbr());
        jsonObject.put("flowXZRL", flowCountYesterDayLogic.getFlowXzrl());
        jsonObject.put("flowXZLH", flowCountYesterDayLogic.getFlowXzlh());
        sendSmsUtil.sendSms(EventConstant.YESTODAY_FLOW_COUNT, jsonObject);
    }

}
