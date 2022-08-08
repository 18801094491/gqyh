package com.zcdy.dsc.modules.operation.equipment.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.equipment.entity.OptLabel;
import com.zcdy.dsc.modules.operation.equipment.vo.OptLabelTreeVo;

import java.util.List;

/**
 * @Description: 区域配置
 * @Author: 智能无人硬核心项目组
 * @Date:   2020-04-20
 * @Version: V1.0
 */
public interface OptLabelService extends IService<OptLabel> {

    List<OptLabelTreeVo> getTreeByParentId(String id);
    List<OptLabelTreeVo> getTreeById(String id);

    void addOptLabel(OptLabel optLabel);

    void removeOptLabel(String id);

    void updateOptLabel(OptLabel optLabel);

    List<OptLabelTreeVo> searchBy(String keyWord);
}
