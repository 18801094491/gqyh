package com.zcdy.dsc.constant;

/**
 * 描述: Redis相关变量参数
 * @author： songguang.jiao
 * 创建时间： 2020年1月20日 上午10:33:48
 * 版本号: V1.0
 */
public class RedisKeyConstant {

	private RedisKeyConstant() {
	}

	/**
	 * 根据设备查找所有变量 根据GISmodelid查询所有变量
	 */
	public static final String PARENT_REDISDATAKEY = "com:zcdy:dsc:ios:%s:*";
	
	/**
	 * 根据设备id和变量名称查询redis
	 */
	public static final String REDISDATAKEY = "com:zcdy:dsc:ios:%s:%s";
	
	/**
	 * 根据GISmodelid查询所有变量
	 */
	public static final String GRANT_REDISDATAKEY = "com:zcdy:dsc:ios:*";

	/**
	 * 模型变量存储的redis可以的前缀
	 */
	public static final String PREFIX_MODEL_REDISKEY = "com:zcdy:dsc:ios:%s:";

	/**
	 * 设备状态rediskey
	 */
	public static final String MODEL_LAST_STATUS_KEY = "com:zcdy:dsc:model:status:%s";
	
	/**
	 * 设备的报警状态信息，暂时不用
	 */
	public static final String MODEL_LAST_WARN_KEY = "com:zcdy:dsc:model:warn:%s";

	// 使用redis计时处理存储标识
	public static final String IOT_DATA_STOREFLAG = "com:zcdy:dsc:iot:store:flag";
}
