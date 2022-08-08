package com.zcdy.dsc.lang;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author: Roberto
 * @CreateTime:2020年1月6日 下午1:57:07
 * @Description: <p></p>
 */
public class TestDecimalFormat {

	@org.junit.Test
	public void test(){
		String varValue = "123.01253";
		if(varValue.matches("-?[0-9]+(\\.[0-9]+)?")){
			DecimalFormat nf = new DecimalFormat("0.00##");
			System.out.println(nf.format(123.01253));
			System.out.println(nf.format(2.01253));
			System.out.println(nf.format(123.01253556));
			System.out.println(nf.format(0.023));
			BigDecimal bf = new BigDecimal(varValue);
			System.out.println(nf.format(bf));
		}else{
			System.out.println(false);
		}
		
		System.out.println(Double.valueOf("1.000").intValue());
		System.out.println(Float.valueOf("1.00000000000").intValue());
		System.out.println(new BigDecimal("1.00000000000").intValue());
	}
}
