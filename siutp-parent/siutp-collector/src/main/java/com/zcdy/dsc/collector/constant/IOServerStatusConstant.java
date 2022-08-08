package com.zcdy.dsc.collector.constant;

/**
 * @author: Roberto
 * @CreateTime:2020年3月12日 上午11:17:46
 * @Description: <p>IOServer状态定义</p>
 */
public class IOServerStatusConstant {

	private IOServerStatusConstant(){}
	
	/**
	 * 连接良好
	 */
	public static final int LINK_FINE = 200;
	
	/**
	 * 连接断开
	 */
	public static final int LINK_BAD = 500;
	
	/**
	 * 工作状态良好
	 */
	public static final int WORK_GOOD = 200;
	
	/**
	 * 工作状态-罢工
	 */
	public static final int WORK_BAD = 500;
}
