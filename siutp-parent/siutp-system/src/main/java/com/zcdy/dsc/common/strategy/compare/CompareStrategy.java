package com.zcdy.dsc.common.strategy.compare;

/**
 * @author： Roberto
 * 创建时间：2020年3月6日 下午7:53:31
 * 描述: <p>比较策略</p>
 */
public interface CompareStrategy {

	/**
	 * 创建人:Roberto
	 * 创建时间:2020年5月8日 下午5:22:39
	 * 描述:<p>执行匹配</p>
	 */
	public boolean matches(String dataType, String value, String comparedValue);
}
