package com.zcdy.dsc.common.util.encryption;

import lombok.Data;

/**
 * @author： Roberto
 * 创建时间：2020年2月25日 上午10:07:51
 * 描述: <p>加密字符串定义</p>
 */
@Data
public class EncryptedString {

	/**
	 * 长度为16个字符
	 */
	public static final String KEY = "1234567890adbcde";

	/**
	 * 长度为16个字符
	 */
	public static final String IV = "1234567890hjlkew";
}
