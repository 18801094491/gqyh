package com.zcdy.dsc.modules.operation.upkeep.mapper;

import com.zcdy.dsc.modules.operation.upkeep.entity.UpkeepPlanRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述： 保养计划
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-06-04
 * 版本： V1.0
 */
public interface UpkeepPlanRecordMapper extends BaseMapper<UpkeepPlanRecord> {

    /**
     * 通过设备id更新更新保养计划 改为派工
     * @param list
     */
    void updateByEquipmentId(@Param("list") List<String> list);

}
