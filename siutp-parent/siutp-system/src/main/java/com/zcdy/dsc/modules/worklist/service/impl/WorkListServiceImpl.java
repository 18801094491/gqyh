package com.zcdy.dsc.modules.worklist.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.common.constant.CommonConstant;
import com.zcdy.dsc.common.framework.redis.RedisService;
import com.zcdy.dsc.common.util.DateUtil;
import com.zcdy.dsc.constant.RedisKeyConstantV2;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.operation.equipment.vo.OptEquipmentModel;
import com.zcdy.dsc.modules.worklist.constant.WorkListConstant;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import com.zcdy.dsc.modules.worklist.entity.WorkListLocation;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatter;
import com.zcdy.dsc.modules.worklist.mapper.WorkListMapper;
import com.zcdy.dsc.modules.worklist.mapper.WorkListMatterMapper;
import com.zcdy.dsc.modules.worklist.service.WorkListMatterService;
import com.zcdy.dsc.modules.worklist.service.WorkListService;
import com.zcdy.dsc.modules.worklist.vo.WorkListLocationVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * @Description: 工单信息
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Service
public class WorkListServiceImpl extends ServiceImpl<WorkListMapper, WorkList> implements WorkListService {

    @Autowired
    private WorkListMatterMapper workListMatterMapper;

    @Autowired
    private WorkListMatterService workListMatterService;

    @Autowired
    private RedisService redisService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @Transactional
    public ResultData<?> addWorkList(WorkList workList) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        workList.setId(uuid);
        workList.setCreateTime(new Date());
        workList.setStatus(WorkListConstant.WORK_LIST_STATUS_ALLOT);//创建就是已分配
        boolean ok = save(workList);
        if(!ok)
        {
            return ResultData.fail("操作失败");
        }
        /*List<String> matterIdList = workList.getMatterIdList();
        if(matterIdList == null || matterIdList.size() == 0)
        {
            return ok;
        }
        WorkListMatter matter = new WorkListMatter();
        matter.setListId(uuid);
        matter.setUpdateTime(new Date());
        matter.setUpdateBy(workList.getCreateBy());
        matter.setIdList(matterIdList);
        matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);
        matter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_MATTER);
        int i = workListMatterMapper.updateMatterListId(matter);*/
        List<WorkListMatter> matterList = workList.getMatterList();
        if(matterList == null || matterList.size() == 0)
        {
            return ResultData.ok("操作成功");
        }
        for(WorkListMatter matter : matterList)
        {
            if(matter.getSeq() == null)
            {
                logger.error("维养工单[{}]中的问题任务[{}]序号不能为空", uuid, matter.getId());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultData.fail("任务的序号不能为空");
            }
            matter.setListId(uuid);
            matter.setUpdateTime(new Date());
            matter.setUpdateBy(workList.getCreateBy());
            matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//状态：已分配
            matter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_MATTER);//更新条件：问题类型
        }
        int i = workListMatterMapper.updateMatterListWhenAddWorkList(matterList);
        if(i > 0)
            return ResultData.ok("操作成功");
        else
            return ResultData.fail("操作失败");
    }

    @Override
    @Transactional
    public ResultData<?> addKeepWorkList(WorkList workList) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        workList.setId(uuid);
        workList.setCreateTime(new Date());
        workList.setStatus(WorkListConstant.WORK_LIST_STATUS_ALLOT);//创建就是已分配
        boolean ok = save(workList);
        if(!ok)
        {
            return ResultData.fail("操作失败");
        }
        List<WorkListMatter> matterList = workList.getMatterList();
        if(matterList == null || matterList.size() == 0)
        {
            return ResultData.ok();
        }
        List<WorkListMatter> keeplist = new ArrayList<>();//维养任务列表
        List<WorkListMatter> matterlist = new ArrayList<>();//问题任务列表
        for(WorkListMatter matter : matterList)
        {
            if(WorkListConstant.WORK_LIST_TYPE_KEEP.equals(matter.getType()))
            {
                //维养任务
                keeplist.add(matter);
            }
            else if(WorkListConstant.WORK_LIST_TYPE_MATTER.equals(matter.getType()))
            {
                if(matter.getId() == null)
                {
                    //问题任务id不能为空
                    logger.error("维养工单[{}]中的问题任务id不能为空", uuid);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResultData.fail("工单中的任务id不能为空");
                }
                matterlist.add(matter);
            }
        }
        if(keeplist.size() == 0)
        {
            //维养工单必须包含维养任务
            logger.error("维养工单[{}]必须包含维养任务", uuid);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultData.fail("维养工单必须包含至少一个维养任务");
        }
        String username = workList.getCreateBy();
        for(WorkListMatter matter : keeplist)
        {
            String matterId = UUID.randomUUID().toString().replaceAll("-", "");
            matter.setId(matterId);
            matter.setListId(uuid);
            matter.setSubId(username);
            matter.setSubTime(new Date());
            matter.setCreateTime(new Date());
            matter.setCreateBy(username);
            matter.setType(WorkListConstant.WORK_LIST_TYPE_KEEP);
            matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//维养任务创建出来就是“已分配”
        }
        workListMatterService.saveBatch(keeplist);

        if(matterlist.size() > 0)
        {
            for(WorkListMatter matter : matterlist)
            {
                matter.setListId(uuid);
                matter.setUpdateBy(username);
                matter.setUpdateTime(new Date());
                matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//状态设为“已分配”
                matter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_DEFAULT);//条件：已创建的才能设为“已分配”
            }
            workListMatterMapper.updateBatchByForeachScript(matterlist);
        }

        return ResultData.ok();
       /* //int seq = 1;
        for(WorkListMatter matter : matterList)
        {
            String matterId = UUID.randomUUID().toString().replaceAll("-", "");
            matter.setId(matterId);
            matter.setListId(uuid);
            matter.setSubId(workList.getCreateBy());
            matter.setSubTime(new Date());
            matter.setCreateTime(new Date());
            matter.setCreateBy(workList.getCreateBy());
//            matter.setType(WorkListConstant.WORK_LIST_TYPE_KEEP);
            matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//维养任务创建出来就是“已分配”
            //matter.setSeq(seq ++);
        }
        return workListMatterService.saveBatch(matterList);*/
    }

    @Override
    @Transactional
    public ResultData<?> addInspWorkList(WorkList workList) {
        boolean ok = save(workList);
        if(!ok)
        {
            return ResultData.fail("操作失败");
        }
        List<WorkListMatter> matterList = workList.getMatterList();
        if(matterList == null || matterList.size() == 0)
        {
            logger.error("巡检工单[{}]未包含巡检任务", workList.getId());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultData.fail("巡检工单必须包含至少一个巡检任务");
        }
        boolean b = workListMatterService.saveBatch(matterList);
        if(b)
            return ResultData.ok();
        else
            return ResultData.fail("操作失败");
    }

    @Override
    public IPage<WorkList> selectPageDate(IPage<WorkList> page, WorkList query) {
        return baseMapper.selectPageDate(page, query);
    }

    @Override
    @Transactional
    public ResultData<?> editMatter(WorkList workList) {
        boolean ok = updateById(workList);
        if(!ok)
        {
            return ResultData.fail("操作失败");
        }
        String listId = workList.getId();
        WorkListMatter matter = new WorkListMatter();
        matter.setListId(listId);
        matter.setUpdateBy(workList.getUpdateBy());
        matter.setUpdateTime(new Date());
        matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_DEFAULT);//状态设回“已创建”
        //释放之前的关系，巡检工单和维养工单的“原生任务”不能解绑，重新绑定的问题（任务）仅限“问题”类型
        matter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_MATTER);//条件：问题类型才能释放
        matter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//条件：已分配未执行的才能释放
        workListMatterMapper.updateListId2NullByListId(matter);
        //重新绑定更新后的关系
        /*List<String> matterIdList = workList.getMatterIdList();
        matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//状态设为“已分配”
        if(matterIdList != null && matterIdList.size() > 0)
        {
            matter.setIdList(matterIdList);
            workListMatterMapper.updateMatterListId(matter);
        }*/
        List<WorkListMatter> matterList = workList.getMatterList();
        if(matterList == null || matterList.size() == 0)
        {
            return ResultData.ok();
        }
        for(WorkListMatter workListMatter : matterList)
        {
            if(workListMatter.getSeq() == null)
            {
                logger.error("维养工单[{}]中的问题任务[{}]序号不能为空", listId, workListMatter.getId());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ResultData.fail("工单中的任务顺序不能为空");
            }
            workListMatter.setListId(listId);
            workListMatter.setUpdateTime(new Date());
            workListMatter.setUpdateBy(workList.getUpdateBy());
            workListMatter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//状态：已分配
            workListMatter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_MATTER);//更新条件：问题类型
        }
        int i = workListMatterMapper.updateMatterListWhenAddWorkList(matterList);
        if(i > 0)
            return ResultData.ok();
        else
            return ResultData.fail("失败");
    }

    @Override
    @Transactional
    public ResultData<?> editKeep(WorkList workList) {
        /**
         * 1修改工单
         * 2删除原有维养任务，再重建（type=2）
         * 3释放原有问题任务，再绑定（type=3）
         */
        boolean ok = updateById(workList);//1修改工单
        if(!ok)
        {
            return ResultData.fail("操作失败");
        }
        List<WorkListMatter> matterList = workList.getMatterList();
        String uuid = workList.getId();
        if(matterList == null || matterList.size() == 0)
        {
            //维养工单必须包含维养任务
            logger.error("维养工单[{}]未包含维养任务", uuid);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultData.fail("维养工单必须包含至少一个维养任务");
        }
        List<WorkListMatter> keeplist = new ArrayList<>();//维养任务列表
        List<WorkListMatter> matterlist = new ArrayList<>();//问题任务列表
        for(WorkListMatter matter : matterList)
        {
            if(WorkListConstant.WORK_LIST_TYPE_KEEP.equals(matter.getType()))
            {
                //维养任务
                keeplist.add(matter);
            }
            else if(WorkListConstant.WORK_LIST_TYPE_MATTER.equals(matter.getType()))
            {
                if(matter.getId() == null)
                {
                    //问题任务id不能为空
                    logger.error("维养工单[{}]中的问题任务id不能为空", uuid);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResultData.fail("工单中的任务id不能为空");
                }
                matterlist.add(matter);
            }
        }
        if(keeplist.size() == 0)
        {
            //维养工单必须包含维养任务
            logger.error("维养工单[{}]必须包含维养任务", uuid);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultData.fail("维养工单中至少包含一个维养任务");
        }

        String listId = workList.getId();
        String username = workList.getUpdateBy();
        WorkListMatter delmatter = new WorkListMatter();
        delmatter.setListId(listId);
        delmatter.setUpdateBy(workList.getUpdateBy());
        delmatter.setUpdateTime(new Date());
        delmatter.setDelFlag(DelFlagConstant.DELETED);
        delmatter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_KEEP);
        delmatter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);
        int i = workListMatterMapper.delByListIdAndType(delmatter);//2删除原有维养任务，下面再重建
        for(WorkListMatter matter : keeplist)
        {
            String matterId = UUID.randomUUID().toString().replaceAll("-", "");
            matter.setId(matterId);
            matter.setListId(uuid);
            matter.setSubId(username);
            matter.setSubTime(new Date());
            matter.setCreateTime(new Date());
            matter.setCreateBy(username);
            matter.setType(WorkListConstant.WORK_LIST_TYPE_KEEP);
            matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//维养任务创建出来就是“已分配”
        }
        workListMatterService.saveBatch(keeplist);//2再重建（type=2）
        //////////////////////////////////////////////////////////
        WorkListMatter id2nullmatter = new WorkListMatter();
        id2nullmatter.setListId(listId);
        id2nullmatter.setUpdateBy(username);
        id2nullmatter.setUpdateTime(new Date());
        id2nullmatter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_DEFAULT);//状态设回“已创建”
        id2nullmatter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_MATTER);
        id2nullmatter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//条件：已分配未执行的才能释放
        workListMatterMapper.updateListId2NullByListId(id2nullmatter);//3释放原有问题任务，再绑定（type=3）
        id2nullmatter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//状态设为“已分配”
        if(matterlist.size() > 0)
        {
            for(WorkListMatter matter : matterlist)
            {
                matter.setListId(listId);
                matter.setUpdateBy(workList.getUpdateBy());
                matter.setUpdateTime(new Date());
                matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//状态设为“已分配”
                matter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_MATTER);
                matter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_DEFAULT);//条件：已创建的才能设为“已分配”
            }
            workListMatterMapper.updateBatchByForeachScript(matterlist);
        }
        return ResultData.ok();
    }

    @Override
    @Transactional
    public ResultData<?> editInsp(WorkList workList) {
        /**
         * 1修改工单
         * 2删除原有维养任务，再重建（type=1）
         * 3释放原有问题任务，再绑定（type=3）
         */
        boolean ok = updateById(workList);//1修改工单
        if(!ok)
        {
            return ResultData.fail("操作失败");
        }
        List<WorkListMatter> matterList = workList.getMatterList();
        String uuid = workList.getId();
        if(matterList == null || matterList.size() == 0)
        {
            //巡检工单必须包含巡检任务
            logger.error("巡检工单[{}]没有发现任务", uuid);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultData.fail("工单中必须包含任务");
        }
        List<WorkListMatter> insplist = new ArrayList<>();//巡检任务列表
        List<WorkListMatter> matterlist = new ArrayList<>();//问题任务列表
        for(WorkListMatter matter : matterList)
        {
            if(WorkListConstant.WORK_LIST_TYPE_INSP.equals(matter.getType()))
            {
                //巡检任务
                insplist.add(matter);
            }
            else if(WorkListConstant.WORK_LIST_TYPE_MATTER.equals(matter.getType()))
            {
                if(matter.getId() == null)
                {
                    //问题任务必须id不能为空
                    logger.error("巡检工单[{}]中的问题任务id不能为空", uuid);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResultData.fail("工单中的任务id不能为空");
                }
                matterlist.add(matter);
            }
        }
        if(insplist.size() == 0)
        {
            //巡检工单必须包含巡检任务
            logger.error("巡检工单[{}]必须包含巡检任务", uuid);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultData.fail("巡检工单中至少包含一个巡检任务");
        }

        String listId = workList.getId();
        String username = workList.getUpdateBy();
        WorkListMatter delmatter = new WorkListMatter();
        delmatter.setListId(listId);
        delmatter.setUpdateBy(workList.getUpdateBy());
        delmatter.setUpdateTime(new Date());
        delmatter.setDelFlag(DelFlagConstant.DELETED);
        delmatter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_INSP);
        delmatter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);
        int i = workListMatterMapper.delByListIdAndType(delmatter);//2删除原有巡检任务，下面再重建
        for(WorkListMatter matter : insplist)
        {
            String matterId = UUID.randomUUID().toString().replaceAll("-", "");
            matter.setId(matterId);
            matter.setListId(uuid);
            matter.setSubId(username);
            matter.setSubTime(new Date());
            matter.setCreateTime(new Date());
            matter.setCreateBy(username);
            matter.setType(WorkListConstant.WORK_LIST_TYPE_INSP);
            matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//巡检任务创建出来就是“已分配”
        }
        workListMatterService.saveBatch(insplist);//2再重建（type=2）
        //////////////////////////////////////////////////////////
        WorkListMatter id2nullmatter = new WorkListMatter();
        id2nullmatter.setListId(listId);
        id2nullmatter.setUpdateBy(username);
        id2nullmatter.setUpdateTime(new Date());
        id2nullmatter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_DEFAULT);//状态设回“已创建”
        id2nullmatter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_MATTER);
        id2nullmatter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//条件：已分配未执行的才能释放
        workListMatterMapper.updateListId2NullByListId(id2nullmatter);//3释放原有问题任务，再绑定（type=3）
        id2nullmatter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//状态设为“已分配”
        if(matterlist.size() > 0)
        {
            for(WorkListMatter matter : matterlist)
            {
                matter.setListId(listId);
                matter.setUpdateBy(workList.getUpdateBy());
                matter.setUpdateTime(new Date());
                matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//状态设为“已分配”
                matter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_MATTER);
                matter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_DEFAULT);//条件：已创建的才能设为“已分配”
            }
            workListMatterMapper.updateBatchByForeachScript(matterlist);
        }
        return ResultData.ok();
    }

    @Override
    @Transactional
    public ResultData<?> deleteMatterById(WorkList workList) {
        boolean ok = updateById(workList);
        if(!ok)
        {
            return ResultData.fail("操作失败");
        }
        WorkListMatter matter = new WorkListMatter();
        matter.setListId(workList.getId());
        matter.setUpdateBy(workList.getUpdateBy());
        matter.setUpdateTime(new Date());
        matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_DEFAULT);
        //释放之前的关系
        matter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_MATTER);
        matter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//条件：已分配未执行的才能释放
        workListMatterMapper.updateListId2NullByListId(matter);
        return ResultData.ok();
    }

    @Override
    @Transactional
    public ResultData<?> deleteKeepById(WorkList workList) {
        /**
         * 1、删除维养工单
         * 2、删除维养任务
         * 3、释放问题任务
         */
        ResultData<?> resultData = deleteMatterById(workList);
        if(resultData.getCode() != CommonConstant.SC_OK_200)
        {
            return ResultData.fail("操作失败");
        }
        WorkListMatter matter = new WorkListMatter();
        matter.setListId(workList.getId());
        matter.setUpdateBy(workList.getUpdateBy());
        matter.setUpdateTime(new Date());
        matter.setDelFlag(DelFlagConstant.DELETED);
        matter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_KEEP);
        matter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);
        workListMatterMapper.delByListIdAndType(matter);
        return ResultData.ok();
    }

    @Override
    @Transactional
    public ResultData<?> deleteInspById(WorkList workList) {
        /**
         * 1、删除巡检工单
         * 2、删除巡检任务
         * 3、释放问题任务
         */
        ResultData<?> resultData = deleteMatterById(workList);
        if(resultData.getCode() != CommonConstant.SC_OK_200)
        {
            return ResultData.fail("操作失败");
        }
        WorkListMatter matter = new WorkListMatter();
        matter.setListId(workList.getId());
        matter.setUpdateBy(workList.getUpdateBy());
        matter.setUpdateTime(new Date());
        matter.setDelFlag(DelFlagConstant.DELETED);
        matter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_INSP);
        matter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);
        workListMatterMapper.delByListIdAndType(matter);
        return ResultData.ok();
    }

    @Override
    @Transactional
    public ResultData<?> deleteBatchMatter(String ids, String username) {
        List<String> list = Arrays.asList(ids.split(","));
        List<WorkList> entityList = new ArrayList<>();
        for(String s : list)
        {
            WorkList workList = new WorkList();
            workList.setId(s);
            workList.setDelFlag(DelFlagConstant.DELETED);//删除标记
            workList.setUpdateTime(new Date());
            workList.setUpdateBy(username);
            entityList.add(workList);
        }
        boolean ok = updateBatchById(entityList);
        if(!ok)
        {
            return ResultData.fail("操作失败");
        }
        WorkListMatter matter = new WorkListMatter();
        matter.setUpdateTime(new Date());
        matter.setUpdateBy(username);
        matter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_MATTER);
        matter.setIdList(list);
        matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_DEFAULT);//状态改回“已创建”
        matter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//条件：只有“已分配”可以改回“已创建”
        workListMatterMapper.updateBatchListId2NullByListId(matter);
        return ResultData.ok();
    }

    @Override
    @Transactional
    public ResultData<?> deleteBatchKeep(String ids, String username) {
        ResultData<?> resultData = deleteBatchMatter(ids, username);
        if(resultData.getCode() != CommonConstant.SC_OK_200)
        {
            return ResultData.fail("操作失败");
        }
        List<String> list = Arrays.asList(ids.split(","));
        WorkListMatter matter = new WorkListMatter();
        matter.setDelFlag(DelFlagConstant.DELETED);
        matter.setUpdateBy(username);
        matter.setUpdateTime(new Date());
        matter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_KEEP);
        matter.setIdList(list);
        matter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//已分配未执行的才能删除
        workListMatterMapper.delBatchByListIdAndType(matter);
        return ResultData.ok();
    }

    @Override
    @Transactional
    public ResultData<?> deleteBatchInsp(String ids, String username) {
        ResultData<?> resultData = deleteBatchMatter(ids, username);
        if(resultData.getCode() != CommonConstant.SC_OK_200)
        {
            return ResultData.fail("操作失败");
        }
        List<String> list = Arrays.asList(ids.split(","));
        WorkListMatter matter = new WorkListMatter();
        matter.setDelFlag(DelFlagConstant.DELETED);
        matter.setUpdateBy(username);
        matter.setUpdateTime(new Date());
        matter.setTypeCodeMatter(WorkListConstant.WORK_LIST_TYPE_INSP);
        matter.setIdList(list);
        matter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);//已分配未执行的才能删除
        workListMatterMapper.delBatchByListIdAndType(matter);
        return ResultData.ok();
    }

    @Override
    public WorkList getOneById(WorkList query) {
        return baseMapper.getOneById(query);
    }

    @Override
    public List<OptEquipmentModel> getEquList(WorkList query) {
        return baseMapper.getEquList(query);
    }

    @Override
    public List<WorkList> getTimeoutAndNoCompleteWorkList(WorkList query) {
        return baseMapper.getTimeoutAndNoCompleteWorkList(query);
    }

    @Override
    @Transactional
    public ResultData<?> setWorkListComplete(WorkList workList) {
        workList.setStatus(WorkListConstant.WORK_LIST_STATUS_COMPLETE);
        boolean b = updateById(workList);
        if(b)
            return ResultData.ok();
        else
            return ResultData.fail("操作失败");
    }

    @Override
    @Transactional
    public ResultData<?> setWorkListAndMatterIncomplete(WorkList workList) {
        workList.setStatus(WorkListConstant.WORK_LIST_STATUS_INCOMPLETE);
        boolean ok = updateById(workList);
        if(!ok)
        {
            return ResultData.fail("操作失败");
        }
        if(!WorkListConstant.WORK_LIST_TYPE_MATTER.equals(workList.getType()))
        {
            //如果不是问题工单，先将下属未完成的非问题任务设为未完成
            WorkListMatter matter = new WorkListMatter();
            matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_INCOMPLETE);
            matter.setUpdateTime(new Date());
            matter.setUpdateBy(workList.getUpdateBy());
            matter.setListId(workList.getId());
            matter.setType(WorkListConstant.WORK_LIST_TYPE_INSP);
            matter.setQueryTypeCode(WorkListConstant.WORK_LIST_TYPE_KEEP);
            matter.setDelFlag(DelFlagConstant.NORMAL);
            matter.setQueryStatusCode(WorkListConstant.WORK_LIST_MATTER_STATUS_DEFAULT);
            matter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);
            workListMatterMapper.updateMatterIncompleteByListidAndType(matter);
        }
        //解绑未完成的问题任务
        WorkListMatter matter = new WorkListMatter();
        matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_DEFAULT);
        matter.setUpdateTime(new Date());
        matter.setUpdateBy(workList.getUpdateBy());
        matter.setListId(workList.getId());
        matter.setType(WorkListConstant.WORK_LIST_TYPE_MATTER);
        matter.setDelFlag(DelFlagConstant.NORMAL);
        matter.setQueryStatusCode(WorkListConstant.WORK_LIST_MATTER_STATUS_DEFAULT);
        matter.setStatusCodeMatter(WorkListConstant.WORK_LIST_MATTER_STATUS_ALLOT);
        workListMatterMapper.updateMatterListid2NullByListidAndType(matter);
        return ResultData.ok();
    }

    @Override
    public ResultData<?> getWorkListLocationById(String id)
    {
        WorkList workList = baseMapper.getWorkListLocationById(id);
        if(workList == null)
        {
            return ResultData.error("工单不存在");
        }
        if(WorkListConstant.WORK_LIST_STATUS_COMPLETE.equals(workList.getStatus()))
        {
            return ResultData.error("工单已完成，无需查询位置");
        }
        String currentDateStr = DateUtil.currentDateStr(DateUtil.dateFormatStr);
        Date today = DateUtil.string2Date(currentDateStr, DateUtil.dateFormatStr);
        if(!DateUtil.isBetweenDates(today, workList.getStartDate(), workList.getOverDate()))
        {
            return ResultData.error("只有执行期间的工单才能查询位置");
        }
        //数据格式：username|realname,username|realname……
        //如：liushengnan|刘胜男,lmy|李明洋,lmy2|李明洋2
        String teamMemberUsername = workList.getTeamMemberUsername();
        if(teamMemberUsername == null)
        {
            return ResultData.error("未查询到班组成员");
        }
        String[] members = teamMemberUsername.split(",");
        String locationKeyPrefix = RedisKeyConstantV2.WORKLIST_APP_LOCATION;
        List<WorkListLocationVo> locationVoList = new ArrayList<>();
        for(String member : members)
        {
            String[] memberStrs = member.split("\\|");
            String username = memberStrs[0];
            String realname = memberStrs[1];
            //通过username去缓存查询位置坐标
            String locationKey = String.format(locationKeyPrefix, username);
            String locationJson = (String) redisService.get(locationKey);
            if(locationJson != null)
            {
                WorkListLocationVo location = JSON.parseObject(locationJson, WorkListLocationVo.class);
                location.setRealname(realname);
                locationVoList.add(location);
            }
        }
        return ResultData.ok(locationVoList);
    }

    @Override
    public Integer getWorkListNumByStatus(String status) {
        WorkList workList = new WorkList();
        workList.setStatus(status);
        Date today = new Date();//今天
        Date startDate = DateUtil.addDay(today, -6);//6天前
        String todayStr = DateUtil.date2String(today, DateUtil.dateFormatStr);//取日期
        String startStr = DateUtil.date2String(startDate, DateUtil.dateFormatStr);//取日期
        today = DateUtil.string2Date(todayStr + " 23:59:59", DateUtil.dateTimeFormatStr);//加上时间
        startDate = DateUtil.string2Date(startStr + " 00:00:00", DateUtil.dateTimeFormatStr);//加上时间
        workList.setStartDate(startDate);
        workList.setOverDate(today);
        return baseMapper.getWorkListNumByStatus(workList);
    }

    @Override
    public List<WorkList> getWorkList7DayList(WorkList workList) {
        return baseMapper.getWorkList7DayList(workList);
    }

    @Override
    public List<WorkList> getWorkListAndTeamMember(WorkList workList) {
        return baseMapper.getWorkListAndTeamMember(workList);
    }
    @Override
    public List<WorkList> getWorkListMatterAndTeamMember(WorkList workList) {
        return baseMapper.getWorkListMatterAndTeamMember(workList);
    }
}
