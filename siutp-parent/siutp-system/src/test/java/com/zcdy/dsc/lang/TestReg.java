package com.zcdy.dsc.lang;

import org.junit.Test;

/**
 * @author: Roberto
 * @CreateTime:2020年1月6日 下午2:51:30
 * @Description: <p></p>
 */
public class TestReg {

	@Test
	public void test(){
		String vName = "ZHGL_01ZFM02_REMOTE";
		boolean ok = vName.matches(".+((_ALARM)|(_CLOSE_TIMEOU)|(_OPEN_TIMEOUT)|(_FULLCLOSE)|(_FULLOPEN)|(_CMD)|(_REMOTE))$");
		System.out.println(ok);
		
		System.out.println(vName.endsWith("_CMD") || vName.endsWith("_REMOTE"));
	}
}
