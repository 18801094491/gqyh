package com.zcdy.dsc.common.api.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author： Roberto
 * 创建时间：2020年3月13日 下午6:05:36
 * 描述: <p>接口基础参数</p>
 */
@ApiModel("AbstractPageParam")
public class AbstractPageParam {

	@ApiModelProperty(value = "当前页码", notes = "当前页码")
	protected Integer pageNo;

	@ApiModelProperty(value = "每页容量", notes = "每页容量")
	protected Integer pageSize;
	

	public Integer getPageNo() {
		if(pageNo==null){
			return 1;
		}
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		if(pageSize==null){
			return 10;
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
