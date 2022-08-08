package com.zcdy.dsc.modules.operation.work.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.operation.alarm.mapper.BusinessDao;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptEquipmentMapper;
import com.zcdy.dsc.modules.operation.work.constant.WorkPointCategoryConstant;
import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionPoint;
import com.zcdy.dsc.modules.operation.work.mapper.WorkInspectionPointMapper;
import com.zcdy.dsc.modules.operation.work.mapper.WorkProblemReportMapper;
import com.zcdy.dsc.modules.operation.work.param.WorkInspectionPointPageParam;
import com.zcdy.dsc.modules.operation.work.service.WorkInspectionPointService;
import com.zcdy.dsc.modules.operation.work.vo.WorkInspectionPointVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkPointDropdown;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 巡检点
 * @author: songguang.jiao
 * @date: 2020-07-01
 */
@Service
public class WorkInspectionPointServiceImpl extends ServiceImpl<WorkInspectionPointMapper, WorkInspectionPoint> implements WorkInspectionPointService {

    @Resource
    private WorkInspectionPointMapper workInspectionPointMapper;

    @Resource
    private OptEquipmentMapper optEquipmentMapper;

    @Resource
    private BusinessDao businessDao;

    @Resource
    private WorkProblemReportMapper workProblemReportMapper;

    @Override
    public IPage<WorkInspectionPointVo> pageData(Page<WorkInspectionPointVo> page, WorkInspectionPointPageParam param) {
        return workInspectionPointMapper.selectPageData(page, param);
    }

    @Override
    public List<WorkPointDropdown> categoryDropdown(String category, String name) {
        List<WorkPointDropdown> list=new ArrayList<>();
        switch (category) {
            case WorkPointCategoryConstant
                    .EUIP:
                list =optEquipmentMapper.equipLocationDropdown(name);
                break;
            case WorkPointCategoryConstant.WARN:
                list=businessDao.undealDropdown(name);
                break;
            case WorkPointCategoryConstant.PROBLEM:
                list=workProblemReportMapper.problemDropdown(name);
            default:
                break;
        }
        return list;
    }

    @Override
    public List<WorkPointDropdown> pointDropdown(String name) {
        return workInspectionPointMapper.pointDropdown(name);
    }
}
