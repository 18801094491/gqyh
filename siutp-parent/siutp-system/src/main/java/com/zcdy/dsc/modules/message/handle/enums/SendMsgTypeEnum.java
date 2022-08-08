package com.zcdy.dsc.modules.message.handle.enums;

import com.zcdy.dsc.common.util.ConvertUtils;

/**
 * 发送消息类型枚举
 * @author : songguang.jiao
 */
public enum SendMsgTypeEnum {

//推送方式：1短信 2邮件 3微信 4系统消息
	SMS("1", "com.zcdy.dsc.modules.message.handle.impl.SmsSendMsgHandle"),
	EMAIL("2", "com.zcdy.dsc.modules.message.handle.impl.EmailSendMsgHandle"),
	WX("3","com.zcdy.dsc.modules.message.handle.impl.WxSendMsgHandle"),
	SYS("4","com.zcdy.dsc.modules.message.handle.impl.SystemMsgHandle");

	private String type;

	private String implClass;

	private SendMsgTypeEnum(String type, String implClass) {
		this.type = type;
		this.implClass = implClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImplClass() {
		return implClass;
	}

	public void setImplClass(String implClass) {
		this.implClass = implClass;
	}

	public static SendMsgTypeEnum getByType(String type) {
		if (ConvertUtils.isEmpty(type)) {
			return null;
		}
		for (SendMsgTypeEnum val : values()) {
			if (val.getType().equals(type)) {
				return val;
			}
		}
		return null;
	}
}
