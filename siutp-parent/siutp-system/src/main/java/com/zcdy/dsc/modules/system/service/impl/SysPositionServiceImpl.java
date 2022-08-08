package com.zcdy.dsc.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcdy.dsc.modules.system.entity.SysPosition;
import com.zcdy.dsc.modules.system.mapper.SysPositionMapper;
import com.zcdy.dsc.modules.system.service.ISysPositionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述: 职务表
 
 
 * 版本号: V1.0
 */
@Service
public class SysPositionServiceImpl extends ServiceImpl<SysPositionMapper, SysPosition> implements ISysPositionService {
    @Autowired
    SysPositionMapper sysPositionMapper;
    @Override
    public List<SysPosition> exportList(String  code, String  name) {
        return sysPositionMapper.exportList(code,name);
    }

    @Override
    public IPage<SysPosition> queryPageList(Page<SysPosition> page, String code,String name,String status) {
        return sysPositionMapper.queryPageList(page,code,name,status);
    }

    @Override
    public void startOrStop(String id, String status) {
        sysPositionMapper.startOrStop(id,status);
    }
}
