package com.zcdy.dsc.modules.message.service;

import com.zcdy.dsc.common.system.base.service.BaseService;
import com.zcdy.dsc.modules.message.entity.SysMessageTemplate;

import java.util.List;

/**
 * 描述: 消息模板
 @author : songguang.jiao
 
 * 版本号: V1.0
 */
public interface ISysMessageTemplateService extends BaseService<SysMessageTemplate> {
    List<SysMessageTemplate> selectByCode(String code);
}
