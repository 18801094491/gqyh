package com.zcdy.dsc.modules.message.constant;

/**
 * 描述: 短信事件模板code
 * @author: songguang.jiao
 * 创建时间:  2020年4月26日 下午3:26:26
 * 版本: V1.0
 */
public interface EventConstant {

	/**
	 * 每日早9:00发送昨日流量统计情况
	 */
	public static final String YESTODAY_FLOW_COUNT="YESTEDAY_FLOW_COUT";
	
	/**
	 * 2小时运营情况
	 */
	public static final String AUTO_RUNNING_IOT_DATA="AUTO_RUNNING_IOT_DATA";

	/**
	 * 20200904--2小时运营情况
	 */
	public static final String AUTO_RUNNING_IOT_DATA_20200904="AUTO_RUNNING_IOT_DATA_20200904";
	
	/**
	 * 告警事件生成
	 */
	public static final String ALARM_CREATE="ALARM_CREATE";
	
	/**
	 * 压力告警事件自动解除
	 */
	public static final String EVENT_RESUME_SMS="EVENT_RESUME_SMS";
	
	/**
	 * 两小时推送(河东流量为基准)
	 */
	public static final String AUTO_RUNNING_DATA_HD="AUTO_RUNNING_DATA_HD";
	
	/**
	 * 两小时推送(7标压力为基准)
	 */
	public static final String AUTO_RUNNING_DATA_7P="AUTO_RUNNING_DATA_7P";
	
	/**
	 * 合同到期提醒短信模板
	 */
	public static final String CONTRACT_EXPIRE_SMS="CONTRACT_EXPIRE_SMS";
}
