package com.zcdy.dsc.lang;

import org.junit.Test;

import com.zcdy.dsc.common.util.PasswordUtil;

/**
 * @author: Roberto
 * @CreateTime:2019年12月31日 上午11:15:08
 * @Description: <p></p>
 */
public class TestPwd {

	@Test
	public void test(){
		String userpassword = PasswordUtil.encrypt("admin", "htby@zcdy", "RCGTeGiH");
		System.err.println(userpassword);
		
		System.out.println("abc.name".substring("abc.name".indexOf(".")));
		
		System.out.println("c77f064e-c1b3-4dc8-8327-a57fc5714487.pdf".matches("[a-zA-Z0-9\\-\\.]+"));
		System.out.println("20200103".matches("\\d{8}"));
	}
}
