package com.zcdy.dsc.modules.operation.equipment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.equipment.entity.KnowlegeItem;

/**
 * 描述: 知识库条目管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
public interface IKnowlegeItemService extends IService<KnowlegeItem> {
    //知识库条目管理-通过知识库id删除对应的条目信息
    Boolean removeInfo(String id);
}
