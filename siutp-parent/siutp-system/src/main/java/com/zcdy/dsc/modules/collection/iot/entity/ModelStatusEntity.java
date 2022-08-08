package com.zcdy.dsc.modules.collection.iot.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2020年3月7日 下午3:45:22
 * 描述: <p>设备状态信息</p>
 * @see com.zcdy.dsc.modules.collection.iot.constant.ModelStatusConstant
 */
@Setter
@Getter
public class ModelStatusEntity {

	//正常、异常
	private String status;
	
	//具体的状态，子状态
	private String detailStatus;
	
	//状态说明
	private String message;
}
