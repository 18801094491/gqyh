package com.zcdy.dsc.modules.worklist.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 工单信息
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
public interface WorkListService extends IService<WorkList> {

    /**
     * 添加问题工单
     * @param workList
     * @return
     */
    ResultData<?> addWorkList(WorkList workList);

    /**
     * 添加维养工单
     * @param workList
     * @return
     */
    ResultData<?> addKeepWorkList(WorkList workList);

    /**
     * 添加巡检工单
     * @param workList
     * @return
     */
    ResultData<?> addInspWorkList(WorkList workList);

    /**
     * 分页查询
     * @param page
     * @param query
     * @return
     */
    IPage<WorkList> selectPageDate(IPage<WorkList> page, WorkList query);

    /**
     * 编辑问题工单
     * @param workList
     * @return
     */
    ResultData<?> editMatter(WorkList workList);

    /**
     * 编辑维养工单
     * @param workList
     * @return
     */
    ResultData<?> editKeep(WorkList workList);

    /**
     * 编辑巡检工单
     * @param workList
     * @return
     */
    ResultData<?> editInsp(WorkList workList);

    /**
     * 删除问题工单
     * @param workList
     * @return
     */
    ResultData<?> deleteMatterById(WorkList workList);

    /**
     * 删除维养工单
     * @param workList
     * @return
     */
    ResultData<?> deleteKeepById(WorkList workList);

    /**
     * 删除巡检工单
     * @param workList
     * @return
     */
    ResultData<?> deleteInspById(WorkList workList);

    /**
     * 批量删除问题工单
     * @param ids
     * @param username
     * @return
     */
    ResultData<?> deleteBatchMatter(String ids, String username);

    /**
     * 批量删除维养工单
     * @param ids
     * @param username
     * @return
     */
    ResultData<?> deleteBatchKeep(String ids, String username);

    /**
     * 批量删除巡检工单
     * @param ids
     * @param username
     * @return
     */
    ResultData<?> deleteBatchInsp(String ids, String username);

    /**
     * 根据id获取工单
     * @param query
     * @return
     */
    WorkList getOneById(WorkList query);

    /**
     * 创建维养工单时的设备列表
     * @param query
     * @return
     */
    List<OptEquipmentModel> getEquList(WorkList query);

    /**
     * 获取超时（超过over_time）且未全部完成的工单
     * @param query
     * @return
     */
    List<WorkList> getTimeoutAndNoCompleteWorkList(WorkList query);

    /**
     * 设置工单及其下属任务状态为已完成
     * @param workList
     * @return
     */
    ResultData<?> setWorkListComplete(WorkList workList);

    /**
     * 设置工单及其下属任务状态为未完成，同时释放未完成的问题任务
     * @param workList
     * @return
     */
    ResultData<?> setWorkListAndMatterIncomplete(WorkList workList);

    /**
     * 根据工单id获取对应班组成员所在位置
     * @param id
     * @return
     */
    ResultData<?> getWorkListLocationById(String id);

    /**
     * 根据状态获取工单数量
     * @param status
     * @return
     */
    Integer getWorkListNumByStatus(String status);

    /**
     * 获取大屏上显示的工单列表，7天内的工单
     * @param workList
     * @return
     */
    List<WorkList> getWorkList7DayList(WorkList workList);

    /**
     * 获取工单和班组成员信息
     * @param workList
     * @return
     */
    List<WorkList> getWorkListAndTeamMember(WorkList workList);

    /**
     * 获取工单信息+任务明细+成员明细
     * @param workList
     * @return
     */
    List<WorkList> getWorkListMatterAndTeamMember(WorkList workList);
}
