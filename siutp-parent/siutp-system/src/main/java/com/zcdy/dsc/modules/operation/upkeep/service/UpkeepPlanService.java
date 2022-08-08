package com.zcdy.dsc.modules.operation.upkeep.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.upkeep.entity.UpkeepPlan;
import com.zcdy.dsc.modules.operation.upkeep.entity.UpkeepPlanRecord;
import com.zcdy.dsc.modules.operation.upkeep.vo.KeepAdvisePageParam;
import com.zcdy.dsc.modules.operation.upkeep.vo.KeepAdviseVo;

/**
 * 描述： 保养计划
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-06-04
 * 版本： V1.0
 */
public interface UpkeepPlanService extends IService<UpkeepPlan> {

    /**
     * 新增维保计划
     * @param one
     * @param record
     */
    void addUpkeepPlan(UpkeepPlan one, UpkeepPlanRecord record);

    /**
     * 修改维保计划
     * @param one
     * @param record
     */
    void updatePlan(UpkeepPlan one, UpkeepPlanRecord record);

    /**
     * 新增保养计划
     * @param days
     * @param usersId
     */
    void changeUserAndTime(String days, String usersId);

    /**
     * 分页查询通知列表
     * @param page
     * @param param
     * @return
     */
    IPage<KeepAdviseVo> advisePageData(Page<KeepAdviseVo> page, KeepAdvisePageParam param);

}
