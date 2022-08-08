package com.zcdy.dsc.common.strategy.compare;

import java.math.BigDecimal;

import com.zcdy.dsc.modules.constant.DataTypeConstant;

/**
 * @author： Roberto
 * 创建时间：2020年3月6日 下午7:56:44
 * 描述: <p>大于等于</p>
 */
public class GreatThanStrategy implements CompareStrategy {

	/*
	 * @see com.zcdy.dsc.common.strategy.CompareStrategy#matches(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean matches(String dataType, String value, String comparedValue) {
		return judge(dataType, value, comparedValue);
	}

	private boolean judge(String dataType, String value, String comparedValue) {
		boolean result = false;
		
		switch (dataType) {
		case DataTypeConstant.TYPE_SHORT:
		case DataTypeConstant.TYPE_INTEGER:
		case DataTypeConstant.TYPE_LONG:
		case DataTypeConstant.TYPE_FLOAT:
		case DataTypeConstant.TYPE_DOUBLE:{
			try {
				BigDecimal tar = new BigDecimal(value);
				BigDecimal limitVal = new BigDecimal(comparedValue);
				if(tar.compareTo(limitVal)>0){
					result = true;
				}
			} catch (NumberFormatException e) {
				result = false;
			}
			break;
		}
		case DataTypeConstant.TYPE_STRING:
		case DataTypeConstant.TYPE_BOOLEAN:
			//不适用的类型
			break;
		default:
			break;
		}
		return result;
	}
}
