package com.zcdy.dsc.modules.worklist.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcdy.dsc.common.system.util.JwtUtil;
import com.zcdy.dsc.modules.operation.equipment.constant.DelFlagConstant;
import com.zcdy.dsc.modules.worklist.constant.WorkListConstant;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatter;
import com.zcdy.dsc.modules.worklist.entity.WorkListMatterFile;
import com.zcdy.dsc.modules.worklist.mapper.WorkListMatterFileMapper;
import com.zcdy.dsc.modules.worklist.mapper.WorkListMatterMapper;
import com.zcdy.dsc.modules.worklist.service.WorkListMatterService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description: 工单任务
 * @Author: 在信汇通
 * @Date:   2021-01-15
 * @Version: V1.0
 */
@Service
public class WorkListMatterServiceImpl extends ServiceImpl<WorkListMatterMapper, WorkListMatter> implements WorkListMatterService {

    /**
     * 项目访问基础路径
     */
    @Value("${com.zcdy.dsc.file.request.domain}")
    protected String baseStoragePath;

    @Autowired
    private WorkListMatterFileMapper workListMatterFileMapper;

    @Override
    public IPage<WorkListMatter> selectPageDate(IPage<WorkListMatter> page, WorkListMatter query) {
        return baseMapper.selectPageDate(page, query);
    }

    @Override
    @Transactional
    public boolean addMatterAndFiles(WorkListMatter matter, String username) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        matter.setId(uuid);
        matter.setStatus(WorkListConstant.WORK_LIST_MATTER_STATUS_DEFAULT);//默认状态
        matter.setType(WorkListConstant.WORK_LIST_TYPE_MATTER);//手动添加的问题只能是“问题”类型
        matter.setCreateTime(new Date());
        matter.setSubTime(new Date());
        matter.setCreateBy(username);
        matter.setSubId(username);
        boolean ok = save(matter);
        if(!ok)
        {
            return false;
        }
        List<WorkListMatterFile> fileList = matter.getFileList();
        if(fileList == null || fileList.size() == 0)
        {
            return ok;
        }
        for(WorkListMatterFile file : fileList)
        {
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            file.setId(id);
            file.setMatterId(uuid);
            String url = file.getUrl();
            String type = url.substring(url.lastIndexOf(".") + 1);
            file.setFileType(type);
            file.setCreateBy(username);
            file.setCreateTime(new Date());
        }
        workListMatterFileMapper.insertBatch(fileList);
        return true;
    }

    @Override
    @Transactional
    public boolean editMatterAndFiles(WorkListMatter matter, String username) {
        matter.setUpdateTime(new Date());
        matter.setUpdateBy(username);
        boolean ok = updateById(matter);
        if(!ok)
        {
            return false;
        }
        WorkListMatterFile files = new WorkListMatterFile();
        files.setMatterId(matter.getId());
        files.setDelFlag(DelFlagConstant.DELETED);
        files.setUpdateTime(new Date());
        files.setUpdateBy(username);
        workListMatterFileMapper.delByMatterId(files);

        List<WorkListMatterFile> fileList = matter.getFileList();
        if(fileList == null || fileList.size() == 0)
        {
            return ok;
        }
        for(WorkListMatterFile file : fileList)
        {
            String id = UUID.randomUUID().toString().replaceAll("-", "");
            file.setId(id);
            file.setMatterId(matter.getId());
            String url = file.getUrl();
            String type = url.substring(url.lastIndexOf(".") + 1);
            file.setFileType(type);
            file.setCreateBy(username);
            file.setCreateTime(new Date());
        }
        workListMatterFileMapper.insertBatch(fileList);
        return true;
    }

    @Override
    @Transactional
    public boolean delMatterAndFiles(String id, String username) {
        WorkListMatterFile file = new WorkListMatterFile();
        file.setDelFlag(DelFlagConstant.DELETED);
        file.setUpdateBy(username);
        file.setUpdateTime(new Date());
        file.setMatterId(id);
        workListMatterFileMapper.delByMatterId(file);

        WorkListMatter workListMatter = new WorkListMatter();
        workListMatter.setId(id);
        workListMatter.setDelFlag(DelFlagConstant.DELETED);//删除标记
        workListMatter.setUpdateTime(new Date());
        workListMatter.setUpdateBy(username);
        boolean ok = updateById(workListMatter);
        return ok;
    }

    @Override
    @Transactional
    public boolean delBatchMatterAndFiles(String ids, String username) {
        List<String> list = Arrays.asList(ids.split(","));
        WorkListMatter matter = new WorkListMatter();
        matter.setDelFlag(DelFlagConstant.DELETED);
        matter.setUpdateBy(username);
        matter.setUpdateTime(new Date());
        matter.setIdList(list);
        int i = baseMapper.delBatch(matter);
        i += workListMatterFileMapper.delBatchByMatterId(matter);
        return i > 0;
    }

    @Override
    public WorkListMatter getOneById(WorkListMatter matter) {
        WorkListMatter one = baseMapper.getOneById(matter);
        List<WorkListMatterFile> fileList = one.getFileList();
        fileList.stream().forEach(workListMatterFile -> workListMatterFile.setUrl(StringUtils.isBlank(workListMatterFile.getUrl()) ? null : baseStoragePath + workListMatterFile.getUrl()));
        return one;
    }

    @Override
    public List<WorkListMatter> getAddList(WorkListMatter matter) {
        return baseMapper.getAddList(matter);
    }
}
