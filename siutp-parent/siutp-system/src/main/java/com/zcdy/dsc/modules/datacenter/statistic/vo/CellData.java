package com.zcdy.dsc.modules.datacenter.statistic.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2020年3月29日 下午2:10:38
 * 描述: <p>java映射Excel单元格数据</p>
 */
@Setter
@Getter
public class CellData implements Cloneable {

	public static final CellData CELL_DATA = new CellData();
	
	private Integer index;
	
	private String value;
	
	/*
	 * @see java.lang.Object#clone()
	 */
	@Override
	public CellData clone() {
		CellData tar = null;
		try {
			tar = (CellData) super.clone();
		} catch (CloneNotSupportedException e) {
			tar = new CellData();
		}
		return tar;
	}
}
