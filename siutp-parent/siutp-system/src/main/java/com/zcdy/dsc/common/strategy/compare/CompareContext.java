package com.zcdy.dsc.common.strategy.compare;

/**
 * @author： Roberto
 * 创建时间：2020年3月6日 下午8:38:04
 * 描述: <p>比较策略上下文</p>
 */
public class CompareContext {

	private CompareStrategy strategy;
	
	public CompareContext(CompareStrategy strategy){
		this.strategy = strategy;
	}
	
	public boolean matches(String dataType, String value, String comparedValue){
		if(null==this.strategy){
			return false;
		}
		return this.strategy.matches(dataType, value, comparedValue);
	}
}
