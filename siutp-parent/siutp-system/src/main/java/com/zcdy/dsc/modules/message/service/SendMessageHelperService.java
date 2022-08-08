package com.zcdy.dsc.modules.message.service;

import com.zcdy.dsc.common.system.base.service.BaseService;
import com.zcdy.dsc.modules.message.entity.SysMessage;
import com.zcdy.dsc.modules.message.vo.SendMessageHelperVo;

/**
 * 2 * @Author: 王海东
 * 3 * @Date: 2020/4/13 13:48
 * 4
 */
public interface SendMessageHelperService extends BaseService< SysMessage> {
    SendMessageHelperVo querySendMessageHelperVoById(String id);

}
