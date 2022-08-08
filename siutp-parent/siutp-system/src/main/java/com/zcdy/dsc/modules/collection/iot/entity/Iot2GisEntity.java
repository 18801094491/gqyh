package com.zcdy.dsc.modules.collection.iot.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author： Roberto
 * 创建时间：2020年3月10日 下午1:23:10
 * 描述: <p>采集设备对照GIS模型设备id</p>
 */
@Setter
@Getter
public class Iot2GisEntity {

	private String iotId;
	
	private String gisId;
}
