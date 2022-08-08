package com.zcdy.dsc.modules.operation.upkeep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.util.UUIDGenerator;
import com.zcdy.dsc.constant.StatusConstant;
import com.zcdy.dsc.modules.configcentre.constant.SystemParamConstant;
import com.zcdy.dsc.modules.configcentre.entity.SystemConfig;
import com.zcdy.dsc.modules.configcentre.service.SystemParamService;
import com.zcdy.dsc.modules.datacenter.statistic.entity.SmsUsers;
import com.zcdy.dsc.modules.datacenter.statistic.mapper.SmsUsersMapper;
import com.zcdy.dsc.modules.operation.upkeep.entity.UpkeepPlan;
import com.zcdy.dsc.modules.operation.upkeep.entity.UpkeepPlanRecord;
import com.zcdy.dsc.modules.operation.upkeep.mapper.UpkeepPlanMapper;
import com.zcdy.dsc.modules.operation.upkeep.mapper.UpkeepPlanRecordMapper;
import com.zcdy.dsc.modules.operation.upkeep.service.UpkeepPlanService;
import com.zcdy.dsc.modules.operation.upkeep.vo.KeepAdvisePageParam;
import com.zcdy.dsc.modules.operation.upkeep.vo.KeepAdviseVo;
import com.zcdy.dsc.modules.operation.work.constant.DispathchStatusConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述： 保养计划 @author： 智能无人硬核心项目组 创建时间： 2020-06-04 版本： V1.0
 */
@Service
public class UpkeepPlanServiceImpl extends ServiceImpl<UpkeepPlanMapper, UpkeepPlan> implements UpkeepPlanService {

    @Resource
    private UpkeepPlanMapper upkeepPlanMapper;

    @Resource
    private UpkeepPlanRecordMapper upkeepPlanRecordMapper;

    @Resource
    private SystemParamService systemParamService;

    @Resource
    private SmsUsersMapper smsUsersMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUpkeepPlan(UpkeepPlan one, UpkeepPlanRecord record) {
        this.upkeepPlanMapper.insert(one);
        record.setPlanId(one.getId());
        //默认状态新增记录状态为当前状态,并且为未派工
        record.setCurrent(StatusConstant.VALID);
        record.setDispatchStatus(DispathchStatusConstant.INIT);
        this.upkeepPlanRecordMapper.insert(record);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePlan(UpkeepPlan one, UpkeepPlanRecord record) {
        this.upkeepPlanMapper.updateById(one);
        upkeepPlanRecordMapper.update(new UpkeepPlanRecord().setCurrent(StatusConstant.INVALID), new LambdaUpdateWrapper<UpkeepPlanRecord>().eq(UpkeepPlanRecord::getPlanId, record.getPlanId()));
        record.setCurrent(StatusConstant.VALID);
        record.setDispatchStatus(DispathchStatusConstant.INIT);
        this.upkeepPlanRecordMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeUserAndTime(String days, String usersId) {
        SystemConfig config = systemParamService.getConfigByKey(SystemParamConstant.UPKEEPER_REMIND_DAYS);
        if (config != null) {
            config.setConfigValue(days);
        }
        systemParamService.updateById(config);
        //设置收件人，删除原有关联人,然后插入
        if (!StringUtils.isEmpty(usersId)) {
            QueryWrapper<SmsUsers> queryWrapper = new QueryWrapper<>();
            //设备模块id为配置主键id
            queryWrapper.lambda().eq(SmsUsers::getModuleId, config.getId());
            smsUsersMapper.delete(queryWrapper);
            String[] userIds = StringUtils.split(usersId, ",");
            List<SmsUsers> userList = new ArrayList<SmsUsers>();
            for (String userId : userIds) {
                SmsUsers smsUsers = new SmsUsers();
                smsUsers.setModuleId(config.getId());
                smsUsers.setUserId(userId);
                smsUsers.setId(UUIDGenerator.generate());
                userList.add(smsUsers);
            }
            if (userList.size() > 0 && !userList.isEmpty()) {
                smsUsersMapper.insertBatchUsers(userList);
            }
        }
    }

    @Override
    public IPage<KeepAdviseVo> advisePageData(Page<KeepAdviseVo> page, KeepAdvisePageParam param) {
        return upkeepPlanMapper.advisePageData(page, param);
    }

}
