package com.zcdy.dsc.constant;

/**
 * @author： Roberto
 * 创建时间：2019年12月24日 下午5:48:27
 * 描述: <p>其停用常量</p>
 */
public class StatusConstant {

	private StatusConstant() {
	}

	/**
	 * 已删除
	 */
	public static final short DELETED = 1;
	
	/**
	 * 正常
	 */
	public static final short WORKING = 0;
	
	/**
	 * 启用
	 */
	public static final short RUN = 1;

	/**
	 * 停用
	 */
	public static final short STOP = 0;
	
	/**
	 * 是(有效)
	 */
	public static final short VALID = 1;

	/**
	 * 否(无效)
	 */
	public static final short INVALID = 0;
	
	/**
	 * 告警
	 */
	public static final String ALARM = "1";

	/**
	 * 没有告警
	 */
	public static final String NO_ALARM = "0";
	
}
