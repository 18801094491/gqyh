package com.zcdy.dsc.modules.collection.gis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2019年12月26日 上午9:46:59
 * 描述: <p>设备状态</p>
 * @see com.zcdy.dsc.modules.collection.iot.constant.ModelStatusConstant
 */
@Setter
@Getter
@ApiModel(value="OperationSatus", description="设备状态")
public class OperationSatus implements Cloneable {
	
	public static final OperationSatus OPERATION_SATUS = new OperationSatus();

	@ApiModelProperty(value="详细状态",notes="二级状态",allowableValues="online-[normal-正常]在线,open-[normal-正常]开,closed-[normal-正常]关,offline-[warn-异常]离线,alarm-[warn-异常]告警")
	private String switchSatus;

	@ApiModelProperty(value="工作状态",notes="一级状态",allowableValues="normal-正常,warn-异常")
	private String workStatus;

	// 发生告警，传输消息
	@ApiModelProperty(value="消息内容",notes="消息内容")
	private String message;

	/*
	 * @see java.lang.Object#clone()
	 */
	@Override
	public OperationSatus clone() {
		OperationSatus status = null;
		try {
			status = (OperationSatus) super.clone();
		} catch (CloneNotSupportedException e) {
			status = new OperationSatus();
		}
		return status;
	}
}
