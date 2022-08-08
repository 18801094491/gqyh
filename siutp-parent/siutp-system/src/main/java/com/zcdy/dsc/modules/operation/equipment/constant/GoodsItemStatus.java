package com.zcdy.dsc.modules.operation.equipment.constant;

/**
 * 描述: 货物状态
 * @author：  songguang.jiao
 * 创建时间： 2020年2月5日 下午2:24:13 
 * 版本号: V1.0
 */
public class GoodsItemStatus {

	private GoodsItemStatus(){}

	/**
	 * 初始化
	 */
	public static final String INIT="0";
	
	/**
	 * 在库
	 */
	public static final String NORMAL="1";
	
	/**
	 * 外借
	 */
	public static final String BORROW="2";
	
	/**
	 * 损坏
	 */
	public static final String INJURE="3";
	
	/**
	 * 报废
	 */
	public static final String SCRAP="4";
	
	/**
	 * 丢失
	 */
	public static final String LOST="5";
	
	/**
	 * 过期
	 */
	public static final String EXPIRE="6";
	
	/**
	 * 已使用
	 */
	public static final String USED="7";
	
	/**
	 * 盘亏
	 */
	public static final String LOSS="8";
	
	/**
	 * 已出库
	 */
	public static final String CHECKOUT="9";
	
}
