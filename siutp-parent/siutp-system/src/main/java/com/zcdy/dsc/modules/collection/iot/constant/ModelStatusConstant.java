package com.zcdy.dsc.modules.collection.iot.constant;

/**
 * @author： Roberto
 * 创建时间：2020年3月5日 下午1:38:45
 * 描述: <p>设备状态常量</p>
 */
public class ModelStatusConstant {

	private ModelStatusConstant(){}
	
	/**
	 * 设备告警,主状态
	 */
	public static final String STATUS_WARNING = "warn";
	
	/**
	 * 设备告警,子状态设备离线
	 */
	public static final String STATUS_WARNING_OFFONLINE = "offline";
	
	/**
	 * 设备告警,子状态数据告警
	 */
	public static final String STATUS_WARNING_DATAALARM = "alarm";
	
	/**
	 * 设备正常，主状态
	 */
	public static final String STATUS_NORMAL = "normal";
	
	/**
	 * 设备正常,子状态设备在线
	 */
	public static final String STATUS_NORMAL_ONLINE = "online";
	
	/**
	 * 设备正常,子状态开
	 */
	public static final String STATUS_NORMAL_OPEN = "open";
	
	/**
	 * 设备正常,子状态关
	 */
	public static final String STATUS_NORMAL_CLOSED = "closed";
}
