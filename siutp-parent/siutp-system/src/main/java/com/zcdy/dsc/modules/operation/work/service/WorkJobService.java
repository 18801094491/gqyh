package com.zcdy.dsc.modules.operation.work.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.work.entity.WorkJob;
import com.zcdy.dsc.modules.operation.work.param.WokJobPageParam;
import com.zcdy.dsc.modules.operation.work.vo.WorkJobVo;

/**
 * @description: 工单
 * @author: songgguang.jiao
 * @date:   2020-07-08
 */
public interface WorkJobService extends IService<WorkJob> {

    /**
     * 生成派工单
     * @param planId 计划id
     * @param executeDate 执行时间时间
     */
    void addWorkJob(String planId, String executeDate);

    /**
     * 分页查询工单列表
     * @param page 分页参数
     * @param param 查询参数
     * @return
     */
    IPage<WorkJobVo> selectPageData(Page<WorkJobVo> page, WokJobPageParam param);

}
