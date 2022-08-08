package com.zcdy.dsc.modules.operation.equipment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.operation.equipment.entity.KnowlegeItem;
import com.zcdy.dsc.modules.operation.equipment.mapper.KnowlegeAttachMapper;
import com.zcdy.dsc.modules.operation.equipment.mapper.KnowlegeCautionMapper;
import com.zcdy.dsc.modules.operation.equipment.mapper.KnowlegeItemMapper;
import com.zcdy.dsc.modules.operation.equipment.mapper.KnowlegeOperationMapper;
import com.zcdy.dsc.modules.operation.equipment.service.IKnowlegeItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 描述: 知识库条目管理
 * @author： 智能无人硬核心项目组
 * 创建时间：   2020-01-09
 * 版本号: V1.0
 */
@Service
public class KnowlegeItemServiceImpl extends ServiceImpl<KnowlegeItemMapper, KnowlegeItem> implements IKnowlegeItemService {

    @Autowired
    private  KnowlegeItemMapper knowlegeItemMapper;
    @Autowired
    private KnowlegeAttachMapper knowlegeAttachMapper;
    @Autowired
    private KnowlegeCautionMapper knowlegeCautionMapper;
    @Autowired
    private KnowlegeOperationMapper knowlegeOperationMapper;

    //知识库条目管理-通过知识库id删除对应的条目信息
    @Override
    @Transactional(rollbackFor=Exception.class)
    public Boolean removeInfo(String id) {
        //删除
        KnowlegeItem knowlegeItem = new KnowlegeItem();
        knowlegeItem.setId(id);
        knowlegeItem.setDelFlag(1);
        knowlegeItemMapper.updateById(knowlegeItem);
        //删除附件表
         knowlegeAttachMapper.deleteByItemId(id);
         knowlegeCautionMapper.deleteByItemId(id);
         knowlegeOperationMapper.deleteByItemId(id);
        return true;
    }
}
