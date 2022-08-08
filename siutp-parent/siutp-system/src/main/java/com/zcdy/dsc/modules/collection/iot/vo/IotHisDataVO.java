package com.zcdy.dsc.modules.collection.iot.vo;

import java.util.List;
import java.util.Map;

import lombok.ToString;

/**
 * @author： Roberto
 * 创建时间：2020年3月18日 下午5:40:46
 * 描述: <p></p>
 */
@ToString
public class IotHisDataVO {

	private long pageNo = 1;
	
	private long total = 0;
	
	private long pageSize = 0;
	
	private long pages = 0;
	
	private List<Map<String, Object>>header;
	
	private List<Map<String, String>> data;
	
	private String title;

	public Long getPageNo() {
		return pageNo;
	}

	public void setPageNo(Long pageNo) {
		this.pageNo = pageNo;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	public Long getPages() {
		this.pages = (this.total+1)/this.pageSize+1;
		return pages;
	}

	public void setPages(long pages) {
		this.pages = pages;
	}

	public List<Map<String, Object>> getHeader() {
		return header;
	}

	public void setHeader(List<Map<String, Object>> header) {
		this.header = header;
	}

	public List<Map<String, String>> getData() {
		return data;
	}

	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
