package com.zcdy.dsc.modules.operation.equipment.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.operation.equipment.entity.OptGoodsApplyItem;
import com.zcdy.dsc.modules.operation.equipment.mapper.OptGoodsApplyItemMapper;
import com.zcdy.dsc.modules.operation.equipment.service.OptGoodsApplyItemService;
import com.zcdy.dsc.modules.operation.equipment.vo.OptApplyItemVo;

import java.util.List;

/**
 * 描述: 货物申请项详情
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-02-06
 * 版本号: V1.0
 */
@Service
public class OptGoodsApplyItemServiceImpl extends ServiceImpl<OptGoodsApplyItemMapper, OptGoodsApplyItem> implements OptGoodsApplyItemService {

    @Override
    public int insertInfo(List<OptApplyItemVo> applyItem) {
        return 0;
    }
}
