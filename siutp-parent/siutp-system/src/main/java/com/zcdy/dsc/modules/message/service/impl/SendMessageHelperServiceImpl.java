package com.zcdy.dsc.modules.message.service.impl;

import com.zcdy.dsc.common.system.base.service.impl.BaseServiceImpl;
import com.zcdy.dsc.modules.message.entity.SysMessage;
import com.zcdy.dsc.modules.message.mapper.SendMessageHelperMapper;
import com.zcdy.dsc.modules.message.service.SendMessageHelperService;
import com.zcdy.dsc.modules.message.vo.SendMessageHelperVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 2 * @Author: 王海东
 * 3 * @Date: 2020/4/13 13:49
 * 4
 */
@Service
public class SendMessageHelperServiceImpl extends BaseServiceImpl<SendMessageHelperMapper, SysMessage> implements SendMessageHelperService {
    @Autowired
    SendMessageHelperMapper sendMessageHelperMapper;
    @Override
    public SendMessageHelperVo querySendMessageHelperVoById(String id) {
        List<SendMessageHelperVo> list=sendMessageHelperMapper.querySendMessageHelperVoById(id);
        SendMessageHelperVo smh= new SendMessageHelperVo();
        if(list!=null&&list.size()>0){
            smh=list.get(0);
            ArrayList<String> phones=sendMessageHelperMapper.queryPhonesByMoudleId(smh.getModuleId());
            smh.setPhones(phones);
        }
        return smh;
    }
}
