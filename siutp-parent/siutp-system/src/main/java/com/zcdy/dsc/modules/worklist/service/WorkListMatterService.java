package com.zcdy.dsc.modules.worklist.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatter;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 工单任务
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface WorkListMatterService extends IService<WorkListMatter> {

    /**
     * 分页查询
     * @param page
     * @param query
     * @return
     */
    IPage<WorkListMatter> selectPageDate(IPage<WorkListMatter> page, WorkListMatter query);

    /**
     * 保存问题和附件列表
     * @param matter
     * @param username
     * @return
     */
    boolean addMatterAndFiles(WorkListMatter matter, String username);
    /**
     * 修改问题和附件列表
     * @param matter
     * @param username
     * @return
     */
    boolean editMatterAndFiles(WorkListMatter matter, String username);

    /**
     * 删除问题和附件
     * @param id
     * @param username
     * @return
     */
    boolean delMatterAndFiles(String id, String username);

    /**
     * 批量删除问题和附件
     * @param ids
     * @param username
     * @return
     */
    boolean delBatchMatterAndFiles(String ids, String username);

    /**
     * 根据id查询问题和附件
     * @param matter
     * @return
     */
    WorkListMatter getOneById(WorkListMatter matter);

    /**
     * 向工单中添加问题（任务）时的列表
     * @param matter
     * @return
     */
    List<WorkListMatter> getAddList(WorkListMatter matter);
}
