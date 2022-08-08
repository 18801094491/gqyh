package com.zcdy.dsc.modules.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcdy.dsc.modules.system.entity.SysPosition;

import java.util.List;

/**
 * 描述: 职务表
 @author : songguang.jiao
 
 * 版本号: V1.0
 */
public interface ISysPositionService extends IService<SysPosition> {

    List<SysPosition> exportList(String  code, String  name);

    IPage<SysPosition> queryPageList(Page<SysPosition> page, String code,String name,String status);

    void startOrStop(String id, String status);
}
