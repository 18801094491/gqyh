package com.zcdy.dsc.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 描述: 数据格式化工具类
 * @author:  songguang.jiao
 * 创建时间:  2020年4月20日 下午5:35:18
 * 版本: V1.0
 */
public class NumberFormatUtil {

	/**
	 * 描述: 数据量过大时转换格式(以万结尾)
	 * @author:  songguang.jiao
	 * 创建时间:  2020年4月20日 下午5:34:41
	 * 版本: V1.0
	 */
	public static String formatData(BigDecimal bg){
		if (bg.compareTo(new BigDecimal("100000")) >= 0 || bg.compareTo(new BigDecimal("-100000")) <= 0) {
			bg = bg.movePointLeft(4);
			return bg.setScale(2, RoundingMode.HALF_UP) + "万";
		} else {
			return bg + "";
		}
	}
}
