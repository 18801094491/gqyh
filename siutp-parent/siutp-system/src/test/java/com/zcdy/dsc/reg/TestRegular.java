package com.zcdy.dsc.reg;

import org.junit.Test;

/**
 * @author: Roberto
 * @CreateTime:2019年12月26日 上午9:21:36
 * @Description: <p></p>
 */
public class TestRegular {

	/**
	 * @author:Roberto
	 * @create:2019年12月26日 上午9:21:54
	 * @Description:<p></p>
	 */
	@Test
	public void test() {
		String reg = ".+((_ALARM)|(_CLOSE_CMD)|(_CLOSE_TIMEOU)|(_FULLCLOSE)|(_FULLOPEN)|(_OPEN_CMD)|(_OPEN_TIMEOUT)|(_REMOTE))$";
		System.out.println("ZHGL_05GFM03_ALARM".matches(reg));
		System.out.println("ZHGL_05GFM03_ALARMaa".matches(reg));
		System.out.println("_ALARMaaa".matches(reg));
		System.out.println("ALARM".matches(reg));
		
		System.out.println("ZHGL_05GFM03_ALARM".indexOf("FM"));
		
		System.out.println("1.2E3".matches("^((-?\\d+.?\\d*)[Ee]{0,1}(-?\\d+))$"));
	}
	
	@Test
	public void test1() {
		/**
		System.out.println("1.2E3".matches("^((-?\\d+.?\\d*)[Ee]{0,1}(-?\\d+))$"));
		System.out.println("1.2".matches("^((-?\\d+.?\\d*)[Ee]{0,1}(-?\\d+))$"));
		System.out.println("1".matches("^((-?\\d+.?\\d*)[Ee]{0,1}(-?\\d+))$"));
		
		System.out.println("1.2E3".matches("-?[0-9]+(\\.[0-9]+)?"));
		System.out.println("1.2".matches("-?[0-9]+(\\.[0-9]+)?"));
		System.out.println("1".matches("-?[0-9]+(\\.[0-9]+)?"));
		*/
		
		System.out.println("9.031250000024471E-5".matches("^[+-]?((\\d+\\.?\\d*)|(\\.\\d+))[Ee][+-]?\\d+$"));
		System.out.println("9.031".matches("^[+-]?((\\d+\\.?\\d*)|(\\.\\d+))[Ee][+-]?\\d+$"));
		System.out.println("9".matches("^[+-]?((\\d+\\.?\\d*)|(\\.\\d+))[Ee][+-]?\\d+$"));
	}
}
