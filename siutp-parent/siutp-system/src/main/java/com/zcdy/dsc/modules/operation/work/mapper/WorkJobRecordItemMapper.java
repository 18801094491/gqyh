package com.zcdy.dsc.modules.operation.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcdy.dsc.modules.operation.work.entity.WorkJobRecordItem;
import com.zcdy.dsc.modules.operation.work.entity.WorkRecordItemInfoEntity;

/**
 * @description: 工单录入-子项
 * @author: songguang.jiao
 * @date:   2020-07-16
 * @version: V1.0
 */
public interface WorkJobRecordItemMapper extends BaseMapper<WorkJobRecordItem> {

    /**
     * 通过巡检点查询 相关信息
     * @param itemId 巡检点id
     * @return
     */
    WorkRecordItemInfoEntity getWorkRecordInfoByPointId(String itemId);
}
