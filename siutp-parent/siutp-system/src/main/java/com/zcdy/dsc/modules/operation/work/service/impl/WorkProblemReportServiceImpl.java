package com.zcdy.dsc.modules.operation.work.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.operation.alarm.constant.WarnStatusConstant;
import com.zcdy.dsc.modules.operation.work.entity.WorkProblemReport;
import com.zcdy.dsc.modules.operation.work.mapper.WorkProblemReportMapper;
import com.zcdy.dsc.modules.operation.work.param.ProblemReportPageParam;
import com.zcdy.dsc.modules.operation.work.service.WorkProblemReportService;
import com.zcdy.dsc.modules.operation.work.vo.ProblemReportVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Description: 问题上报管理
 * @Author: 智能无人硬核心项目组
 * @Date: 2020-06-04
 * @Version: V1.0
 */
@Service
public class WorkProblemReportServiceImpl extends ServiceImpl<WorkProblemReportMapper, WorkProblemReport> implements WorkProblemReportService {

    @Resource
    private WorkProblemReportMapper workProblemReportMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProblem(WorkProblemReport workProblemReport) {
        //生成最大编号
        String sn = workProblemReportMapper.selectMaxSn();
        if (StringUtils.isEmpty(sn)) {
            sn = "P000001";
        } else {
            sn = "P" + StringUtils.leftPad(String.valueOf(Integer.parseInt(StringUtils.removeStart(sn, "P")) + 1), 6, "0");
        }
        workProblemReport.setProblemSn(sn);
        if (StringUtils.isEmpty(workProblemReport.getId())) {
            workProblemReport.setProblemStatus(WarnStatusConstant.INIT);
            workProblemReportMapper.insert(workProblemReport);
        } else {
            workProblemReportMapper.updateById(workProblemReport);
        }
    }

    @Override
    public IPage<ProblemReportVo> selectPageData(Page<ProblemReportVo> page, ProblemReportPageParam param) {
        return workProblemReportMapper.selectPageData(page, param);
    }


}
