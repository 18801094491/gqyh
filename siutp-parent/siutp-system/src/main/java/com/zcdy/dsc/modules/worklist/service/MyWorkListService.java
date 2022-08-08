package com.zcdy.dsc.modules.worklist.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatter;

import java.util.List;

/**
 * @Description: 我的工单
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface MyWorkListService extends IService<WorkList> {
    /**
     * 分页查询列表
     * @param query
     * @return
     */
    IPage<WorkList> selectPageDate(IPage<WorkList> page, WorkList query);

    /**
     * 标记完成
     * @param ids
     * @param username
     * @return
     */
    ResultData<?> complete(String ids, String username);

    /**
     * 标记完成单个任务
     * @param matter
     * @param username
     * @return
     */
    ResultData<?> completeOne(WorkListMatter matter, String username);
}
