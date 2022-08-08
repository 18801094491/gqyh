package com.zcdy.dsc.modules.inspection.job;

import com.alibaba.druid.support.json.JSONUtils;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.system.api.ISysBaseApi;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.modules.inspection.constant.InspConstant;
import com.zcdy.dsc.modules.inspection.entity.InspPlan;
import com.zcdy.dsc.modules.inspection.entity.InspPoint;
import com.zcdy.dsc.modules.inspection.service.InspPlanService;
import com.zcdy.dsc.modules.operation.alarm.constant.WarnLevelConstant;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 每天定时生成巡检工单
 * @Author: 在信汇通
 * @Date:   2021-01-22
 * @Version: V1.0
 */
public class InspJob implements Job {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 创建几天之内的巡检工单，默认值
     */
    private static final Integer WITHIN_DEFAULT = 3;
    /**
     * 每个工单持续几天，默认值
     */
    private static final Integer CONTINUED_DEFAULT = 1;
    /**
     * 默认数据createby的username
     */
    private static final String CREATEBY_DEFAULT = "inspJob";

    @Autowired
    private InspPlanService inspPlanService;

    @Autowired
    private WorkListService workListService;

    @Autowired
    private ISysBaseApi baseApi;

    /**
     * 定时任务参数
     * within:创建几天之内的巡检工单
     * continued：每个工单持续几天，1为持续1天（当天完成）
     * isSendMsg：是否给班组管理员发送创建通知，1是，其他值均为否
     * createBy：创建人username
     * {
     *  "within":3,
     *  "continued":1,
     *  "isSendMsg":1,
     *  "createBy":"inspJob"
     * }
     */
    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        logger.info("巡检计划定时任务启动");
        Integer within = this.WITHIN_DEFAULT;
        Integer continued = this.CONTINUED_DEFAULT;
        String isSendMsg = null;
        String createBy = null;
        if(parameter != null) {
            Map<String, String> parMap = (Map<String, String>) JSONUtils.parse(parameter);
            if (parMap != null) {
                String withinStr = parMap.get("within");
                String continuedStr = parMap.get("continued");
                isSendMsg = parMap.get("isSendMsg");
                createBy = parMap.get("createBy");
                if (withinStr != null)
                    within = Integer.parseInt(withinStr);

                if (continuedStr != null)
                    continued = Integer.parseInt(continuedStr);

                if (createBy == null)
                    createBy = CREATEBY_DEFAULT;
            }
        }
        int result = 0;
        String todayStr = DateUtil.date2String(new Date(), DateUtil.dateFormatStr);
        Date today = DateUtil.string2Date(todayStr, DateUtil.dateFormatStr);
        InspPlan queryPlan = new InspPlan();
        queryPlan.setDelFlag(DelFlagConstant.NORMAL);
        queryPlan.setQueryStatusCode(InspConstant.INSP_PLAN_STATUS_ENABLE);
        queryPlan.setStartDate(today);
        queryPlan.setQueryTypeCode(WorkListConstant.WORK_LIST_TYPE_INSP);
        List<InspPlan> planList = inspPlanService.getPlanAndWorkList(queryPlan);
        for(InspPlan plan : planList)
        {
            Date endDay = DateUtil.addDay(today, within);
            Date overDate = plan.getOverDate();
            //如果巡检计划有效期在计划任务within之内（overDate < endDay），则以overDate为期限创建巡检工单
            if(overDate.before(endDay))
            {
                endDay = overDate;
            }
            String frequencyType = plan.getFrequencyType();
            String frequencyDesc = plan.getFrequencyDesc();
            List<Date> dateList = new ArrayList<>();

            if(InspConstant.INSP_PLAN_FREQUENCY_TYPE_DAY.equals(frequencyType))//每天
            {
                List<Date> daylist = DateUtil.getEverDayBetweenDate(today, endDay);
                if(daylist != null)
                    dateList = daylist;
            }
            else if(InspConstant.INSP_PLAN_FREQUENCY_TYPE_WEEK.equals(frequencyType))//每周
            {
                if(frequencyDesc == null)
                {
                    logger.error("巡检计划[{}]的频次描述为空，跳过执行", plan.getName());
                    continue;
                }
                String[] descs = frequencyDesc.split(",");
                for(String s : descs)
                {
                    int week = Integer.parseInt(s);
                    List<Date> weekDayList = DateUtil.getWeekDaysBetweenDate(week, today, endDay);
                    if(weekDayList != null)
                        dateList.addAll(weekDayList);
                }
            }
            else if(InspConstant.INSP_PLAN_FREQUENCY_TYPE_MONTH.equals(frequencyType))//每月
            {
                if(frequencyDesc == null)
                {
                    logger.error("巡检计划[{}]的频次描述为空，跳过执行", plan.getName());
                    continue;
                }
                String[] descs = frequencyDesc.split(",");
                for(String s : descs)
                {
                    int month = Integer.parseInt(s);
                    List<Date> monthDayList = DateUtil.getMonthDaysBetweenDate(month, today, endDay);
                    if(monthDayList != null)
                        dateList.addAll(monthDayList);
                }
            }
            dateList = new ArrayList<>(new HashSet<>(dateList));//去重
            List<WorkList> workListList = plan.getWorkListList();//获取与之对应的，已经创建的巡检工单
            for(WorkList workList : workListList)
            {
                Date startDate = workList.getStartDate();
                dateList.remove(startDate);//去除已经存在的日期
            }
            if(dateList.size() == 0)
            {
                continue;
            }
            //按时间排序
            dateList = dateList.stream().sorted(Comparator.comparing(Date::getTime)).collect(Collectors.toList());
            //如果还有未创建的巡检工单则创建
            for(Date planStartdate : dateList)
            {
                WorkList workList = new WorkList();
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                workList.setId(uuid);//id
                workList.setName(plan.getName() + "-" + DateUtil.date2String(planStartdate, DateUtil.dateFormatStr));//名称
                workList.setCode(plan.getCode() + "-" + DateUtil.date2String(planStartdate, DateUtil.dateFormatStr));//编码
                workList.setType(WorkListConstant.WORK_LIST_TYPE_INSP);//类型：巡检工单
                workList.setAreaId(plan.getAreaId());//区域
                workList.setRouteId(plan.getRouteId());//路线
                workList.setPlanId(plan.getId());//对应巡检计划
                workList.setTeamId(plan.getTeamId());//班组
                workList.setTeamLeaderId(plan.getTeamLeaderId());//班组管理员
                workList.setStatus(WorkListConstant.WORK_LIST_STATUS_ALLOT);//状态：已分配
                workList.setStartDate(planStartdate);//开始执行日期
                workList.setOverDate(DateUtil.addDay(planStartdate, continued - 1));//结束日期
                workList.setCreateBy(createBy);//创建人
                workList.setCreateTime(new Date());//创建时间
                List<WorkListMatter> matterList = new ArrayList<>();
                List<InspPoint> pointList = plan.getPointList();
                if(pointList == null)
                {
                    logger.error("巡检计划[{}]中未包含任何巡检点，跳过执行", plan.getName());
                    continue;
                }
                for(InspPoint point : pointList)
                {
                    WorkListMatter matter = new WorkListMatter();
                    String id = UUID.randomUUID().toString().replaceAll("-", "");
                    matter.setId(id);
                    matter.setListId(uuid);
                    matter.setTitle(point.getName());
                    matter.setDescription(point.getNotices());
                    matter.setSeq(point.getSeq());
                    matter.setType(WorkListConstant.WORK_LIST_TYPE_INSP);
                    matter.setMatterLatitude(point.getLatitude());
                    matter.setMatterLongitude(point.getLongitude());
                    matter.setEquipmentId(point.getEquipmentId());
                    matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);
                    matter.setSubId(createBy);
                    matter.setSubTime(new Date());
                    matter.setCreateBy(createBy);
                    matter.setCreateTime(new Date());
                    matterList.add(matter);
                }
                workList.setMatterList(matterList);
                ResultData resultData = workListService.addInspWorkList(workList);
                if(resultData.getCode() == CommonConstant.SC_OK_200)
                {
                    result ++;
                }
                else
                {
                    logger.error("巡检工单[{}]创建失败", workList.getId());
                    continue;
                }

                if("1".equals(isSendMsg))
                {
                    //发送站内信
                    baseApi.sendSysAnnouncement(createBy, plan.getTeamLeaderName(), "巡检工单已分派", workList.getName());
                }
            } //end for(Date planStartdate : dateList)
        } //end for(InspPlan plan : planList)

        logger.info("巡检计划定时任务执行完成，共创建任务{}个", result);
    }
}
