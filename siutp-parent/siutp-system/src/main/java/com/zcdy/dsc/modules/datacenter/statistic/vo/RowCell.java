package com.zcdy.dsc.modules.datacenter.statistic.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2020年3月29日 下午2:19:04
 * 描述: <p>采集数据映射Excel行数据</p>
 */
@Getter
@Setter
public class RowCell implements Cloneable ,Comparable<RowCell>{

	private Long timestamp;
	
	private List<CellData> datas;
	
	/*
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RowCell obj) {
		return this.timestamp.compareTo(obj.getTimestamp());
	}
	
	@Override
	public RowCell clone() {
		RowCell tar = null;
		try {
			tar = (RowCell) super.clone();
		} catch (CloneNotSupportedException e) {
			tar = new RowCell();
		}
		return tar;
	}
}
