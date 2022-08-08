package com.zcdy.dsc.modules.worklist.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.common.api.vo.ResultData;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.worklist.constant.WorkListConstant;
import com.zcdy.dsc.modules.worklist.entity.WorkList;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatter;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatterFile;
import com.zcdy.dsc.modules.worklist.mapper.MyWorkListMapper;
import com.zcdy.dsc.modules.worklist.mapper.WorkListMatterFileMapper;
import com.zcdy.dsc.modules.worklist.service.MyWorkListService;
import com.zcdy.dsc.modules.worklist.service.WorkListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description: 我的工单
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Service
public class MyWorkListServiceImpl extends ServiceImpl<MyWorkListMapper, WorkList> implements MyWorkListService {

    @Autowired
    private WorkListMatterFileMapper workListMatterFileMapper;

    @Override
    public IPage<WorkList> selectPageDate(IPage<WorkList> page, WorkList query) {
        return baseMapper.selectPageDate(page, query);
    }

    @Override
    @Transactional
    public ResultData<?> complete(String ids, String username) {
        //先判断工单状态，正在进行的才可更新
        String[] idarray = ids.split(",");
        WorkListMatter mm = new WorkListMatter();
        mm.setId(idarray[0]);
        mm.setDelFlag(DelFlagConstant.NORMAL);
        WorkList workList = baseMapper.getWorkListByMatterId(mm);
        if(workList == null)
        {
            return ResultData.fail("工单不存在");
        }
        if(WorkListConstant.WORK_LIST_STATUS_COMPLETE.equals(workList.getStatus()))
        {
            return ResultData.fail("工单已完成，无需再次提交任务");
        }
        if(WorkListConstant.WORK_LIST_STATUS_INCOMPLETE.equals(workList.getStatus()))
        {
            return ResultData.fail("工单已过时，不能再提交任务");
        }

        WorkListMatter matter = new WorkListMatter();
        matter.setIdList(Arrays.asList(idarray));
        matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_COMPLETE);
        matter.setUpdateBy(username);
        matter.setUpdateTime(new Date());
        matter.setSolveId(username);
        matter.setSolveTime(new Date());
        int i = baseMapper.updateMatterComplete(matter);
        if(i != matter.getIdList().size())
        {
            log.error("工单任务>标记完成：标记完成数量与传入数量不一致，回滚更新操作");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultData.fail("操作失败");
        }

        //判断该工单下是否所有任务状态均为已完成
        matter = new WorkListMatter();
        matter.setDelFlag(DelFlagConstant.NORMAL);
        matter.setId(idarray[0]);
        List<WorkListMatter> matterlist = baseMapper.getMatterListByMatterId(matter);
        boolean isAllComplete = true;
        for(WorkListMatter m : matterlist)
        {
            if(!WorkListConstant.WORK_LIST_MATTER_STATUS_COMPLETE.equals(m.getStatus()))
            {
                isAllComplete = false;
                break;
            }
        }
        if(isAllComplete)
        {
            //更新工单状态为已完成
            matter = new WorkListMatter();
            matter.setStatus(WorkListConstant.WORK_LIST_STATUS_COMPLETE);
            matter.setId(idarray[0]);
            matter.setUpdateBy(username);
            matter.setUpdateTime(new Date());
            baseMapper.updateWorkListCompleteByMatterId(matter);
        }
        return ResultData.ok();
    }

    @Override
    @Transactional
    public ResultData<?> completeOne(WorkListMatter matter, String username) {
        if(matter.getId() == null)
        {
            return ResultData.fail("任务id不能为空");
        }
        //先判断工单状态，正在进行的才可更新
        WorkListMatter mm = new WorkListMatter();
        mm.setId(matter.getId());
        mm.setDelFlag(DelFlagConstant.NORMAL);
        WorkList workList = baseMapper.getWorkListByMatterId(mm);
        if(workList == null)
        {
            return ResultData.fail("工单不存在");
        }
        if(WorkListConstant.WORK_LIST_STATUS_COMPLETE.equals(workList.getStatus()))
        {
            return ResultData.fail("工单已完成，无需再次提交任务");
        }
        if(WorkListConstant.WORK_LIST_STATUS_INCOMPLETE.equals(workList.getStatus()))
        {
            return ResultData.fail("工单已过时，不能再提交任务");
        }

        //先清理一下之前可能存在的问题
        WorkListMatter delMatter = new WorkListMatter();
        delMatter.setMatterId(matter.getId());
        delMatter.setUpdateTime(new Date());
        delMatter.setUpdateBy(username);
        delMatter.setDelFlag(DelFlagConstant.DELETED);
        baseMapper.deleteMatterByMatterId(delMatter);

        if("是".equals(matter.getHasMatter()))
        {
            //含有问题，添加问题
            String newMatterId = UUID.randomUUID().toString().replaceAll("-", "");
            WorkListMatter newMatter = new WorkListMatter();
            newMatter.setId(newMatterId);
            newMatter.setSubId(username);
            newMatter.setSubTime(new Date());
            newMatter.setCreateTime(new Date());
            newMatter.setCreateBy(username);
            newMatter.setType(WorkListConstant.WORK_LIST_TYPE_MATTER);//类型：问题
            newMatter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_DEFAULT);//状态
            newMatter.setMatterId(matter.getId());//关联的问题id
            newMatter.setDescription(matter.getDescription());//问题描述
            newMatter.setMatterType(matter.getMatterType());//问题类型
            newMatter.setMatterLevel(matter.getMatterLevel());//问题等级
            newMatter.setMatterLongitude(matter.getSolveLongitude());//经度
            newMatter.setMatterLatitude(matter.getSolveLatitude());//纬度
            newMatter.setEquipmentId(matter.getEquipmentId());//设备
            newMatter.setTitle(matter.getTitle() + "发现的新问题");
            baseMapper.insertNewMatter(newMatter);
            //复制附件列表
            List<WorkListMatterFile> fileList = matter.getFileList();
            if(fileList != null && fileList.size() > 0)
            {
                for(WorkListMatterFile file : fileList)
                {
                    String fileId = UUID.randomUUID().toString().replaceAll("-", "");
                    file.setId(fileId);
                    file.setMatterId(newMatterId);
                    String url = file.getUrl();
                    String type = url.substring(url.lastIndexOf(".") + 1);
                    file.setFileType(type);
                    file.setCreateBy(username);
                    file.setCreateTime(new Date());
                }
                workListMatterFileMapper.insertBatch(fileList);
            }
        }

        //再更新问题本身
        matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_COMPLETE);//完成
        matter.setUpdateBy(username);
        matter.setUpdateTime(new Date());
        matter.setSolveId(username);
        matter.setSolveTime(new Date());
        baseMapper.updateMatterCompleteById(matter);
        //清空之前附件再重新添加
        WorkListMatterFile matterFile = new WorkListMatterFile();
        matterFile.setDelFlag(DelFlagConstant.DELETED);
        matterFile.setUpdateTime(new Date());
        matterFile.setUpdateBy(username);
        matterFile.setMatterId(matter.getId());
        workListMatterFileMapper.delByMatterId(matterFile);
        //重新添加，如果有
        List<WorkListMatterFile> fileList = matter.getFileList();
        if(fileList != null && fileList.size() > 0)
        {
            for(WorkListMatterFile file : fileList)
            {
                String fileId = UUID.randomUUID().toString().replaceAll("-", "");
                file.setId(fileId);
                file.setMatterId(matter.getId());
                String url = file.getUrl();
                String type = url.substring(url.lastIndexOf(".") + 1);
                file.setFileType(type);
                file.setCreateBy(username);
                file.setCreateTime(new Date());
            }
            workListMatterFileMapper.insertBatch(fileList);
        }

        //判断该工单下是否所有任务状态均为已完成
        WorkListMatter queryMatter = new WorkListMatter();
        queryMatter.setDelFlag(DelFlagConstant.NORMAL);
        queryMatter.setId(matter.getId());
        List<WorkListMatter> matterlist = baseMapper.getMatterListByMatterId(queryMatter);
        boolean isAllComplete = true;
        for(WorkListMatter m : matterlist)
        {
            if(!WorkListConstant.WORK_LIST_MATTER_STATUS_COMPLETE.equals(m.getStatus()))
            {
                isAllComplete = false;
                break;
            }
        }
        if(isAllComplete)
        {
            //更新工单状态为已完成
            queryMatter = new WorkListMatter();
            queryMatter.setStatus(WorkListConstant.WORK_LIST_STATUS_COMPLETE);
            queryMatter.setId(matter.getId());
            queryMatter.setUpdateBy(username);
            queryMatter.setUpdateTime(new Date());
            baseMapper.updateWorkListCompleteByMatterId(queryMatter);
        }

        return ResultData.ok();
    }
}
