package com.zcdy.dsc.modules.constant;

/**
 * @author： Roberto
 * 创建时间：2020年3月7日 上午10:29:05
 * 描述: <p>规则类型：告警、正常；正常包括：开、关</p>
 */
public class RuleTypeConstant {

	private RuleTypeConstant(){}
	
	/**
	 * 告警
	 */
	public static final String RULE_WARN = "0";
	
	/**
	 * 开
	 */
	public static final String RULE_OPEN = "1";
	
	/**
	 * 关
	 */
	public static final String RULE_CLOSED = "2";
}
