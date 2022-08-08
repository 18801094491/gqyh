package com.zcdy.dsc.modules.operation.alarm.constant;

/**
 * 描述: 告警状态
 * @author：  songguang.jiao
 * 创建时间： 2020年2月18日 上午10:50:49 
 * 版本号: V1.0
 */
public interface WarnStatusConstant {

	/**
	 * 初始化
	 */
	public static final String INIT="0";
	/**
	 * 未处理
	 */
	public static final String UNDEAL="1";
	/**
	 * 已处理
	 */
	public static final String DEAL="2";
	
	/**
	 * 已关闭
	 */
	public static final String DEAL_CLOSED="3";
	
	
	/**
	 * 待确认
	 */
	public static final String CONFIRM_WILL = "0";
	
	/**
	 * 已确认
	 */
	public static final String CONFIRM_DONE = "1";
	/**
	 * 已关闭
	 */
	public static final String CLOSED="3";
	
}
