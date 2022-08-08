package com.zcdy.dsc.modules.operation.upkeep.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zcdy.dsc.common.system.api.ISysBaseApi;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.modules.configcentre.constant.SystemParamConstant;
import com.zcdy.dsc.modules.configcentre.entity.SystemConfig;
import com.zcdy.dsc.modules.configcentre.service.SystemParamService;
import com.zcdy.dsc.modules.datacenter.statistic.service.SmsUsersService;
import com.zcdy.dsc.modules.operation.equipment.service.IOptEquipmentService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import com.zcdy.dsc.modules.operation.upkeep.entity.UpkeepPlan;
import com.zcdy.dsc.modules.operation.upkeep.service.UpkeepPlanService;
import com.zcdy.dsc.modules.system.entity.SysUser;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 执行查询保养计划，根据计划发送通知
 *
 * @author Roberto
 * @date 2020/06/04
 */
public class CheckUpkeepJob implements Job {

    @Resource
    private UpkeepPlanService upkeepPlanService;

    @Resource
    private SystemParamService systemParamService;

    @Resource
    private ISysBaseApi sysBaseApi;

    @Resource
    private SmsUsersService smsUsersService;

    @Resource
    private IOptEquipmentService optEquipmentService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //查询出设备倒计时时间,计算实际保养日期
        SystemConfig config = systemParamService.getConfigByKey(SystemParamConstant.UPKEEPER_REMIND_DAYS);
        if (null == config) {
            return;
        }
        LocalDate remindDay = LocalDate.now().plusDays(Long.valueOf(config.getConfigValue()));
        List<UpkeepPlan> upkeepPlanList = upkeepPlanService.list(Wrappers.lambdaQuery(new UpkeepPlan()).select(UpkeepPlan::getOptId, UpkeepPlan::getNextPlanDate));
        List<UpkeepPlan> needUpkeep = new ArrayList<>();
        upkeepPlanList.forEach(upkeepPlan -> {
            if (DateUtil.convertDateToLocalDate(upkeepPlan.getNextPlanDate()).toLocalDate().compareTo(remindDay) == 0) {
                needUpkeep.add(upkeepPlan);
            }
        });

        if (needUpkeep.size() < 1) {
            return;
        }
        //查询收件人发送系统消息
        List<SysUser> list = smsUsersService.queryUsersInfo(config.getId());
        StringBuilder stringToUsers = new StringBuilder();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i > 0 && i < list.size()) {
                    stringToUsers.append(",");
                }
                stringToUsers.append(list.get(i).getUsername());
            }
        }
        //发送系统通知
        needUpkeep.forEach(upkeepPlan -> {
            OptEquipmentModel equipmentModel = optEquipmentService.getDetail(upkeepPlan.getOptId());
            StringBuilder msgContent = new StringBuilder();
            msgContent.append("您好,");
            msgContent.append(equipmentModel.getEquipmentSection()).append("[").
                    append(equipmentModel.getEquipmentLocation()).append("][").append(equipmentModel.getEquipmentSn()).append("]");
            msgContent.append("的设备即将到期保养，保养日期为").append(DateUtil.convertDateToLocalDate(upkeepPlan.getNextPlanDate()).toLocalDate())
                    .append(",请及时保养");
            sysBaseApi.sendSysAnnouncement("admin", stringToUsers.toString(), "关于资产设备即将到期保养的通知", msgContent.toString());
        });

    }
}
