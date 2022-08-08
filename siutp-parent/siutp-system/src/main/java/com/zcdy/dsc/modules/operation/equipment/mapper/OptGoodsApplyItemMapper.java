package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.equipment.entity.OptGoodsApplyItem;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述: 货物申请项详情
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-06
 * 版本号: V1.0
 */
public interface OptGoodsApplyItemMapper extends BaseMapper<OptGoodsApplyItem> {

    int insertInfo(List<OptGoodsApplyItem> applyItem);

    void deleteByApplyId(@Param("applyId") String applyId);
}
