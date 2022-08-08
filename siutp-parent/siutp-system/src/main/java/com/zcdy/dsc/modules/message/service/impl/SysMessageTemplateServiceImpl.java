package com.zcdy.dsc.modules.message.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zcdy.dsc.common.system.base.service.impl.BaseServiceImpl;
import com.zcdy.dsc.modules.message.entity.SysMessageTemplate;
import com.zcdy.dsc.modules.message.mapper.SysMessageTemplateMapper;
import com.zcdy.dsc.modules.message.service.ISysMessageTemplateService;

import java.util.List;

/**
 * 描述: 消息模板
 @author : songguang.jiao
 
 * 版本号: V1.0
 */
@Service
public class SysMessageTemplateServiceImpl extends BaseServiceImpl<SysMessageTemplateMapper, SysMessageTemplate> implements ISysMessageTemplateService {

    @Autowired
    private SysMessageTemplateMapper sysMessageTemplateMapper;


    @Override
    public List<SysMessageTemplate> selectByCode(String code) {
        return sysMessageTemplateMapper.selectByCode(code);
    }
}
