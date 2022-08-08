package com.zcdy.dsc.constant;

/**
 * 描述: Redis相关变量参数
 * 	<p>所有的key都是前缀+模块，%s表示模糊匹配，可以通过String.format方法替换</p>
 * <p>前缀是com:zcdy:dsc</p>
 * <p>:ios表示IOSERVER</p>
 * 创建时间： 2020年1月20日 上午10:33:48
 * 版本号: V1.0
 * @author : songguang.jiao
 */
public class RedisKeyConstantV2 {

	private RedisKeyConstantV2() {
	}

	/**
	 * 根据设备id和变量名称查询redis
	 * %s需要用IOServer的采集变量名称替换
	 */
	public static final String REDISDATAKEY = "com:zcdy:dsc:ios:%s";
	

	/**
	 * 设备状态rediskey
	 */
	public static final String MODEL_LAST_STATUS_KEY = "com:zcdy:dsc:model:status:%s";
	
	/**
	 * 记录设备上次的状态
	 */
	public static final String MODEL_PREV_STATUS_KEY = "com:zcdy:dsc:model:prev:status:%s";
	
	/**
	 * 设备的报警状态信息，暂时不用
	 */
	public static final String MODEL_LAST_WARN_KEY = "com:zcdy:dsc:model:warn:status:%s";


	/**
	 * 7标2标压力值
	 */
	public static final String MODEL_PRESS_NORMAL = "com:zcdy:dsc:model:press:normal:%s";


	public static final String MODEL_PRESS_WARN = "com:zcdy:dsc:model:press:warn:%s";
	
	/**
	 * 变量持续告警
	 */
	public static final String MODEL_VARIABLE_WARN_TIME = "com:zcdy:dsc:model:alarm:expire:%s";
	
	/**
	 * 告警事件是否发送小于0.1短信标识
	 */
	public static final String ALARM_EVENT_SMS_FLAG = "com:zcdy:dsc:alarm:sms:%s";

	/**
	 * 执行工单人员的实时位置，%s需要用用户的userid替换
	 */
	public static final String WORKLIST_APP_LOCATION = "com:zcdy:dsc:worklist:location:%s";
}
