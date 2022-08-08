package com.zcdy.dsc.common.util.sms;

import org.apache.commons.lang.StringUtils;

/**
 * @author： Roberto
 * 创建时间：2020年2月25日 上午10:03:18
 * 描述: <p>短信枚举定义</p>
 */
public enum DySmsEnum {
	
	/**
	 * 登录验证码
	 */
	LOGIN_TEMPLATE_CODE("SMS_186810686", DySmsEnum.DEFAULTSIGNNAME,"code"),
	
	/**
	 * 忘记密码
	 */
	FORGET_PASSWORD_TEMPLATE_CODE("SMS_186810683", DySmsEnum.DEFAULTSIGNNAME,"code"),
	
	/**
	 * 注册短信
	 */
	REGISTER_TEMPLATE_CODE("SMS_186810684", DySmsEnum.DEFAULTSIGNNAME,"code"),
	
	/**
	 * 发送运行信息   (原有模板SMS_187260402)
	 */
	AUTO_RUNNING_IOT_DATA("SMS_188636691", DySmsEnum.DEFAULTSIGNNAME,"statisticDate,satisticCycle,maxValue7P,minValue7P,avgValue7P,maxValue7L,minValue7L,avgValue7L"
			+ ",maxValue2P,minValue2P,avgValue2P,maxValue2L,minValue2L,avgValue2L,maxValueHDL,minValueHDL,avgValueHDL"),
	
	
	/**
	 * 发送压力告警信息
	 */
	SEND_WARN_PRESURE("SMS_187571339", DySmsEnum.DEFAULTSIGNNAME,"section,alarmValue,time,LL2,LL7,P2,P7,LLHD"),
	
	/**
	 * 告警事件自动解除发送短信
	 */
	EVENT_RESUME_SMS("SMS_187745196", DySmsEnum.DEFAULTSIGNNAME, "section,pvalue,time");
	
	/**
	 * 定义默认签名信息
	 */
	public static final String DEFAULTSIGNNAME = "大运河水务智能化平台";
	
	/**
	 * 短信模板编码
	 */
	private String templateCode;
	/**
	 * 签名
	 */
	private String signName;
	/**
	 * 短信模板必需的数据名称，多个key以逗号分隔，此处配置作为校验
	 */
	private String keys;
	
	
	 DySmsEnum(String templateCode, String signName, String keys) {
		this.templateCode = templateCode;
		this.signName = signName;
		this.keys = keys;
	}
	
	public String getTemplateCode() {
		return templateCode;
	}
	
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	
	public String getSignName() {
		return signName;
	}
	
	public void setSignName(String signName) {
		this.signName = signName;
	}
	
	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public static DySmsEnum toEnum(String templateCode) {
		if(StringUtils.isEmpty(templateCode)){
			return null;
		}
		for(DySmsEnum item : DySmsEnum.values()) {
			if(item.getTemplateCode().equals(templateCode)) {
				return item;
			}
		}
		return null;
	}
}

