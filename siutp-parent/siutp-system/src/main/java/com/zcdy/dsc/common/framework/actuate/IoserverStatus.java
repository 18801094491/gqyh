package com.zcdy.dsc.common.framework.actuate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author: Roberto
 * @CreateTime:2020年3月12日 上午11:15:28
 * @Description: <p>IOServer状态类</p>
 * @see com.zcdy.dsc.collector.constant.IOServerStatusConstant
 */
@ApiModel(value="IoserverStatus", description="IOServer连接状态")
class IoserverStatus {

	/**
	 * 连接状态
	 */
	@ApiModelProperty(value="连接状态", notes="连接状态", allowableValues="200-连接良好,500-连接失败", example="200")
	private int linkStatus;
	
	
	/**
	 * 工作状态
	 */
	@ApiModelProperty(value="工作状态", notes="工作状态", allowableValues="200-工作状况良好,500-停工", example="200")
	private int workStatus;


	public int getLinkStatus() {
		return linkStatus;
	}


	public void setLinkStatus(int linkStatus) {
		this.linkStatus = linkStatus;
	}


	public int getWorkStatus() {
		return workStatus;
	}


	public void setWorkStatus(int workStatus) {
		this.workStatus = workStatus;
	}
}
