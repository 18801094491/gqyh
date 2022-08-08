package com.zcdy.dsc.modules.settle.job;

import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.modules.settle.constant.FlowModuleConstant;
import com.zcdy.dsc.modules.settle.entity.FlowDayRecord;
import com.zcdy.dsc.modules.settle.logic.FlowCountYesterDayLogic;
import com.zcdy.dsc.modules.settle.service.FlowDayRecordService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 监控模块用水量记录(昨日数据) 每日00：30执行
 * 
 * @author songguang.jiao
 * @date 2020/05/11 10:11:24
 */
public class FlowDayRecordJob implements Job {

    @Resource
    private FlowDayRecordService flowDayRecordService;

    @Resource
    private FlowCountYesterDayLogic flowCountYesterDayLogic;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<FlowDayRecord> entityList = new ArrayList<FlowDayRecord>();
        Date countDate = DateUtil.convertLocalDateToDate(LocalDateTime.now().minusDays(1));
        String flowHd = flowCountYesterDayLogic.getFlowHd();
        this.addRecord(entityList, flowHd, FlowModuleConstant.FLOW_HD, countDate);
        String flowXzq = flowCountYesterDayLogic.getFlowXzq();
        this.addRecord(entityList, flowXzq, FlowModuleConstant.FLOW_XZQ, countDate);
        String flowXzrl = flowCountYesterDayLogic.getFlowXzrl();
        this.addRecord(entityList, flowXzrl, FlowModuleConstant.FLOW_XZRL, countDate);
        String flowXzlh = flowCountYesterDayLogic.getFlowXzlh();
        this.addRecord(entityList, flowXzlh, FlowModuleConstant.FLOW_XZLH, countDate);
        String flowFzg = flowCountYesterDayLogic.getFlowFzg();
        this.addRecord(entityList, flowFzg, FlowModuleConstant.FLOW_FZG, countDate);
        String flowHdbr = flowCountYesterDayLogic.getFlowHdbr();
        this.addRecord(entityList, flowHdbr, FlowModuleConstant.FLOW_HDBR, countDate);
        flowDayRecordService.saveBatch(entityList);
    }

    /**
     * 增加存入模块
     * 
     * @param entityList
     * @param countValue
     * @param countCode
     * @param countDate
     */
    private void addRecord(List<FlowDayRecord> entityList, String countValue, String countCode, Date countDate) {
        FlowDayRecord record = new FlowDayRecord();
        record.setCountCode(countCode);
        record.setCountDate(countDate);
        record.setCountValue(countValue);
        entityList.add(record);
    }

}
