package com.zcdy.dsc.modules.constant;

/**
 * @author： Roberto
 * 创建时间：2020年1月19日 上午9:17:39
 * 描述: <p>报警规则</p>
 */
public class WarnRuleConstant {

	private WarnRuleConstant(){}
	/**
	 * 大于某个阈值
	 */
	public static final String greatThan = "1";
	/**
	 * 小于某个阈值
	 */
	public static final String lessThan = "2";
	/**
	 * 等于某个阈值
	 */
	public static final String equals = "3";
	/**
	 * 大于等于某个阈值
	 */
	public static final String greatThanAndEquals = "4";
	/**
	 * 小于等于某个值
	 */
	public static final String lessThanAndEquals = "5";
	/**
	 * 在某个范围内
	 * 不在适用
	 */
	@Deprecated
	public static final String in = "6";
	/**
	 * 在某个范围外
	 * 不在适用
	 */
	@Deprecated
	public static final String notIn = "7";
}
