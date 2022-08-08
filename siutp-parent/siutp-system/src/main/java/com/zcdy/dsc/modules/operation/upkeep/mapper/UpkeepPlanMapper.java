package com.zcdy.dsc.modules.operation.upkeep.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcdy.dsc.modules.operation.upkeep.entity.UpkeepPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.upkeep.vo.KeepAdvisePageParam;
import com.zcdy.dsc.modules.operation.upkeep.vo.KeepAdviseVo;
import org.apache.ibatis.annotations.Param;

/**
 * 描述： 保养计划
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-06-04
 * 版本： V1.0
 */
public interface UpkeepPlanMapper extends BaseMapper<UpkeepPlan> {

    /**
     * 分页查询通知列表  派单后,或者重新设备保养日期后,其它记录置为无效
     * @param page
     * @param param
     * @return
     */
    IPage<KeepAdviseVo> advisePageData(Page<KeepAdviseVo> page,@Param("param") KeepAdvisePageParam param);
}
