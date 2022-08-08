package com.zcdy.dsc.modules.collection.iot.constant;

/**
 * @author： Roberto
 * 创建时间：2020年1月14日 下午5:19:30
 * 描述: <p></p>
 */
public final class AlermConstant {

	private AlermConstant() {
	}

	/**
	 * 无采集信号
	 */
	public static final String COLLECT_BAD = "33";
	
	/**
	 * 设备报警
	 */
	public static final String EQUIP_ALERM = "30";
	
	/**
	 * 关超时
	 */
	public static final String CLOSE_OUTTIME = "31";
	
	/**
	 * 开超时
	 */
	public static final String OPEN_OUTTIME = "32";
	
	/**
	 * 无报警
	 */
	public static final String NO_WARN = "0";
	
	/**
	 * 阀门开
	 */
	public static final String FM_OPEN = "1";

	/**
	 * 阀门关
	 */
	public static final String FM_CLOSE = "2";
}
