package com.zcdy.dsc.modules.operation.work.service.impl;

import com.zcdy.dsc.modules.operation.work.entity.WorkJobRecordItem;
import com.zcdy.dsc.modules.operation.work.entity.WorkRecordItemInfoEntity;
import com.zcdy.dsc.modules.operation.work.mapper.WorkJobRecordItemMapper;
import com.zcdy.dsc.modules.operation.work.service.WorkJobRecordItemService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @description: 工单录入-子项
 * @author: songguang.jiao
 * @date:   2020-07-16
 */
@Service
public class WorkJobRecordItemServiceImpl extends ServiceImpl<WorkJobRecordItemMapper, WorkJobRecordItem> implements WorkJobRecordItemService {

    @Resource
    private WorkJobRecordItemMapper workJobRecordItemMapper;

    @Override
    public WorkRecordItemInfoEntity getWorkRecordInfoByPointId(String itemId) {
        return workJobRecordItemMapper.getWorkRecordInfoByPointId(itemId);
    }
}
