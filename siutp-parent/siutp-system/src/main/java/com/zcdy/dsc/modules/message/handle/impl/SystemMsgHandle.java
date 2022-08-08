package com.zcdy.dsc.modules.message.handle.impl;

import org.springframework.beans.factory.annotation.Autowired;
import com.zcdy.dsc.common.system.api.ISysBaseApi;
import com.zcdy.dsc.modules.message.handle.ISendMsgHandle;
import org.springframework.stereotype.Service;

/**
 * 描述: 发送系统消息通知
 * @author：  songguang.jiao
 * 创建时间： 2020年3月6日 下午2:46:57 
 * 版本号: V1.0
 */
@Service
public class SystemMsgHandle implements ISendMsgHandle{

	@Autowired
    private ISysBaseApi sysBaseApi;
	
	@Override
	public void sendMsg(String receiver, String title, String content) {
		sysBaseApi.sendSysAnnouncement("admin", receiver, title, content);
	}

}
