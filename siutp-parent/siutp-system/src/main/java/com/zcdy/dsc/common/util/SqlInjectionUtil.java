package com.zcdy.dsc.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author： Roberto
 * 创建时间：2020年2月25日 上午9:49:59
 * 描述: <p>sql注入处理工具类</p>
 */
@Slf4j
public class SqlInjectionUtil {
	final static String XSSSTR = "'|and |exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |;|or |+|,";

	/**
	 * sql注入过滤处理，遇到注入关键字抛异常
	 * @param value
	 * @return
	 */
	public static void filterContent(String value) {
		if (value == null || "".equals(value)) {
			return;
		}
		value = value.toLowerCase();
		String[] xssArr = XSSSTR.split("\\|");
		for (int i = 0; i < xssArr.length; i++) {
			if (value.indexOf(xssArr[i]) > -1) {
				log.error("请注意，值可能存在SQL注入风险!---> {}", value);
				throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
			}
		}
	}

	/**
	 * sql注入过滤处理，遇到注入关键字抛异常
	 * 
	 * @param value
	 * @return
	 */
	public static void filterContent(String[] values) {
		String[] xssArr = XSSSTR.split("\\|");
		for (String value : values) {
			if (value == null || "".equals(value)) {
				return;
			}
			value = value.toLowerCase();
			for (int i = 0; i < xssArr.length; i++) {
				if (value.indexOf(xssArr[i]) > -1) {
					log.error("请注意，值可能存在SQL注入风险!---> {}", value);
					throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
				}
			}
		}
	}

	/**
	 * @特殊方法(不通用) 仅用于字典条件SQL参数，注入过滤
	 * @param value
	 * @return
	 */
	public static void specialFilterContent(String value) {
		String specialXssStr = "exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |;|+|";
		String[] xssArr = specialXssStr.split("\\|");
		if (value == null || "".equals(value)) {
			return;
		}
		value = value.toLowerCase();
		for (int i = 0; i < xssArr.length; i++) {
			if (value.indexOf(xssArr[i]) > -1) {
				log.error("请注意，值可能存在SQL注入风险!---> {}", value);
				throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
			}
		}
	}
	
	
	/**
	 * @特殊方法(不通用) 仅用于Online报表SQL解析，注入过滤
	 * @param value
	 * @return
	 */
	@Deprecated
	public static void specialFilterContentForOnlineReport(String value) {
		String specialXssStr = "exec |insert |delete |update |drop |chr |mid |master |truncate |char |declare |";
		String[] xssArr = specialXssStr.split("\\|");
		if (value == null || "".equals(value)) {
			return;
		}
		value = value.toLowerCase();
		for (int i = 0; i < xssArr.length; i++) {
			if (value.indexOf(xssArr[i]) > -1) {
				log.error("请注意，值可能存在SQL注入风险!---> {}", value);
				throw new RuntimeException("请注意，值可能存在SQL注入风险!--->" + value);
			}
		}
		return;
	}

}
