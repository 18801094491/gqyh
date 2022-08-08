package com.zcdy.dsc.modules.worklist.job;

import com.alibaba.druid.support.json.JSONUtils;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.worklist.constant.WorkListConstant;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatter;
import com.zcdy.dsc.modules.worklist.service.WorkListService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 每天定时处理前一天未完成的工单
 * @Author: 在信汇通
 * @Date:   2021-01-29
 * @Version: V1.0
 */
public class WorkListEndJob implements Job
{
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WorkListService workListService;
    /**
     * 定时任务参数
     * updateBy：修改人username
     * {
     *  "updateBy":"inspJob"
     * }
     */
    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
    /**
     * 默认数据updateBy的username
     */
    private static final String UPDATEBY_DEFAULT = "workListJob";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        logger.info("工单定时任务启动");
        String updateBy = UPDATEBY_DEFAULT;
        Map<String, String> parMap = (Map<String, String>) JSONUtils.parse(parameter);
        if(parMap != null)
        {
            String createByPar = parMap.get("updateBy");
            if(createByPar != null)
                updateBy = createByPar;
        }
        WorkList query = new WorkList();
        Date today = new Date();
        String todayStr = DateUtil.date2String(today, DateUtil.dateFormatStr);
        today = DateUtil.string2Date(todayStr, DateUtil.dateFormatStr);
        query.setQueryStatusCode(WorkListConstant.WORK_LIST_STATUS_COMPLETE);
        query.setQueryMatterStatusCode(WorkListConstant.WORK_LIST_MATTER_STATUS_COMPLETE);
        query.setDelFlag(DelFlagConstant.NORMAL);
        query.setOverDate(today);
        List<WorkList> workListList = workListService.getTimeoutAndNoCompleteWorkList(query);
        if(workListList == null || workListList.size() == 0)
        {
            logger.info("未发现有未处理完成的工单，退出执行");
            return;
        }
        for(WorkList workList : workListList)
        {
            List<WorkListMatter> matterList = workList.getMatterList();
            if(matterList == null || matterList.size() == 0)
            {
                logger.error("工单[{}]未发现所属任务，直接将状态改为“未完成”", workList.getId());
                workList.setStatus(WorkListConstant.WORK_LIST_STATUS_INCOMPLETE);
                workList.setUpdateBy(updateBy);
                workList.setUpdateTime(new Date());
                workListService.updateById(workList);
            }
            else
            {
                boolean isAllComplete = true;//是否所有任务全部完成
                for(WorkListMatter matter : matterList)
                {
                    if(!WorkListConstant.WORK_LIST_MATTER_STATUS_COMPLETE.equals(matter.getStatus()))
                    {
                        isAllComplete = false;
                        break;
                    }
                }
                if(!isAllComplete)
                {
                    //没有全部完成
                    logger.info("检测到工单[{}]有未完成任务，开始处理");
                    workList.setUpdateBy(updateBy);
                    workListService.setWorkListAndMatterIncomplete(workList);
                    logger.info("工单[{}]，处理完成", workList.getId());
                }
                else
                {
                    //全部完成
                    if(WorkListConstant.WORK_LIST_STATUS_COMPLETE.equals(workList.getStatus()))
                    {
                        logger.info("检测到工单[{}]任务全部完成，但工单状态非未完成，更新处理", workList.getId());
                        workList.setUpdateBy(updateBy);
                        workList.setUpdateTime(new Date());
                        workListService.setWorkListComplete(workList);
                    }
                }
            }
        }
        logger.info("工单定时任务执行完毕");
    }
}
