package com.zcdy.dsc.modules.message.handle.impl;

import com.zcdy.dsc.modules.message.handle.ISendMsgHandle;
import org.springframework.stereotype.Service;

/**
 * @author : songguang.jiao
 */
@Service
public class SmsSendMsgHandle implements ISendMsgHandle {

	@Override
	public void sendMsg(String receiver, String title, String content) {
		System.out.println("SmsSendMsgHandle");
	}

}
