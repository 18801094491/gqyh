package com.zcdy.dsc.modules.operation.work.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.work.entity.WorkInspectionPoint;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.work.param.WorkInspectionPointPageParam;
import com.zcdy.dsc.modules.operation.work.vo.WorkInspectionPointVo;
import com.zcdy.dsc.modules.operation.work.vo.WorkPointDropdown;

import java.util.List;

/**
 * @description: 巡检点
 * @author: songgguang.jiao
 * @date:   2020-07-01
 */
public interface WorkInspectionPointService extends IService<WorkInspectionPoint> {

    /**
     * 分页查询
     * @param page
     * @param param
     * @return
     */
    IPage<WorkInspectionPointVo> pageData(Page<WorkInspectionPointVo> page, WorkInspectionPointPageParam param);

    /**
     * 根据类别下拉选
     * @param category
     * @param name
     * @return
     */
    List<WorkPointDropdown> categoryDropdown(String category, String name);

    /**
     * 巡检点下拉选
     * @param name
     * @return
     */
    List<WorkPointDropdown> pointDropdown(String name);
}
