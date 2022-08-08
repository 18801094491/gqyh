package com.zcdy.dsc.modules.worklist.job;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.zcdy.dsc.common.framework.redis.RedisService;
import com.zcdy.dsc.common.system.api.ISysBaseApi;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.system.entity.SysUser;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatter;
import com.zcdy.dsc.modules.worklist.service.WorkListService;
import com.zcdy.dsc.modules.worklist.utils.MapUtil;
import com.zcdy.dsc.modules.worklist.vo.WorkListLocationVo;
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
 * @Description: 工单偏离告警
 * @Author: 在信汇通
 * @Date:   2021-04-02
 * @Version: V1.0
 */
public class AwayWarnJob implements Job
{
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WorkListService workListService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ISysBaseApi baseApi;
    /**
     * 定时任务参数
     * away：距离，单位米，必填
     * nullWarn：无位置信息是否提醒，1为是，其余值均为否，非必填，默认为null
     * users：要通知的用户username，非id，非必填，默认为admin，多个用户之间用英文逗号隔开
     *
     * 示例如下：
     * {
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
        logger.info("工单偏离告警定时任务启动");
        String away = null;
        String nullWarn = null;
        String users = null;
        if(parameter != null) {
            Map<String, String> parMap = (Map<String, String>) JSONUtils.parse(parameter);
            if (parMap != null) {
                away = parMap.get("away");
                nullWarn = parMap.get("nullWarn");
                users = parMap.get("users");
            }
        }
        if(away == null)
        {
            logger.error("配置信息[距离]为空，请检查定时任务参数，工单偏离告警定时任务未执行");
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
        List<WorkList> workListList = workListService.getWorkListMatterAndTeamMember(query);
        if(workListList == null)
        {
            logger.info("未找到今天要执行的工单，工单偏离告警定时任务退出");
            return;
        }
        //遍历工单，根据工单中的组员获取位置信息
        for(WorkList workList : workListList)
        {
            List<SysUser> userList = workList.getUserList();
            if(userList == null)
            {
                logger.error("工单[{}]未包含组员明细，跳过执行工单偏离告警任务", workList.getName());
                continue;
            }
            List<WorkListMatter> matterList = workList.getMatterList();
            if(matterList == null)
            {
                logger.error("工单[{}]未包含任务明细，跳过执行工单偏离告警任务", workList.getName());
                continue;
            }
            teamMember:
            for(SysUser user : userList)
            {
                String username = user.getUsername();
                String realname = user.getRealname();
                String locationKeyPrefix = RedisKeyConstantV2.WORKLIST_APP_LOCATION;
                String locationKey = String.format(locationKeyPrefix, username);
                String locationJson = (String) redisService.get(locationKey);
                if(locationJson == null)
                {
                    if("1".equals(nullWarn))
                    {
                        //位置为空通知
                        String[] strings = users.split(",");
                        for(String user1 : strings)
                        {
                            baseApi.sendSysAnnouncement("admin", user1, "工单偏离告警", "工单：" + workList.getName() + "，班组成员：" + realname + "，无位置信息");
                        }
                        continue teamMember;
                    }
                }
                else
                {
                    //有位置信息
                    WorkListLocationVo location = JSON.parseObject(locationJson, WorkListLocationVo.class);
                    location.setRealname(realname);
                    Date locationDateTime = location.getDateTime();
                    if(!DateUtil.isSameDay(locationDateTime, new Date()))
                    {
                        //如果位置信息不是今天的，也视为无位置信息
                        if("1".equals(nullWarn))
                        {
                            //位置为空通知
                            String[] strings = users.split(",");
                            for(String user1 : strings)
                            {
                                baseApi.sendSysAnnouncement("admin", user1, "工单偏离告警", "工单：" + workList.getName() + "，班组成员：" + realname + "，无位置信息");
                            }
                            continue teamMember;
                        }
                    }
                    else
                    {
                        String latitude = location.getLatitude();
                        String longitude = location.getLongitude();
                        for(WorkListMatter matter : matterList)
                        {
                            //遍历各任务点与最后一次位置的距离
                            double distance = MapUtil.getDistance(Double.valueOf(longitude), Double.valueOf(latitude), Double.valueOf(matter.getMatterLongitude()), Double.valueOf(matter.getMatterLatitude()));
                            if(distance < awayD)
                            {
                                //如果与任意任务点距离小于规定，就视为未偏移
                                continue teamMember;
                            }
                        }
                        //如果执行到这，就说明与所有点距离都大于规定，发送通知
                        String[] strings = users.split(",");
                        for(String user1 : strings)
                        {
                            baseApi.sendSysAnnouncement("admin", user1, "工单偏离告警", "工单：" + workList.getName() + "，班组成员：" + user.getRealname() + "，位置偏离规定范围");
                        }
                    }
                }
            }
        }
    }
}
