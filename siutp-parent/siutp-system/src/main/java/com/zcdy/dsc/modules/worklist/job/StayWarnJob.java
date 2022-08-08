package com.zcdy.dsc.modules.worklist.job;

import com.alibaba.druid.support.json.JSONUtils;
import com.zcdy.dsc.common.framework.influxdb.InfluxService;
import com.zcdy.dsc.common.framework.influxdb.entity.WorkListLocationInfluxResult;
import com.zcdy.dsc.common.system.api.ISysBaseApi;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.system.entity.SysUser;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import com.zcdy.dsc.modules.worklist.service.WorkListService;
import com.zcdy.dsc.modules.worklist.utils.MapUtil;
import org.influxdb.dto.Query;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 工单停留告警
 * @Author: 在信汇通
 * @Date:   2021-04-02
 * @Version: V1.0
 */
public class StayWarnJob implements Job
{
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WorkListService workListService;
    @Autowired
    private InfluxService influxService;
    @Autowired
    private ISysBaseApi baseApi;
    /**
     * 定时任务参数
     * time：停留时长，单位秒，必填
     * away：距离，单位米，必填
     * nullWarn：无位置信息是否提醒，1为是，其余值均为否，非必填，默认为null
     * users：要通知的用户username，非id，非必填，默认为admin，多个用户之间用英文逗号隔开
     *
     * 示例如下：
     * {
     *  "time": "1800",
     *  "away": "500",
     *  "nullWarn": "1",
     *  "users": "admin,ldn,lmy"
     * }
     */
    private String parameter;

    /**
     * users默认值
     */
    private static final String USERS_DEFAULT = "admin";

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("工单停留告警定时任务启动");
        String time = null;
        String away = null;
        String nullWarn = null;
        String users = null;
        if(parameter != null) {
            Map<String, String> parMap = (Map<String, String>) JSONUtils.parse(parameter);
            if (parMap != null) {
                time = parMap.get("time");
                away = parMap.get("away");
                nullWarn = parMap.get("nullWarn");
                users = parMap.get("users");
            }
        }
        if(time == null)
        {
            logger.error("配置信息[停留时长]为空，请检查定时任务参数，工单停留告警定时任务未执行");
            return;
        }
        if(away == null)
        {
            logger.error("配置信息[距离]为空，请检查定时任务参数，工单停留告警定时任务未执行");
            return;
        }
        if(users == null)
        {
            //默认值
            users = USERS_DEFAULT;
        }
        //double类型距离
        double awayD = Double.valueOf(away);

        //获取今天进行的工单
        WorkList query = new WorkList();
        query.setDelFlag(DelFlagConstant.NORMAL);
        query.setStartDate(DateUtil.string2Date(DateUtil.date2String(new Date(), DateUtil.dateFormatStr), DateUtil.dateFormatStr));
        query.setOverDate(DateUtil.string2Date(DateUtil.date2String(new Date(), DateUtil.dateFormatStr), DateUtil.dateFormatStr));
        List<WorkList> workListList = workListService.getWorkListAndTeamMember(query);
        if(workListList == null)
        {
            logger.info("未找到今天要执行的工单，工单停留告警定时任务退出");
            return;
        }
        Long timel = Long.valueOf(time);
        Long endTime = System.currentTimeMillis() * 1000000L;
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plusSeconds(0 - timel);
        Long startTime = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() * 1000000L;
        //遍历工单，根据工单中的组员获取位置信息
        for(WorkList workList : workListList)
        {
            List<SysUser> userList = workList.getUserList();
            teamMember:
            for(SysUser user : userList)
            {
                String username = user.getUsername();
                StringBuilder querySql = new StringBuilder();
                querySql.append("select user_name, lat, lon from worklist_location where ");
                querySql.append("user_name = '").append(username).append("' ")
                        .append(" and time >= ")
                        .append(startTime)
                        .append(" and time <= ")
                        .append(endTime);
                Query query1 = new Query(querySql.toString());
                List<WorkListLocationInfluxResult> locationInfluxList = influxService.query(query1, WorkListLocationInfluxResult.class);
                if(locationInfluxList.size() == 0 && "1".equals(nullWarn))
                {
                    //位置为空通知
                    String[] strings = users.split(",");
                    for(String user1 : strings)
                    {
                        baseApi.sendSysAnnouncement("admin", user1, "工单停留告警", "工单：" + workList.getName() + "，班组成员：" + user.getRealname() + "，无位置信息");
                    }
                }
                if(locationInfluxList.size() > 1)
                {
                    //两条以上才能判断是否停留
                    WorkListLocationInfluxResult result0 = locationInfluxList.get(0);
                    String lat0 = result0.getLat();
                    String lon0 = result0.getLon();
                    for(int i = 1; i < locationInfluxList.size(); i++)
                    {
                        WorkListLocationInfluxResult result = locationInfluxList.get(i);
                        String lat = result.getLat();
                        String lon = result.getLon();
                        double distance = MapUtil.getDistance(Double.valueOf(lon0), Double.valueOf(lat0), Double.valueOf(lon), Double.valueOf(lat));
                        if(distance > awayD)
                        {
                            continue teamMember;
                        }
                    }
                    //没有大于规定距离算停留，发出告警
                    String[] strings = users.split(",");
                    for(String user1 : strings)
                    {
                        baseApi.sendSysAnnouncement("admin", user1, "工单停留告警", "工单：" + workList.getName() + "，班组成员：" + user.getRealname() + "，位置没有移动出规定范围");
                    }
                }
            }
        }
    }
}
