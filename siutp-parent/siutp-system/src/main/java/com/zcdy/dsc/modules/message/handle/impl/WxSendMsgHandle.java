package com.zcdy.dsc.modules.message.handle.impl;

import com.zcdy.dsc.modules.message.handle.ISendMsgHandle;
import org.springframework.stereotype.Service;

@Service
public class WxSendMsgHandle implements ISendMsgHandle {

	@Override
	public void sendMsg(String receiver, String title, String content) {
	}

}
