package com.zcdy.dsc.modules.operation.work.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.constant.WorkingStatus;
import com.zcdy.dsc.modules.operation.work.constant.InspectionPeriodConstant;
import com.zcdy.dsc.modules.operation.work.constant.WorkRecordUserTypeConstant;
import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionPlan;
import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionPlanMember;
import com.zcdy.dsc.modules.operation.work.mapper.WorkInspectionPlanMapper;
import com.zcdy.dsc.modules.operation.work.param.RecordParam;
import com.zcdy.dsc.modules.operation.work.param.WorkInspectionPlanPageParam;
import com.zcdy.dsc.modules.operation.work.param.WorkInspectionPlanParam;
import com.zcdy.dsc.modules.operation.work.service.WorkInspectionPlanMemberService;
import com.zcdy.dsc.modules.operation.work.service.WorkInspectionPlanService;
import com.zcdy.dsc.modules.operation.work.service.WorkJobRecordService;
import com.zcdy.dsc.modules.operation.work.service.WorkJobService;
import com.zcdy.dsc.modules.operation.work.vo.WorkInspectionPlanVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkPlanDropdown;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 巡检计划
 * @author: songguang.jiao
 * @date: 2020-07-06
 */
@Service
public class WorkInspectionPlanServiceImpl extends ServiceImpl<WorkInspectionPlanMapper, WorkInspectionPlan> implements WorkInspectionPlanService {

    @Resource
    private WorkInspectionPlanMapper workInspectionPlanMapper;

    @Resource
    private WorkInspectionPlanMemberService workInspectionPlanMemberService;

    @Resource
    private WorkJobService workJobService;

    @Resource
    private WorkJobRecordService workJobRecordService;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveOne(WorkInspectionPlanParam planParam) {
        //保存计划跟人员
        WorkInspectionPlan newPlan = new WorkInspectionPlan();
        WorkInspectionPlan oldPlan = null;
        BeanUtil.copyProperties(planParam, newPlan);
        if (StringUtils.isEmpty(planParam.getId())) {
            newPlan.setPlanStatus(WorkingStatus.WORKING);
            workInspectionPlanMapper.insert(newPlan);
        } else {
            oldPlan = workInspectionPlanMapper.selectById(planParam.getId());
            workInspectionPlanMapper.updateById(newPlan);
        }
        String[] usersId = planParam.getUsersId().split(",");
        List<WorkInspectionPlanMember> planMembers = new ArrayList<>(usersId.length);
        for (String userId : usersId) {
            WorkInspectionPlanMember workInspectionPlanMember = new WorkInspectionPlanMember();
            workInspectionPlanMember.setPlanId(newPlan.getId());
            workInspectionPlanMember.setUserId(userId);
            planMembers.add(workInspectionPlanMember);
        }
        //先清空再保存
        workInspectionPlanMemberService.remove(Wrappers.<WorkInspectionPlanMember>lambdaQuery().eq(WorkInspectionPlanMember::getPlanId, newPlan.getId()));
        workInspectionPlanMemberService.saveBatch(planMembers);

        //如果巡检周期不是单次的就产生一条派工记录
        int timeParam = 0;
        if (StringUtils.isNotEmpty(planParam.getPeriodExecuteDate())) {
            timeParam = Integer.parseInt(planParam.getPeriodExecuteDate());
        }
        LocalDate now = LocalDate.now();
        LocalDate executeDay = now;
        switch (planParam.getPeriod()) {
            case InspectionPeriodConstant.DAY:
                executeDay = now.plusDays(1);
                break;
            case InspectionPeriodConstant.WEEK:
                //计算执行日期，大于当前日期就执行本周期,否则执行下一周期
                executeDay = now.with(DayOfWeek.of(timeParam));
                if (executeDay.compareTo(now) < 1) {
                    executeDay = executeDay.plusWeeks(1);
                }
                break;
            case InspectionPeriodConstant.MONTH:
                executeDay = now.with(TemporalAdjusters.firstDayOfMonth()).plusDays(timeParam - 1);
                if (executeDay.compareTo(now) < 1) {
                    executeDay = executeDay.plusMonths(1);
                }
                break;
            default:
                break;
        }
        //如果是新增,或者周期修改变动的话就产生派工记录
        if (StringUtils.isEmpty(planParam.getId())) {
            this.saveJobItem(newPlan.getId(), planParam.getStartDate(), planParam.getEndDate(), executeDay);
        } else {
            //判断新周期是否变化
            boolean equalsPeriod = oldPlan.getPeriod().equals(newPlan.getPeriod());
            boolean equalsPeriodDate = oldPlan.getPeriodExecuteDate().equals(newPlan.getPeriodExecuteDate());
            if (!equalsPeriod) {
                this.saveJobItem(newPlan.getId(), planParam.getStartDate(), planParam.getEndDate(), executeDay);
            }
            if (equalsPeriod && !equalsPeriodDate) {
                this.saveJobItem(newPlan.getId(), planParam.getStartDate(), planParam.getEndDate(), executeDay);
            }
        }

        //保存工单
        RecordParam recordParam = new RecordParam();
        recordParam.setPlanId(newPlan.getId());
        recordParam.setTplId(planParam.getTplId());
        recordParam.setUserType(WorkRecordUserTypeConstant.DISPATCH);
        workJobRecordService.saveRecordItems(recordParam);
    }

    @Override
    public IPage<WorkInspectionPlanVo> pageData(Page<WorkInspectionPlanVo> page, WorkInspectionPlanPageParam param) {
        return workInspectionPlanMapper.slectPageData(page, param);
    }

    @Override
    public void editStatus(String id) {
        workInspectionPlanMapper.editStatus(id);
    }

    @Override
    public List<WorkPlanDropdown> dropdown() {
        return workInspectionPlanMapper.dropdown();
    }


    /**
     * 生成计划
     *
     * @param planId      计划id
     * @param startDate   开始日期
     * @param overDate    截至日期
     * @param executeDate 实际执行日期
     */
    private void saveJobItem(String planId, Date startDate, Date overDate, LocalDate executeDate) {
        //执行日期需要在开始跟结束时间区间
        if (executeDate.compareTo(DateUtil.convertDateToLocalDate(startDate).toLocalDate()) <= 0 && executeDate.compareTo(DateUtil.convertDateToLocalDate(overDate).toLocalDate()) >= 0) {
            workJobService.addWorkJob(planId, executeDate.toString());
        }
    }
}
