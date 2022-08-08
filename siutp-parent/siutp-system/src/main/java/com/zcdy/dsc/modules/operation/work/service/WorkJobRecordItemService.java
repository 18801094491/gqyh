package com.zcdy.dsc.modules.operation.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobRecordItem;
import com.zcdy.dsc.modules.operation.work.entity.WorkRecordItemInfoEntity;

/**
 * @description: 工单录入-子项
 * @author: songgguang.jiao
 * @date:   2020-07-16
 */
public interface WorkJobRecordItemService extends IService<WorkJobRecordItem> {


    /**
     * 通过巡检点查询 相关信息
     * @param itemId 巡检点id
     * @return
     */
    WorkRecordItemInfoEntity getWorkRecordInfoByPointId(String itemId);
}
