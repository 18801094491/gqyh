package com.zcdy.dsc.modules.operation.equipment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.equipment.entity.KnowlegeItem;

import java.util.LinkedList;

/**
 * 描述: 知识库条目管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
public interface KnowlegeItemMapper extends BaseMapper<KnowlegeItem> {

    int addKnowlegeItemInfo(LinkedList<KnowlegeItem> knowlegeItemList);
}
